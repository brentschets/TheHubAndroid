package brentschets.com.projecthub

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private val TAG = "FirebaseEmailPassword"

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener(this)
        btn_create_account.setOnClickListener(this)

        //firebase
        mAuth = FirebaseAuth.getInstance()
    }


    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            //updateUI(currentUser)
        }
    }

    override fun onClick(view: View?) {
        val i = view!!.id


        if (i == R.id.btn_create_account) {
            creatAccount(txt_email.text.toString(), txt_password.text.toString())

        } else if (i == R.id.btn_login) {
            signIn(txt_email.text.toString(), txt_password.text.toString())

        }
    }

    private fun creatAccount(email: String, password: String) {
        Log.e(TAG, "createAccount" + email)
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.e(TAG, "creatAccount: success")
                // Sign in: success
                // update UI for current User
                //val user = mAuth!!.currentUser
                //updateUI(user)
            } else {
                // Sign in: fail
                Log.e(TAG, "creatAccount: fail")
                //updateUI(null)
            }
        }
    }


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
                        val intent = Intent(this, MainActivity::class.java).apply { }
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "signIn: Fail!", task.exception)
                        Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }

    }

    private fun logCurrentUserOut() {
        mAuth!!.signOut()
        //updateUI(null)
    }

    private fun validateForm(email: String, password : String): Boolean{
        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.length < 6){
            Toast.makeText(applicationContext, "Password to short minimum of 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    }



