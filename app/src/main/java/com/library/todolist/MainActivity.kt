package com.library.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.library.todolist.databinding.ActivityMainBinding
import com.library.todolist.fragment.FirstPage
import com.library.todolist.fragment.SecondPage
import com.library.todolist.realm.RealmControllerEvent
import com.library.todolist.realm.RealmControllerUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val realmControllerUser by lazy { RealmControllerUser() }
    private val realmControllerEvent by lazy { RealmControllerEvent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        insertDefaultUser()
        binding.tvSubmit.setOnClickListener {
            if (binding.etUserName.text.isEmpty()) {
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.etPass.text.isEmpty()) {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (realmControllerUser.isUserCorrect(
                    binding.etUserName.text.toString(),
                    binding.etPass.text.toString()
                )
            ) {
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show()

            }


        }
    }

    private fun insertDefaultUser(
    ) {
        if (!realmControllerUser.isUserCorrect(
                "test", "123456"
            )
        ) {
            realmControllerUser.insertChatBaseDataToRoom(
                "test", "123456"
            )
            realmControllerEvent.insertEvent(
                "Olahraga Sepak bola",
                TimeConverter.getTime("2021-06-01 08:30:31"),
                "Bawa Perlengkapan sendiri",
                "Budi",
                false
            ) {}
            realmControllerEvent.insertEvent(
                "Main Musik Bareng",
                TimeConverter.getTime("2021-06-02 13:00:00"),
                "-",
                "Andi",
                false
            ) {}
            realmControllerEvent.insertEvent(
                "Makan Siang Perusahaan",
                TimeConverter.getTime("2021-06-04 12:30:31"),
                "Diharapkan Tepat Waktu",
                "Jessica",
                false
            ) {}
            realmControllerEvent.insertEvent(
                "Reuni Sekolah",
                TimeConverter.getTime("2021-06-03 18:30:31"),
                "-",
                "Toni",
                false
            ) {}
        }

    }
}