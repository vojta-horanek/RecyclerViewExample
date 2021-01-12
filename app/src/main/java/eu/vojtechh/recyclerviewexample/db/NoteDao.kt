package eu.vojtechh.recyclerviewexample.db

import androidx.lifecycle.LiveData
import androidx.room.*
import eu.vojtechh.recyclerviewexample.NoteModel

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): LiveData<List<NoteModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteModel): Long

    @Update
    suspend fun updateNote(note: NoteModel)

    @Delete
    suspend fun deleteNote(note: NoteModel)


    @Delete
    suspend fun deleteNotes(notes: List<NoteModel>)
}
