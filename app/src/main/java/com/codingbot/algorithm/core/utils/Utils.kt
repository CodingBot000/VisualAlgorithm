package com.codingbot.algorithm.core.utils

fun scaledNumber(randomValues: Array<Int>, from: Int, to: Int): List<Int> {
    val min = randomValues.minOrNull() ?: return listOf() // 최솟값이 없다면 빈 리스트 반환
    val max = randomValues.maxOrNull() ?: return listOf() // 최댓값이 없다면 빈 리스트 반환

    // 변환 범위 계산
    val range = to - from

    // 비율 계산 및 새로운 값으로 스케일링
    return randomValues.map { data ->
        // 각 원소를 from에서 to 사이로 변환
        ((data - min).toFloat() / (max - min) * range + from).toInt()
    }
}