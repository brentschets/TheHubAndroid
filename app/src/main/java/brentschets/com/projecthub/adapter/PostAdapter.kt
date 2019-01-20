package brentschets.com.projecthub.adapter

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.fragments.DetailPostFragment
import brentschets.com.projecthub.model.Post
import brentschets.com.projecthub.utils.FragmentUtil
import brentschets.com.projecthub.viewmodels.PostViewModel

class PostAdapter(private val postList : MutableLiveData<List<Post>>, private val parentActivity: MainActivity) : RecyclerView.Adapter<PostAdapter.ViewHolder> () {

    /**
     * [PostViewModel] met de data over account
     */
    private var postViewModel: PostViewModel = ViewModelProviders.of(parentActivity).get(PostViewModel::class.java)

    /**
     * Een *on click listener* die er voor zorgt dat op het klikken van een [Post]
     * naar de bijhorende [DetailPostFragment] gegaan wordt.
     */
    private val onClickListener: View.OnClickListener

    init {
        //indien op een post geklikt haal uit de tag de desbetreffende post op en toon detailpagina
        onClickListener = View.OnClickListener { v ->
            val selectedPost = v.tag as Post
            postViewModel.setSelectedPost(selectedPost.id)
            postViewModel.ownerPost(selectedPost)
            //fragment transaction
            FragmentUtil.replace(DetailPostFragment(), parentActivity)
        }
    }

    /**
     * Methode die recyclerview nodig heeft om te bepalen hoeveel items hij moet renderen.
     *
     * Dit is het aantal items in de meegeven lijst [postList].
     */
    override fun getItemCount(): Int {
        return postList.value!!.size
    }

    /**
     * Vult de viewholder met de nodige data.
     *
     * De viewholder krijgt ook een tag zijnde de bijhorende [Post]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post : Post = postList.value!![position]
        holder.textTitle.text = post.title
        holder.textCategory.text = post.category
        holder.textDate.text = post.date
        holder.textAuthor.text = post.owner

        with(holder.itemView){
            tag = post
            setOnClickListener(onClickListener)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item,  p0, false)
        return ViewHolder(view)
    }

    /**
     * Viewholder die:
     * - [textTitle]
     * - [textCategory]
     * - [textAuthor]
     * - [textDate]
     * hun bijhorend UI element bijhoud om later op te vullen.
     */
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val textTitle = itemView.findViewById(R.id.txt_title) as TextView
        val textCategory = itemView.findViewById(R.id.txt_category) as TextView
        val textDate = itemView.findViewById(R.id.txt_date) as TextView
        val textAuthor = itemView.findViewById(R.id.txt_author) as TextView
    }
}