package nl.jacobras.vexflow

interface TextMeasure {
    val x: Int
    val y: Int
    val width: Int
    val height: Int
}

interface RenderContext {
    val CATEGORY: Category
        get() = Category.RenderContext

    fun clear()
    fun setFillStyle(style: String): RenderContext
    fun setBackgroundFillStyle(style: String): RenderContext
    fun setStrokeStyle(style: String): RenderContext
    fun setShadowColor(color: String): RenderContext
    fun setShadowBlur(blur: Number): RenderContext
    fun setLineWidth(width: Number): RenderContext

    //    fun setLineCap(capType: CanvasLineCap): RenderContext
    fun setLineDash(dashPattern: IntArray): RenderContext
    fun scale(x: Int, y: Int): RenderContext
    fun rect(x: Int, y: Int, width: Int, height: Int): RenderContext
    fun resize(width: Int, height: Int): RenderContext
    fun fillRect(x: Int, y: Int, width: Int, height: Int): RenderContext
    fun clearRect(x: Int, y: Int, width: Int, height: Int): RenderContext
    fun pointerRect(x: Int, y: Int, width: Int, height: Int): RenderContext
    fun beginPath(): RenderContext
    fun moveTo(x: Int, y: Int): RenderContext
    fun lineTo(x: Int, y: Int): RenderContext
    fun bezierCurveTo(cp1x: Int, cp1y: Int, cp2x: Int, cp2y: Int, x: Int, y: Int): RenderContext
    fun quadraticCurveTo(cpx: Int, cpy: Int, x: Int, y: Int): RenderContext
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
    fun closePath(): RenderContext
    fun fillText(text: String, x: Int, y: Int): RenderContext
    fun save(): RenderContext
    fun restore(): RenderContext;
    fun openGroup(cls: String?, id: String?)
    fun closeGroup()
    fun openRotation(angleDegrees: Int, x: Int, y: Int)
    fun closeRotation()

    fun add(child: Any)
    fun measureText(text: String): TextMeasure

//    fun setFillStyle(style: String | CanvasGradient | CanvasPattern);
//    fun getFillStyle(): String | CanvasGradient | CanvasPattern;
//
//    fun setStrokeStyle(style: String | CanvasGradient | CanvasPattern);
//    fun getStrokeStyle(): String | CanvasGradient | CanvasPattern;

    fun setFont(f: FontInfo, size: Number? = 0, weight: Number? = null, style: String? = null): RenderContext
    fun getFont(): String
}

/**
 * Draw a tiny dot marker on the specified context. A great debugging aid.
 * @param ctx context
 * @param x dot x coordinate
 * @param y dot y coordinate
 * @param color
 */
fun drawDot(ctx: RenderContext, x: Int, y: Int, color: String = "#F55") {
    ctx.save();
    ctx.setFillStyle(color);

    // draw a circle
    ctx.beginPath();
    ctx.arc(x, y, 3, 0, Math.PI * 2, false);
    ctx.closePath();
    ctx.fill();
    ctx.restore();
}