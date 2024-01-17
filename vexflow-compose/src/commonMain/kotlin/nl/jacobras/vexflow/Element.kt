package nl.jacobras.vexflow

class ElementAttributes(
    var id: String,
    val type: Category, // TODO: changed from String, not sure if right
    var `class`: String = "",
    val otherAttributes: MutableMap<String, String?> = mutableMapOf()
)

/** Element style. */
data class ElementStyle(
    /**
     * CSS color used for the shadow.
     *
     * Examples: 'red', '#ff0000', '#ff000010', 'rgb(255,0,0)'
     *
     * See [CSS Legal Color Values](https://www.w3schools.com/cssref/css_colors_legal.asp)
     */
    val shadowColor: String? = null,

    /**
     * Level of blur applied to shadows.
     *
     * Values that are not finite numbers greater than or equal to zero are ignored.
     */
    val shadowBlur: Number? = null,

    /**
     * CSS color used with context fill command.
     *
     * Examples: 'red', '#ff0000', '#ff000010', 'rgb(255,0,0)'
     *
     * See [CSS Legal Color Values](https://www.w3schools.com/cssref/css_colors_legal.asp)
     */
    val fillStyle: String? = null,

    /**
     * CSS color used with context stroke command.
     *
     * Examples: 'red', '#ff0000', '#ff000010', 'rgb(255,0,0)'
     *
     * See [CSS Legal Color Values](https://www.w3schools.com/cssref/css_colors_legal.asp)
     */
    val strokeStyle: String? = null,

    /**
     * Line width, 1.0 by default.
     */
    val lineWidth: Double = 1.0,

    /**
     * See: [SVG `stroke-dasharray` attribute](https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-dasharray)
     */
    val lineDash: String? = null
)

/**
 * Element implements a generic base class for VexFlow, with implementations
 * of general functions and properties that can be inherited by all VexFlow elements.
 *
 * The Element handles style and font properties for the Element and any child
 * elements, along with working with the Registry to create unique ids.
 *
 * The `text` is a series of unicode characters (including SMuFL codes).
 * The `textFont` property contains information required to style the text (i.e., font family, size, weight, and style).
 * This font family is a comma separated list of fonts.
 * The method `measureText` calculates the `textMetrics`, `boundingBox`, `height` and `width` of the `text`.
 * The method `renderText(...)` will render the text using the provided context and coordinates,
 * taking `xShift` and `yShift` into account.
 */
abstract class Element(category: Category?) {
    // Element objects keep a list of children that they are responsible for.
    // Children inherit the style from their parents (see: setGroupStyle(s)).
    protected val children: MutableList<Element> = mutableListOf()
    var context: RenderContext? = null
    val attrs = ElementAttributes(
        id = newID(),
        type = (category ?: Category.Element),
        `class` = ""
    )
    private var rendered = false
    private var style: ElementStyle? = null
    private var registry: Registry? = null
    protected val fontInfo: FontInfo = Metrics.getFontInfo(attrs.type.name)
    private val fontScale = Metrics.get<Number>("${attrs.type.name}.fontScale")
    private val text = ""
    private var metricsValid = false
    private val textMetrics: TextMetrics = TextMetrics(
        fontBoundingBoxAscent = 0,
        fontBoundingBoxDescent = 0,
        actualBoundingBoxAscent = 0,
        actualBoundingBoxDescent = 0,
        actualBoundingBoxLeft = 0,
        actualBoundingBoxRight = 0,
        width = 0
    )

    protected var height = 0
    var width = 0
    private var xShift = 0
    private var yShift = 0
    var x = 0
    protected var y = 0

    init {
        // If a default registry exist, then register with it right away.
        Registry.getDefaultRegistry()?.register(this)
    }

    /**
     * Adds a child Element to the Element, which lets it inherit the
     * same style as the parent when setGroupStyle() is called.
     *
     * Examples of children are noteheads and stems.  Modifiers such
     * as Accidentals are generally not set as children.
     *
     * Note that StaveNote calls setGroupStyle() when setStyle() is called.
     */
    fun addChildElement(child: Element): Element {
        this.children += child
        return this
    }

    fun getCategory(): Category {
        return attrs.type
    }

