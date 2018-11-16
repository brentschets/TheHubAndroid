package brentschets.com.projecthub


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_plaats_post.*
import brentschets.com.projecthub.model.Post
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class PlaatsPostFragment : Fragment(), View.OnClickListener{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_plaats_post, container, false)
        val btnSubmitPost = view.findViewById<Button>(R.id.btn_post_submit)
        btnSubmitPost.setOnClickListener(this)
        return view

    }

    //click event voor save post en maak de text fields leeg
    override fun onClick(view: View?) {
        val i = view!!.id
        if(i == R.id.btn_post_submit){
            if(validateForm(txt_post_titel.text.toString(), sp_post_category.selectedItem.toString(),txt_post_message.text.toString())){
                savePost()
            }else{
                Toast.makeText(activity,"Het toevoegen van een post ondervond een fout!", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
            Toast.makeText(activity,"Post werd succesvol toegevoegd", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(titel: String, categorie : String, message: String): Boolean {
        if (TextUtils.isEmpty(titel)) {
            Toast.makeText(activity, "Gelieve een titel in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(categorie)) {
            Toast.makeText(activity, "Gelieve een categorie in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(activity, "Gelieve een bericht in te vullen", Toast.LENGTH_SHORT).show()
            return false
        }

        if(titel.length < 5){
            Toast.makeText(activity, "Gelieve een titel langer dan 5 karakters te kiezen", Toast.LENGTH_SHORT).show()
            return false
        }
        if(message.length < 15){
            Toast.makeText(activity, "Gelieve een bericht in te geven dat langer is dan 15 karakters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}
