package nl.jacobras.vexflow

data class StaveLineConfig(
    val visible: Boolean? = null
)

data class StaveOptions(
    var bottomTextPosition: Int = 0,
    val lineConfig: MutableList<StaveLineConfig> = mutableListOf(),
    val spaceBelowStaffLn: Int = 0,
    val spaceAboveStaffLn: Int = 0,
    val verticalBarWidth: Int = 0,
    val fillStyle: String? = null,
    val leftBar: Boolean = false,
    val rightBar: Boolean = false,
    val spacingBetweenLinesPx: Int = 0,
    val topTextPosition: Int = 0,
    val numLines: Int = 0
)

// Used by Stave.format() to sort the modifiers at the beginning and end of a stave.
// The keys (computed property names) match the CATEGORY property in the
// Barline, Clef, KeySignature, TimeSignature classes.
val SORT_ORDER_BEG_MODIFIERS = mapOf(
    Category.Barline to 0,
    Category.Clef to 1,
    Category.KeySignature to 2,
    Category.TimeSignature to 3
)

val SORT_ORDER_END_MODIFIERS = mapOf(
    Category.TimeSignature to 0,
    Category.KeySignature to 1,
    Category.Barline to 2,
    Category.Clef to 3
)

class Stave(private val options: StaveOptions) : Element(category = Category.Stave) {
    private var startX = 0
    private var endX = 0
    private var clef: String? = null
    private val endClef: String? = null
    private var formatted = false
    private val measure = 0
    private var bounds: Bounds? = null
    private var modifiers = mutableListOf<StaveModifier>() // TODO: figure out type

    protected var defaultLedgerLineStyle: ElementStyle? = null

    constructor(
        x: Int,
        y: Int,
        width: Int,
        options: StaveOptions
    ) : this(
        options = options.copy(
            verticalBarWidth = 10, // Width around vertical bar end-marker
            numLines = 5,
            fillStyle = "#999999",
            leftBar = true, // draw vertical bar on left
            rightBar = true, // draw vertical bar on right
            spacingBetweenLinesPx = Tables.STAVE_LINE_DISTANCE, // in pixels
            spaceAboveStaffLn = 4, // in staff lines
            spaceBelowStaffLn = 4, // in staff lines
            topTextPosition = 1, // in staff lines
            bottomTextPosition = 4, // in staff lines
            lineConfig = mutableListOf()
        )
    ) {
        this.x = x
        this.y = y
        this.width = width
        this.startX = x + 5
        this.endX = x + width
        this.clef = "treble"

        bounds = object : Bounds {
            override val x: Double = this@Stave.x.toDouble()
            override val y: Double = this@Stave.y.toDouble()
            override val w: Double = this@Stave.width.toDouble()
            override val h: Double = 0.0
        }
        defaultLedgerLineStyle = ElementStyle(strokeStyle = "#444", lineWidth = 1.4)

        resetLines();

        // beg bar
        addModifier(Barline(type = if (options.leftBar) BarlineType.SINGLE else BarlineType.NONE))
        // end bar
        addEndModifier(Barline(type = if (options.rightBar) BarlineType.SINGLE else BarlineType.NONE))
    }

    fun resetLines() {
        options.lineConfig.clear()
        repeat(options.numLines) {
            options.lineConfig.add(StaveLineConfig(visible = true))
        }
        height = (this.options.numLines + options.spaceAboveStaffLn) * options.spacingBetweenLinesPx
        options.bottomTextPosition = this.options.numLines
    }

    fun getNumLines(): Int {
        return this.options.numLines!!
    }

    fun getTopLineTopY(): Int {
        return this.getYForLine(0) - Tables.STAVE_LINE_THICKNESS / 2;
    }

    fun getBottomLineBottomY(): Int {
        return this.getYForLine(this.getNumLines() - 1) + Tables.STAVE_LINE_THICKNESS / 2;
    }

