package org.lwr.edgar.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_Version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ArchivosDB.crearTablaArchivos)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + ArchivosDB.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private val DB_NAME = "EdgarApp"
        private val DB_Version = 1
    }
}
