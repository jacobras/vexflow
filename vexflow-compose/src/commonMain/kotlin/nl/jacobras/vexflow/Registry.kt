package nl.jacobras.vexflow

// This file implements a registry for VexFlow elements. It allows users
// to track, query, and manage some subset of generated elements, and
// dynamically get and set attributes.
//
// There are two ways to register with a registry:
//
// 1) Explicitly call `registry.register(elem:Element, id?:string)`, or,
// 2) Call `Registry.enableDefaultRegistry(registry)` when ready, and all future
//    elements will automatically register with it.
//
// Once an element is registered, selected attributes are tracked and indexed by
// the registry. This allows fast look up of elements by attributes like id, type,
// and class.

// Indexes are represented as maps of maps of maps. This allows
// for both multi-labeling (e.g., an element can have multiple classes)
// and efficient lookup.
class Index {
    // [attributeName][attributeValue][elementId] => Element
    val index: MutableMap<String, MutableMap<String, MutableMap<String, Element>>> = mutableMapOf()

    init {
        index["id"] = mutableMapOf()
        index["type"] = mutableMapOf()
        index["class"] = mutableMapOf()
    }
}

data class RegistryUpdate(
    val id: String,
    val name: String,
    val value: String?,
    val oldValue: String?
)

class Registry private constructor() {
    companion object {
        private var defaultRegistry: Registry? = null

        fun getDefaultRegistry(): Registry? {
            return defaultRegistry
        }

        // If you call `enableDefaultRegistry`, any new elements will auto-register with
        // the provided registry as soon as they're constructed.
        fun enableDefaultRegistry(registry: Registry) {
            defaultRegistry = registry
        }

        fun disableDefaultRegistry() {
            defaultRegistry = null
        }
    }

    protected var index: Index = Index()

    fun clear(): Registry {
        index = Index()
        return this
    }

    fun setIndexValue(name: String, value: String, id: String, elem: Element) {
        val indexMap = index.index[name]!!.getOrPut(value) { mutableMapOf() }
        indexMap[id] = elem
    }

    fun updateIndex(update: RegistryUpdate) {
        val elem = getElementById(update.id)
        if (update.oldValue != null && index.index[update.name]?.get(update.oldValue) != null) {
            index.index[update.name]?.get(update.oldValue)?.remove(update.id)
        }
        if (update.value != null && elem != null) {
            setIndexValue(update.name, update.value, elem.attrs.id, elem)
        }
    }

    fun register(elem: Element, id: String? = elem.attrs.id): Registry {
        val elementId = id ?: throw RuntimeException("Can't add element without `id` attribute to registry")

        // Manually add id to index, then update other indexes.
        elem.attrs.id = elementId
        setIndexValue("id", elementId, elementId, elem)
        updateIndex(RegistryUpdate(elementId, "type", elem.attrs.type.name, null))
        elem.onRegister(this)

        return this
    }

    fun getElementById(id: String): Element? {
        return index.index["id"]?.get(id)?.get(id)
    }

    fun getElementsByAttribute(attribute: String, value: String): List<Element> {
        val indexAttr = index.index[attribute]
        return indexAttr?.get(value)?.values?.toList() ?: emptyList()
    }

    fun getElementsByType(type: String): List<Element> {
        return getElementsByAttribute("type", type)
    }

    fun getElementsByClass(className: String): List<Element> {
        return getElementsByAttribute("class", className)
    }

    fun onUpdate(info: RegistryUpdate): Registry {
        val allowedNames = listOf("id", "type", "class")
        if (allowedNames.contains(info.name)) {
            updateIndex(info)
        }
        return this
    }
}