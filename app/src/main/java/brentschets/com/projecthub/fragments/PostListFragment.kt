package brentschets.com.projecthub.fragments

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.adapter.PostAdapter
import brentschets.com.projecthub.model.Post
import brentschets.com.projecthub.viewmodels.PostViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.ArrayList

class PostListFragment : Fragment() {

    /**
     * Firebase DatabaseObject
     */
    lateinit var ref : DatabaseReference

    /**
     * Lijst van posts
     */
    private lateinit var postList: MutableLiveData<ArrayList<Post>>

    /**
     * [PostAdapter] die de lijst vult.
     */
    private lateinit var adapter: PostAdapter

    /**
     * [PostViewModel] met de data van de posts
     */
    private lateinit var postViewModel: PostViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)

        //viewmodel vullen
        postViewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)

        //ref voor datachanges
        ref = postViewModel.ref!!.getReference("Posts")

        //list vullen met posts van postviewmodel
        postList = postViewModel.postList

        adapter = PostAdapter(postList, requireActivity() as MainActivity)

        ref.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(MainActivity.getContext(), "Er liep iets fout bij het ophalen van de posts", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    view.recyclerView.adapter = adapter
                }
            }
        })
        return view
    }

    private fun initListeners(){
        postList.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }






}
