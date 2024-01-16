package nl.jacobras.vexflow

interface FontInfo {
    /** CSS font-family, e.g., 'Arial', 'Helvetica Neue, Arial, sans-serif', 'Times, serif' */
    val family: String;

    /**
     * CSS font-size (e.g., '10pt', '12px').
     * For backwards compatibility with 3.0.9, plain numbers are assumed to be specified in 'pt'.
     */
    val size: Int

    val weight: FontWeight
    val style: FontStyle
}

enum class FontWeight {
    NORMAL,
    BOLD
}

enum class FontStyle {
    NORMAL,
    ITALIC
}

object Font {
    // CSS Font Sizes: 36pt == 48px == 3em == 300% == 0.5in
    /** Given a length (for units: pt, px, em, %, in, mm, cm) what is the scale factor to convert it to px? */
    val scaleToPxFrom: Map<String, Double> = mapOf(
        "pt" to 4.0 / 3,
        "px" to 1.0,
        "em" to 16.0,
        "%" to 4.0 / 25,
        "in" to 96.0,
        "mm" to 96.0 / 25.4,
        "cm" to 96.0 / 2.54
    )

    /**
     * @param fontSize a font size to convert. Can be specified as a CSS length string (e.g., '16pt', '1em')
     * or as a number (the unit is assumed to be 'pt'). See `Font.scaleToPxFrom` for the supported
     * units (e.g., pt, em, %).
     * @returns the number of pixels that is equivalent to `fontSize`
     */
    fun convertSizeToPixelValue(fontSize: String): Double {
        val numericFontSize = fontSize.toDoubleOrNull()
        return if (numericFontSize != null) {
            // Assume the numeric fontSize is specified in pt.
            numericFontSize * scaleToPxFrom["pt"]!!
        } else {
            val value = fontSize.replace(Regex("[\\d.\\s]"), "").lowercase()
            val conversionFactor = scaleToPxFrom[value] ?: 1.0
            return fontSize.toFloat() * conversionFactor
        }
    }

    /**
     * @param fontSize a font size to convert. Can be specified as a CSS length string (e.g., '16pt', '1em')
     * or as a number (the unit is assumed to be 'pt'). See `Font.scaleToPxFrom` for the supported
     * units (e.g., pt, em, %).
     * @returns the number of points that is equivalent to `fontSize`
     */
    fun convertSizeToPointValue(fontSize: String): Double {
        val numericFontSize = fontSize.toDoubleOrNull()
        return if (numericFontSize != null) {
            // Assume the numeric fontSize is specified in pt.
            numericFontSize
        } else {
            val value = fontSize.replace(Regex("[\\d.\\s]"), "").lowercase()
            val conversionFactor = (scaleToPxFrom[value] ?: 1.0) / scaleToPxFrom["pt"]!!
            return fontSize.toFloat() * conversionFactor
        }
    }
}