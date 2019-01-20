package brentschets.com.projecthub.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "post_table")
class Post (@PrimaryKey val id : String,
            val title : String,
            val owner : String?,
            val date : String,
            val category : String,
            val message : String){
    @Ignore
    private constructor() : this(
            "",
            "",
            "",
            "",
            "",
            ""
    )
}