    /**
     * Set the element style used to render.
     *
     * Example:
     * ```typescript
     * element.setStyle({ fillStyle: 'red', strokeStyle: 'red' });
     * element.draw();
     * ```
     * Note: If the element draws additional sub-elements (ie.: Modifiers in a Stave),
     * the style can be applied to all of them by means of the context:
     * ```typescript
     * element.setStyle({ fillStyle: 'red', strokeStyle: 'red' });
     * element.getContext().setFillStyle('red');
     * element.getContext().setStrokeStyle('red');
     * element.draw();
     * ```
     * or using drawWithStyle:
     * ```typescript
     * element.setStyle({ fillStyle: 'red', strokeStyle: 'red' });
     * element.drawWithStyle();
     * ```
     */
    fun setStyle(style: ElementStyle?): Element {
        this.style = style
        return this
    }

    /** Set the element & associated children style used for rendering. */
    fun setGroupStyle(style: ElementStyle): Element {
        this.style = style
        children.forEach { it.setGroupStyle(style) }
        return this
    }

    /** Get the element style used for rendering. */
    fun getStyle(): ElementStyle? {
        return this.style
    }

    /** Apply the element style to `context`. */
    fun applyStyle(
        context: RenderContext? = this.context,
        style: ElementStyle? = this.getStyle()
    ): Element {
        if (style == null) return this
        if (context == null) return this

        context.save()
        style.shadowColor?.let { context.setShadowColor(it); }
        style.shadowBlur?.let { context.setShadowBlur(it); }
        style.fillStyle?.let { context.setFillStyle(it); }
        style.strokeStyle?.let { context.setStrokeStyle(it); }
        style.lineWidth?.let { context.setLineWidth(it); }
        style.lineDash?.let { context.setLineDash(it.split(' ').map { it.toInt() }.toIntArray()); }

        return this
    }

    /** Restore the style of `context`. */
    fun restoreStyle(
        context: RenderContext? = this.context,
        style: ElementStyle? = getStyle()
    ): Element {
        if (style == null) return this
        if (context == null) return this
        context.restore()
        return this
    }

    /**
     * Draw the element and all its sub-elements (ie.: Modifiers in a Stave)
     * with the element's style (see `getStyle()` and `setStyle()`)
     */
    fun drawWithStyle() {
        this.checkContext()
        this.applyStyle()
        this.draw()
        this.restoreStyle()
    }

    abstract fun draw()

    /** Check if it has a class label (An element can have multiple class labels). */
    fun hasClass(className: String): Boolean {
        return attrs.`class`.split(" ").contains(className)
    }

    /** Add a class label (An element can have multiple class labels). */
    fun addClass(className: String): Element {
        if (hasClass(className)) return this
        if (attrs.`class`.isEmpty()) {
            attrs.`class` = className
        } else {
            attrs.`class` = "${attrs.`class`} $className"
        }
        this.registry?.onUpdate(
            RegistryUpdate(
                id = attrs.id,
                name = "class",
                value = className,
                oldValue = null,
            )
        )
        return this
    }

    /** Remove a class label (An element can have multiple class labels). */
    fun removeClass(className: String): Element {
        if (!hasClass(className)) return this
        val arr = attrs.`class`.split(' ').toMutableList()
        arr.remove(className)
        attrs.`class` = arr.joinToString(" ")
        registry?.onUpdate(
            RegistryUpdate(
                id = attrs.id,
                name = "class",
                value = null,
                oldValue = className
            )
        )
        return this
    }

    /** Call back from registry after the element is registered. */
    fun onRegister(registry: Registry): Element {
        this.registry = registry
        return this
    }

//    /** Return an attribute, such as 'id', 'type' or 'class'. */
//    fun getAttribute(name: String): Any? {
//        return this.attrs[name];
//    }
//
//    /** Set an attribute such as 'id', 'class', or 'type'. */
//    fun setAttribute(name: String, value: String?): Element {
//        val oldID = this.attrs["id"] as String
//        val oldValue = this.attrs[name] as String
//        if (value != null) {
//            this.attrs[name] = value
//        } else {
//            // TODO: remove key?
//        }
//        // Register with old id to support id changes.
//        this.registry?.onUpdate(RegistryUpdate(id = oldID, name, value, oldValue)); // TODO
//        return this
//    }

    /** Get the boundingBox. */
    fun getBoundingBox(): BoundingBox {
        return BoundingBox(
            x = (x + xShift).toDouble(),
            y = (y + yShift - textMetrics.actualBoundingBoxAscent).toDouble(),
            w = width.toDouble(),
            h = height.toDouble()
        )
    }

    /** Validate and return the rendering context. */
    fun checkContext(): RenderContext {
        return context ?: error("No rendering context attached to instance.")
    }

    /** Set the rendered status. */
    fun setRendered(rendered: Boolean = true): Element {
        this.rendered = rendered
        return this
    }

    companion object {
        private var ID: Int = 1000

        fun newID(): String {
            return "auto${ID++}"
        }
    }
}