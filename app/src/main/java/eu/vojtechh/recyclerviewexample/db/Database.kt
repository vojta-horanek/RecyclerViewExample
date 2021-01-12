package eu.vojtechh.recyclerviewexample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.vojtechh.recyclerviewexample.NoteModel

@Database(
    entities = [NoteModel::class],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}