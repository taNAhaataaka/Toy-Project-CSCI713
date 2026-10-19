package com.example.notes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NotesDeletionTest {
    @Test
    fun `deletes requested notes and remaps editing and selected indices`() {
        // Arrange
        val notes = listOf("A", "B", "C", "D", "E")

        // Act
        val result = deleteNotes(
            notes = notes,
            indices = listOf(1, 3),
            editingNoteIndex = 4,
            selectedNoteIndices = listOf(0, 1, 4)
        )

        // Assert
        assertEquals(listOf("A", "C", "E"), result.notes)
        assertEquals(2, result.editingNoteIndex)
        assertEquals(listOf(0, 2), result.selectedNoteIndices)
    }

    @Test
    fun `clears editing index when its note is deleted`() {
        // Arrange
        val notes = listOf("A", "B", "C")

        // Act
        val result = deleteNotes(
            notes = notes,
            indices = listOf(1),
            editingNoteIndex = 1,
            selectedNoteIndices = emptyList()
        )

        // Assert
        assertEquals(listOf("A", "C"), result.notes)
        assertEquals(null, result.editingNoteIndex)
    }

    @Test
    fun `ignores invalid indices and duplicate deletion requests`() {
        // Arrange
        val notes = listOf("A", "B", "C")

        // Act
        val result = deleteNotes(
            notes = notes,
            indices = listOf(-1, 1, 1, 3),
            editingNoteIndex = 2,
            selectedNoteIndices = listOf(0, 1, 2)
        )

        // Assert
        assertEquals(listOf("A", "C"), result.notes)
        assertEquals(1, result.editingNoteIndex)
        assertEquals(listOf(0, 1), result.selectedNoteIndices)
    }

    @Test
    fun `preserves state when deletion indices are empty`() {
        // Arrange
        val notes = listOf("A", "B")

        // Act
        val result = deleteNotes(
            notes = notes,
            indices = emptyList(),
            editingNoteIndex = 1,
            selectedNoteIndices = listOf(0)
        )

        // Assert
        assertEquals(notes, result.notes)
        assertEquals(1, result.editingNoteIndex)
        assertEquals(listOf(0), result.selectedNoteIndices)
    }
}
