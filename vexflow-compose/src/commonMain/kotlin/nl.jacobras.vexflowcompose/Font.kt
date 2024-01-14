package nl.jacobras.vexflowcompose

interface FontInfo {
    /** CSS font-family, e.g., 'Arial', 'Helvetica Neue, Arial, sans-serif', 'Times, serif' */
    val family: String;

    /**
     * CSS font-size (e.g., '10pt', '12px').
     * For backwards compatibility with 3.0.9, plain numbers are assumed to be specified in 'pt'.
     */
    val size: Number

    /** `bold` or a number (e.g., 900) as inspired by CSS font-weight. */
    val weight: Number

    /** `italic` as inspired by CSS font-style. */
    val style: String
}