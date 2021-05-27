package com.example.newsfy_rework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class PreferencesActivity : AppCompatActivity() {
    private var categoryArray : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preferences_page)

        val chipBox: ChipGroup = findViewById(R.id.preferences_chip_group)
        val finishButton : Button = findViewById(R.id.button_done)
        val addButton : Button = findViewById(R.id.add_preference_button)
        val input: EditText = findViewById(R.id.preference_name)

        addButton.setOnClickListener {

            if(chipBox.childCount > 5){
                Toast.makeText(this, "Too many!", Toast.LENGTH_SHORT)
            }
            else {
                val topic = input.text.toString().trim()
                if (input.text.isEmpty()) {
                    Toast.makeText(this, "No topic!", Toast.LENGTH_SHORT)
                } else {
                    categoryArray.add(topic)
                    val chip =
                        layoutInflater.inflate(R.layout.filter_chip_layout, chipBox, false) as Chip
                    chip.text = topic
                    chip.isClickable = true
                    chip.isChecked = true

                    chip.setOnClickListener {
                        chipBox.removeView(chip)
                        categoryArray.remove(topic)
                    }

                    chipBox.addView(chip)
                    input.text.clear()
                }
            }
        }

        //on finished, add filters to the preferences
        finishButton.setOnClickListener {
            val intent = Intent(this@PreferencesActivity, SignupActivity::class.java)
            intent.putExtra("preferences", categoryArray)
            startActivity(intent)
        }
    }
}