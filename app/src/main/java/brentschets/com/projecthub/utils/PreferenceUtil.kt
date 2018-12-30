package brentschets.com.projecthub.utils

import android.content.Context
import brentschets.com.projecthub.activities.MainActivity
import brentschets.com.projecthub.constants.*

object PreferenceUtil {

    /**
     * De shared preferences in [Context.MODE_PRIVATE] zodat andere applicaties hier niet aan kunnen
     */
    private val sharedPreferences = MainActivity.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * Haalt de gebruikersnaam op van de shared preferences
     */
    @JvmStatic
    fun getUsername() : String? {
        return sharedPreferences.getString(PREFERENCE_USERNAME, "")
    }

    /**
     * Slaat de gebruikersnaam op in de shared prefences
     *
     * @param username : de gebruikersnaam van de aangemelde user
     */
    @JvmStatic
    fun setUsername(username : String) {
        sharedPreferences.edit().putString(PREFERENCE_USERNAME, username).apply()
    }

    /**
     * Haalt de token op van de shared preferences
     */
    @JvmStatic
    fun getToken() : String? {
        return sharedPreferences.getString(PREFERENCE_TOKEN, "")
    }

    /**
     * Slaat de token op in de shared prefences
     *
     * @param token : de token van de aangemelde gebruiker
     */
    @JvmStatic
    fun setToken(token : String) {
        sharedPreferences.edit().putString(PREFERENCE_TOKEN, token).apply()
    }

    /**
     * Verwijderd alle sharedPreferences
     */
    @JvmStatic
    fun deletePreferences() {
        sharedPreferences.edit().clear().apply()
    }

}