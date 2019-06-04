package org.lwr.edgar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.sendbird.android.*
import org.lwr.edgar.utils.PreferenceUtils
import org.lwr.edgar.utils.PushUtils
import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelListQuery
import org.lwr.edgar.view.CircularTextView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.andexert.library.RippleView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.lwr.edgar.chat.GroupChannelActivity
import org.lwr.edgar.chat.OpenChannelActivity
import java.util.*
import kotlin.concurrent.timerTask


class HomeMain : AppCompatActivity(), View.OnClickListener {
    lateinit var toolbar: Toolbar
    lateinit var rpCalendario: RelativeLayout
    lateinit var rpChat: RelativeLayout
    lateinit var rpDocumentos: RelativeLayout
    lateinit var rpVideoTutoriales: RelativeLayout


    private var Session: SharedPreferences? = null
    lateinit var txName: TextView
    //private var mChannelListQuery: OpenChannelListQuery? = null
    private var mChannelListQuery: GroupChannelListQuery? = null
    lateinit var circularTextView: CircularTextView
    var id_usuario = ""
    var username = ""
    var role = ""

    private var mChannelList: MutableList<OpenChannel>? = null
    private val CHANNEL_LIST_LIMIT = 5
    private val CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST"
    private val CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_LIST"
    lateinit var dialogHome: ACProgressFlower
    lateinit var context: Activity
    lateinit var mHandler: Handler
    lateinit var mRunnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        context = this
        inits()


    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)

        id_usuario = Session!!.getInt("id",0).toString()
        username = Session!!.getString("username","").toString()
        role = Session!!.getString("role","").toString()

        toolbar = findViewById(R.id.my_awesome_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.drawer);

        txName = findViewById(R.id.txName) as TextView
        txName.setText(username)

        rpCalendario = findViewById(R.id.rpCalendario) as RippleView
        rpChat = findViewById(R.id.rpChat) as RippleView
        rpDocumentos = findViewById(R.id.rpDocumentos) as RippleView
        rpVideoTutoriales = findViewById(R.id.rpVideoTutoriales) as RippleView
        circularTextView = findViewById(R.id.circularTextView) as CircularTextView
        circularTextView.setStrokeWidth(1)
        circularTextView.setStrokeColor("#ffffff")
        circularTextView.setSolidColor("#000000")
        circularTextView.visibility = View.GONE


        rpChat.setOnClickListener(View.OnClickListener {

            val timer = Timer()
            timer.schedule(timerTask {

                if(role.toString().equals("tecnhical")){
                    val intent = Intent(baseContext, GroupChannelActivity::class.java)
                    startActivity(intent)
                }else{

                    //System.out.println("role "+Session!!.getString("url_chat","").toString())


                    if (Session!!.getString("url_chat","").toString().equals("0") || Session!!.getString("url_chat","").toString().equals("null") || Session!!.getString("url_chat","").toString().equals("")) {

                        //System.out.println("role entro")

                        runOnUiThread(
                                object : Runnable {
                                    override fun run() {
                                        getAssignUser()
                                    }
                                }
                        )

                    }else{
                        val intent = Intent(baseContext, OpenChannelActivity::class.java)
                        intent.putExtra("name", "Pregunta a Edgar")
                        intent.putExtra("url", Session!!.getString("url_chat","").toString())
                        startActivity(intent)
                    }
                }
            }, 500)


        })

        rpCalendario.setOnClickListener(View.OnClickListener {

            val timer = Timer()
            timer.schedule(timerTask {
                val selectCalendario = Intent(this@HomeMain, Calendario::class.java)
                startActivity(selectCalendario)
            }, 500)


        })

        rpDocumentos.setOnClickListener(View.OnClickListener {

            val timer = Timer()
            timer.schedule(timerTask {
                val selectDocumentos = Intent(this@HomeMain, Documentos::class.java)
                startActivity(selectDocumentos)
            }, 500)


        })

        rpVideoTutoriales.setOnClickListener(View.OnClickListener {

            val timer = Timer()
            timer.schedule(timerTask {
                val selectVideo = Intent(this@HomeMain, VideoTutoriales::class.java)
                startActivity(selectVideo)
            }, 500)


        })


    }

    override fun onStart() {
        super.onStart()
        System.out.println("entroo")
        System.out.println(PreferenceUtils.getConnected())

        connectToSendBird(PreferenceUtils.getUserId(), PreferenceUtils.getNickname())

    }

    private fun connectToSendBird(userId: String, userNickname: String) {
        // Show the loading indicator
        ConnectionManager.login(userId, SendBird.ConnectHandler { user, e ->
            // Callback received; hide the progress bar.

            if (e != null) {
                // Error!
                PreferenceUtils.setConnected(false)
                return@ConnectHandler
            }

            PreferenceUtils.setConnected(true)
            updateCurrentUserPushToken()
            refresh()
        })
    }

    private fun updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(this@HomeMain, null)
    }

    public override fun onResume() {
        Log.d("LIFECYCLE", "GroupChannelListFragment onResume()")

        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)
        username = Session!!.getString("username","").toString()
        txName.setText(username)
        ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, object : ConnectionManager.ConnectionManagementHandler {
            override fun onConnected(reconnect: Boolean) {
                refresh()
            }

        })

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(baseChannel: BaseChannel, baseMessage: BaseMessage) {}

            override fun onChannelChanged(baseChannel: BaseChannel) {

                if(role.toString().equals("tecnhical")){
                    vibrarMovil()
                }else{
                    if(baseChannel.url.toString().equals(Session!!.getString("url_chat","").toString())){
                        vibrarMovil()
                    }
                }



            }

            override fun onTypingStatusUpdated(channel: GroupChannel) {


                //if(channel.url.toString().equals(Session!!.getString("url_chat","").toString())){
                    if(channel.getUnreadMessageCount()>0){
                        circularTextView.visibility = View.VISIBLE
                        circularTextView.text = ""+channel.getUnreadMessageCount()


                    }else{
                        circularTextView.visibility = View.GONE
                    }
                //}

                //System.out.println(baseChannel.url)
            }
        })

        super.onResume()
    }

    private fun vibrarMovil() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(500)
        }
    }


    private fun refresh() {
        refreshChannelList(CHANNEL_LIST_LIMIT)
    }

    private fun refreshChannelList(numChannels: Int) {
        mChannelListQuery = GroupChannel.createMyGroupChannelListQuery()
        mChannelListQuery!!.setLimit(numChannels)

        mChannelListQuery!!.next(GroupChannelListQuery.GroupChannelListQueryResultHandler { list, e ->
            if (e != null) {
                // Error!
                e.printStackTrace()
                return@GroupChannelListQueryResultHandler
            }

            for (itemsChanel in list) {

                if(itemsChanel.url.toString().equals(Session!!.getString("url_chat","").toString())){
                    if(itemsChanel.getUnreadMessageCount()>0){
                        circularTextView.visibility = View.VISIBLE
                        circularTextView.text = ""+itemsChanel.getUnreadMessageCount()
                    }else{
                        circularTextView.visibility = View.GONE
                    }
                }

            }

        })

    }

    public override fun onPause() {

        Log.d("LIFECYCLE", "GroupChannelListFragment onPause()")

        ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID)
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID)
        super.onPause()
    }


    override fun onClick(v: View?) {
        val item_id = v!!.id



                /*val intent = Intent(baseContext, OpenChannelActivity::class.java)
                intent.putExtra("name", "Pregunta a Edgar")
                intent.putExtra("url", Constants.urlChat)
                startActivity(intent)

                circularTextView.text = ""
                circularTextView.visibility = View.GONE*/

                /*val users: MutableList<String> = ArrayList()
                users.add("test1")
                users.add("test2")
                val mIsDistinct = PreferenceUtils.getGroupChannelDistinct()

                GroupChannel.createChannelWithUserIds(users, mIsDistinct, GroupChannel.GroupChannelCreateHandler { groupChannel, e ->
                    if (e != null) {    // Error.
                        return@GroupChannelCreateHandler
                    }
                })*/

                /*val users: MutableList<String> = ArrayList()
                users.add("test1")
                users.add("test2")

                val params = GroupChannelParams()
                        .setPublic(false)
                        .setEphemeral(false)
                        .setDistinct(false)
                        .addUserIds(users)
                        .setName("pruebaChat")

                GroupChannel.createChannel(params, GroupChannel.GroupChannelCreateHandler { groupChannel, e ->
                    if (e != null) {    // Error.
                        return@GroupChannelCreateHandler
                    }
                })*/

                /*mChannelListQuery = OpenChannel.createOpenChannelListQuery()
                mChannelListQuery!!.setLimit(5)
                mChannelListQuery!!.next(OpenChannelListQuery.OpenChannelListQueryResultHandler { list, e ->
                    if (e != null) {
                        e.printStackTrace()
                        return@OpenChannelListQueryResultHandler
                    }

                    mChannelList = list
                    if(mChannelList!!.size != 0){
                        var iCount = 0
                        for (item in mChannelList!!) {
                            /*if (iCount == 0){
                                iCount ++
                                val intent = Intent(baseContext, OpenChannelActivity::class.java)
                                intent.putExtra("name", item.name)
                                intent.putExtra("url", "urlChat")
                                startActivity(intent)
                            }*/
                            System.out.println("============")
                            System.out.println(item.name)
                            System.out.println(item.url)
                            System.out.println("============")
                        }
                    }else{
                        OpenChannel.createChannelWithOperatorUserIds("chat1", null, null, null, OpenChannel.OpenChannelCreateHandler { openChannel, e ->
                            if (e != null) {
                                // Error!
                                return@OpenChannelCreateHandler
                            }

                            val intent = Intent(baseContext, OpenChannelActivity::class.java)
                            intent.putExtra("name", openChannel.name)
                            intent.putExtra("url", openChannel.url)
                            startActivity(intent)
                        })
                    }





                })*/
                //val intent = Intent(this@HomeMain, DemoActivity::class.java)
                //startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.ver_perfil -> {
                val selectPerfil = Intent(this@HomeMain, Perfil::class.java)
                startActivity(selectPerfil)
            }
            R.id.cerrar_sesion -> {

                SendBird.unregisterPushTokenAllForCurrentUser { e ->
                    if (e != null) {
                        // Error!
                        e.printStackTrace()

                        // Don't return because we still need to disconnect.
                    } else {
                        //                    Toast.makeText(MainActivity.this, "All push tokens unregistered.", Toast.LENGTH_SHORT).show();
                    }

                    ConnectionManager.logout {
                        PreferenceUtils.setConnected(false)
                        Session!!.edit().putString("id", "").commit()
                        Session!!.edit().putString("username", "").commit()
                        Session!!.edit().putString("email", "").commit()
                        Session!!.edit().putString("gender", "").commit()
                        Session!!.edit().putString("user_tecnhical", "").commit()
                        Session!!.edit().putString("url_chat", "").commit()
                        Session!!.edit().putString("country", "").commit()
                        Session!!.edit().putString("role", "").commit()

                        val selectStation = Intent(this@HomeMain, Splash::class.java)
                        startActivity(selectStation)
                        finishAffinity()
                    }
                }


            }
        }
        return true
    }

    fun getAssignUser(){

        dialogHome = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogHome.setCancelable(false)
        dialogHome.show()



        var requestQueue = Volley.newRequestQueue(context)


        var urlString = "";
        urlString = urlString+"?id=$id_usuario"

        System.out.println(Constants.URLAssignUser+urlString)


        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLAssignUser+urlString,
                Response.Listener { response ->


                    val res = JSONObject(response)
                    var user = res.getJSONObject("user")

                    if(user.has("user_tecnhical")){
                        var user_tecnhical = user.getString("user_tecnhical")
                        var id_chat = user.getString("user_chat_id")

                        saveChat(user_tecnhical,id_chat)
                    }else{
                        Toast.makeText(context, "Ha ocurrido un error, inténtelo más tarde", Toast.LENGTH_SHORT).show()
                        dialogHome.dismiss()
                    }


                }, Response.ErrorListener { error ->

            Toast.makeText(context, "Ha ocurrido un error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
            VolleyLog.d("Error", "Error: " + error.message)
            dialogHome.dismiss()
        }
        ) {
        }

        // Añadir petición a la cola
        requestQueue!!.add(jsonObjRequestHome)
    }

    fun saveChat(user_tecnhical: String,id_chat: String) {


        val users: MutableList<String> = ArrayList()
        users.add(id_usuario)
        users.add(user_tecnhical)

        System.out.println(users)


        val mIsDistinct = PreferenceUtils.getGroupChannelDistinct()

        GroupChannel.createChannelWithUserIds(users, mIsDistinct,"$username",null,"","", GroupChannel.GroupChannelCreateHandler { groupChannel, e ->
            if (e != null) {
                Toast.makeText(context, "Ha ocurrido un error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
            }else{


                var requestQueue = Volley.newRequestQueue(context)


                var urlString = "";
                urlString = urlString+"?id=$id_usuario"
                urlString = urlString+"&id_chat=$id_chat"
                urlString = urlString+"&url_chat="+groupChannel.url

                System.out.println(Constants.URLSaveChat+urlString)


                val jsonObjRequestHome = object : StringRequest(
                        Request.Method.GET,
                        Constants.URLSaveChat+urlString,
                        Response.Listener { response ->


                            dialogHome.dismiss()

                            Session!!.edit().putString("user_tecnhical", user_tecnhical).commit()
                            Session!!.edit().putString("url_chat",groupChannel.url).commit()

                            val intent = Intent(baseContext, OpenChannelActivity::class.java)
                            intent.putExtra("name", "Conversando con Edgar")
                            intent.putExtra("url", groupChannel.url)
                            startActivity(intent)


                        }, Response.ErrorListener { error ->

                    Toast.makeText(context, "Ha ocurrido un error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
                    VolleyLog.d("Error", "Error: " + error.message)
                    dialogHome.dismiss()
                }
                ) {
                }

                // Añadir petición a la cola
                requestQueue!!.add(jsonObjRequestHome)

            }
        })



    }

}
