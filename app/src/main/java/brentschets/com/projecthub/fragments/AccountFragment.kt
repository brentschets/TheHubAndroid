package brentschets.com.projecthub.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.databinding.FragmentAccountBinding
import brentschets.com.projecthub.viewmodels.AccountViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import kotlin.math.sign

/**
 * Een [Fragment] voor het weergeven van de gegevens van de gebruiker
 *
 */
class AccountFragment : Fragment(), View.OnClickListener {


    /**
     * [AccountViewModel] met de data over account
     */
    private lateinit var accountViewModel: AccountViewModel

    /**
     * De [FragmentAccountBinding] dat we gebruiken voor de effeciteve databinding
     */
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        //Accountviewmodel vullen
        accountViewModel = ViewModelProviders.of(requireActivity()).get(AccountViewModel::class.java)

        val view = binding.root

        binding.accountViewModel = accountViewModel
        binding.setLifecycleOwner(activity)

        val btnLogOut = view.findViewById<Button>(R.id.btn_account_signout)
        btnLogOut.setOnClickListener(this)


        return view
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        if(i == R.id.btn_account_signout){
            signOut()
        }
    }

    /**
     * Instantieer de listeners
     */
    private fun initListeners() {
        //aangemeld en parantactivity bijhouden
        val loggedIn  = accountViewModel.isLoggedIn

        //indien aangemeld naar lijst gaan
        loggedIn.observe(this, Observer {
            if (loggedIn.value == false) {
                //simuleert een button click op lijst om er voor te zorgen dat juiste
                //item actief is + zet fragment etc automatisch juist
                replaceFragment(LoginFragment())
            }
        })
    }

    /**
     * Stop de listeners
     */
    @Suppress("UNUSED_EXPRESSION")
    private fun stopListeners() {
        //afmeldknop
        btn_account_signout.setOnClickListener { null }
    }

    private fun signOut(){
        accountViewModel.signOut()
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    override fun onStop() {
        stopListeners()
        super.onStop()
    }

    /**
     * methode voor een fragmenttransaction
     */
    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


}
