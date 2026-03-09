package com.example.instagram_followers

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val usernames = intent.getStringArrayListExtra("usernames") ?: arrayListOf()
        val hrefs = intent.getStringArrayListExtra("hrefs") ?: arrayListOf()
        val following = intent.getIntExtra("following", 0)
        val followers = intent.getIntExtra("followers", 0)

        val users = usernames.zip(hrefs)

        findViewById<TextView>(R.id.tvStats).text =
            "Pratim: $following  |  Prate me: $followers  |  Ne prate: ${users.size}"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(users)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}