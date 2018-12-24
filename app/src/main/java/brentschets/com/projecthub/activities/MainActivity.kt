package brentschets.com.projecthub.activities

import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import brentschets.com.projecthub.R
import brentschets.com.projecthub.fragments.AccountFragment
import brentschets.com.projecthub.fragments.LoginFragment
import brentschets.com.projecthub.fragments.PostListFragment
import brentschets.com.projecthub.fragments.PlaatsPostFragment
import brentschets.com.projecthub.viewmodels.AccountViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {

    /**
     * De [AccountViewModel] met informatie over de *aangemelde gebruiker*.
     */
    private lateinit var accountViewModel: AccountViewModel


    /**
     * Listener voor de bottom navigation view dat er voor zorgt dat juiste fragment getoond wordt on click.
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.list -> {
                println("list pressed")
                replaceFragment(PostListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.add -> {
                println("add pressed")
                replaceFragment(PlaatsPostFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                println("account pressed")
                replaceFragment(AccountFragment())
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
        setContentView(R.layout.activity_main)
        if(accountViewModel.isLoggedIn.value == true){
            bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            replaceFragment(PostListFragment())
        }else{
            replaceFragment(LoginFragment())
        }

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
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
