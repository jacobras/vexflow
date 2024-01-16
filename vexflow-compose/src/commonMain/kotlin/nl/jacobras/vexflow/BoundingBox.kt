package nl.jacobras.vexflow

interface Bounds {
    val x: Double
    val y: Double
    val w: Double
    val h: Double
}

data class BoundingBox(
    override var x: Double,
    override var y: Double,
    override var w: Double,
    override var h: Double
) : Bounds {

    companion object {
        fun copy(that: BoundingBox): BoundingBox {
            return BoundingBox(that.x, that.y, that.w, that.h)
        }
    }

    fun move(x: Double, y: Double): BoundingBox {
        this.x += x
        this.y += y
        return this
    }

    fun mergeWith(boundingBox: BoundingBox): BoundingBox {
        val newX = if (x < boundingBox.x) x else boundingBox.x
        val newY = if (y < boundingBox.y) y else boundingBox.y
        val newW = maxOf(x + w, boundingBox.x + boundingBox.w) - newX
        val newH = maxOf(y + h, boundingBox.y + boundingBox.h) - newY

        x = newX
        y = newY
        w = newW
        h = newH

        return this
    }
}