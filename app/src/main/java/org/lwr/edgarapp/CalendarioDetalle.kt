package org.lwr.edgarapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.net.Uri
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import org.lwr.edgarapp.adapter.AdapterCalendario
import org.lwr.edgarapp.models.calendarioM
import java.util.ArrayList


class CalendarioDetalle : AppCompatActivity(), View.OnClickListener {
    lateinit var toolbar: Toolbar
    lateinit var context: Activity
    lateinit var videov: VideoView
    lateinit var play_button: ImageView
    lateinit var mediaC: MediaController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendario_detalle)
        context = this;

        inits()


    }

    private fun inits() {
        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        //toolbar.inflateMenu(R.menu.drawer);

        videov = findViewById(R.id.videov) as VideoView
        play_button = findViewById(R.id.play_button) as ImageView
        play_button.setOnClickListener(this)

        mediaC =  MediaController(this)
        var videoPath = "android.resource://org.lwr.edgarapp/"+R.raw.video_app
        var uri:Uri = Uri.parse(videoPath)
        videov.setVideoURI(uri)
        videov.setMediaController(mediaC)
        mediaC.setAnchorView(videov)
    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.play_button -> {
                reproducirVideo()
            }
        }
    }

    private fun reproducirVideo() {
        play_button.visibility = View.GONE
        videov.start()
    }


    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up

        return false
    }


}