    /**
     * Gets the pixels to shift from the beginning of the stave
     * following the modifier at the provided index
     * @param  {Number} index The index from which to determine the shift
     * @return {Number}       The amount of pixels shifted
     */
    fun getModifierXShift(index: Int = 0): Int {
        if (!this.formatted) this.format();

        if (this.getModifiers(StaveModifierPosition.BEGIN).size == 1) {
            return 0;
        }

        // for right position modifiers zero shift seems correct, see 'Volta + Modifier Measure Test'
        if (this.modifiers[index].getPosition() === StaveModifierPosition.RIGHT) {
            return 0;
        }

        var startX = this.startX - this.x;
        val begBarline = this.modifiers[0] as Barline;
        if (begBarline.type === BarlineType.REPEAT_BEGIN && startX > begBarline.width) {
            startX -= begBarline.width
        }
        return startX;
    }

    fun getSpacingBetweenLines(): Int {
        return this.options.spacingBetweenLinesPx!!
    }

    /** @returns the y for the *center* of a staff line */
    fun getYForLine(line: Int): Int {
        val options = this.options;
        val spacing = options.spacingBetweenLinesPx!!
        val headroom = options.spaceAboveStaffLn!!

        val y = this.y + line * spacing + headroom * spacing;

        return y;
    }

    fun getYForTopText(line: Int = 0): Int {
        return this.getYForLine(-line - this.options.topTextPosition!!)
    }

    // This method adds a stave modifier to the stave. Note that the first two
    // modifiers (BarLines) are automatically added upon construction.
    fun addModifier(modifier: StaveModifier, position: StaveModifierPosition? = null): Stave {
        if (position != null) {
            modifier.setPosition(position)
        }

        modifier.setStave(this)
        this.formatted = false
        this.modifiers += modifier
        return this
    }

    fun addEndModifier(modifier: StaveModifier): Stave {
        this.addModifier(modifier, StaveModifierPosition.END)
        return this
    }

    /**
     * @param position
     * @param category
     * @returns array of StaveModifiers that match the provided position and category.
     */
    fun getModifiers(position: StaveModifierPosition?, category: Category? = null): List<StaveModifier> {
        val noPosition = position === null;
        val noCategory = category === null;
        if (noPosition && noCategory) {
            return this.modifiers;
        } else if (noPosition) {
            // A category was provided.
            return this.modifiers.filter { m -> m.getCategory() == category }
        } else if (noCategory) {
            // A position was provided.
            return this.modifiers.filter { m -> position == m.getPosition() }
        } else {
            // Both position and category were provided!
            return this.modifiers.filter { m -> position == m.getPosition() && m.getCategory() == category }
        }
    }

    /**
     * Use the modifier's `getCategory()` as a key for the `order` array.
     * The retrieved value is used to sort modifiers from left to right (0 to to 3).
     */
    fun sortByCategory(items: MutableList<StaveModifier>, order: Map<Category, Int>) {
        for (i in items.size - 1 downTo 0) {
            for (j in 0 until i) {
                val categoryJ = items[j].getCategory()
                val categoryJ1 = items[j + 1].getCategory()

                if ((order[categoryJ] ?: 0) > (order[categoryJ1] ?: 0)) {
                    val temp = items[j]
                    items[j] = items[j + 1]
                    items[j + 1] = temp
                }
            }
        }
    }

