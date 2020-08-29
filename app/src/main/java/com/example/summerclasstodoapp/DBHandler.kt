package com.example.summerclasstodoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.summerclasstodoapp.DTO.ToDo

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createToDoTable:String =
            "CREATE TABLE $TABLE_TODO ("+
                    "$COL_ID integer PRIMARY KEY AUTOINCREMENT,"+
                    "$COL_CREATED_AT datetime default CURRENT_TIMESTAMP,"+
                    "$COL_NAME name varchar);"

        val createToDoItemTable:String =
            "CREATE TABLE $TABLE_TODO_ITEM ("+
                    "$COL_ID integer PRIMARY KEY AUTOINCREMENT,"+
                    "$COL_CREATED_AT datetime default CURRENT_TIMESTAMP,"+
                    "$COL_TODO_ID integer,"+
                    "$COL_ITEM_NAME varchar,"+
                    "$COL_IS_COMPLETED boolean);"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addToDo(toDo: ToDo): Boolean{
        val db:SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        val result = db.insert(TABLE_TODO, null, cv)
        return result != (-1).toLong()
    }

    fun getToDos(): MutableList<ToDo> {
        val result: MutableList<ToDo> = ArrayList()
        val db:SQLiteDatabase  = readableDatabase
        val queryResult: Cursor = db.rawQuery("SELECT * FROM $TABLE_TODO", null)
        if (queryResult.moveToFirst()) {
            do{
                val todo = ToDo()
                todo.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                result.add(todo)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }
}