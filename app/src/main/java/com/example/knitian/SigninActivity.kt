package com.example.knitian

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        signup_link_btn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        login_btn.setOnClickListener {
            loginUser()

        }


    }

    private fun loginUser() {
        val emaill = emaill_login.text.toString()
        val password = password_login.text.toString()

        when{
            TextUtils.isEmpty(emaill)-> Toast.makeText(this, "please enter your emaill" , Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password)-> Toast.makeText(this, "creat password" , Toast.LENGTH_LONG).show()

            else -> {
                val proogressDialog = ProgressDialog(this)
                proogressDialog.setTitle("LogIn")
                proogressDialog.setMessage("Please wait, this a while.....")
                proogressDialog.setCanceledOnTouchOutside(false)
                proogressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(emaill, password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        proogressDialog.dismiss()

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
    }



    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null)
        {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}