package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.model.User
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
     * Firebase objecten
     */
    private var mAuth: FirebaseAuth? = null
    private var ref: FirebaseDatabase? = null

    init {
        username = PreferenceUtil.getUsername()
        isLoggedIn.value = PreferenceUtil.getToken() != ""
        //firebase object
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance()
    }

    /**
     * Aanmelden van een gebruiker door middel van Firebase
     */
    fun login(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateFormLogin(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                Toast.makeText(MainActivity.getContext(), "Succesvol ingelogd", Toast.LENGTH_SHORT).show()
                onRetrieveLoginSuccess(email)
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

    /**
     * Registreren van een gebruiker
     */
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

    /**
     * Methode om de gebruiker af te melden
     */
    fun signOut(){
        mAuth!!.signOut()
        PreferenceUtil.deletePreferences()
        isLoggedIn.value = false
        isRegistered.value = false
    }

    /**
     * Validatie op het login form
     */
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


    /**
     * Validatie op het registreer form
     */
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
     */
    private fun onRetrieveLoginSuccess(email: String) {
        isLoggedIn.value = true

        val dbRef = ref!!.getReference("User")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Toast bij een error bij het ophalen van de data
                Toast.makeText(MainActivity.getContext(), "Er ging iets fout bij het aanmelden", Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                val userList = ArrayList<User>()
                if (p0.exists()) {
                    userList.clear()
                    for (h in p0.children) {
                        val user = h.getValue(User::class.java)
                        userList.add(user!!)
                    }
                }
                for (i in userList) {
                    if (i.email == email) {
                        PreferenceUtil.setUsername(i.username)
                    }
                }
            }
        })
    }

    /**
     * Functie voor het succesvol registreren van een gebruiker
     */
    private fun onRetrieveRegisterSuccess(){
        isRegistered.value = true
    }
}
