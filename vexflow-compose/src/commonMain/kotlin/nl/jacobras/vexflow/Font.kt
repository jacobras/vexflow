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