package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.model.User
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
     * Bool of user al dan niet gerigistreerd is
     */
    var isRegistered = MutableLiveData<Boolean>()
        private set

    /**
     * tag om mee te geven wanneer in de log
     */
    private val TAG = "FirebaseEmailPassword"

    /**
     * firebase object
     */
    private var mAuth: FirebaseAuth? = null
    private var ref: FirebaseDatabase? = null

    init {
        username = PreferenceUtil.getUsername()
        isLoggedIn.value = PreferenceUtil.getToken() != ""
        //firebase object
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance()
        isRegistered.value = PreferenceUtil.getToken() != ""
    }

    //gebruiker aanmelden door middel van firebase authenticatie
    fun login(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateFormLogin(email, password)) {
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
                    PreferenceUtil.setUsername(username.toString())
                    PreferenceUtil.setToken(user.uid)
                }

            }else{
                Toast.makeText(MainActivity.getContext(), "Er is een fout opgetreden bij het aanmelden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun register(email: String, password: String, username:String){

        if(!validateFormRegister(email,password)){
            return
        }
        //user aanmaken in de Realtime database
        val dbRef = ref!!.getReference("User")
        val userId = dbRef.push().key.toString()
        val user = User(userId, username, email)
        dbRef.child(userId).setValue(user)

        //User aan maken in de authentication database
        Log.e(TAG, "createAccount" + email)
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                //succesvolle registratie
                onRetrieveRegisterSuccess()
                Log.e(TAG,"createAccount: success" )
                Toast.makeText(MainActivity.getContext(), "Registratie voltooid!", Toast.LENGTH_SHORT).show()
            }else {
                //registratie mislukt
                Log.e(TAG, "createAccount: fail")
                Toast.makeText(MainActivity.getContext(), "Registratie mislukt!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //validatie van het login form
    private fun validateFormLogin(email: String, password : String): Boolean{
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


    //validatie van het registratie formulier
    private fun validateFormRegister(email: String, password : String): Boolean{
        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.getContext(), "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.getContext(), "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.length < 6){
            Toast.makeText(MainActivity.getContext(), "Password to short minimum of 6 characters", Toast.LENGTH_SHORT).show()
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

    private fun onRetrieveRegisterSuccess(){
        isRegistered.value = true
    }
}
