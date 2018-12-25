package brentschets.com.projecthub.fragments


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import brentschets.com.projecthub.R
import kotlinx.android.synthetic.main.fragment_plaats_post.*
import brentschets.com.projecthub.viewmodels.*


class PlaatsPostFragment : Fragment(), View.OnClickListener{

    /**
     * [PostViewModel] met de data over een post
     */
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_plaats_post, container, false)
        val btnSubmitPost = view.findViewById<Button>(R.id.btn_post_submit)
        btnSubmitPost.setOnClickListener(this)
        postViewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)
        return view

    }

    //click event voor save post en maak de text fields leeg
    override fun onClick(view: View?) {
        val i = view!!.id
        if(i == R.id.btn_post_submit){
            if(postViewModel.validateForm(txt_post_titel.text.toString(), sp_post_category.selectedItem.toString(),txt_post_message.text.toString())){
                postViewModel.savePost(txt_post_titel.text.toString(), sp_post_category.selectedItem.toString(),txt_post_message.text.toString())
                txt_post_titel.text.clear()
                txt_post_message.text.clear()
            }
        }
    }
}
