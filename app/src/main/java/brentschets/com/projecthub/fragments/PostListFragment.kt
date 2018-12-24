package brentschets.com.projecthub.fragments



import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import brentschets.com.projecthub.R
import brentschets.com.projecthub.adapter.PostAdapter
import brentschets.com.projecthub.model.Post
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.ArrayList


class PostListFragment : Fragment() {

    lateinit var ref : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)


        val mRecyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayout.VERTICAL, false)

        ref = FirebaseDatabase.getInstance().getReference("Posts")

        val postList = ArrayList<Post>()

        ref.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    postList.clear()
                    for (h in p0.children){
                        val post = h.getValue(Post::class.java)
                        postList.add(post!!)
                    }
                    val adapter = PostAdapter(postList)
                    view.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        })
        return view
    }




}
