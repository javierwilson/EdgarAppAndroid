package org.lwr.edgar

import `in`.galaxyofandroid.spinerdialog.OnSpinerItemClick
import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import java.util.*
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sendbird.android.SendBird
import org.json.JSONObject
import org.lwr.edgar.models.countryM
import org.lwr.edgar.utils.PreferenceUtils
import org.lwr.edgar.utils.PushUtils
import java.text.SimpleDateFormat


class Perfil : AppCompatActivity(), View.OnClickListener {
    lateinit var context: Activity
    lateinit var etFecha: EditText

    lateinit var edNombre: EditText
    lateinit var etPassword: EditText
    lateinit var etPasswordConfirm: EditText
    lateinit var etEmail: EditText
    lateinit var toolbar: Toolbar
    lateinit var radioGeneroM: RadioButton
    lateinit var radioGeneroF: RadioButton



    lateinit var etNombreLayout: TextInputLayout
    lateinit var etPasswordLayout: TextInputLayout
    lateinit var etPasswordLayoutConfirm: TextInputLayout



    lateinit var myRadioGroupSexo:RadioGroup
    lateinit var radioSexButton:RadioButton

    lateinit var btRegistrar: Button


    internal var nombre = ""
    internal var fechaNacimiento = ""
    internal var phone = ""
    internal var password = ""
    internal var passwordConfirm = ""
    internal var gender = ""
    internal var email = ""

    private var Session: SharedPreferences? = null
    private var mProgressBar: ContentLoadingProgressBar? = null
    var year = 0
    var mes = 0

