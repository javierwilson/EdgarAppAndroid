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
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import java.util.*


class Login : AppCompatActivity(), View.OnClickListener {
    lateinit var context: Activity

    lateinit var etPhone: EditText
    lateinit var etPassword: EditText
    lateinit var toolbar: Toolbar

    lateinit var etPhoneLayout: TextInputLayout
    lateinit var etPasswordLayout: TextInputLayout

    lateinit var rlIniciarSesion: Button

    internal var phone = ""
    internal var password = ""
    private var Session: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        context = this;
        inits()

    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);



        rlIniciarSesion = findViewById(R.id.rlIniciarSesion) as Button
        rlIniciarSesion.setOnClickListener(this)


        etPhone = findViewById(R.id.etPhone) as EditText
        etPassword = findViewById(R.id.etPassword) as EditText

        etPhoneLayout = findViewById(R.id.etPhoneayout) as TextInputLayout
        etPasswordLayout = findViewById(R.id.etPasswordLayout) as TextInputLayout

    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.rlIniciarSesion -> {
                validar()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
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
        } else if (password.length < 5) {
            etPasswordLayout.setError("Password muy corto")
            etPasswordLayout.requestFocus()
            return
        }else {
            Session!!.edit().putString("nombreUser", "Usuario").commit()
            Session!!.edit().putString("correo", phone.toString()).commit()

            val selectStation = Intent(this@Login, HomeMain::class.java)
            startActivity(selectStation)
            finishAffinity()
        }


    }


}
