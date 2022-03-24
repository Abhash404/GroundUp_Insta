package com.pasa.groundup_insta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        if(ParseUser.getCurrentUser() != null) {

            goToMainActivity()
        }

        findViewById<Button>(R.id.btLogin).setOnClickListener{

            val username = findViewById<EditText>(R.id.etUsername).text.toString();
            val password = findViewById<EditText>(R.id.etPassword).text.toString();
            loginUser(username, password);
        }

        findViewById<Button>(R.id.btSignup).setOnClickListener{

            val username = findViewById<EditText>(R.id.etUsername).text.toString();
            val password = findViewById<EditText>(R.id.etPassword).text.toString();
            signUpUser(username, password);
        }
    }

    private fun loginUser(username: String, password: String) {

        ParseUser.logInInBackground(username, password, ({ user, e ->

            if (user != null) {

                Log.i(TAG, "Logged In")
                goToMainActivity()

            }

            else {

                e.printStackTrace()
                Toast.makeText(this, "Error! Please try again", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun signUpUser(username: String, password: String){

        val user = ParseUser()

        user.username = username
        user.setPassword(password)

        user.signUpInBackground { e ->

            if (e == null) {

                Log.i(TAG, "Signed Up")
                goToMainActivity()

            }

            else {

                e.printStackTrace()
                Toast.makeText(this, "Error! Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {

        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}