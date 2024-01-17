package nl.jacobras.vexflow

private var DEBUG = false

// To enable logging for this class. Set `VexFlow.EasyScore.DEBUG` to `true`.
fun L(vararg args: Any) {
//    if (DEBUG) log("VexFlow.EasyScore", args) // TODO
}

typealias CommitHook = (obj: Any, note: StemmableNote, builder: Builder) -> Unit

interface BuilderElements {
    val notes: List<StemmableNote>
    val accidentals: List<List<Accidental?>>
}

class Builder(factory: Factory) {
    var factory: Factory = factory
    var elements: BuilderElements = BuilderElements(listOf(), listOf())
    var options: BuilderOptions = BuilderOptions()
    var piece: Piece = Piece()
    var commitHooks: MutableList<CommitHook> = mutableListOf()
    var rollingDuration: String = ""

    init {
        reset()
    }

    fun reset(options: BuilderOptions = BuilderOptions()) {
        this.options = BuilderOptions(stem = "auto", clef = "treble") + options
        this.elements = BuilderElements(listOf(), listOf())
        this.rollingDuration = "8"
        resetPiece()
    }

    fun getFactory(): Factory {
        return factory
    }

    fun getElements(): BuilderElements {
        return elements
    }

    fun addCommitHook(commitHook: CommitHook) {
        commitHooks.add(commitHook)
    }

    fun resetPiece() {
        piece = Piece(rollingDuration)
    }

    fun setNoteDots(dots: List<Match>) {
        if (dots.isNotEmpty()) {
            piece.dots = dots.size
        }
    }

    fun setNoteDuration(duration: String?) {
        piece.duration = duration ?: rollingDuration
    }

    fun setNoteType(type: String?) {
        type?.let { piece.type = it }
    }

    fun addNoteOption(key: String, value: String) {
        piece.options[key] = value
    }

    fun addNote(key: String?, accid: String?, octave: String?) {
        key?.let {
            piece.chord.add(NotePiece(it, accid, octave))
        }
    }

    fun addSingleNote(key: String, accid: String?, octave: String?) {
        addNote(key, accid, octave)
    }

    fun addChord(notes: List<Match>) {
        if (notes.isNotEmpty()) {
            if (notes[0] is String) {
                addSingleNote(notes[0] as String)
            } else {
                notes.mapNotNull { it as? List<String> }
                    .forEach { addNoteList(it) }
            }
        }
    }

    fun commitPiece() {
        val options = this.options + piece.options

        val stem = options.stem.toLowerCase()
        val clef = options.clef.toLowerCase()

        val keys = piece.chord.map {
            "${it.key}${it.accid ?: ""}/${it.octave}"
        }.toTypedArray()

        val autoStem = stem == "auto"

        val note = if (piece.type?.toLowerCase() == "g") {
            factory.GhostNote(keys = keys, duration = piece.duration, dots = piece.dots)
        } else {
            factory.StaveNote(
                keys = keys,
                duration = piece.duration,
                dots = piece.dots,
                type = piece.type,
                clef = clef,
                autoStem = autoStem
            ).apply {
                if (!autoStem) {
                    setStemDirection(if (stem == "up") Stem.UP else Stem.DOWN)
                }
            }
        }

        val accidentals = piece.chord.map { notePiece ->
            notePiece.accid?.let { factory.Accidental(type = it) }
        }

        accidentals.forEachIndexed { index, accidental ->
            accidental?.let { note.addModifier(it, index) }
        }

        for (i in 0 until piece.dots) {
            Dot.buildAndAttach(listOf(note), DotOptions(all = true))
        }

        commitHooks.forEach { it(options, note, this) }

        elements.notes.add(note)
        elements.accidentals.add(accidentals)
        resetPiece()
    }

    private fun addNoteList(noteList: List<String>) {
        if (noteList.size >= 3) {
            addNote(
                key = noteList[0],
                accid = noteList[1].takeIf { it.isNotEmpty() },
                octave = noteList[2].takeIf { it.isNotEmpty() }
            )
        }
    }
}

class EasyScore {

    fun parse(line: String, options: Map<String, Any> = mutableMapOf()): Result {
        builder.reset(options)
        val result = parser.parse(line)
        if (!result.success && options.throwOnError) {
            L(result)
            error("Error parsing line: $line")
        }
        return result
    }

    fun notes(line: String, options: Map<String, Any> = mutableMapOf()): List<StemmableNote> {
        var options = options.copy(clef = this.defaults.clef, stem = this.defaults.stem)
        parse(line, options)
        return builder.getElements().notes
    }

    fun voice(notes: List<Note>, options: VoiceOptions = VoiceOptions()): Voice {
        val voiceOptions = VoiceOptions(time = this.defaults.time, options = options.options)
        return this.factory.Voice(voiceOptions).addTickables(notes)
    }
}