package brentschets.com.projecthub.utils

import android.support.v4.app.Fragment
import brentschets.com.projecthub.R
import brentschets.com.projecthub.activities.MainActivity

/**
 * Een util om je te helpen met het aanpassen van *fragments*.
 */
object FragmentUtil {

    /**
     * Doet een fragment transactie
     *
     * @param fragment : De fragment die getoond moet worden
     *
     * @param parentActivity : Geeft de activity van de fragment terug.
     *
     */
    @JvmStatic
    fun replace(fragment : Fragment, parentActivity: MainActivity){
        val fragmentTransaction = parentActivity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

}