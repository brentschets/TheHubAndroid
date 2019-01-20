package brentschets.com.projecthub.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import brentschets.com.projecthub.database.PostDao
import brentschets.com.projecthub.model.Post
import brentschets.com.projecthub.network.ProjectHubApi
import brentschets.com.projecthub.repositories.PostRepository
import brentschets.com.projecthub.utils.MessageUtil
import brentschets.com.projecthub.utils.PreferenceUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PostViewModel: ViewModel() {

    /**
     * Een lijst met alle posts
     */
    var postList = MutableLiveData<ArrayList<Post>>()
        private set
    /**
     * Een repository object voor de persistence van de posts
     */
    @Inject
    lateinit var postRepo: PostRepository

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

    var roomPosts = MutableLiveData<ArrayList<Post>>()
        private set

    init {

        getPosts()
        isOwnerPost.value = false
        roomPosts = postRepo!!.posts
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
            MessageUtil.showToast("Post succesvol toegevoegd")
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
                MessageUtil.showToast("Er liep iets fout bij het ophalen van de posts")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    posts.clear()
                    for (h in p0.children){
                        val post = h.getValue(Post::class.java)
                        posts.add(post!!)
                    }
                    postList.value = posts
                    addPostRoom(posts)
                }
            }
        })
    }

    fun deletePost(postId: String){
        val dbRef = projectApi.postRef
        dbRef!!.child(postId).removeValue().addOnCompleteListener{ task ->
            if(task.isSuccessful)
                MessageUtil.showToast("Uw bericht werd succesvol verwijderd")
            getPosts()
        }

    }

    fun ownerPost(post: Post){
        isOwnerPost.value = PreferenceUtil.getUsername() == post.owner
    }

    private fun addPostRoom(posts: ArrayList<Post>){
        doAsync{
            postRepo!!.insert(posts)
        }
    }



    /**
     * Validatie voor het toevoegen van een post
     */
    fun validateForm(titel: String, categorie : String, message: String): Boolean {
        if (TextUtils.isEmpty(titel)) {
            MessageUtil.showToast("Gelieve een titel in te vullen")
            return false
        }

        if (TextUtils.isEmpty(categorie)) {
            MessageUtil.showToast("Gelieve een categorie in te vullen")
            return false
        }

        if (TextUtils.isEmpty(message)) {
            MessageUtil.showToast("Gelieve een bericht in te vullen")
            return false
        }

        if(titel.length < 5){
            MessageUtil.showToast("Gelieve een titel langer dan 5 karakters te kiezen")
            return false
        }
        if(message.length < 15){
            MessageUtil.showToast("Gelieve een bericht in te geven dat langer is dan 15 karakters")
            return false
        }

        return true
    }

}
