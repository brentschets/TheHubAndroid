package brentschets.com.projecthub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import brentschets.com.projecthub.R
import brentschets.com.projecthub.model.Post

class PostAdapter(val mCtx : Context, val layoutResId : Int, val postList : List<Post>) : ArrayAdapter<Post> (mCtx, layoutResId, postList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val txtView = view.findViewById<TextView>(R.id.txt_name)
        val post = postList[position]
        txtView.text = post.title
        return view
    }
}