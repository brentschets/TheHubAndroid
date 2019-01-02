package brentschets.com.projecthub.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Dit is de netwerk klasse hier worden alle references aangemaakt voor Firebase maar ook de lokale persistentie van Firebase wordt hier
 * gebruikt
 */
class ProjectHubApi {

    /**
     * [FirebaseAuth] object om het mogelijk te maken dat de gebruiker zich kan aanmelden en registreren
     */
    var mAuth: FirebaseAuth? = null

    /**
     * [FirebaseDatabase] object om de nodige gebruikers en posts op te halen
     */
    private var ref: FirebaseDatabase? = null

    /**
     * [DatabaseReference] object reference naar de gebruikers
     */
    var userRef: DatabaseReference? = null

    /**
     * [DatabaseReference] object reference naar de posts
     */
    var postRef: DatabaseReference?= null

    init {
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance()
        userRef = ref!!.getReference("User")
        postRef = ref!!.getReference("Posts")
        userRef!!.keepSynced(true)
        postRef!!.keepSynced(true)
    }
}