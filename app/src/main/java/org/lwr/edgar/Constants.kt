package org.lwr.edgar

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.inputmethod.InputMethodManager

import java.util.regex.Pattern

/**
 * Created by Francisco on 31/5/15.
 */
object Constants {
    var URL = ""


    var conexion = "Verificar Conexión a Internet"
    var message = "Habilitar GPS. Haga clic en Aceptar para ir a la configuración de servicios de ubicación."

    //var userId = "Gabriela Lagos"
    //var userNickname = "Edgar"
    var DEVELOPER_KEY = "AIzaSyAdCDOW9RD9XfDXFQg2pdvmjZTA8r3_9gE"

    //var userId = "Williams"
    //var userNickname = "williams"


    //var urlChat = "sendbird_group_channel_101534802_be38641cfd0902c3400f05d9306a2e04c9b86c43"

    //var urlChat = "sendbird_open_channel_46599_cb87891cea6e9a7e384508023bb25975ed74087a"

    //val URL_BASE = "http://edg.codecastle.com.sv/api/v1/"
    val URL_BASE = "http://panel.proyectoedgar.com/api/v1/"


    val URLCalendario = URL_BASE+"events"
    val URLDocumentos = URL_BASE+"documents"
    val URLCountries = URL_BASE+"users/get_countries"
    val URLCreateUser = URL_BASE+"users/create_user"
    val URLGetUser = URL_BASE+"users/get_user"
    val URLAssignUser = URL_BASE+"users/assign_user"
    val URLSaveChat = URL_BASE+"users/save_url_chat"
    val URLTutorial = URL_BASE+"tutorials"
    val URLValidateSocial = URL_BASE+"users/validate_social"
    val URLForgot = URL_BASE+"users/forgot_password"
    val URLPerfil = URL_BASE+"users/user_profile?id="
    val URLPerfilUpdate = URL_BASE+"users/user_update"


    val URLFile= "http://edg.codecastle.com.sv"
    val nameFolder= "filesEdgar"




    fun verificaConexion(ctx: Activity): Boolean {
        var bConectado = false
        val connec = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val redes = connec.allNetworkInfo
        for (i in 0..1) {
            if (redes[i].state == NetworkInfo.State.CONNECTED) {
                bConectado = true
            }
        }
        return bConectado
    }


    fun isEmailValid(email: String): Boolean {
        var isValid = false

        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }


    fun cerrarTeclado(context: Activity) {
        val view = context.currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}