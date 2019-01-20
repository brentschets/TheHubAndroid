package brentschets.com.projecthub.injection.modules

import android.content.Context
import brentschets.com.projecthub.database.PostDao
import brentschets.com.projecthub.database.PostDatabase
import brentschets.com.projecthub.repositories.PostRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule(private val context: Context) {
    /**
     * Returnt [PostRepository] voor room.
     */
    @Provides
    @Singleton
    fun provideOrderRepository(postDao: PostDao): PostRepository {
        return PostRepository(postDao)
    }

    /**
     * Returnt [PostDao] voor room.
     */
    @Provides
    @Singleton
    fun provideOrderDao(postDatabase: PostDatabase): PostDao {
        return postDatabase.postDao()
    }

    /**
     * Returnt [OrderDatabase] voor room.
     */
    @Provides
    @Singleton
    fun provideOrderDatabase(context: Context): PostDatabase {
        return PostDatabase.getInstance(context)
    }

    /**
     * Returnt de [Context] voor gebruik alvorens de is geladen en hij daar dus niet beschikbaar is.
     */
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }
}