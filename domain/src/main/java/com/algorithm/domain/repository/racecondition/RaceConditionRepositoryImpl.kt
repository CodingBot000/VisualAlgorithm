package com.algorithm.domain.repository.racecondition

import com.algorithm.model.AsynchronousData
import com.algorithm.model.AsynchronousDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class RaceConditionRepositoryImpl @Inject constructor() : RaceConditionRepository {
    private var incrementIdx =  AtomicInteger(0)
    private var counter = 0
    private val repeatCnt = 100
    private val increment = mutableListOf<AsynchronousData>()
    private val sharedCountList = mutableListOf<Int>()
    private val criticalSectionMap = hashMapOf<Int, AsynchronousData>()
    private var debugCnt = 0

    private val _tryCnt = MutableStateFlow(0)

    override suspend fun getRaceConditionRepository(): AsynchronousDataResult {
        refreshLoop()
        _tryCnt.value = 0
        return runRaceCondition()
    }

    override fun getTryCnt(): StateFlow<Int> {
        return _tryCnt
    }

    suspend fun increment(conditionNum: Int) {
        withContext(Dispatchers.Default) {
            repeat(repeatCnt) { times ->

                val oldValue = counter
                counter++

                incrementIdx.getAndIncrement()
                // counter 업데이트 후에 값을 캡처,
                // 캡쳐하지않으면 저장하면서 값이 다시 업데이트되어 실제 로그와 리스트에 들어간값이 달라진다
                // 로그에 순간적으로 출력되는 값과 동일한값을 리스트에 넣기위해 캡쳐를 한다
                val capturedOldValue = oldValue
                val capturedCounter = counter
                sharedCountList.add(capturedCounter)

                increment.add(AsynchronousData(conditionNum, capturedOldValue, capturedCounter))

                println("==== [$conditionNum] times : ${times} Previous: $oldValue, Updated: $counter")
                if (capturedCounter != capturedOldValue + 1) {

                    println("==== [$conditionNum] - idx[${incrementIdx.get()}] Race condition detected! Previous: $oldValue, Updated: $counter")
                    criticalSectionMap[incrementIdx.get()] = AsynchronousData(
                        coroutineNum = conditionNum,
                        valueA = capturedOldValue,
                        valueB = capturedCounter)

                }
            }
        }
    }

    suspend fun runRaceCondition() = withContext(Dispatchers.Default) {

        //  때까지 반복하여 경쟁 조건 탐지
        while (true) {
//            delay(100) // 딜레이를 주면 raceCondition 오류가 발생을 잘안한다..
            _tryCnt.value++
            println("==== : $_tryCnt  ====================================")
            List(2) { index ->
                launch { increment(index + 1) }
            }.forEach { it ->
                it.join()
            }

            println("tryCnt : $_tryCnt")
//            if (counter != repeatCnt * 2) {
            if (counter != repeatCnt * 2 && criticalSectionMap.isNotEmpty()) {
                println("Counter value after race condition: $counter")
                break
            } else {
                refreshLoop()
                println("Counter value after race condition ???? : $counter")
            }

        }

        println("Completed with counter: $counter")

        AsynchronousDataResult(
            sharedCountList,
            increment,
            criticalSectionMap)
    }

    private fun refreshLoop() {
        sharedCountList.clear()
        incrementIdx.set(0)
        increment.clear()
        criticalSectionMap.clear()

        counter = 0
        debugCnt = 0
    }

}