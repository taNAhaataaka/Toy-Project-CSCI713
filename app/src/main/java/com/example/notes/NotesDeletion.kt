package com.example.notes

internal data class NotesDeletionState(
    val notes: List<String>,
    val editingNoteIndex: Int?,
    val selectedNoteIndices: List<Int>
)

internal fun deleteNotes(
    notes: List<String>,
    indices: List<Int>,
    editingNoteIndex: Int?,
    selectedNoteIndices: List<Int>
): NotesDeletionState {
    return NotesDeletionState(
        notes = emptyList(),
        editingNoteIndex = null,
        selectedNoteIndices = emptyList()
    )
}
