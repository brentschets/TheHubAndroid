package brentschets.com.projecthub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import brentschets.com.projecthub.Models.Post
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_plaats_post.*
import java.text.SimpleDateFormat
import java.util.*

class PlaatsPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plaats_post)

        //save post en maak de text fields leeg
        btn_post_submit.setOnClickListener {
            if(validateForm(txt_post_titel.text.toString(), sp_post_category.selectedItem.toString(),txt_post_message.text.toString())){
                savePost()
                txt_post_titel.text = null
                txt_post_message.text = null
            }else{
                Toast.makeText(applicationContext,"Het toevoegen van een post ondervond een fout!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //het opslaan van een post naar de firebase databank
    private fun savePost() {
        val titel = txt_post_titel.text.toString()
        val categorie = sp_post_category.selectedItem.toString()
        val message = txt_post_message.text.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val date = sdf.format(Date())

        //aanmaken post object en push naar databank
        val ref = FirebaseDatabase.getInstance().getReference("Posts")
        val postId = ref.push().key.toString()
        val post = Post(postId, titel, "Brent", date, categorie, message)

        ref.child(postId).setValue(post).addOnCompleteListener{
            Toast.makeText(applicationContext,"Post werd succesvol toegevoegd", Toast.LENGTH_SHORT).show()
        }
    }


    //validatie van het formulier om een post toe te voegen
    private fun validateForm(titel: String, categorie : String, message: String): Boolean {
        if (TextUtils.isEmpty(titel)) {
            Toast.makeText(applicationContext, "Gelieve een titel in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(categorie)) {
            Toast.makeText(applicationContext, "Gelieve een categorie in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(applicationContext, "Gelieve een bericht in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if(titel.length < 5){
            Toast.makeText(applicationContext, "Gelieve een titel langer dan 5 karakters te kiezen", Toast.LENGTH_SHORT).show()
            return false
        }
        if(message.length < 15){
            Toast.makeText(applicationContext, "Gelieve een bericht in te geven dat langer is dan 15 karakters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
