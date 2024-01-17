package nl.jacobras.vexflow

data class TextMetrics(
    val fontBoundingBoxAscent: Int,
    val fontBoundingBoxDescent: Int,
    val actualBoundingBoxAscent: Int,
    val actualBoundingBoxDescent: Int,
    val actualBoundingBoxLeft: Int,
    val actualBoundingBoxRight: Int,
    val width: Int
)