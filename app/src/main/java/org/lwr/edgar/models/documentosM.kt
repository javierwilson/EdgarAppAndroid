package org.lwr.edgar.models

/**
 * Created by Francisco on 27/11/17.
 */

class documentosM {
    var id: String = ""
    var titulo: String = ""
    var file: String = ""
    var descargado:Boolean = false
    lateinit var formato: String

    constructor(id: String, titulo: String,file:String,descargado:Boolean,formato:String) {
        this.id = id
        this.titulo = titulo
        this.file = file
        this.descargado = descargado
        this.formato = formato
    }


   /* constructor(id: String, imagen: String, nombre: String, categorias: String, calificacion: String, favorito: String) {
        this.id = id
        this.imagen = imagen
        this.nombre = nombre
        this.categorias = categorias
        this.calificacion = calificacion
        this.favorito = favorito
    }

    constructor() {
        this.id = id
        this.imagen = imagen
        this.nombre = nombre
        this.categorias = categorias
        this.calificacion = calificacion
        this.localidad = localidad
        this.horario = horario
    }*/

}
