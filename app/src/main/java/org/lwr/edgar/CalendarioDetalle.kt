package org.lwr.edgar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*


class CalendarioDetalle : AppCompatActivity(), View.OnClickListener {
    lateinit var toolbar: Toolbar
    lateinit var context: Activity
    lateinit var videov: VideoView
    lateinit var play_button: ImageView
    lateinit var mediaC: MediaController

    var txFecha: TextView? = null
    var txCategoria: TextView? = null
    var txTitulo: TextView? = null
    var moon_uno: ImageView? = null
    var moon_dos: ImageView? = null
    var moon_tres: ImageView? = null
    var moon_cuatro: ImageView? = null
    var encabezado: RelativeLayout?=null
    var mWebView: WebView? = null

    var id=""
    var titulo=""
    var start_date=""
    var end_date=""
    var name=""
    var article=""
    var color=""
    var first_moon_cycle=0
    var second_moon_cycle=0
    var third_moon_cycle=0
    var four_moon_cycle=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendario_detalle)
        context = this;

        inits()


    }

    private fun inits() {


        val extras = intent.extras
        if (extras != null) {
            id = extras.getString("id")
            start_date = extras.getString("start_date")
            end_date = extras.getString("end_date")
            name = extras.getString("name")
            article = extras.getString("article")
            first_moon_cycle = extras.getInt("first_moon_cycle")
            second_moon_cycle = extras.getInt("second_moon_cycle")
            third_moon_cycle = extras.getInt("third_moon_cycle")
            four_moon_cycle = extras.getInt("four_moon_cycle")
            color = extras.getString("color")
            titulo = extras.getString("title")
        }


        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        //toolbar.inflateMenu(R.menu.drawer);


        txFecha = findViewById(R.id.txFecha) as TextView
        txTitulo = findViewById(R.id.txTitulo) as TextView
        txCategoria = findViewById(R.id.txCategoria) as TextView
        encabezado = findViewById(R.id.encabezado) as RelativeLayout

        moon_uno = findViewById(R.id.moon_uno) as ImageView
        moon_dos = findViewById(R.id.moon_dos) as ImageView
        moon_tres = findViewById(R.id.moon_tres) as ImageView
        moon_cuatro = findViewById(R.id.moon_cuatro) as ImageView


        videov = findViewById(R.id.videov) as VideoView
        play_button = findViewById(R.id.play_button) as ImageView
        play_button.setOnClickListener(this)


        txFecha!!.setText(start_date+" - "+end_date)
        txTitulo!!.setText(titulo)
        txCategoria!!.setText(name)

        /*if (calendarioItem.cicloLunar == true) {
            holder.rlCiclo!!.visibility =  View.VISIBLE
        }else {
            holder.rlCiclo!!.visibility =  View.GONE
        }*/

        var sdk = android.os.Build.VERSION.SDK_INT


        setDrawableFilterColor(context, Color.parseColor(color), encabezado!!.getBackground())


        if(first_moon_cycle!= 0){
            moon_uno!!.visibility = View.VISIBLE
        }else{
           moon_uno!!.visibility = View.GONE
        }

        if(second_moon_cycle!= 0){
            moon_dos!!.visibility = View.VISIBLE

        }else{
            moon_dos!!.visibility = View.GONE
        }

        if(third_moon_cycle!= 0){
            moon_tres!!.visibility = View.VISIBLE

        }else{
            moon_tres!!.visibility = View.GONE
        }

        if(four_moon_cycle!= 0){
            moon_cuatro!!.visibility = View.VISIBLE

        }else{
            moon_cuatro!!.visibility = View.GONE
        }


        mWebView = findViewById<View>(R.id.webview) as WebView
        mWebView!!.getSettings().setJavaScriptEnabled(true);
        mWebView!!.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //v1
        mWebView!!.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView!!.getSettings().setSupportMultipleWindows(true);
        mWebView!!.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)


        val textoWeb = "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/fonts/ubuntu_r.ttf\")" +
                "}" +
                "body {font-family: MyFont;font-size: medium;font-weight: 300;font-size: 14px;line-height: 20px;margin-left: 16px;margin-right: 16px;}" +
                ".nota_text2 {font-weight: 300;font-size: 12px;line-height: 18px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                article +
                "</body>" +
                "</html>"

        mWebView!!.loadData(textoWeb, "text/html", "UTF-8")

        /*mediaC =  MediaController(this)
        var videoPath = "android.resource://org.lwr.edgarapp/"+R.raw.video_app
        var uri:Uri = Uri.parse(videoPath)
        videov.setVideoURI(uri)
        videov.setMediaController(mediaC)
        mediaC.setAnchorView(videov)*/
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

    fun setDrawableFilterColor(context: Context, colorResource: Int, drawable: Drawable) {

        //val filterColor = Color.parseColor(context.resources.getString(colorResource))
        drawable.colorFilter = PorterDuffColorFilter(colorResource, PorterDuff.Mode.MULTIPLY)

    }
}
