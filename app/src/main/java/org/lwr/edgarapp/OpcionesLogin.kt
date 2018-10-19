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
import android.content.pm.PackageManager
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
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
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class OpcionesLogin : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    lateinit var context: Activity

    lateinit var rlEmail: RelativeLayout
    lateinit var rlFb: RelativeLayout
    lateinit var rlGoogle: RelativeLayout

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

    private var mGoogleApiClient: GoogleApiClient? = null


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

            logeoNext(account.displayName.toString(),account.email.toString());

            System.out.println(account.email)
            goMainScreen()
        } else {
            Toast.makeText(context, "Algo ha salido mal, Intentelo mas tarde", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goMainScreen() {

        val intent = Intent(this, HomeMain::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun inits() {
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)

        rlEmail = findViewById(R.id.rlEmail) as RelativeLayout
        rlEmail.setOnClickListener(this)

        rlFb = findViewById(R.id.rlFb) as RelativeLayout
        rlFb.setOnClickListener(this)

        txRegistrar = findViewById(R.id.txRegistrar) as TextView
        txRegistrar.setOnClickListener(this)

        rlGoogle = findViewById(R.id.rlGoogle) as RelativeLayout
        rlGoogle.setOnClickListener(this)


        //configuracion fb
        try {
            val info = packageManager.getPackageInfo(
                    "org.lwr.edgarapp",
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

                    //profileId = `object`.getString("id")

                    if (`object`.has("first_name")) {
                        nameSocial = `object`.getString("first_name")
                    } else {
                        nameSocial = ""
                    }

                    if (`object`.has("last_name")) {
                        nameSocial = nameSocial + " " + `object`.getString("last_name")
                    }


                    //http://graph.facebook.com/1933353450024438/picture?type=large
                    Constants.cerrarTeclado(context)
                    LoginManager.getInstance().logOut();
                    logeoNext(nameSocial,emailSocial);

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




        //Initializing google api client
        /*mGoogleApiClient = GoogleApiClient.Builder(this@OpcionesLogin)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()*/

    }



    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(context, "Algo ha salido mal, Intentelo mas tarde", Toast.LENGTH_SHORT).show()
    }

    private fun logeoNext(Usuario:String,Email:String) {
        Session!!.edit().putString("nombreUser", Usuario).commit()
        Session!!.edit().putString("correo", Email).commit()

        val selectStation = Intent(this@OpcionesLogin, HomeMain::class.java)
        startActivity(selectStation)
        finishAffinity()
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
