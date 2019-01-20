package brentschets.com.projecthub.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import brentschets.com.projecthub.model.Post

/**
 * De database met als entity [Post] en childs
 */
@Database(entities = [Post::class], version = 1)
abstract class PostDatabase: RoomDatabase() {

    abstract fun postDao() : PostDao

    companion object {
        private var instance : PostDatabase? = null

        fun getInstance(context: Context): PostDatabase{
            if(instance != null) return instance!!

            val newInstance = Room.databaseBuilder(
                    context,
                    PostDatabase::class.java,
                    "POST_DATABASE"
            ).build()

            instance = newInstance

            return newInstance
        }
    }
}