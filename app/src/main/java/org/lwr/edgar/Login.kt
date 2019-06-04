package org.lwr.edgar

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.*
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sendbird.android.SendBird
import org.json.JSONObject
import org.lwr.edgar.utils.PreferenceUtils
import org.lwr.edgar.utils.PushUtils


class Login : AppCompatActivity(), View.OnClickListener {
    lateinit var context: Activity

    lateinit var etPhone: EditText
    lateinit var etPassword: EditText
    lateinit var toolbar: Toolbar
    lateinit var txPass: TextView

    lateinit var etPhoneLayout: TextInputLayout
    lateinit var etPasswordLayout: TextInputLayout

    lateinit var rlIniciarSesion: Button

    internal var phone = ""
    internal var password = ""
    private var Session: SharedPreferences? = null
    private var mProgressBar: ContentLoadingProgressBar? = null
    lateinit var dialogRegistro: ACProgressFlower

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        context = this;
        inits()

    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        txPass = findViewById(R.id.txPass) as TextView

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        // A loading indicator
        mProgressBar = findViewById(R.id.progress_bar_login) as ContentLoadingProgressBar


        rlIniciarSesion = findViewById(R.id.rlIniciarSesion) as Button
        rlIniciarSesion.setOnClickListener(this)


        etPhone = findViewById(R.id.etPhone) as EditText
        etPassword = findViewById(R.id.etPassword) as EditText

        etPhoneLayout = findViewById(R.id.etPhoneayout) as TextInputLayout
        etPasswordLayout = findViewById(R.id.etPasswordLayout) as TextInputLayout

