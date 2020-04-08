package com.example.dotify

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private val randomNumber = Random.nextInt(0, 100000000)
    private var playNum = randomNumber


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numOfPlay = findViewById<TextView>(R.id.tvNumOfPlay)
        numOfPlay.text = "$randomNumber plays"
        val userInput = findViewById<EditText>(R.id.tvUserName)
        userInput.isEnabled = false

        val image = findViewById<ImageView>(R.id.imageView)
        image.setOnLongClickListener {
            val all = findViewById<ConstraintLayout>(R.id.all)
            val result = ArrayList<TextView>()
            for (i in 0 until all.childCount) {
                if (all.getChildAt(i) is TextView) {
                    result.add((all.getChildAt(i) as TextView))
                }
            }


            for (i in 0 until result.size) {
                if (result[i].currentTextColor != resources.getColor(R.color.colorGrey)) {
                    result[i].setTextColor(resources.getColor(R.color.colorGrey))
                } else {
                    result[i].setTextColor(resources.getColor(R.color.colorBlack))
                }
            }

            true
        }

    }

    fun playClicked(view: View) {
        playNum += 1
        findViewById<TextView>(R.id.tvNumOfPlay).text = "$playNum plays"
    }

    fun skipPre(view: View) {
        Toast.makeText(this, "Skipping to previous track", Toast.LENGTH_SHORT).show()
    }

    fun skipNext(view: View) {
        Toast.makeText(this, "Skipping to next track", Toast.LENGTH_SHORT).show()
    }

    fun changeUser(view: View) {
        val userInput = findViewById<EditText>(R.id.tvUserName)
        val buttonStatus = findViewById<Button>(R.id.btnChangeUser)
        if (buttonStatus.text == "CHANGE USER") {
            buttonStatus.text = "APPLY"
            userInput.isEnabled = true
        } else if (userInput.text.toString() != "") {
            buttonStatus.text = "CHANGE USER"
            userInput.isEnabled = false
        }
    }

}
