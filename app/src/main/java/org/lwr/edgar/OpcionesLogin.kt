package org.lwr.edgar

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.widget.ContentLoadingProgressBar
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.sendbird.android.SendBird
import org.json.JSONException
import org.json.JSONObject
import org.lwr.edgar.utils.PreferenceUtils
import org.lwr.edgar.utils.PushUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class OpcionesLogin : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    lateinit var context: Activity

    lateinit var rlEmail: ImageView
    lateinit var rlFb: ImageView
    lateinit var rlGoogle: ImageView

    lateinit var txRegistrar:TextView
    var emailSocial:String = ""
    var nameSocial:String = ""

    //logeo fb
    lateinit var callbackManager: CallbackManager
    lateinit var accesstoken: AccessToken
    lateinit var loginButton: LoginButton

    lateinit var googleApiClient: GoogleApiClient
    lateinit var resultados:GoogleSignInResult
    lateinit var signInButton: SignInButton;
    val SIGN_IN_CODE = 777
    private var Session: SharedPreferences? = null

    private var mProgressBar: ContentLoadingProgressBar? = null

    lateinit var dialogLogin: ACProgressFlower



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        setContentView(R.layout.opciones_login)
        context = this;
        inits()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)


        if (requestCode == SIGN_IN_CODE) {
            resultados = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(resultados);
        }
    }


    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            var account: GoogleSignInAccount = result.signInAccount!!

            Auth.GoogleSignInApi.signOut(googleApiClient)
            googleApiClient!!.disconnect()
            logeoNext(account.displayName.toString(),account.id.toString(),"google")



            //System.out.println(account.email)
            //goMainScreen()
        } else {
            Toast.makeText(context, "Algo ha salido mal, Intentelo mas tarde", Toast.LENGTH_SHORT).show()
        }

        System.out.println("edgar google "+result.toString())
    }

    private fun goMainScreen() {

        val intent = Intent(this, HomeMain::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)

        rlEmail = findViewById(R.id.rlEmail) as ImageView
        rlEmail.setOnClickListener(this)

        rlFb = findViewById(R.id.rlFb) as ImageView
        rlFb.setOnClickListener(this)

        txRegistrar = findViewById(R.id.txRegistrar) as TextView
        txRegistrar.setOnClickListener(this)
        txRegistrar.setText(Html.fromHtml("¿No posees cuenta? <u>Registrate Ahora</u>"))

        rlGoogle = findViewById(R.id.rlGoogle) as ImageView
        rlGoogle.setOnClickListener(this)

        // A loading indicator
        mProgressBar = findViewById(R.id.progress_bar_login) as ContentLoadingProgressBar

        //configuracion fb
        try {
            val info = packageManager.getPackageInfo(
                    "org.lwr.edgar",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }


        loginButton = findViewById(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("email")

        loginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {

        val accessToken = loginResult.accessToken.token
        println("aqui $accessToken")

        accesstoken = loginResult.accessToken


        val request = GraphRequest.newMeRequest(
            accesstoken
            ) { `object`, response ->
                Log.v("LoginActivity", response.toString())
                try {
                    Log.v("LoginActivity", response.toString())


                    if (`object`.has("email")) {
                        emailSocial = `object`.getString("email")
                    } else {
                        emailSocial = ""
                    }

                    var profileId = `object`.getString("id")

                    if (`object`.has("first_name")) {
                        nameSocial = `object`.getString("first_name")
                    } else {
                        nameSocial = ""
                    }

                    if (`object`.has("last_name")) {
                        nameSocial = nameSocial + " " + `object`.getString("last_name")
                    }

                    //System.out.println("object "+`object`+"")


                    //http://graph.facebook.com/1933353450024438/picture?type=large
                    Constants.cerrarTeclado(context)
                    LoginManager.getInstance().logOut();
                    logeoNext(nameSocial,profileId,"fb")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

                val parameters = Bundle()
                parameters.putString("fields", "id, first_name, last_name, email") // Parámetros que pedimos a facebook
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {}

            override fun onError(exception: FacebookException) {
            println(exception.message)
            }
        })


        //configuracion google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()


        signInButton = findViewById<View>(R.id.signInButton) as SignInButton

        signInButton.setSize(SignInButton.SIZE_WIDE)

        signInButton.setColorScheme(SignInButton.COLOR_DARK)



        /*mGoogleApiClient = GoogleApiClient.Builder(this@OpcionesLogin)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()*/

    }



    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(context, "Algo ha salido mal, Intentelo mas tarde", Toast.LENGTH_SHORT).show()
    }

    private fun logeoNext(Usuario:String,id:String,tipo:String) {

        System.out.println(tipo)
        dialogLogin = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build()

        dialogLogin.setCancelable(false)
        dialogLogin.show()

        var urlString = "";
        urlString = urlString+"?social_id=$id"
        urlString = urlString+"&social_network=$tipo"

        var requestQueue = Volley.newRequestQueue(context)

        val jsonObjRequestHome = object : StringRequest(
                Request.Method.GET,
                Constants.URLValidateSocial+urlString,
                Response.Listener { response ->

                    dialogLogin.dismiss()
                    val res = JSONObject(response)
                    System.out.println(res)
                    if(res.getBoolean("success") == true){

                        val res = JSONObject(response)
                        var user = res.getJSONObject("user")
                        var chat = user.getJSONObject("chat")


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

                    }else{
                        val i = Intent(context, RegistroRedes::class.java)
                        i.putExtra("usuario", Usuario)
                        i.putExtra("id", id)
                        i.putExtra("tipo", tipo)
                        startActivity(i)
                    }

                }, Response.ErrorListener { error ->


            VolleyLog.d("Error", "Error: " + error.message)
            dialogLogin.dismiss()
        }
        ) {
        }

        // Añadir petición a la cola
        jsonObjRequestHome.setRetryPolicy(DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 15, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        requestQueue!!.add(jsonObjRequestHome)




        /*var nameFull = Usuario.replace("\\s".toRegex(), "")

        val i = Intent(context, Registro::class.java)
        i.putExtra("nameUser", nameFull.toLowerCase())
        startActivity(i)*/

        /*Session!!.edit().putString("nombreUser", Usuario).commit()
        Session!!.edit().putString("correo", Email).commit()

        PreferenceUtils.setUserId(Constants.userId)
        PreferenceUtils.setNickname(Constants.userNickname)

        connectToSendBird(Constants.userId, Constants.userNickname,tipo)*/
    }





    private fun connectToSendBird(userId: String, userNickname: String) {
        // Show the loading indicator
        showProgressBar(true)

        ConnectionManager.login(userId, SendBird.ConnectHandler { user, e ->
            // Callback received; hide the progress bar.
            showProgressBar(false)

            if (e != null) {
                // Error!
                Toast.makeText(
                        this@OpcionesLogin, "" + e.code + ": " + e.message,
                        Toast.LENGTH_SHORT)
                        .show()

                // Show login failure snackbar
                //showSnackbar("Login to SendBird failed")
                PreferenceUtils.setConnected(false)
                return@ConnectHandler
            }

            PreferenceUtils.setConnected(true)

            // Update the user's nickname
            updateCurrentUserInfo(userNickname)
            updateCurrentUserPushToken()

            // Proceed to MainActivity
            val selectStation = Intent(this@OpcionesLogin, HomeMain::class.java)
            startActivity(selectStation)
            finishAffinity()
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

    /**
     * Update the user's push token.
     */
    private fun updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(this@OpcionesLogin, null)
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
                        this@OpcionesLogin, "" + e.code + ":" + e.message,
                        Toast.LENGTH_SHORT)
                        .show()

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed")

                return@UserInfoUpdateHandler
            }

            PreferenceUtils.setNickname(userNickname)
        })
    }

    override fun onClick(v: View?) {
        val item_id = v!!.id
        when (item_id) {
            R.id.rlFb -> {
                loginButton.performClick()
            }
            R.id.rlGoogle -> {
                //signIn();
                System.out.println("entroo")
                val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
                startActivityForResult(intent, SIGN_IN_CODE)
            }
            R.id.txRegistrar-> {
                val selectStation = Intent(this@OpcionesLogin, Registro::class.java)
                startActivity(selectStation)
            }
            R.id.rlEmail-> {
                val selectStation = Intent(this@OpcionesLogin, Login::class.java)
                startActivity(selectStation)
            }
        }
    }


}
