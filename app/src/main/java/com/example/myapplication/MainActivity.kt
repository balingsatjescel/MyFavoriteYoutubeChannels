package com.example.myapplication

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.handlers.YoutubeCHandler
import com.example.myapplication.models.Youtube
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var nameEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var rankEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var addEditButton: Button
    lateinit var youtubeCHandler: YoutubeCHandler
    lateinit var youtubechannels: ArrayList<Youtube>
    lateinit var youtubeListView: ListView
    lateinit var youtubeGettingEdited: Youtube

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        linkEditText = findViewById(R.id.linkEditText)
        rankEditText = findViewById(R.id.rankEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        addEditButton = findViewById(R.id.addEditButton)
        youtubeCHandler = YoutubeCHandler()

        youtubechannels = ArrayList()
        youtubeListView = findViewById(R.id.youtubeListView)

        addEditButton.setOnClickListener {
            val Cname = nameEditText.text.toString()
            val Clink = linkEditText.text.toString()
            val Crank = rankEditText.text.toString()
            val reason = reasonEditText.text.toString()

            if (addEditButton.text.toString() == "Add") {
                val youtube = Youtube(Cname = Cname, Clink = Clink, Crank = Crank, reason = reason)
                if (youtubeCHandler.create(youtube)) {
                    Toast.makeText(applicationContext, "Youtube Channel added", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            } else if (addEditButton.text.toString() == "Update") {
                val youtube = Youtube(id = youtubeGettingEdited.id, Cname = Cname, Clink = Clink, Crank = Crank, reason = reason)
                if (youtubeCHandler.update(youtube)) {
                    Toast.makeText(applicationContext, "Youtube Channel updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }

        }
        registerForContextMenu(youtubeListView)
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.youtube_options, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit_youtube -> {
                youtubeGettingEdited = youtubechannels[info.position]
                nameEditText.setText(youtubeGettingEdited.Cname)
                linkEditText.setText(youtubeGettingEdited.Clink)
                rankEditText.setText(youtubeGettingEdited.Crank)
                reasonEditText.setText(youtubeGettingEdited.reason)
                addEditButton.setText("Update")
                true
            }
            R.id.delete_youtube -> {

                if (youtubeCHandler.delete(youtubechannels[info.position])) {
                    Toast.makeText(applicationContext, "Youtube Channel updated", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }

    }

    override fun onStart() {
        super.onStart()
        youtubeCHandler.youtubeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                youtubechannels.clear()

                snapshot.children.forEach { it ->
                    val youtube = it.getValue(Youtube::class.java)
                    youtubechannels.add(youtube!!)
                }
                val adapter = ArrayAdapter<Youtube>(applicationContext, android.R.layout.simple_list_item_1, youtubechannels)
                youtubeListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun clearFields() {
        nameEditText.text.clear()
        linkEditText.text.clear()
        rankEditText.text.clear()
        reasonEditText.text.clear()
        addEditButton.setText("Add")
    }
}