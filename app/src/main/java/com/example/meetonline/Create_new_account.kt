package com.example.meetonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Create_new_account : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    //for go to the previous page
    private lateinit var btn_already_have_an_account: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_Name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener{

            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()


            register(name,email,password)

        }


        //this is also for previous page
        btn_already_have_an_account = findViewById(R.id.btn_already_have_an_account)
        btn_already_have_an_account.setOnClickListener {
            val intent = Intent( this,Login::class.java)
            startActivity(intent)
        }
    }

    private fun register(name: String , email: String , password: String){
        //logic of creating user

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for jumping to home activity

                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)

                    val intent = Intent(this@Create_new_account,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@Create_new_account,"Some error occurred", Toast.LENGTH_SHORT ).show()
                }
            }
    }

    private fun addUserToDatabase(name: String , email: String , uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}