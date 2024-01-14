package nl.jacobras.vexflowcompose

interface TextMeasure {
    val x: Int
    val y: Int
    val width: Int
    val height: Int
}

interface RenderContext {
    fun setFillStyle(style: String): RenderContext
    fun setStrokeStyle(style: String): RenderContext
    fun setShadowColor(color: String): RenderContext
    fun setShadowBlur(blur: Number): RenderContext
    fun setLineWidth(width: Number): RenderContext
    fun setLineDash(dashPattern: IntArray): RenderContext
    fun fillRect(x: Int, y: Int, width: Int, height: Int): RenderContext
    fun beginPath(): RenderContext
    fun moveTo(x: Int, y: Int): RenderContext
    fun lineTo(x: Int, y: Int): RenderContext
    fun arc(
        x: Number,
        y: Number,
        radius: Number,
        startAngle: Number,
        endAngle: Number,
        counterclockwise: Boolean
    ): RenderContext

    fun fill(attributes: List<Any> = emptyList()): RenderContext
    fun stroke(): RenderContext
    fun fillText(text: String, x: Int, y: Int): RenderContext
    fun save(): RenderContext
    fun restore(): RenderContext;
    fun openGroup(cls: String?, id: String?)
    fun closeGroup()
    fun measureText(text: String): TextMeasure
    fun setFont(f: FontInfo, size: Number? = 0, weight: Number? = null, style: String? = null): RenderContext
    fun getFont(): String
}