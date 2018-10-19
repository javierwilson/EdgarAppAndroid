package org.lwr.edgarapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import org.lwr.edgarapp.materialSpinner.MaterialSpinner
import java.util.*
import android.widget.ArrayAdapter
import org.lwr.edgarapp.R.id.spinnerCountry
import android.widget.AdapterView
import android.widget.Toast
import org.lwr.edgarapp.R.id.spinnerCountry






class Registro : AppCompatActivity(), View.OnClickListener {
    lateinit var context: Activity
    lateinit var etFecha: EditText

    lateinit var edNombre: EditText
    lateinit var etPhone: EditText
    lateinit var etPassword: EditText
    lateinit var toolbar: Toolbar

    lateinit var etNombreLayout: TextInputLayout
    lateinit var etPhoneLayout: TextInputLayout
    lateinit var etPasswordLayout: TextInputLayout

    lateinit var btRegistrar: Button


    internal var nombre = ""
    internal var fechaNacimiento = ""
    internal var phone = ""
    internal var password = ""
    internal var pais = "País"

    private var Session: SharedPreferences? = null

    lateinit var spinnerCountry: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)
        context = this;
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


        edNombre = findViewById(R.id.edNombre) as EditText
        etPhone = findViewById(R.id.etPhone) as EditText
        etPassword = findViewById(R.id.etPassword) as EditText

        etNombreLayout = findViewById(R.id.etNombreLayout) as TextInputLayout
        etPhoneLayout = findViewById(R.id.etPhoneLayout) as TextInputLayout
        etPasswordLayout = findViewById(R.id.etPasswordLayout) as TextInputLayout

        spinnerCountry = findViewById(R.id.spinnerCountry) as Spinner
        val paises = arrayOf("País","Afghanistan","Albania","Algeria","American Samoa","Andorra","Angola","Anguilla","Antarctica","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Bouvet Island","Brazil","British Indian Ocean Territory","British Virgin Islands","Brunei","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Christmas Island","Cocos (Keeling) Islands","Colombia","Comoros","Congo","Cook Islands","Costa Rica","Cote d\'Ivoire","Croatia","Cuba","Cyprus","Czech Republic","Democratic Republic of the Congo","Denmark","Djibouti","Dominica","Dominican Republic","East Timor","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Faeroe Islands","Falkland Islands","Fiji","Finland","Former Yugoslav Republic of Macedonia","France","French Guiana","French Polynesia","French Southern Territories","Gabon","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe","Guam","Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Heard Island and McDonald Islands","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Niue","Norfolk Island","North Korea","Northern Marianas","Norway","Oman","Pakistan","Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Pitcairn Islands","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russia","Rwanda","Sqo Tome and Principe","Saint Helena","Saint Kitts and Nevis","Saint Lucia","Saint Pierre and Miquelon","Saint Vincent and the Grenadines","Samoa","San Marino","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Georgia and the South Sandwich Islands","South Korea","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Svalbard and Jan Mayen","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","The Bahamas","The Gambia","Togo","Tokelau","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Virgin Islands","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","United States Minor Outlying Islands","Uruguay","Uzbekistan","Vanuatu","Vatican City","Venezuela","Vietnam","Wallis and Futuna","Western Sahara","Yemen","Yugoslavia","Zambia","Zimbabwe")
        spinnerCountry.adapter = ArrayAdapter(this, R.layout.ms__list_item, paises)


        spinnerCountry.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, pos: Int, id: Long) {
                pais = adapterView.getItemAtPosition(pos) as String
                //Toast.makeText(adapterView.context,adapterView.getItemAtPosition(pos) as String, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
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
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }

    private fun validar() {
        nombre = edNombre.text.toString().trim { it <= ' ' }
        phone = etPhone.getText().toString().trim({ it <= ' ' })
        password = etPassword.getText().toString().trim({ it <= ' ' })
        fechaNacimiento = etFecha.getText().toString().trim({ it <= ' ' })

        etNombreLayout.error = null
        etPhoneLayout.setError(null)
        etPasswordLayout.setError(null)

        if (nombre.toString().equals("")) {
            etNombreLayout.error = "Ingrese Nombre"
            return
        } else if (phone.toString().equals("")) {
            etPhoneLayout.setError("Ingrese Teléfono")
            etPhoneLayout.requestFocus()
            return
        } else if (password.length < 5) {
            etPasswordLayout.setError("Password muy corto")
            etPasswordLayout.requestFocus()
            return
        }else if (pais.toString().equals("País")) {
            Toast.makeText(context, "Seleccione País", Toast.LENGTH_SHORT).show()
            return
        }else if (fechaNacimiento.toString().equals("")) {
            Toast.makeText(context, "Ingrese Fecha de Nacimiento", Toast.LENGTH_SHORT).show()
            return
        } else {
            Session!!.edit().putString("nombreUser", nombre.toString()).commit()
            Session!!.edit().putString("correo", "").commit()

            val selectStation = Intent(this@Registro, HomeMain::class.java)
            startActivity(selectStation)
            finishAffinity()
        }


    }

    private fun showCalendario(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR) - 18
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            etFecha.setText(dayOfMonth.toString() + "-" + (monthOfYear+1) + "-" + year)
        }, year, month, day)
        //dpd.setMinDate(cal.timeInMillis)
        dpd.show()
    }
}
