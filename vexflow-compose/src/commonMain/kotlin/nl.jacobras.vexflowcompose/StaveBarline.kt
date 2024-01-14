package nl.jacobras.vexflowcompose

enum class BarlineType {
    SINGLE,
    DOUBLE,
    END,
    REPEAT_BEGIN,
    REPEAT_END,
    REPEAT_BOTH,
    NONE,
}

class Barline(
    val type: BarlineType
) : StaveModifier() {

    override val CATEGORY = Category.Barline

    override fun drawStave(stave: Stave, x: Int) {
        val ctx = stave.checkContext();
        this.setRendered();

        this.applyStyle(ctx);
        ctx.openGroup("stavebarline", this.getAttribute("id") as String);
        when (this.type) {
            BarlineType.SINGLE -> this.drawVerticalBar(stave, this.x, false)
            BarlineType.DOUBLE -> this.drawVerticalBar(stave, this.x, true)
            BarlineType.END -> this.drawVerticalEndBar(stave, this.x);
            BarlineType.REPEAT_BEGIN -> {
                // If the barline is shifted over (in front of clef/time/key)
                // Draw vertical bar at the beginning.
                this.drawRepeatBar(stave, this.x, true);
                if (stave.x != this.x) {
                    this.drawVerticalBar(stave, stave.x);
                }
            }
            BarlineType.REPEAT_END -> this.drawRepeatBar(stave, this.x, false);
            BarlineType.REPEAT_BOTH -> {
                this.drawRepeatBar(stave, this.x, false)
                this.drawRepeatBar(stave, this.x, true);
            }
            else -> {
                // Default is NONE, so nothing to draw
            }
        }
        ctx.closeGroup();
        this.restoreStyle(ctx);
    }

    fun drawVerticalBar(stave: Stave, x: Int, doubleBar: Boolean = false) {
        val staveCtx = stave.checkContext();
        val topY = stave.getTopLineTopY();
        val botY = stave.getBottomLineBottomY();
        if (doubleBar) {
            staveCtx.fillRect(x - 3, topY, 1, botY - topY);
        }
        staveCtx.fillRect(x, topY, 1, botY - topY);
    }

    fun drawVerticalEndBar(stave: Stave, x: Int) {
        val staveCtx = stave.checkContext();
        val topY = stave.getTopLineTopY();
        val botY = stave.getBottomLineBottomY();
        staveCtx.fillRect(x - 5, topY, 1, botY - topY);
        staveCtx.fillRect(x - 2, topY, 3, botY - topY);
    }

    fun drawRepeatBar(stave: Stave, x: Int, begin: Boolean) {
        val staveCtx = stave.checkContext();

        val topY = stave.getTopLineTopY();
        val botY = stave.getBottomLineBottomY();
        var xShift = 3;

        if (!begin) {
            xShift = -5;
        }

        staveCtx.fillRect(x + xShift, topY, 1, botY - topY);
        staveCtx.fillRect(x - 2, topY, 3, botY - topY);

        val dotRadius = 2;

        // Shift dots left or right
        if (begin) {
            xShift += 4;
        } else {
            xShift -= 4;
        }

        val dotX = x + xShift + dotRadius / 2;

        // calculate the y offset based on number of stave lines
        var yOffset = (stave.getNumLines() - 1) * stave.getSpacingBetweenLines();
        yOffset = yOffset / 2 - stave.getSpacingBetweenLines() / 2;
        var dotY = topY + yOffset + dotRadius / 2;

        // draw the top repeat dot
        staveCtx.beginPath();
        staveCtx.arc(dotX, dotY, dotRadius, 0, Math.PI * 2, false);
        staveCtx.fill();

        // draw the bottom repeat dot
        dotY += stave.getSpacingBetweenLines()
        staveCtx.beginPath();
        staveCtx.arc(dotX, dotY, dotRadius, 0, Math.PI * 2, false);
        staveCtx.fill()
    }
}