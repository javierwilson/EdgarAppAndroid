package org.lwr.edgarapp.models

/**
 * Created by Francisco on 27/11/17.
 */

class documentosM {
    var id: String = ""
    var titulo: String = ""

    constructor(id: String, titulo: String) {
        this.id = id
        this.titulo = titulo
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
