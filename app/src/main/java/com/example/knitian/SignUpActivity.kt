package com.example.knitian

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signin.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)



        signin_link_btn.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }


        signup_btn.setOnClickListener {
            CreateAccount()
        }
    }



    private fun CreateAccount() {
        val fullName = fullname_signup.text.toString()
        val userName = username_signup.text.toString()
        val emaill = emaill_signup.text.toString()
        val password = password_signup.text.toString()

        when{
            TextUtils.isEmpty(fullName)-> Toast.makeText(this, "please enter your full name" , Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName)-> Toast.makeText(this, "please enter username" , Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(emaill)-> Toast.makeText(this, "please enter your emaill" , Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password)-> Toast.makeText(this, "creat password" , Toast.LENGTH_LONG).show()

            else -> {
                val proogressDialog = ProgressDialog(this)
                proogressDialog.setTitle("SignUp")
                proogressDialog.setMessage("Please wait, this a while.....")
                proogressDialog.setCanceledOnTouchOutside(false)
                proogressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(emaill, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            saveUserInfo(fullName ,userName , emaill, proogressDialog)

                        }else{
                            val massage = task.exception!!.toString()
                            Toast.makeText(this, "Error: $massage", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            proogressDialog.dismiss()
                        }
                    }
            }
        }
    }

    private fun saveUserInfo(fullName: String, userName: String, emaill: String, proogressDialog: ProgressDialog) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName.toLowerCase()
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = emaill
        userMap["bio"] = "hey I am KNITian."
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/knitian-4d990.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=ce8cb132-0318-460d-8887-503f7b770e82"


        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    proogressDialog.dismiss()
                    Toast.makeText(this, "Account hase been created successefully", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val massage = task.exception!!.toString()
                    Toast.makeText(this, "Error: $massage", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    proogressDialog.dismiss()

                }
            }

    }
}