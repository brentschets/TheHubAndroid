package brentschets.com.projecthub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.list -> {
                println("list pressed")
                replaceFragment(ListFragment())
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(ListFragment())

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