        txPass.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.rlIniciarSesion -> {
                validar()
            }
            R.id.txPass-> {
                popForgot()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

    private fun popForgot(){
        var dialogPop = Dialog(context)
        dialogPop.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogPop.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogPop.setContentView(R.layout.pop_forgot)

        val btCancelar: Button
        val btAceptar: Button

        lateinit var etEmail: EditText
        lateinit var etEmaillayout: TextInputLayout



        btCancelar = dialogPop.findViewById(R.id.btCancelar)
        btAceptar = dialogPop.findViewById(R.id.btAceptar)
        etEmail = dialogPop.findViewById(R.id.etEmail)
        etEmaillayout = dialogPop.findViewById(R.id.etEmaillayout)

        btAceptar.setOnClickListener {
            //dialogPop.dismiss()

            etEmail.setError(null)

            if (etEmail.text.toString().equals("")) {
                etEmaillayout.setError("Ingrese Email")
                etEmaillayout.requestFocus()
            } else {

                dialogRegistro = ACProgressFlower.Builder(context)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .text("Cargando")
                        .fadeColor(Color.DKGRAY).build()

                dialogRegistro.setCancelable(false)
                dialogRegistro.show()



                var requestQueue = Volley.newRequestQueue(context)


                var urlString = "";
                urlString = urlString+"?email="+etEmail.text.toString()


                System.out.println(Constants.URLForgot+urlString)


                val jsonObjRequestHome = object : StringRequest(
                        Request.Method.GET,
                        Constants.URLForgot+urlString,
                        Response.Listener { response ->

                            dialogRegistro.dismiss()
                            //val res = JSONObject(response)


                            Toast.makeText(context, "Correo enviado", Toast.LENGTH_SHORT).show()



                        }, Response.ErrorListener { error ->

                    Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    VolleyLog.d("Error", "Error: " + error.message)
                    dialogRegistro.dismiss()
                }
                ) {
                }

                // Añadir petición a la cola
                requestQueue!!.add(jsonObjRequestHome)
            }

        }

        btCancelar.setOnClickListener {
            dialogPop.dismiss()
        }

        dialogPop.show()
    }

    private fun validar() {
        phone = etPhone.getText().toString().trim({ it <= ' ' })
        password = etPassword.getText().toString().trim({ it <= ' ' })

        etPhoneLayout.setError(null)
        etPasswordLayout.setError(null)

        if (phone.toString().equals("")) {
            etPhoneLayout.setError("Ingrese Teléfono")
            etPhoneLayout.requestFocus()
            return
        } else if (password.length < 7) {
            etPasswordLayout.setError("Password muy corto")
            etPasswordLayout.requestFocus()
            return
        }else {

            sendLogin()
            /*Session!!.edit().putString("nombreUser", "Usuario").commit()
            Session!!.edit().putString("correo", phone.toString()).commit()




            PreferenceUtils.setUserId(Constants.userId)
            PreferenceUtils.setNickname(Constants.userNickname)

            connectToSendBird(Constants.userId, Constants.userNickname)*/


        }


    }

    override fun onStart() {
        super.onStart()

    }

    fun sendLogin(){

        dialogRegistro = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogRegistro.setCancelable(false)
        dialogRegistro.show()



        var requestQueue = Volley.newRequestQueue(context)


        var urlString = "";
        urlString = urlString+"?password=$password"
        urlString = urlString+"&phone=$phone"


        System.out.println(Constants.URLGetUser+urlString)


        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLGetUser+urlString,
                Response.Listener { response ->

                    dialogRegistro.dismiss()
                    val res = JSONObject(response)
                    var user = res.getJSONObject("user")
                    var chat = user.getJSONObject("chat")


                    System.out.println("response "+response)


                    Session!!.edit().putInt("id",user.getInt("id")).commit()
                    Session!!.edit().putString("username", user.getString("username")).commit()
                    Session!!.edit().putString("email", user.getString("email")).commit()
                    Session!!.edit().putString("gender", user.getString("gender")).commit()
                    Session!!.edit().putString("user_tecnhical", chat.getString("user_tecnhical")).commit()
                    Session!!.edit().putString("url_chat", chat.getString("url_chat")).commit()
                    Session!!.edit().putString("country", user.getString("country")).commit()
                    Session!!.edit().putString("role", user.getString("role")).commit()
                    Session!!.edit().putString("id_chat", chat.getString("id_chat")).commit()

                    PreferenceUtils.setUserId(user.getString("id"))
                    PreferenceUtils.setNickname(user.getString("username"))

                    connectToSendBird(user.getString("id"), user.getString("username"))



                }, Response.ErrorListener { error ->

            Toast.makeText(context, "Ha ocurrido un error, posiblemente los datos ingresados no son correctos", Toast.LENGTH_SHORT).show()
            VolleyLog.d("Error", "Error: " + error.message)
            dialogRegistro.dismiss()
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }


    private fun connectToSendBird(userId: String, userNickname: String) {
        // Show the loading indicator
        showProgressBar(true)
        rlIniciarSesion.setEnabled(false)

        ConnectionManager.login(userId, SendBird.ConnectHandler { user, e ->
            // Callback received; hide the progress bar.
            showProgressBar(false)

            if (e != null) {
                // Error!
                Toast.makeText(
                        this@Login, "" + e.code + ": " + e.message,
                        Toast.LENGTH_SHORT)
                        .show()

                // Show login failure snackbar
                //showSnackbar("Login to SendBird failed")
                rlIniciarSesion.setEnabled(true)
                PreferenceUtils.setConnected(false)
                return@ConnectHandler
            }

            PreferenceUtils.setConnected(true)

            // Update the user's nickname
            updateCurrentUserInfo(userNickname)
            updateCurrentUserPushToken()

            // Proceed to MainActivity
            val selectStation = Intent(this@Login, HomeMain::class.java)
            startActivity(selectStation)
            finishAffinity()
        })
    }

    /**
     * Update the user's push token.
     */
    private fun updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(this@Login, null)
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private fun updateCurrentUserInfo(userNickname: String) {
        SendBird.updateCurrentUserInfo(userNickname, null, SendBird.UserInfoUpdateHandler { e ->
            if (e != null) {
                // Error!
                Toast.makeText(
                        this@Login, "" + e.code + ":" + e.message,
                        Toast.LENGTH_SHORT)
                        .show()

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed")

                return@UserInfoUpdateHandler
            }

            PreferenceUtils.setNickname(userNickname)
        })
    }
    // Shows or hides the ProgressBar
    private fun showProgressBar(show: Boolean) {
        if (show) {
            mProgressBar!!.show()
        } else {
            mProgressBar!!.hide()
        }
    }
}
