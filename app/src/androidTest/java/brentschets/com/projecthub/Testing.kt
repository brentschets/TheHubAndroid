package brentschets.com.projecthub

import android.support.test.runner.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Testing {

    @Test
    fun testLogin(){
        onView(withId(R.id.txt_email)).perform(typeText("demo@demo.com"))
        onView(withId(R.id.txt_password)).perform(typeText("test123"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.layout.fragment_list)).check(matches(isDisplayed()))
    }

    @Test
    fun testRegister(){
        onView(withId(R.id.txt_register_email)).perform(typeText("woltex@demo.com"))
        onView(withId(R.id.txt_register_username)).perform(typeText("woltex"))
        onView(withId(R.id.txt_register_password)).perform(typeText("test123"))
        onView(withId(R.id.txt_register_cpassword)).perform(typeText("test123"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.layout.fragment_login)).check(matches(isDisplayed()))
    }
}