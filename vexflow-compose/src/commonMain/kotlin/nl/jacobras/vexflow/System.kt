package nl.jacobras.vexflow

data class SystemFormatterOptions(
    val alpha: Double? = null
) : FormatterOptions()

data class SystemStave(
    val voices: List<Voice>,
    val stave: Stave? = null,
    val noJustification: Boolean? = null,
    val options: StaveOptions? = null,
    val spaceAbove: Int? = null,
    val spaceBelow: Int? = null,
    val debugNoteMetrics: Boolean? = null
)

data class StaveInfo(
    val noJustification: Boolean,
    val options: StaveOptions,
    val spaceAbove: Int,
    val spaceBelow: Int,
    val debugNoteMetrics: Boolean
)

data class SystemOptions(
    val factory: Factory? = null,
    val noPadding: Boolean? = null,
    val debugFormatter: Boolean? = null,
    val spaceBetweenStaves: Double? = null,
    val formatIterations: Int? = null,
    val autoWidth: Boolean? = null,
    val x: Int = 0,
    val width: Int = 0,
    val y: Int = 0,
    val details: SystemFormatterOptions? = null,
    val formatOptions: FormatParams? = null,
    val noJustification: Boolean? = null
)

/**
 * System implements a musical system, which is a collection of staves,
 * each which can have one or more voices. All voices across all staves in
 * the system are formatted together.
 */
class System : Element(category = Category.System) {
    protected lateinit var options: SystemOptions
    protected lateinit var factory: Factory
    private var partStaves = mutableListOf<Stave>()
    private var partStaveInfos = mutableListOf<StaveInfo>()
    private var partVoices = mutableListOf<Voice>()

    fun addStave(params: SystemStave): Stave {
        val staveOptions = params.options?.copy(leftBar = false)

        val stave = params.stave ?: this.factory.Stave(
            x = this.options.x,
            y = this.options.y,
            width = this.options.width,
            options = staveOptions
        )

        val p = params.copy(
            spaceAbove = 0,
            spaceBelow = 0,
            debugNoteMetrics = false,
            noJustification = false,
            options = staveOptions
        )

        p.voices.forEach { voice ->
            voice.context = this@System.context
            voice.stave = stave
            voice.tickables.forEach { it.stave = stave }
            partVoices.add(voice)
        }

        partStaves.add(stave)
        partStaveInfos.add(
            StaveInfo(
                noJustification = p.noJustification!!,
                options = p.options!!,
                spaceAbove = p.spaceAbove!!,
                spaceBelow = p.spaceBelow!!,
                debugNoteMetrics = p.debugNoteMetrics!!
            )
        )
        return stave
    }

    override fun draw() {
        TODO("Not yet implemented")
    }
}