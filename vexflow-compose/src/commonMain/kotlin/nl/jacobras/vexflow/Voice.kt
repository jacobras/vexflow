package nl.jacobras.vexflow

class Voice : Element(category = Category.Voice) {

    var stave: Stave? = null
    var tickables = mutableListOf<Tickable>()

    override fun draw() {
        TODO("Not yet implemented")
    }
}