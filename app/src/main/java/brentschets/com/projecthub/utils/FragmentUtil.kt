package brentschets.com.projecthub.utils

import android.support.v4.app.Fragment
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity

object FragmentUtil {

    @JvmStatic
    fun replace(fragment : Fragment, parentActivity: MainActivity){
        val fragmentTransaction = parentActivity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

}