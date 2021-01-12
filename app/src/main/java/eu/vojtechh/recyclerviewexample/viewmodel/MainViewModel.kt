package eu.vojtechh.recyclerviewexample.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import eu.vojtechh.recyclerviewexample.NoteAddResponse
import eu.vojtechh.recyclerviewexample.NoteModel
import eu.vojtechh.recyclerviewexample.repository.NoteRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    /**
     * **true** if any notes are selected
     * If **true** the activity should display a delete icon
     */
    private val _isDeleteVisible = MutableLiveData(false)
    val isDeleteVisible: LiveData<Boolean> = _isDeleteVisible

    /**
     * The activity observes this field and uses it in it's UI.
     */
    private val _notes = MutableLiveData<List<NoteModel>>(listOf())
    val notes: LiveData<List<NoteModel>> = _notes

    /**
     * The activity observes this field and returns it back with [oneNewRepoNotes].
     * This should not be used as the data for a recycler view.
     */
    val notesRepo = noteRepository.getAllNotes().map {
        // Modify repository data with current selection
        notes.value!!.forEach { note ->
            it.find { repoNote -> repoNote.id == note.id }?.isSelected = note.isSelected
        }
        it
    }

    private var newNote = NoteModel.getInstance()

    /**
     * Called from the activity when Room data changes.
     *
     * @param notes new notes from Room.
     */
    fun oneNewRepoNotes(notes: List<NoteModel>) {
        _notes.value = notes
    }

    /**
     * Called when the user requests to add the [newNote] to Room.
     * The activity should also observe on the return to check for the result.
     */
    fun onAddNote() = liveData {
        when {
            newNote.name.isBlank() -> {
                emit(NoteAddResponse.ERROR_NAME)
            }
            newNote.text.isBlank() -> {
                emit(NoteAddResponse.ERROR_TEXT)
            }
            else -> {
                noteRepository.addNote(newNote)
                newNote = NoteModel.getInstance()
                emit(NoteAddResponse.SUCCESS)
            }
        }
    }

    /**
     * Called when the name of the note is changed.
     *
     * @param newName the new name that [newNote] should be updated with.
     */
    fun onNameChanged(newName: String) {
        newNote.name = newName
    }

    /**
     * Called when the text of the note is changed.
     *
     * @param newText the new text that [newNote] should be updated with.
     */
    fun onTextChanged(newText: String) {
        newNote.text = newText
    }

    /**
     * Called when the delete button from the activity's toolbar is pressed.
     *
     * Deletes all the selected notes.
     */
    fun onDeletePressed() = viewModelScope.launch {
        noteRepository.deleteNotes(notes.value!!.filter { note -> note.isSelected })
        _isDeleteVisible.value = false
    }

    /**
     * Called when a [note] is clicked.
     * Selects the [note] if the RecyclerView is currently in select mode (eg. [isDeleteVisible] is true).
     *
     * @param note the note that was clicked.
     */
    fun onNoteClick(note: NoteModel) {
        if (_isDeleteVisible.value!!) {
            onSelectChange(note)
        }
    }

    /**
     * Called when a [note] is long pressed.
     * Always marks the [note] as selected.
     *
     * @param note the note that was long pressed.
     */
    fun onNoteLongClick(note: NoteModel) {
        onSelectChange(note)
    }

    /**
     * Called when the note selection is changed.
     *
     * @param _note a note which's selection was changed
     */
    private fun onSelectChange(_note: NoteModel) {
        // Update the list with this note selected state changed
        val note = _note.copy()
        note.isSelected = !_note.isSelected
        _notes.value = _notes.value!!.toList().map {
            if (it.id == note.id) note else it
        }

        // Show the delete icon accordingly
        _isDeleteVisible.value = _notes.value!!.any { item -> item.isSelected }
    }
}