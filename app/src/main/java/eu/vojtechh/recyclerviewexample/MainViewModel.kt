package eu.vojtechh.recyclerviewexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _isDeleteVisible = MutableLiveData(false)
    val isDeleteVisible: LiveData<Boolean> = _isDeleteVisible

    private val _notes = MutableLiveData<List<NoteModel>>(listOf())
    val notes: LiveData<List<NoteModel>> = _notes

    private var lastId = 1
    private var note = NoteModel(lastId, "", "")

    fun onAddNote(): NoteAddResponse {
        if (note.name.isBlank()) return NoteAddResponse.ERROR_NAME
        if (note.text.isBlank()) return NoteAddResponse.ERROR_TEXT

        val newList = _notes.value!!.toMutableList()
        newList.add(note)
        _notes.value = newList

        note = NoteModel(++lastId, "", "")

        return NoteAddResponse.SUCCESS
    }

    fun onNameChanged(newName: String) {
        note.name = newName
    }

    fun onTextChanged(newText: String) {
        note.text = newText
    }

    fun onDeletePressed() {
        _notes.value = _notes.value!!.filterNot { note -> note.isSelected }
        _isDeleteVisible.value = false
    }

    fun onNoteClick(note: NoteModel) {
        if (_isDeleteVisible.value!!) {
            onSelectChange(note)
        }
    }

    fun onNoteLongClick(note: NoteModel) {
        onSelectChange(note)
    }

    private fun onSelectChange(_note: NoteModel) {
        // Update the list with this note selected state changed
        val note = _note.copy()
        note.isSelected = !note.isSelected
        _notes.value = _notes.value!!.toList().map {
            if (it.id == note.id) note else it
        }

        // Show the delete icon accordingly
        _isDeleteVisible.value = _notes.value!!.any { item -> item.isSelected }
    }
}