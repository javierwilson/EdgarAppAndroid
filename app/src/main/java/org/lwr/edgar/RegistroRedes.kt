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
import android.os.Build
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.Toolbar
import android.text.Html
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


class RegistroRedes : AppCompatActivity(), View.OnClickListener {
    lateinit var context: Activity
    lateinit var etFecha: EditText
    lateinit var etPhone: EditText
    lateinit var toolbar: Toolbar
    lateinit var txBienvenida:TextView

    lateinit var etFechaLayout: TextInputLayout
    lateinit var etPhoneLayout: TextInputLayout
    lateinit var myRadioGroupSexo:RadioGroup
    lateinit var radioSexButton:RadioButton
    lateinit var etEmail: EditText

    lateinit var btRegistrar: Button
    lateinit var txPais: TextView


    internal var nombre = ""
    internal var fechaNacimiento = ""
    internal var phone = ""
    internal var pais = "País"
    internal var gender = ""
    internal var id = ""
    internal var tipo = ""
    internal var email = ""



    private var Session: SharedPreferences? = null

    lateinit var mostrarPaices: RelativeLayout
    private var mProgressBar: ContentLoadingProgressBar? = null

    //pais
    lateinit var spinnerDialog: SpinnerDialog
    private var items = ArrayList<String>()
    internal var listCountry: MutableList<countryM> = ArrayList<countryM>()

