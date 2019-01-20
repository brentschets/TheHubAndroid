package brentschets.com.projecthub.repositories

import brentschets.com.projecthub.database.PostDao
import brentschets.com.projecthub.model.Post

/**
 * Een room repository voor bewerkingen op de lokale opgeslagen posts.
 */
class PostRepository(private val postDao: PostDao){

    val posts = postDao.getAllPosts()

    fun insert(posts: List<Post>) {
        if (posts.isNotEmpty()) postDao.deleteAllPost()
        posts.forEach { postDao.insert(it) }
    }

    fun clearDatabase() {
        postDao.deleteAllPost()
    }

}