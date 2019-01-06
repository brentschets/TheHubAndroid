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
import android.widget.Button
import android.widget.TextView
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.utils.FragmentUtil
import brentschets.com.projecthub.viewmodels.AccountViewModel
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Een [Fragment] waar een gebruiker zich kan aanmelden of kan doorklikken naar registreren.
 */
class LoginFragment: Fragment(), View.OnClickListener {
    /**
     * [AccountViewModel] met de data over account
     */
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener(this)
        val btnRegister = view.findViewById<TextView>(R.id.btn_create_account)
        btnRegister.setOnClickListener(this)
        accountViewModel = ViewModelProviders.of(requireActivity()).get(AccountViewModel::class.java)
        return view
    }


    override fun onClick(view: View?) {
        val i = view!!.id
        if(i == R.id.btn_login){
            login()
        }else if( i == R.id.btn_create_account){
            register()
        }
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
    }

    /**
     * Stop de listeners
     */
    @Suppress("UNUSED_EXPRESSION")
    private fun stopListeners() {
        btn_login.setOnClickListener { null }

        btn_create_account.setOnClickListener { null }
    }

    /**
     * Gaat naar het registratiefragment
     */
    private fun register() {
        FragmentUtil.replace(RegisterFragment(), requireActivity() as MainActivity)
    }

    /**
     * Controleert de waarden en logt in
     */
    private fun login() {
        accountViewModel.login(txt_email.text.toString(), txt_password.text.toString())
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