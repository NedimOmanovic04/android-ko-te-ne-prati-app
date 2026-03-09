package com.example.instagram_followers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.zip.ZipInputStream

class MainActivity : AppCompatActivity() {

    private val PICK_ZIP = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Button>(R.id.btnInfo).setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        findViewById<Button>(R.id.btnFollowers).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" }
            startActivityForResult(intent, PICK_ZIP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        val uri: Uri = data?.data ?: return
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnAnalyze = findViewById<Button>(R.id.btnAnalyze)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        if (requestCode == PICK_ZIP) {
            try {


                var followersJson: String? = null
                var followingJson: String? = null

                val inputStream = contentResolver.openInputStream(uri)
                val zis = ZipInputStream(inputStream)
                var entry = zis.nextEntry

                while (entry != null) {
                    val name = entry.name.lowercase()
                    val bytes = zis.readBytes() // čitaj bytes prije closeEntry
                    val text = bytes.toString(Charsets.UTF_8)

                    when {
                        name.endsWith("followers_1.json") -> {
                            followersJson = text
                        }
                        name.endsWith("following.json") -> {
                            followingJson = text
                        }
                    }
                    zis.closeEntry()
                    entry = zis.nextEntry
                }
                zis.close()

                if (followersJson == null || followingJson == null) {
                    tvStatus.text = "❌ Nisu pronađeni followers/following fajlovi u ZIP-u!"
                    return
                }

                val gson = Gson()

// Parsuj followers
                val followersType = object : TypeToken<List<UserEntry>>() {}.type
                val followersList: List<UserEntry> = gson.fromJson(followersJson, followersType)
                val followersSet = followersList.map { entry ->
                    entry.string_list_data.firstOrNull()?.value?.takeIf { it.isNotEmpty() }
                        ?: entry.title
                        ?: entry.string_list_data.firstOrNull()?.href?.substringAfterLast("/") ?: ""
                }.filter { it.isNotEmpty() }.toSet()

// Parsuj following
                var followingList: List<Pair<String, String>> = emptyList()
                try {
                    val root: FollowingRoot = gson.fromJson(followingJson, FollowingRoot::class.java)
                    followingList = root.relationships_following.map { entry ->
                        val href = entry.string_list_data.firstOrNull()?.href ?: ""
                        val username = entry.string_list_data.firstOrNull()?.value?.takeIf { it.isNotEmpty() }
                            ?: entry.title
                            ?: href.substringAfterLast("/")
                        Pair(username, href)
                    }.filter { it.first.isNotEmpty() }
                } catch (e: Exception) {
                    val type = object : TypeToken<List<UserEntry>>() {}.type
                    val list: List<UserEntry> = gson.fromJson(followingJson, type)
                    followingList = list.map { entry ->
                        val href = entry.string_list_data.firstOrNull()?.href ?: ""
                        val username = entry.string_list_data.firstOrNull()?.value?.takeIf { it.isNotEmpty() }
                            ?: entry.title
                            ?: href.substringAfterLast("/")
                        Pair(username, href)
                    }.filter { it.first.isNotEmpty() }
                }
                // Izračunaj ko ne prati nazad
                val notFollowingBack = followingList.filter { (username, _) ->
                    username !in followersSet
                }



                val intent = Intent(this, ResultsActivity::class.java).apply {
                    putStringArrayListExtra("usernames", ArrayList(notFollowingBack.map { it.first }))
                    putStringArrayListExtra("hrefs", ArrayList(notFollowingBack.map { it.second }))
                    putExtra("following", followingList.size)
                    putExtra("followers", followersSet.size)
                }
                startActivity(intent)

            } catch (e: Exception) {
                tvStatus.text = "❌ Greška: ${e.message}"
            }
        }
    }
}