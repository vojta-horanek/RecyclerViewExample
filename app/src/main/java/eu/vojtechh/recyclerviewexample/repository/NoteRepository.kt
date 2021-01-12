package eu.vojtechh.recyclerviewexample.repository

import eu.vojtechh.recyclerviewexample.NoteModel
import eu.vojtechh.recyclerviewexample.db.NoteDao
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes() = noteDao.getAll()
    suspend fun addNote(note: NoteModel) = noteDao.insertNote(note)
    suspend fun deleteNotes(notes: List<NoteModel>) = noteDao.deleteNotes(notes)
}