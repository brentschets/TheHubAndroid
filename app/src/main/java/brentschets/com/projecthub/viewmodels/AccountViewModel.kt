package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountViewModel: ViewModel() {

    /**
     * De gebruikersnaam van de aangemelde persoon
     */
    var username: String?
        private set

    /**
     * Bool of user al dan niet aangemeld is
     */
    var isLoggedIn = MutableLiveData<Boolean>()
        private set

    /**
     * tag om mee te geven wanneer in de log
     */
    private val TAG = "FirebaseEmailPassword"

    /**
     * firebase object
     */
    private var mAuth: FirebaseAuth? = null

    init {
        username = PreferenceUtil.getUsername()
        isLoggedIn.value = PreferenceUtil.getToken() != ""
        //firebase object
        mAuth = FirebaseAuth.getInstance()
    }

    //gebruiker aanmelden door middel van firebase authenticatie
    fun login(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                Toast.makeText(MainActivity.getContext(), "Succesvol ingelogd", Toast.LENGTH_SHORT).show()
                onRetrieveLoginSuccess()
                var user = mAuth!!.currentUser
                if (user != null) {
                    username = user.displayName
                    PreferenceUtil.setToken(user.uid)
                }

            }else{
                Toast.makeText(MainActivity.getContext(), "Er is een fout opgetreden bij het aanmelden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //validatie van het login form
    private fun validateForm(email: String, password : String): Boolean{
        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.getContext(), "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.getContext(), "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    /**
     * Functie voor het behandelen van het succesvol aanmelden
     *
     * zal token instellen en opslaan, en isLoggedIn in de VM op true zetten
     */
    private fun onRetrieveLoginSuccess() {
        isLoggedIn.value = true
    }
}