    lateinit var dialogRegistro: ACProgressFlower

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_redes)
        context = this
        inits()

    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        txPais = findViewById(R.id.txPais) as TextView


        etFecha = findViewById(R.id.etFecha) as EditText

        btRegistrar = findViewById(R.id.btRegistrar) as Button
        btRegistrar.setOnClickListener(this)

        // A loading indicator
        mProgressBar = findViewById(R.id.progress_bar_login) as ContentLoadingProgressBar

        etPhone = findViewById(R.id.etPhone) as EditText
        etFechaLayout = findViewById(R.id.etFechaLayout) as TextInputLayout
        etPhoneLayout = findViewById(R.id.etPhoneLayout) as TextInputLayout
        myRadioGroupSexo = findViewById(R.id.myRadioGroupSexo) as RadioGroup
        txBienvenida = findViewById(R.id.txBienvenida) as TextView
        etEmail= findViewById(R.id.etEmail) as EditText


        mostrarPaices = findViewById(R.id.mostrarPaices) as RelativeLayout
        mostrarPaices.setOnClickListener(this)
        listCountry.clear()
        //items.addAll(Arrays.asList("Afghanistan","Albania","Algeria","American Samoa","Andorra","Angola","Anguilla","Antarctica","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Bouvet Island","Brazil","British Indian Ocean Territory","British Virgin Islands","Brunei","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Christmas Island","Cocos (Keeling) Islands","Colombia","Comoros","Congo","Cook Islands","Costa Rica","Cote d\'Ivoire","Croatia","Cuba","Cyprus","Czech Republic","Democratic Republic of the Congo","Denmark","Djibouti","Dominica","Dominican Republic","East Timor","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Faeroe Islands","Falkland Islands","Fiji","Finland","Former Yugoslav Republic of Macedonia","France","French Guiana","French Polynesia","French Southern Territories","Gabon","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe","Guam","Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Heard Island and McDonald Islands","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Niue","Norfolk Island","North Korea","Northern Marianas","Norway","Oman","Pakistan","Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Pitcairn Islands","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russia","Rwanda","Sqo Tome and Principe","Saint Helena","Saint Kitts and Nevis","Saint Lucia","Saint Pierre and Miquelon","Saint Vincent and the Grenadines","Samoa","San Marino","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Georgia and the South Sandwich Islands","South Korea","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Svalbard and Jan Mayen","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","The Bahamas","The Gambia","Togo","Tokelau","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Virgin Islands","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","United States Minor Outlying Islands","Uruguay","Uzbekistan","Vanuatu","Vatican City","Venezuela","Vietnam","Wallis and Futuna","Western Sahara","Yemen","Yugoslavia","Zambia","Zimbabwe"))
        //spinnerCountry.adapter = ArrayAdapter(this, R.layout.ms__list_item, paises)


        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey("usuario")) {
                nombre = extras.getString("usuario")
                id = extras.getString("id")
                tipo = extras.getString("tipo")
            }

        }


        var textoBienvenida = "Hola <b>$nombre</b><br>Gracias por registrarte en Edgar App<br>Para finalizar tu registro, solo debes completar la siguiente información"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txBienvenida.setText(Html.fromHtml(textoBienvenida, Html.FROM_HTML_MODE_COMPACT))
        } else {
            txBienvenida.setText(Html.fromHtml(textoBienvenida))
        }


        loadCountry()


        //spinnerDialog = SpinnerDialog(context, paises, "Select or Search City", "Close Button Text")// With No Animation

        //spinnerCountry.setItems()
        //spinnerCountry.setOnItemSelectedListener { view, position, id, item -> System.out.println("prueba") }


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
            R.id.mostrarPaices -> {
                spinnerDialog.showSpinerDialog()
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



        phone = etPhone.getText().toString().trim({ it <= ' ' })
        fechaNacimiento = etFecha.getText().toString().trim({ it <= ' ' })
        email =  etEmail.getText().toString().trim({ it <= ' ' })

        etPhoneLayout.setError(null)
        etFechaLayout.setError(null)

        if (phone.toString().equals("")) {
            etPhoneLayout.setError("Ingrese Teléfono")
            etPhoneLayout.requestFocus()
            return
        } else if (pais.toString().equals("País")) {
            Toast.makeText(context, "Seleccione País", Toast.LENGTH_SHORT).show()
            return
        }else if (fechaNacimiento.toString().equals("")) {
            Toast.makeText(context, "Ingrese Año de Nacimiento", Toast.LENGTH_SHORT).show()
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


    fun loadCountry(){

        dialogRegistro = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogRegistro.setCancelable(false)
        dialogRegistro.show()



        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLCountries,
                Response.Listener { response ->

                    dialogRegistro.dismiss()
                    val res = JSONObject(response)
                    var countries = res.getJSONArray("countries")

                    for(i in 0..countries.length()-1) {
                        val itemsCountry = countries.getJSONObject(i)

                        listCountry.add(countryM(itemsCountry.getString("id"),itemsCountry.getString("name")))
                        items.add(itemsCountry.getString("name"))

                    }

                    spinnerDialog = SpinnerDialog(context, items, "Seleccione País", R.style.DialogAnimations_SmileWindow, "Cerrar")// With 	Animation

                    spinnerDialog.bindOnSpinerListener(object : OnSpinerItemClick {
                        override fun onClick(p0: String?, position: Int) {
                            //Toast.makeText(context, p0 + "  " + p1+"", Toast.LENGTH_SHORT).show();
                            pais = listCountry.get(position).id
                            txPais.text = listCountry.get(position).nombre
                            Constants.cerrarTeclado(context)
                        }

                    })

                    System.out.println("response "+response)

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

        var nameFull = nombre.replace("\\s".toRegex(), "")

        var urlString = "";
        urlString = urlString+"?username="+nameFull.toLowerCase()
        urlString = urlString+"&email=$phone@edgar.org"
        urlString = urlString+"&password=$id"
        urlString = urlString+"&password_confirmation=$id"
        urlString = urlString+"&country_id=$pais"
        urlString = urlString+"&genre=$gender"
        urlString = urlString+"&birth_date=01/01/"+etFecha.text.toString()
        urlString = urlString+"&phone=$phone"
        urlString = urlString+"&social_id=$id"
        urlString = urlString+"&social_network=$tipo"


        System.out.println(Constants.URLCreateUser+urlString)


        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLCreateUser+urlString,
                Response.Listener { response ->

                    dialogRegistro.dismiss()
                    val res = JSONObject(response)
                    var user = res.getJSONObject("user")
                    System.out.println("response "+response)


                    Session!!.edit().putInt("id",user.getInt("id")).commit()
                    Session!!.edit().putString("username", user.getString("username")).commit()
                    Session!!.edit().putString("email", user.getString("email")).commit()
                    Session!!.edit().putString("gender", user.getString("gender")).commit()
                    Session!!.edit().putString("user_tecnhical","").commit()
                    Session!!.edit().putString("url_chat", "0").commit()
                    Session!!.edit().putString("country", user.getString("country")).commit()
                    Session!!.edit().putString("role", user.getString("role")).commit()
                    Session!!.edit().putString("id_chat", "0").commit()

                    PreferenceUtils.setUserId(user.getString("id"))
                    PreferenceUtils.setNickname(user.getString("username"))

                    connectToSendBird(user.getString("id"), user.getString("username"))



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
                        this@RegistroRedes, "" + e.code + ": " + e.message,
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
            val selectStation = Intent(this@RegistroRedes, HomeMain::class.java)
            startActivity(selectStation)
            finishAffinity()
        })
    }

    /**
     * Update the user's push token.
     */
    private fun updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(this@RegistroRedes, null)
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
                        this@RegistroRedes, "" + e.code + ":" + e.message,
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
        val year = c.get(Calendar.YEAR) - 18
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
        }, year, month, day)
        //dpd.setMinDate(cal.timeInMillis)
        dpd.show()
    }
}
