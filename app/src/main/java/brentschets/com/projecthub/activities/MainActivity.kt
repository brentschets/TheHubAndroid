package brentschets.com.projecthub.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import brentschets.com.projecthub.R
import brentschets.com.projecthub.fragments.AccountFragment
import brentschets.com.projecthub.fragments.LoginFragment
import brentschets.com.projecthub.fragments.PostListFragment
import brentschets.com.projecthub.fragments.PlaatsPostFragment
import brentschets.com.projecthub.viewmodels.AccountViewModel
import brentschets.com.projecthub.databinding.ActivityMainBinding
import brentschets.com.projecthub.utils.FragmentUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * De [AccountViewModel] met informatie over de *aangemelde gebruiker*.
     */
    private lateinit var accountViewModel: AccountViewModel

    /**
     * De [ActivityMainBinding] dat we gebruiken voor de effeciteve databinding met de [MainActivity]
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Listener voor de bottom navigation view dat er voor zorgt dat juiste fragment getoond wordt on click.
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.list -> {
                println("list pressed")
                FragmentUtil.replace(PostListFragment(), parentActivity = instance!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.add -> {
                println("add pressed")
                FragmentUtil.replace(PlaatsPostFragment(), parentActivity = instance!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                println("account pressed")
                FragmentUtil.replace(AccountFragment(), parentActivity = instance!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //context instellen voor globaal gebruik
        instance = this

        super.onCreate(savedInstanceState)
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)

        //main activity binden
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.accountViewModel = accountViewModel
        binding.setLifecycleOwner(this)

        if(accountViewModel.isLoggedIn.value == true){
            bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            FragmentUtil.replace(PostListFragment(), parentActivity = instance!!)
        }else{
            FragmentUtil.replace(LoginFragment(), parentActivity = instance!!)
        }

    }


    /**
     * Companian object van de mainactivity om globale communicatie mogelijk te maken.
     */
    companion object {
        //voor globaal gebruik van context
        //handig om toasts van eender waar te doen en gebruik in andere utils
        private var instance: MainActivity? = null

        /**
         * returnt de [Context] van de app zijn MainActivity
         */
        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}
