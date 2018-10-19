package org.lwr.edgarapp.models

/**
 * Created by Francisco on 27/11/17.
 */

class calendarioM {
    var id: String = ""
    var titulo: String = ""
    var descripcion: String = ""
    var cicloLunar: Boolean = false
    var tipo: Int = 0
    var categoria: String = ""
    var fecha: String = ""

    constructor(id: String, titulo: String, descripcion: String, cicloLunar: Boolean, tipo: Int, categoria: String, fecha: String) {
        this.id = id
        this.titulo = titulo
        this.descripcion = descripcion
        this.cicloLunar = cicloLunar
        this.tipo = tipo
        this.categoria = categoria
        this.fecha = fecha
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