    fun format() {
        val begBarline = this.modifiers.getOrNull(0) as? Barline;
        val endBarline = this.modifiers.getOrNull(1)

        val begModifiers = this.getModifiers(StaveModifierPosition.BEGIN).toMutableList()
        val endModifiers = this.getModifiers(StaveModifierPosition.END).toMutableList()

        this.sortByCategory(begModifiers, SORT_ORDER_BEG_MODIFIERS);
        this.sortByCategory(endModifiers, SORT_ORDER_END_MODIFIERS);

        if (begModifiers.size > 1 && begBarline?.type === BarlineType.REPEAT_BEGIN) {
            val firstItem = begModifiers.removeAt(0)
            begModifiers.add(firstItem)
            begModifiers.add(0, Barline(BarlineType.SINGLE))
        }

        if (endModifiers.indexOf(endBarline) > 0) {
            begModifiers.add(0, Barline(BarlineType.NONE))
        }

        var offset = 0
        var x = this.x
        for (i in 0 until begModifiers.size) {
            val modifier = begModifiers[i];
            val padding = modifier.getPadding(i + offset);
            val width = modifier.width

            x += padding;
            modifier.x = x
            x += width;

            if (padding + width == 0) offset--;
        }

        this.startX = x;
        x = this.x + this.width;

        val widths = mutableMapOf(
            "left" to 0,
            "right" to 0,
            "paddingRight" to 0,
            "paddingLeft" to 0
        )

        for (i in 0 until endModifiers.size) {
            val modifier = endModifiers[i];
            val lastBarlineIdx = if (modifier.getCategory() == Category.Barline) i else 0

            widths["right"] = 0;
            widths["left"] = 0;
            widths["paddingRight"] = 0;
            widths["paddingLeft"] = 0;
            val layoutMetrics = modifier.getLayoutMetrics();

            if (layoutMetrics != null) {
                if (i != 0) {
                    widths["right"] = layoutMetrics.xMax;
                    widths["paddingRight"] = layoutMetrics.paddingRight
                }
                widths["left"] = -layoutMetrics.xMin
                widths["paddingLeft"] = layoutMetrics.paddingLeft

                if (i == endModifiers.lastIndex) {
                    widths["paddingLeft"] = 0;
                }
            } else {
                widths["paddingRight"] = modifier.getPadding(i - lastBarlineIdx);
                if (i != 0) {
                    widths["right"] = modifier.width
                }
                if (i == 0) {
                    widths["left"] = modifier.width
                }
            }
            x -= widths["paddingRight"]!!
            x -= widths["right"]!!

            modifier.x = x

            x -= widths["left"]!!
            x -= widths["paddingLeft"]!!
        }

        this.endX = if (endModifiers.size == 1) this.x + this.width else x
        this.formatted = true
    }

    override fun draw() {
        val ctx = this.checkContext();
        this.setRendered();

        this.applyStyle();
        ctx.openGroup("stave", attrs.id)
        if (!this.formatted) this.format();

        val numLines = this.options.numLines!!
        val width = this.width;
        val x = this.x;

        // Render lines
        for (line in 0 until numLines) {
            val y = this.getYForLine(line);

            if (this.options.lineConfig?.get(line)?.visible == true) {
                ctx.beginPath();
                ctx.moveTo(x, y);
                ctx.lineTo(x + width, y);
                ctx.stroke();
            }
        }

        ctx.closeGroup();
        this.restoreStyle();

        // Draw the modifiers (bar lines, coda, segno, repeat brackets, etc.)
        for (i in 0 until modifiers.size) {
            val modifier = this.modifiers[i];
            // Only draw modifier if it has a draw function
//            if (typeof modifier.draw === 'function') { // TODO: draw() into interface, then check interface here?
            modifier.applyStyle(ctx);
            modifier.drawStave(this, this.getModifierXShift(i));
            modifier.restoreStyle(ctx);
//            }
        }

        // Render measure numbers
        if (this.measure > 0) {
            ctx.save();
            ctx.setFont(this.fontInfo!!)
            val textWidth = ctx.measureText("" + this.measure).width;
            y = this.getYForTopText(0) + 3;
            ctx.fillText("" + this.measure, this.x - textWidth / 2, y);
            ctx.restore();
        }
    }

    companion object {

        // This is the sum of the padding that normally goes on left + right of a stave during
        // drawing. Used to size staves correctly with content width.
        fun defaultPadding() = Metrics.get<Int>("Stave.padding") + Metrics.get<Int>("Stave.endPaddingMax")

        // Right padding, used by system if startX is already determined.
        fun rightPadding() = Metrics.get<Int>("Stave.endPaddingMax")
    }
}