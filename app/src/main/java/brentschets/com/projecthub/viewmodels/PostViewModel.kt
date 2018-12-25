package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import brentschets.com.projecthub.R.id.*
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.adapter.PostAdapter
import brentschets.com.projecthub.model.Post
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostViewModel: ViewModel() {

    /**
     * Firebase object
     */
    private var ref: FirebaseDatabase? = null

    /**
     * Methode om een methode toe te voegen
     */
    fun savePost(titel: String, categorie: String, message: String) {

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val date = sdf.format(Date())

        //aanmaken post object en push naar databank
        val dbRef = ref!!.getReference("Posts")
        val postId = dbRef.push().key.toString()
        val post = Post(postId, titel, PreferenceUtil.getUsername() , date, categorie, message)

        dbRef.child(postId).setValue(post).addOnCompleteListener{
            Toast.makeText(MainActivity.getContext(),"Post werd succesvol toegevoegd", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Validatie voor het toevoegen van een post
     */
    fun validateForm(titel: String, categorie : String, message: String): Boolean {
        if (TextUtils.isEmpty(titel)) {
            Toast.makeText(MainActivity.getContext(), "Gelieve een titel in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(categorie)) {
            Toast.makeText(MainActivity.getContext(), "Gelieve een categorie in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(MainActivity.getContext(), "Gelieve een bericht in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if(titel.length < 5){
            Toast.makeText(MainActivity.getContext(), "Gelieve een titel langer dan 5 karakters te kiezen", Toast.LENGTH_SHORT).show()
            return false
        }
        if(message.length < 15){
            Toast.makeText(MainActivity.getContext(), "Gelieve een bericht in te geven dat langer is dan 15 karakters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}
