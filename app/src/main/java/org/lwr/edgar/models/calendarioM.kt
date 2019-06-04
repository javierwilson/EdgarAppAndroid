package org.lwr.edgar.models

/**
 * Created by Francisco on 27/11/17.
 */

class calendarioM {
    var id: String = ""
    var title: String = ""
    var descripcion: String = ""
    var first_moon_cycle = 0
    var second_moon_cycle = 0
    var third_moon_cycle = 0
    var four_moon_cycle = 0
    var article: String = ""
    var start_date: String = ""
    var end_date: String = ""
    var name: String = ""
    var color: String = ""

    constructor(id: String, title: String, descripcion: String, first_moon_cycle: Int, second_moon_cycle: Int, third_moon_cycle: Int, four_moon_cycle: Int,article: String,start_date: String,end_date: String,name: String,color: String) {
        this.id = id
        this.title = title
        this.descripcion = descripcion
        this.first_moon_cycle = first_moon_cycle
        this.second_moon_cycle = second_moon_cycle
        this.third_moon_cycle = third_moon_cycle
        this.four_moon_cycle = four_moon_cycle
        this.article = article
        this.start_date = start_date
        this.end_date = end_date
        this.name = name
        this.color = color
    }


   /* constructor(id: String, imagen: String, nombre: String, categorias: String, calificacion: String, favorito: String) {
        this.id = id
        this.imagen = imagen
        this.nombre = nombre
        this.categorias = categorias
        this.calificacion = calificacion
        this.favorito = favorito
    }*/

    constructor() {

    }

}
