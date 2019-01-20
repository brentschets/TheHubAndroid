package brentschets.com.projecthub.database

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import brentschets.com.projecthub.model.Post

@Dao
interface PostDao {
    /**
     * Plaats de lijst van [Post] items in de lokale room database
     *
     * @param[posts] [Post] objecten die in room opgeslagen zullen worden
     */
    @Insert
    fun insert(posts: Post)

    /**
     * Returnt alle [Post] objecten uit de room database
     */
    @Query("SELECT * FROM post_table")
    fun getAllPosts(): MutableLiveData<ArrayList<Post>>

    /**
     * Verwijderd alle [Post] objecten uit de room database
     */
    @Query("DELETE FROM post_table")
    fun deleteAllPost()

}