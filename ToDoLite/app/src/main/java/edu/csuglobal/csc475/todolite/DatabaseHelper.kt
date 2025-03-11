package edu.csuglobal.csc475.todolite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todolite.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "todos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TASK = "task"
        private const val COLUMN_IS_COMPLETED = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK TEXT,
                $COLUMN_IS_COMPLETED INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addToDo(todo: ToDo): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, todo.task)
            put(COLUMN_IS_COMPLETED, if (todo.isCompleted) 1 else 0)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllToDos(): List<ToDo> {
        val todos = mutableListOf<ToDo>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val task = it.getString(it.getColumnIndexOrThrow(COLUMN_TASK))
                val isCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
                todos.add(ToDo(id, task, isCompleted))
            }
        }
        return todos
    }

    fun updateToDo(todo: ToDo): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, todo.task)
            put(COLUMN_IS_COMPLETED, if (todo.isCompleted) 1 else 0)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(todo.id.toString()))
    }

    fun deleteToDo(todoId: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(todoId.toString()))
    }
}