    lateinit var dialogRegistro: ACProgressFlower

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)
        context = this
        inits()

    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);


        etFecha = findViewById(R.id.etFecha) as EditText
        etFecha.setOnClickListener(this)

        btRegistrar = findViewById(R.id.btRegistrar) as Button
        btRegistrar.setOnClickListener(this)

        // A loading indicator
        mProgressBar = findViewById(R.id.progress_bar_login) as ContentLoadingProgressBar

        edNombre = findViewById(R.id.edNombre) as EditText
        etPassword = findViewById(R.id.etPassword) as EditText
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm) as EditText
        etEmail= findViewById(R.id.etEmail) as EditText
        etNombreLayout = findViewById(R.id.etNombreLayout) as TextInputLayout
        etPasswordLayout = findViewById(R.id.etPasswordLayout) as TextInputLayout
        etPasswordLayoutConfirm = findViewById(R.id.etPasswordLayoutConfirm) as TextInputLayout
        myRadioGroupSexo = findViewById(R.id.myRadioGroupSexo) as RadioGroup

        radioGeneroM = findViewById(R.id.radioGeneroM) as RadioButton
        radioGeneroF = findViewById(R.id.radioGeneroF) as RadioButton

        loadPerfil()

    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.etFecha -> {
                showCalendario()
            }
            R.id.btRegistrar -> {
                validar()
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }


    // Shows or hides the ProgressBar
    private fun showProgressBar(show: Boolean) {
        if (show) {
            mProgressBar!!.show()
        } else {
            mProgressBar!!.hide()
        }
    }

    private fun validar() {



        nombre = edNombre.text.toString().trim { it <= ' ' }
        password = etPassword.getText().toString().trim({ it <= ' ' })
        fechaNacimiento = etFecha.getText().toString().trim({ it <= ' ' })
        passwordConfirm = etPasswordConfirm.getText().toString().trim({ it <= ' ' })
        email =  etEmail.getText().toString().trim({ it <= ' ' })

        etNombreLayout.error = null
        etPasswordLayout.setError(null)
        etPasswordLayoutConfirm.setError(null)



        /*else if (password.length < 7) {
            etPasswordLayout.setError("Password muy corto")
            etPasswordLayout.requestFocus()
            return
        }else if (!password.toString().equals(passwordConfirm.toString())) {
            etPasswordLayoutConfirm.setError("Password no coincide")
            etPasswordLayoutConfirm.requestFocus()
            return
        }*/

        if (nombre.toString().equals("")) {
            etNombreLayout.error = "Ingrese Nombre"
            return
        }else if (fechaNacimiento.toString().equals("")) {
            Toast.makeText(context, "Ingrese Fecha de Nacimiento", Toast.LENGTH_SHORT).show()
            return
        } else {
            /*Session!!.edit().putString("nombreUser", nombre.toString()).commit()
            Session!!.edit().putString("correo", "").commit()

            PreferenceUtils.setUserId(Constants.userId)
            PreferenceUtils.setNickname(Constants.userNickname)

            connectToSendBird(Constants.userId, Constants.userNickname)*/

            var selectedId = myRadioGroupSexo.getCheckedRadioButtonId()

			// find the radiobutton by returned id
            radioSexButton = findViewById(selectedId) as RadioButton
            if(radioSexButton.text.equals("Masculino")){
                gender = "m"
            }else{
                gender = "f"
            }

            if(email.length==0){
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Si no guardas tu correo electronico, no podrás recuperar tu contraseña en un futuro")
                builder.setPositiveButton("Continuar"){dialog, which ->
                    sendRegistro()
                }

                // Display a neutral button on alert dialog
                builder.setNeutralButton("Cancelar"){_,_ ->

                }

                // Finally, make the alert dialog using builder
                var dialogPop: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialogPop.show()
            }else{
                sendRegistro()
            }




        }


    }


    fun loadPerfil(){

        dialogRegistro = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogRegistro.setCancelable(false)
        dialogRegistro.show()

        System.out.println(Constants.URLPerfil+Session!!.getInt("id",0).toString())

        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLPerfil+Session!!.getInt("id",0).toString(),
                Response.Listener { response ->

                    dialogRegistro.dismiss()

                    val res = JSONObject(response)
                    var user = res.getJSONObject("user")
                    var profile = user.getJSONObject("profile")

                    Session!!.edit().putString("username", profile.getString("username")).commit()
                    Session!!.edit().putString("email", profile.getString("email")).commit()
                    Session!!.edit().putString("gender", profile.getString("genre")).commit()

                    edNombre.setText(profile.getString("username"))
                    etEmail.setText(profile.getString("another_email"))


                    if(profile.getString("genre").toString().equals("m")){
                        radioGeneroM.isChecked = true
                    }else{
                        radioGeneroF.isChecked = true
                    }

                    var birth_date = profile.getString("birth_date")
                    val separate2 = birth_date.split("-")

                    etFecha.setText(separate2[2]+"/"+separate2[1]+"/"+separate2[0])

                    mes = separate2[1].toInt() -1

                    year = separate2[0].toInt()
                    gender = profile.getString("genre")


                }, Response.ErrorListener { error ->


            VolleyLog.d("Error", "Error: " + error.message)
            dialogRegistro.dismiss()
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }


    fun sendRegistro(){

        dialogRegistro = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogRegistro.setCancelable(false)
        dialogRegistro.show()



        var requestQueue = Volley.newRequestQueue(context)


        var urlString = "";
        urlString = urlString+"?username=$nombre"
        if(password.length!=0){
            urlString = urlString+"&password=$password"
            urlString = urlString+"&password_confirmation=$passwordConfirm"
        }


        urlString = urlString+"&genre=$gender"
        urlString = urlString+"&birth_date="+etFecha.text.toString()
        urlString = urlString+"&another_email=$email"
        urlString = urlString+"&id="+Session!!.getInt("id",0).toString()


        System.out.println(Constants.URLPerfilUpdate+urlString)


        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLPerfilUpdate+urlString,
                Response.Listener { response ->

                    dialogRegistro.dismiss()
                    val res = JSONObject(response)
                    var user = res.getJSONObject("user")
                    var profile = user.getJSONObject("profile")

                    Session!!.edit().putString("username", profile.getString("username")).commit()
                    Session!!.edit().putString("email", profile.getString("email")).commit()
                    Session!!.edit().putString("gender", profile.getString("genre")).commit()
                    Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()

                }, Response.ErrorListener { error ->

                        Toast.makeText(context, "Ha ocurrido un error, posiblemente el numero telefónico ya ha sido registrado", Toast.LENGTH_SHORT).show()
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
        btRegistrar.setEnabled(false)

        ConnectionManager.login(userId, SendBird.ConnectHandler { user, e ->
            // Callback received; hide the progress bar.
            showProgressBar(false)

            if (e != null) {
                // Error!
                Toast.makeText(
                        this@Perfil, "" + e.code + ": " + e.message,
                        Toast.LENGTH_SHORT)
                        .show()

                // Show login failure snackbar
                //showSnackbar("Login to SendBird failed")
                btRegistrar.setEnabled(true)
                PreferenceUtils.setConnected(false)
                return@ConnectHandler
            }

            PreferenceUtils.setConnected(true)

            // Update the user's nickname
            updateCurrentUserInfo(userNickname)
            updateCurrentUserPushToken()

            // Proceed to MainActivity
            val selectStation = Intent(this@Perfil, HomeMain::class.java)
            startActivity(selectStation)
            finishAffinity()
        })
    }

    /**
     * Update the user's push token.
     */
    private fun updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(this@Perfil, null)
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
                        this@Perfil, "" + e.code + ":" + e.message,
                        Toast.LENGTH_SHORT)
                        .show()

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed")

                return@UserInfoUpdateHandler
            }

            PreferenceUtils.setNickname(userNickname)
        })
    }

    private fun showCalendario(){
        val c = Calendar.getInstance()
        var yearNow = c.get(Calendar.YEAR) - year

        System.out.println("yearNow $yearNow")
        System.out.println("yearNow $year")

        val year = c.get(Calendar.YEAR) - yearNow
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            var month  = monthOfYear+1
            var monthString=""
            if(month<9){
                monthString = "0"+month
            }else{
                monthString = ""+month
            }
            etFecha.setText(dayOfMonth.toString() + "/" + (monthString) + "/" + year)
        }, year, mes, day)
        //dpd.setMinDate(cal.timeInMillis)
        dpd.show()
    }
}
