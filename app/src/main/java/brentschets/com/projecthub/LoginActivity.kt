package brentschets.com.projecthub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private val TAG = "FirebaseEmailPassword"

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //actionbar title
        setSupportActionBar(findViewById(R.id.my_toolbar_login))
        val actionbar = supportActionBar
        actionbar!!.title = "Meld je aan"

        btn_login.setOnClickListener(this)
        btn_create_account.setOnClickListener(this)

        //firebase object
        mAuth = FirebaseAuth.getInstance()
    }


    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            //updateUI(currentUser)
        }
    }

    //click events om naar registratie scherm te gaan of de gebruiker aan te melden
    override fun onClick(view: View?) {
        val i = view!!.id

        if (i == R.id.btn_create_account) {
            val intent = Intent(this, RegisterActivity::class.java).apply {}
            startActivity(intent)

        } else if (i == R.id.btn_login) {
            signIn(txt_email.text.toString(), txt_password.text.toString())
        }
    }


    //gebruiker aanmelden door middel van firebase authenticatie
    private fun signIn(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e(TAG, "signIn: Success!")
                        // update UI with the signed-in user's information
                        //val user = mAuth!!.getCurrentUser()
                        //updateUI(user)
                        val intent = Intent(this, PlaatsPostActivity::class.java).apply { }
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "signIn: Fail!", task.exception)
                        Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }

    }


    //afmelden maar nog nergens gebruikt
    private fun logCurrentUserOut() {
        mAuth!!.signOut()
        //updateUI(null)
    }

    //validatie van het login form
    private fun validateForm(email: String, password : String): Boolean{
        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    }



