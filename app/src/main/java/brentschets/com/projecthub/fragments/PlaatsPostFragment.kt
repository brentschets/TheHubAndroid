package brentschets.com.projecthub.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.utils.FragmentUtil
import kotlinx.android.synthetic.main.fragment_plaats_post.*
import brentschets.com.projecthub.viewmodels.*


class PlaatsPostFragment : Fragment() {

    /**
     * [PostViewModel] met de data over een post
     */
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_plaats_post, container, false)

        postViewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)

        return view
    }

    /**
     * Instantieer de listeners
     */
    private fun initListeners(){
        btn_post_submit.setOnClickListener{
            if(postViewModel.validateForm(txt_post_titel.text.toString(),
                            sp_post_category.selectedItem.toString(),
                            txt_post_message.text.toString())){
                postViewModel.savePost(txt_post_titel.text.toString(),
                        sp_post_category.selectedItem.toString(),
                        txt_post_message.text.toString())
              FragmentUtil.replace(PostListFragment(), requireActivity() as MainActivity)

            }
        }
    }

    /**
     * Stop de listeners
     */
    @Suppress("UNUSED_EXPRESSION")
    private fun stopListeners() {
        btn_post_submit.setOnClickListener { null }
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    override fun onStop() {
        stopListeners()
        super.onStop()
    }
}
