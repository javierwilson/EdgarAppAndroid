package org.lwr.edgar.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class ArchivosDB(context: Context) {

    companion object {
        val TABLE_NAME = "Archivos"

        val id = "_id"
        val name = "name"
        val idArchivo = "idArchivo"


        val crearTablaArchivos= ("create table " + TABLE_NAME + " ("
                + id + " integer primary key AUTOINCREMENT, "
                + name + " text, "
                + idArchivo + " text ); ")

        val columnas = arrayOf(name, idArchivo)
    }

    private val helper: DbHelper
    private val db: SQLiteDatabase

    init {
        helper = DbHelper(context)
        db = helper.writableDatabase
    }

    private fun generarContentValues(nameInfo: String, idArchivoInfo: String): ContentValues {
        val valores = ContentValues()
        valores.put(name, nameInfo)
        valores.put(idArchivo, idArchivoInfo)
        return valores
    }

    fun insertar(name: String, idArchivo: String) {
        db.insert(TABLE_NAME, null, generarContentValues(name, idArchivo))
    }


    fun eliminar(id: String) {
        db.delete(TABLE_NAME, "$idArchivo=?", arrayOf(id))
    }

    fun eliminarTodo() {
        db.delete(TABLE_NAME, null, null)
    }


    fun getArchivoByArchivo(idArchivoInfo:String): Cursor {
        val param = arrayOf<String>(idArchivoInfo)
        return db.rawQuery("SELECT * FROM $TABLE_NAME where $idArchivo=?", param)
    }

}
