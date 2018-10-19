package org.lwr.edgarapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast




class HomeMain : AppCompatActivity(), View.OnClickListener {
    lateinit var toolbar: Toolbar
    lateinit var rlCalendario: RelativeLayout
    lateinit var rlChat: RelativeLayout
    lateinit var rlDocumentos: RelativeLayout
    private var Session: SharedPreferences? = null
    lateinit var txName: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)
        inits()


    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)

        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.drawer);

        txName = findViewById(R.id.txName) as TextView
        txName.setText(Session!!.getString("nombreUser","").toString())

        rlCalendario = findViewById(R.id.rlCalendario) as RelativeLayout
        rlChat = findViewById(R.id.rlChat) as RelativeLayout
        rlDocumentos = findViewById(R.id.rlDocumentos) as RelativeLayout

        rlChat.setOnClickListener(this)
        rlCalendario.setOnClickListener(this)
        rlDocumentos.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.rlChat -> {

            }
            R.id.rlCalendario -> {
                val selectCalendario = Intent(this@HomeMain, Calendario::class.java)
                startActivity(selectCalendario)
            }
            R.id.rlDocumentos -> {
                val selectDocumentos = Intent(this@HomeMain, Documentos::class.java)
                startActivity(selectDocumentos)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.cerrar_sesion -> {

                Session!!.edit().putString("nombreUser", "").commit()
                Session!!.edit().putString("correo", "").commit()
                val selectStation = Intent(this@HomeMain, Splash::class.java)
                startActivity(selectStation)
                finishAffinity()
            }
        }
        return true
    }
}
