package nl.jacobras.vexflowcompose

interface LayoutMetrics {
    val xMin: Int
    val xMax: Int
    val paddingLeft: Int
    val paddingRight: Int
}

enum class StaveModifierPosition(val code: Int) {
    CENTER(0),
    LEFT(1),
    RIGHT(2),
    ABOVE(3),
    BELOW(4),
    BEGIN(5),
    END(6)
}

abstract class StaveModifier : Element() {

    private var padding: Int = 10
    private var position = StaveModifierPosition.ABOVE
    private var stave: Stave? = null
    private var layoutMetrics: LayoutMetrics? = null

    open val CATEGORY: Category = Category.StaveModifier

    init {
        this.padding = 10
        this.position = StaveModifierPosition.ABOVE
    }

    fun getPosition(): StaveModifierPosition {
        return position
    }

    fun setPosition(position: StaveModifierPosition): StaveModifier {
        this.position = position
        return this
    }

    fun getStave(): Stave? {
        return stave
    }

    fun checkStave(): Stave {
        return stave ?: error("No stave attached to instance.")
    }

    fun setStave(stave: Stave): StaveModifier {
        this.stave = stave
        return this
    }

    fun getPadding(index: Int?): Int {
        return if (index != null && index < 2) 0 else padding
    }

    fun setPadding(padding: Int): StaveModifier {
        this.padding = padding
        return this
    }

    fun setLayoutMetrics(layoutMetrics: LayoutMetrics): StaveModifier {
        this.layoutMetrics = layoutMetrics
        return this
    }

    fun getLayoutMetrics(): LayoutMetrics? {
        return layoutMetrics
    }

    override fun draw() {
        // DO NOTHING.
    }

    abstract fun drawStave(stave: Stave, x: Int)

    //        val Position: StaveModifierPosition
//            get() = StaveModifierPosition // TODO: figure out
}