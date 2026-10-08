package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotesScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun NotesScreen(modifier: Modifier = Modifier) {
    var noteText by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var editingNoteIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var editText by rememberSaveable { mutableStateOf("") }
    var pendingDeleteIndices by rememberSaveable { mutableStateOf<List<Int>?>(null) }
    var selectedNoteIndices by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    var isSelectionMode by rememberSaveable { mutableStateOf(false) }
    val trimmedNote = noteText.trim()
    val trimmedEdit = editText.trim()

    fun deleteNotes(indices: List<Int>) {
        val indicesToDelete = indices.toSet()
        notes = notes.filterIndexed { index, _ -> index !in indicesToDelete }
        editingNoteIndex = editingNoteIndex?.let { editingIndex ->
            if (editingIndex in indicesToDelete) {
                null
            } else {
                editingIndex - indicesToDelete.count { it < editingIndex }
            }
        }
        selectedNoteIndices = selectedNoteIndices
            .filter { it !in indicesToDelete }
            .map { selectedIndex ->
                selectedIndex - indicesToDelete.count { it < selectedIndex }
            }
    }

    if (pendingDeleteIndices != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteIndices = null },
            title = {
                Text(
                    pluralStringResource(
                        R.plurals.delete_confirmation_title,
                        pendingDeleteIndices!!.size,
                        pendingDeleteIndices!!.size
                    )
                )
            },
            text = {
                Text(
                    pluralStringResource(
                        R.plurals.delete_confirmation_message,
                        pendingDeleteIndices!!.size,
                        pendingDeleteIndices!!.size
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteNotes(pendingDeleteIndices!!)
                        pendingDeleteIndices = null
                        selectedNoteIndices = emptyList()
                        isSelectionMode = false
                    }
                ) {
                    Text(stringResource(R.string.delete_note))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteIndices = null }) {
                    Text(stringResource(R.string.cancel_edit))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.notes_title),
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.note_input_label)) },
            minLines = 3,
            maxLines = 6
        )
        Button(
            onClick = {
                notes = listOf(trimmedNote) + notes
                editingNoteIndex = editingNoteIndex?.plus(1)
                selectedNoteIndices = selectedNoteIndices.map { it + 1 }
                noteText = ""
            },
            enabled = trimmedNote.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_note))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.notes_section_title),
                style = MaterialTheme.typography.titleLarge
            )
            if (notes.isNotEmpty()) {
                if (isSelectionMode) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(
                            onClick = {
                                selectedNoteIndices = emptyList()
                                isSelectionMode = false
                            }
                        ) {
                            Text(stringResource(R.string.cancel_edit))
                        }
                        TextButton(
                            onClick = { pendingDeleteIndices = selectedNoteIndices },
                            enabled = selectedNoteIndices.isNotEmpty()
                        ) {
                            Text(
                                stringResource(
                                    R.string.delete_selected_notes,
                                    selectedNoteIndices.size
                                )
                            )
                        }
                    }
                } else {
                    TextButton(onClick = { isSelectionMode = true }) {
                        Text(stringResource(R.string.select_notes))
                    }
                }
            }
        }
        if (notes.isEmpty()) {
            Text(
                text = stringResource(R.string.notes_empty),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(notes) { index, note ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isSelectionMode) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Checkbox(
                                        checked = index in selectedNoteIndices,
                                        onCheckedChange = { isChecked ->
                                            selectedNoteIndices = if (isChecked) {
                                                selectedNoteIndices + index
                                            } else {
                                                selectedNoteIndices - index
                                            }
                                        }
                                    )
                                    Text(
                                        text = note,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            } else if (editingNoteIndex == index) {
                                OutlinedTextField(
                                    value = editText,
                                    onValueChange = { editText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(stringResource(R.string.edit_note_label)) },
                                    minLines = 3,
                                    maxLines = 6
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            notes = notes.toMutableList().also {
                                                it[index] = trimmedEdit
                                            }
                                            editingNoteIndex = null
                                        },
                                        enabled = trimmedEdit.isNotEmpty()
                                    ) {
                                        Text(stringResource(R.string.save_note))
                                    }
                                    Button(
                                        onClick = { editingNoteIndex = null }
                                    ) {
                                        Text(stringResource(R.string.cancel_edit))
                                    }
                                    Button(onClick = { pendingDeleteIndices = listOf(index) }) {
                                        Text(stringResource(R.string.delete_note))
                                    }
                                }
                            } else {
                                Text(
                                    text = note,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            editingNoteIndex = index
                                            editText = note
                                        }
                                    ) {
                                        Text(stringResource(R.string.edit_note))
                                    }
                                    Button(onClick = { pendingDeleteIndices = listOf(index) }) {
                                        Text(stringResource(R.string.delete_note))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}