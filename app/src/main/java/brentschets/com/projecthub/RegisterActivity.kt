package brentschets.com.projecthub


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "FirebaseEmailPassword"

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_register.setOnClickListener(this)
        //firebase
        mAuth = FirebaseAuth.getInstance()
    }

    //click event op de registratie knop
    override fun onClick(view: View?) {
        val i = view!!.id
        if(i == R.id.btn_register){
            val pw = txt_register_password.text.toString()
            val cpw = txt_register_cpassword.text.toString()
            //check of passwords hetzelfde zijn
            if(pw != cpw){
                Toast.makeText(applicationContext, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }else{
                creatAccount(txt_register_email.text.toString(),txt_register_password.text.toString())
            }
        }
    }

    //firebase authenticatie registratie
    private fun creatAccount(email: String, password: String) {
        if(!validateForm(email, password)){
            return
        }
        Log.e(TAG, "createAccount" + email)
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in: success
                Log.e(TAG, "creatAccount: success")
                Toast.makeText(applicationContext, "Registration succeeded!", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in: fail
                Log.e(TAG, "creatAccount: fail")
                Toast.makeText(applicationContext, "Registration failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //validatie van het registratie formulier
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
