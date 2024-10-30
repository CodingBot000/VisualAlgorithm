package com.algorithm.model


data class TrackingData(
    val order: Int = 0,
    val isVisited: Boolean = false
)
data class TrackingDataResult(
    val result: Array<Array<TrackingData>>,
    val targetX: Int,
    val targetY: Int,
    val order: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrackingDataResult

        if (!result.contentDeepEquals(other.result)) return false
        if (targetX != other.targetX) return false
        if (targetY != other.targetY) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = result.contentDeepHashCode()
        result1 = 31 * result1 + targetX
        result1 = 31 * result1 + targetY
        result1 = 31 * result1 + order
        return result1
    }
}