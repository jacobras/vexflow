package nl.jacobras.vexflow

object Metrics {

    /** Use the provided key to look up a FontInfo in CommonMetrics. **/
    fun getFontInfo(key: String): FontInfo {
        return object : FontInfo {
            override val family: String = get("${key}.fontFamily")
            override val size: Int = (get<Int>("${key}.fontSize") * get<Float>("${key}.fontScale")).toInt()
            override val weight: FontWeight = get("${key}.fontWeight")
            override val style: FontStyle = get("${key}.fontStyle")
        }
    }

    fun <T : Any> get(key: String, defaultValue: Any? = null): T {
        return (MetricsDefaults[key] ?: defaultValue) as T
    }
}

val MetricsDefaults = mapOf(
    "fontFamily" to "Bravura,Academico",
    "fontSize" to 30,
    "fontScale" to 1.0,
    "fontWeight" to FontWeight.NORMAL,
    "fontStyle" to FontStyle.NORMAL,

    "Accidental" to mapOf(
        "cautionary" to mapOf(
            "fontSize" to 20
        ),
        "grace" to mapOf(
            "fontSize" to 25
        ),
        "noteheadAccidentalPadding" to 1,
        "leftPadding" to 2,
        "accidentalSpacing" to 3
    ),
    "Annotation" to mapOf(
        "fontSize" to 10
    ),
    "Bend" to mapOf(
        "fontSize" to 10
    ),
    "ChordSymbol" to mapOf(
        "fontSize" to 12,
        "spacing" to 0.05,
        "subscriptOffset" to 0.2,
        "superscriptOffset" to -0.4,
        "superSubRatio" to 0.6
    ),
    "FretHandFinger" to mapOf(
        "fontSize" to 9,
        "fontWeight" to "bold"
    ),
    "GraceNote" to mapOf(
        "fontScale" to 2 / 3.0
    ),
    "GraceTabNote" to mapOf(
        "fontScale" to 2 / 3.0
    ),
    "NoteHead" to mapOf(
        "minPadding" to 2
    ),
    "PedalMarking" to mapOf(
        "text" to mapOf(
            "fontSize" to 12,
            "fontStyle" to "italic"
        )
    ),
    "Repetition" to mapOf(
        "text" to mapOf(
            "fontSize" to 12,
            "fontWeight" to "bold",
            "offsetX" to 12,
            "offsetY" to 25,
            "spacing" to 5
        ),
        "coda" to mapOf(
            "offsetY" to 25
        ),
        "segno" to mapOf(
            "offsetY" to 10
        )
    ),
    "Stave" to mapOf(
        "fontSize" to 8,
        "padding" to 12,
        "endPaddingMax" to 10,
        "endPaddingMin" to 5,
        "unalignedNotePadding" to 10
    ),
    "StaveConnector" to mapOf(
        "text" to mapOf(
            "fontSize" to 16
        )
    ),
    "StaveLine" to mapOf(
        "fontSize" to 10
    ),
    "StaveSection" to mapOf(
        "fontSize" to 10,
        "fontWeight" to "bold"
    ),
    "StaveTempo" to mapOf(
        "fontSize" to 14,
        "glyph" to mapOf(
            "fontSize" to 25
        ),
        "name" to mapOf(
            "fontWeight" to "bold"
        )
    ),
    "StaveText" to mapOf(
        "fontSize" to 16
    ),
    "StaveTie" to mapOf(
        "fontSize" to 10
    ),
    "StringNumber" to mapOf(
        "fontSize" to 10,
        "fontWeight" to "bold",
        "verticalPadding" to 8,
        "stemPadding" to 2,
        "leftPadding" to 5,
        "rightPadding" to 6
    ),
    "Stroke" to mapOf(
        "text" to mapOf(
            "fontSize" to 10,
            "fontStyle" to "italic",
            "fontWeight" to "bold"
        )
    ),
    "TabNote" to mapOf(
        "text" to mapOf(
            "fontSize" to 9
        )
    ),
    "TabSlide" to mapOf(
        "fontSize" to 10,
        "fontStyle" to "italic",
        "fontWeight" to "bold"
    ),
    "TabTie" to mapOf(
        "fontSize" to 10
    ),
    "TextBracket" to mapOf(
        "fontSize" to 15,
        "fontStyle" to "italic"
    ),
    "TextNote" to mapOf(
        "text" to mapOf(
            "fontSize" to 12
        )
    ),
    "Tremolo" to mapOf(
        "spacing" to 7
    ),
    "Volta" to mapOf(
        "fontSize" to 9,
        "fontWeight" to "bold"
    )
)