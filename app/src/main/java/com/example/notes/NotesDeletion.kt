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
    val indicesToDelete = indices.filter { it in notes.indices }.toSet()
    return NotesDeletionState(
        notes = notes.filterIndexed { index, _ -> index !in indicesToDelete },
        editingNoteIndex = editingNoteIndex?.let { editingIndex ->
            if (editingIndex in indicesToDelete) {
                null
            } else {
                editingIndex - indicesToDelete.count { it < editingIndex }
            }
        },
        selectedNoteIndices = selectedNoteIndices
            .filter { it !in indicesToDelete }
            .map { selectedIndex ->
                selectedIndex - indicesToDelete.count { it < selectedIndex }
            }
    )
}
