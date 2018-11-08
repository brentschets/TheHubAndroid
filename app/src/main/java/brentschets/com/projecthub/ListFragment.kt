package brentschets.com.projecthub


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import brentschets.com.projecthub.adapter.PostAdapter
import brentschets.com.projecthub.model.Post
import com.google.firebase.database.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    lateinit var editTextTitle : EditText
    lateinit var editTextOwner : EditText
    lateinit var editTextCategory: EditText
    lateinit var editTextDate : EditText
    lateinit var editTextMessage : EditText


    lateinit var postList : MutableList<Post>
    lateinit var ref : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)


        val listView = view.findViewById<ListView>(R.id.list_view)



        postList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("Posts")
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
                    val adapter = PostAdapter(activity!!.applicationContext, R.layout.posts, postList)
                    listView.adapter = adapter
                }
            }
        })


        return view
    }




}
