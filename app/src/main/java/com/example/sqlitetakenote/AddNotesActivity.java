package com.example.sqlitetakenote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitetakenote.databinding.ActivityAddNotesBinding;

public class AddNotesActivity extends AppCompatActivity {
    ActivityAddNotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(binding.title.getText().toString().trim()) && !TextUtils.isEmpty(binding.description.getText().toString().trim())) {
                    Database db = new Database(AddNotesActivity.this);
                    db.addNotes(binding.title.getText().toString(), binding.description.getText().toString());

                    Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddNotesActivity.this, "Both Fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}