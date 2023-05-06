package com.example.mad_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditGoalActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        val nameEditText = findViewById<EditText>(R.id.edit_goal_name_edit_text)
        val amountEditText = findViewById<EditText>(R.id.edit_goal_amount_edit_text)
        val descriptionEditText = findViewById<EditText>(R.id.edit_goal_description_edit_text)
        val categoryEditText = findViewById<EditText>(R.id.edit_goal_category_edit_text)
        val savedAmountEditText = findViewById<EditText>(R.id.edit_goal_saved_amount_edit_text)

        val goalId = intent.getStringExtra("GOAL_ID")
        if (goalId == null) {
            finish()
            return
        }

        db.collection("goals")
            .document(goalId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val goal = documentSnapshot.toObject(Goal::class.java)
                if (goal != null) {
                    nameEditText.setText(goal.name)
                    amountEditText.setText(goal.amount.toString())
                    savedAmountEditText.setText(goal.savedAmount.toString())
                    descriptionEditText.setText(goal.description)
                    categoryEditText.setText(goal.category)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
        val updateButton = findViewById<Button>(R.id.update_goal_button)
        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val savedAmount = savedAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val description = descriptionEditText.text.toString()
            val category = categoryEditText.text.toString()

            db.collection("goals")
                .document(goalId)
                .set(Goal(goalId, name, amount, savedAmount, description, category))
                .addOnSuccessListener {
                    // Handle success
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }


        val deleteButton = findViewById<Button>(R.id.delete_goal_button)
        deleteButton.setOnClickListener {
            db.collection("goals")
                .document(goalId)
                .delete()
                .addOnSuccessListener {
                    // Handle success
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }
    }
}
