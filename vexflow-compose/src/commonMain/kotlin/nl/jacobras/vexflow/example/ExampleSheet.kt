package nl.jacobras.vexflow.example

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import nl.jacobras.vexflow.FontInfo
import nl.jacobras.vexflow.RenderContext
import nl.jacobras.vexflow.Stave
import nl.jacobras.vexflow.StaveLineConfig
import nl.jacobras.vexflow.StaveOptions
import nl.jacobras.vexflow.TextMeasure

@Composable
fun ExampleSheet(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        Stave(
            options = StaveOptions(
                bottomTextPosition = 0,
                lineConfig = mutableListOf(
                    StaveLineConfig(visible = true),
                    StaveLineConfig(visible = true),
                    StaveLineConfig(visible = true),
                    StaveLineConfig(visible = true),
                    StaveLineConfig(visible = true)
                ),
                spaceBelowStaffLn = 0,
                spaceAboveStaffLn = 0,
                verticalBarWidth = 10,
                fillStyle = null,
                leftBar = false,
                rightBar = false,
                spacingBetweenLinesPx = 10,
                topTextPosition = 0,
                numLines = 5,
            )
        )
            .apply {
                context = CanvasRenderer(this@Canvas)
                attrs.id = "testStave"
                width = 200 // TODO: from system or something
            }
            .draw()
    }
}

private class CanvasRenderer(private val drawScope: DrawScope) : RenderContext {
    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun setFillStyle(style: String): RenderContext {
        TODO("Not yet implemented")
    }

    override fun setBackgroundFillStyle(style: String): RenderContext {
        TODO("Not yet implemented")
    }

    override fun setStrokeStyle(style: String): RenderContext {
        TODO("Not yet implemented")
    }

    override fun setShadowColor(color: String): RenderContext {
        TODO("Not yet implemented")
    }

    override fun setShadowBlur(blur: Number): RenderContext {
        TODO("Not yet implemented")
    }

    override fun setLineWidth(width: Number): RenderContext {
        TODO("Not yet implemented")
    }

    override fun setLineDash(dashPattern: IntArray): RenderContext {
        TODO("Not yet implemented")
    }

    override fun scale(x: Int, y: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun rect(x: Int, y: Int, width: Int, height: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun resize(width: Int, height: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun fillRect(x: Int, y: Int, width: Int, height: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun clearRect(x: Int, y: Int, width: Int, height: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun pointerRect(x: Int, y: Int, width: Int, height: Int): RenderContext {
        TODO("Not yet implemented")
    }

    val paths = mutableListOf<Path>()

    override fun beginPath(): RenderContext {
        paths.add(Path())
        return this
    }

    override fun moveTo(x: Int, y: Int): RenderContext = apply {
        val currentPath = paths[paths.lastIndex]
        currentPath.moveTo(x.toFloat(), y.toFloat())
    }

    override fun lineTo(x: Int, y: Int): RenderContext = apply {
        val currentPath = paths[paths.lastIndex]
        currentPath.lineTo(x.toFloat(), y.toFloat())
    }

    override fun bezierCurveTo(cp1x: Int, cp1y: Int, cp2x: Int, cp2y: Int, x: Int, y: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun quadraticCurveTo(cpx: Int, cpy: Int, x: Int, y: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun arc(x: Number, y: Number, radius: Number, startAngle: Number, endAngle: Number, counterclockwise: Boolean): RenderContext {
        TODO("Not yet implemented")
    }

    override fun fill(attributes: List<Any>): RenderContext {
        TODO("Not yet implemented")
    }

    override fun stroke(): RenderContext = apply {
        // TODO
    }

    override fun closePath(): RenderContext {
        TODO("Not yet implemented")
    }

    override fun fillText(text: String, x: Int, y: Int): RenderContext {
        TODO("Not yet implemented")
    }

    override fun save(): RenderContext = apply {
        drawScope.drawContext.canvas.save()
    }

    override fun restore(): RenderContext = apply {
        drawScope.drawContext.canvas.restore()
    }

    override fun openGroup(cls: String?, id: String?) {
        // Containers not implemented
    }

    override fun closeGroup() {
        for (path in paths) {
            // TODO: only stroke if stroke() called
            drawScope.drawPath(path, Color.Red, style = Stroke(width = 1f))
        }
    }

    override fun openRotation(angleDegrees: Int, x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun closeRotation() {
        TODO("Not yet implemented")
    }

    override fun add(child: Any) {
        TODO("Not yet implemented")
    }

    override fun measureText(text: String): TextMeasure {
        TODO("Not yet implemented")
    }

    override fun setFont(f: FontInfo, size: Number?, weight: Number?, style: String?): RenderContext {
        TODO("Not yet implemented")
    }

    override fun getFont(): String {
        TODO("Not yet implemented")
    }
}