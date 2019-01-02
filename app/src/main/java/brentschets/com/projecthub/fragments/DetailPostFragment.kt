package brentschets.com.projecthub.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import brentschets.com.projecthub.R
import brentschets.com.projecthub.databinding.FragmentDetailPostBinding
import brentschets.com.projecthub.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.fragment_detail_post.*


class DetailPostFragment : Fragment() {

    /**
     * [PostViewModel] met alle informatie over de posts
     */
    private lateinit var postViewModel: PostViewModel

    /**
     * De [FragmentDetailPostBinding] die we gebruiken voor de effectieve databinding
     */
    private lateinit var binding: FragmentDetailPostBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_post, container, false)

        //viewmodel vullen
        postViewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)

        val view = binding.root
        binding.postViewModel = postViewModel
        binding.setLifecycleOwner(activity)

        return view
    }

    /**
     * Instantieer de listeners
     */
    private fun initListiners(){
        //delete button
        btn_delete.setOnClickListener{
            postViewModel.deletePost(postViewModel.selectedPost.value!!.id)
            replaceFragment(PostListFragment())
        }
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun stopListeners(){
        btn_delete.setOnClickListener{ null }
    }

    override fun onStart() {
        super.onStart()
        initListiners()
    }

    override fun onStop() {
        stopListeners()
        super.onStop()
    }

    /**
     * methode voor fragmenttransaction
     */
    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

}
