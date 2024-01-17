package nl.jacobras.vexflow

open class FormatterOptions(
    val softmaxFactor: Int = Tables.SOFTMAX_FACTOR,
    val globalSoftmax: Boolean = false,
    val maxIterations: Int = 5
)

data class FormatParams(
    val alignRests: Boolean? = null,
    val stave: Stave? = null,
    val context: RenderContext? = null,
    val autoBeam: Boolean? = null
)