package nl.jacobras.vexflowcompose

/** Element style. */
interface ElementStyle {
    /**
     * CSS color used for the shadow.
     *
     * Examples: 'red', '#ff0000', '#ff000010', 'rgb(255,0,0)'
     *
     * See [CSS Legal Color Values](https://www.w3schools.com/cssref/css_colors_legal.asp)
     */
    val shadowColor: String?

    /**
     * Level of blur applied to shadows.
     *
     * Values that are not finite numbers greater than or equal to zero are ignored.
     */
    val shadowBlur: Number?

    /**
     * CSS color used with context fill command.
     *
     * Examples: 'red', '#ff0000', '#ff000010', 'rgb(255,0,0)'
     *
     * See [CSS Legal Color Values](https://www.w3schools.com/cssref/css_colors_legal.asp)
     */
    val fillStyle: String?

    /**
     * CSS color used with context stroke command.
     *
     * Examples: 'red', '#ff0000', '#ff000010', 'rgb(255,0,0)'
     *
     * See [CSS Legal Color Values](https://www.w3schools.com/cssref/css_colors_legal.asp)
     */
    val strokeStyle: String?

    /**
     * Line width, 1.0 by default.
     */
    val lineWidth: Number?

    /**
     * See: [SVG `stroke-dasharray` attribute](https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-dasharray)
     */
    val lineDash: String?
}

abstract class Element {
    var context: RenderContext? = null
    private val attrs = mutableMapOf<String, Any>()
    private var rendered = false
    private var style: ElementStyle? = null
    protected val fontInfo: FontInfo? = null // TODO: constructor?

    private var height = 0
    var width = 0
    private var xShift = 0
    private var yShift = 0
    var x = 0
    protected var y = 0

    /** Get the element style used for rendering. */
    fun getStyle(): ElementStyle? {
        return this.style;
    }

    /** Apply the element style to `context`. */
    fun applyStyle(
        context: RenderContext? = this.context,
        style: ElementStyle? = this.getStyle()
    ): Element {
        if (style == null) return this;
        if (context == null) return this;

        context.save();
        style.shadowColor?.let { context.setShadowColor(it); }
        style.shadowBlur?.let { context.setShadowBlur(it); }
        style.fillStyle?.let { context.setFillStyle(it); }
        style.strokeStyle?.let { context.setStrokeStyle(it); }
        style.lineWidth?.let { context.setLineWidth(it); }
        style.lineDash?.let { context.setLineDash(it.split(' ').map { it.toInt() }.toIntArray()); }

        return this;
    }

    /** Restore the style of `context`. */
    fun restoreStyle(
        context: RenderContext? = this.context,
        style: ElementStyle? = getStyle()
    ): Element {
        if (style == null) return this;
        if (context == null) return this;
        context.restore();
        return this;
    }

    abstract fun draw()

    /** Return the element attributes. */
    fun getAttributes(): Map<String, Any> {
        return attrs
    }

    /** Return an attribute, such as 'id', 'type' or 'class'. */
    fun getAttribute(name: String): Any? {
        return this.attrs[name];
    }

    /** Set an attribute such as 'id', 'class', or 'type'. */
    fun setAttribute(name: String, value: Any): Element {
        val oldID = this.attrs["id"]
        val oldValue = this.attrs[name];
        this.attrs[name] = value;
        // Register with old id to support id changes.
        //this.registry?.onUpdate({ id: oldID, name, value, oldValue }); // TODO
        return this
    }

    /** Validate and return the rendering context. */
    fun checkContext(): RenderContext {
        return context ?: error("No rendering context attached to instance.")
    }

    /** Set the rendered status. */
    fun setRendered(rendered: Boolean = true): Element {
        this.rendered = rendered;
        return this
    }
}