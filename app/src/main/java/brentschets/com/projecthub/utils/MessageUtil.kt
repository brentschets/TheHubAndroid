package brentschets.com.projecthub.utils

import android.widget.Toast
import brentschets.com.projecthub.activities.MainActivity

/**
 * Een util om je te helpen met het weergeven van *berichten* aan de gebruiker.
 */
object MessageUtil {

    /**
     * Toont een toast op het scherm. Context is voorzien door de [MainActivity]
     *
     * @param message : Het message dat weergegeven moet worden. Required of type String
     *
     * @param lengthOnScreen : Hoe lang de toast op het scherm moet blijven. Optional of type Int (Toast Length), default Toast.LENGTH_LONG.
     *
     */
    @JvmStatic
    fun showToast(message: String, lengthOnScreen: Int = Toast.LENGTH_LONG) {
        Toast.makeText(MainActivity.getContext(), message, lengthOnScreen).show()
    }
}