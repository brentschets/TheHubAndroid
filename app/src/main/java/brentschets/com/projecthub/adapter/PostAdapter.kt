package brentschets.com.projecthub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import brentschets.com.projecthub.R
import brentschets.com.projecthub.model.Post

class PostAdapter(val userList : ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder> () {

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val post : Post = userList[p1]
        p0.textTitle.text = post.title
        p0.textCategory.text = post.category
        p0.textDate.text = post.date
        p0.textAuthor.text = post.owner

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item,  p0, false)
        return ViewHolder(view)
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val textTitle = itemView.findViewById(R.id.txt_title) as TextView
        val textCategory = itemView.findViewById(R.id.txt_category) as TextView
        val textDate = itemView.findViewById(R.id.txt_date) as TextView
        val textAuthor = itemView.findViewById(R.id.txt_author) as TextView
    }
}