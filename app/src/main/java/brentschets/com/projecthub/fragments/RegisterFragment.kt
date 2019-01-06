package brentschets.com.projecthub.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.utils.FragmentUtil
import brentschets.com.projecthub.utils.MessageUtil
import brentschets.com.projecthub.viewmodels.AccountViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: Fragment() {

    /**
     * [AccountViewModel] met de data over account
     */
    //Globaal ter beschikking stellen van Accountviewmodel
    private lateinit var accountViewModel : AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        accountViewModel = ViewModelProviders.of(requireActivity()).get(AccountViewModel::class.java)

        return view
    }


    /**
     * Instantieer de listeners
     */
    private fun initListeners(){
        //aangemeld en parantactivity bijhouden
        val loggedIn  = accountViewModel.isLoggedIn

        val intent = Intent(MainActivity.getContext(), MainActivity::class.java).apply { }
        //indien aangemeld naar lijst gaan
        loggedIn.observe(this, Observer {
            if (loggedIn.value == true) {
                //simuleert een button click op lijst om er voor te zorgen dat juiste
                //item actief is + zet fragment etc automatisch juist
                (requireActivity() as AppCompatActivity).startActivity(intent)
            }
        })

        val registered  = accountViewModel.isRegistered

        //indien geregistreerd naar login
        registered.observe(this, Observer {
            if (registered.value == true) {
                //simuleert een button click op lijst om er voor te zorgen dat juiste
                //item actief is + zet fragment etc automatisch juist
                FragmentUtil.replace(LoginFragment(), requireActivity() as MainActivity)
            }
        })

        btn_register.setOnClickListener{
            register()
        }

        btn_register_login.setOnClickListener{
            FragmentUtil.replace(LoginFragment(), requireActivity() as MainActivity)
        }
    }

    /**
     * Stop de listeners
     */
    @Suppress("UNUSED_EXPRESSION")
    private fun stopListeners() {
        btn_register.setOnClickListener { null }
    }

    /**
     * Kijkt of de wachtwoorden overeenkomen en probeert te registreren
     */
    private fun register(){
        if(txt_register_password.text.toString() != txt_register_cpassword.text.toString()){
            MessageUtil.showToast("Wachtwoorden komen niet overeen")
        }else{
            accountViewModel.register(
                    txt_register_email.text.toString(),
                    txt_register_password.text.toString(),
                    txt_register_username.text.toString())
        }
    }

    override fun onStart() {
        initListeners()
        super.onStart()
    }

    override fun onStop() {
        stopListeners()
        super.onStop()
    }
}