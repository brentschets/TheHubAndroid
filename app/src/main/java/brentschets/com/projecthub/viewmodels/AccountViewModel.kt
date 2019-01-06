package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import brentschets.com.projecthub.model.User
import brentschets.com.projecthub.network.ProjectHubApi
import brentschets.com.projecthub.utils.MessageUtil
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AccountViewModel: ViewModel() {

    /**
     * De gebruikersnaam van de aangemelde persoon
     */
    var username = MutableLiveData<String>()
        private set

    /**
     * Bool of user al dan niet aangemeld is
     */
    var isLoggedIn = MutableLiveData<Boolean>()
        private set

    /**
     * Bool of user al dan niet geregistreerd is
     */
    var isRegistered = MutableLiveData<Boolean>()
        private set

    /**
     * tag om mee te geven wanneer in de log
     */
    private val TAG = "FirebaseEmailPassword"

    /**
     * [ProjectHubApi] Instantie van de api
     */
    private var projectApi = ProjectHubApi()


    init {
        username.value = PreferenceUtil.getUsername()
        isLoggedIn.value = PreferenceUtil.getToken() != ""
    }

    /**
     * Aanmelden van een gebruiker door middel van Firebase
     */
    fun login(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateFormLogin(email, password)) {
            return
        }

        projectApi.mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                MessageUtil.showToast("Succesvol aangemeld")
                onRetrieveLoginSuccess(email)
                var user = projectApi.mAuth!!.currentUser
                if (user != null) {
                    PreferenceUtil.setToken(user.uid)
                }

            }else{
                MessageUtil.showToast("Er is iets foutgelopen bij het aanmelden")
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
        val dbRef = projectApi.userRef
        val userId = dbRef!!.push().key.toString()
        val user = User(userId, username, email)
        dbRef.child(userId).setValue(user)

        //User aan maken in de authentication database
        Log.e(TAG, "createAccount" + email)
        projectApi.mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                //succesvolle registratie
                onRetrieveRegisterSuccess()
                Log.e(TAG,"createAccount: success" )
                MessageUtil.showToast("Succesvol geregistreerd")
            }else {
                //registratie mislukt
                Log.e(TAG, "createAccount: fail")
                MessageUtil.showToast("Registratie mislukt")
            }
        }
    }

    /**
     * Methode om de gebruiker af te melden
     */
    fun signOut(){
        projectApi.mAuth!!.signOut()
        PreferenceUtil.deletePreferences()
        isLoggedIn.value = false
        isRegistered.value = false
    }

    /**
     * Validatie op het login form
     */
    private fun validateFormLogin(email: String, password : String): Boolean{
        if(TextUtils.isEmpty(email)){
            MessageUtil.showToast("Vul emailadres in")
            return false
        }

        if(TextUtils.isEmpty(password)){
            MessageUtil.showToast("Vul wachtwoord in")
            return false
        }

        return true
    }


    /**
     * Validatie op het registreer form
     */
    private fun validateFormRegister(email: String, password : String): Boolean{
        if(TextUtils.isEmpty(email)){
            MessageUtil.showToast("Vul emailadres in")
            return false
        }

        if(TextUtils.isEmpty(password)){
            MessageUtil.showToast("Vul wachtwoord in")
            return false
        }

        if(password.length < 6){
            MessageUtil.showToast("Password te kort gelieve een wachtwoord te kiezen van minstnes 6 karakters")
            return false
        }

        return true
    }

    /**
     * Functie voor het behandelen van het succesvol aanmelden
     */
    private fun onRetrieveLoginSuccess(email: String) {
        isLoggedIn.value = true

        val dbRef = projectApi.userRef
        dbRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Toast bij een error bij het ophalen van de data
                MessageUtil.showToast("Er ging iets fout bij het aanmelden")
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
                        username.value = i.username
                        PreferenceUtil.setUsername(i.username)
                        println(i.username)
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
