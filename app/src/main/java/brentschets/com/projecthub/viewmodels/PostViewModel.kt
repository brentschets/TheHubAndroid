package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import android.widget.Toast
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.model.Post
import brentschets.com.projecthub.network.ProjectHubApi
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostViewModel: ViewModel() {

    /**
     * Een lijst met alle posts
     */
    var postList = MutableLiveData<ArrayList<Post>>()
        private set

    /**
     * Lijst van alle posts
     */
    var posts = ArrayList<Post>()

    /**
     * De geselecteerde lunch
     */
    val selectedPost = MutableLiveData<Post>()

    /**
     * Eigenaar van de geselecteerde post
     */
    var isOwnerPost = MutableLiveData<Boolean>()

    /**
     * [ProjectHubApi] Instantie van de api
     */
    var projectApi = ProjectHubApi()

    init {
        getPosts()
        isOwnerPost.value = false
        postList.value = posts
    }

    /**
     * Methode om een post toe te voegen
     */
    fun savePost(titel: String, categorie: String, message: String) {

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val date = sdf.format(Date())

        //aanmaken post object en push naar databank
        val dbRef = projectApi.postRef
        val postId = dbRef!!.push().key.toString()
        val post = Post(postId, titel, PreferenceUtil.getUsername() , date, categorie, message)

        dbRef.child(postId).setValue(post).addOnCompleteListener{
            Toast.makeText(MainActivity.getContext(),"Post werd succesvol toegevoegd", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * returnt de lijst van alle lunches als MutableLiveData
     */
    fun setSelectedPost(postId: String) {
        selectedPost.value = posts.firstOrNull { it.id == postId }
    }

    fun getPosts(){
        val dbRef = projectApi.postRef

        posts = ArrayList()

        dbRef!!.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(MainActivity.getContext(), "Er liep iets fout bij het ophalen van de posts", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    posts.clear()
                    for (h in p0.children){
                        val post = h.getValue(Post::class.java)
                        posts.add(post!!)
                    }
                    postList.value = posts
                }
            }
        })
    }

    fun deletePost(postId: String){
        val dbRef = projectApi.postRef
        dbRef!!.child(postId).removeValue().addOnCompleteListener{ task ->
            if(task.isSuccessful)
                Toast.makeText(MainActivity.getContext(), "Uw bericht werd succesvol verwijderd!", Toast.LENGTH_SHORT).show()
            getPosts()
        }

    }

    fun ownerPost(post: Post){
        isOwnerPost.value = PreferenceUtil.getUsername() == post.owner
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
