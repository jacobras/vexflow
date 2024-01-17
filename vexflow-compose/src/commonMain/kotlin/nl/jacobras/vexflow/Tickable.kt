package nl.jacobras.vexflow

abstract class Tickable(category: Category = Category.Tickable) : Element(category = category) {
    abstract var stave: Stave
}