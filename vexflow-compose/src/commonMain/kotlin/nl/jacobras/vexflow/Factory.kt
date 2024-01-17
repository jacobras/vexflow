package nl.jacobras.vexflow

data class FactoryOptions(
    val stave: FactoryStaveOptions,
    val renderer: FactoryRendererOptions
)

data class FactoryStaveOptions(
    val space: Int = 0
)

data class FactoryRendererOptions(
    val elementId: String?,
    val backend: Int? = null,
    val width: Int,
    val height: Int,
    val background: String? = null
)

class Factory(
    protected val options: FactoryOptions
) {

    private var stave: Stave? = null
    private lateinit var context: RenderContext
    private val staves = mutableListOf<Stave>()
    private val voices = mutableListOf<Voice>()

    fun Stave(x: Int, y: Int, width: Int, options: StaveOptions?): Stave {
        val staveSpace = this.options.stave.space
        val staveOptions = StaveOptions(spacingBetweenLinesPx = staveSpace)
        val stave = Stave(
            x = 0,
            y = 0,
            width = (this.options.renderer.width - staveSpace).toInt(),
            options = staveOptions.copy(
                spacingBetweenLinesPx = staveSpace.toInt()
            )
        )

        staves.add(stave)
        stave.context = context
        this.stave = stave
        return stave
    }

}