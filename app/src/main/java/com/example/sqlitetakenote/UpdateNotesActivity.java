package com.example.sqlitetakenote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitetakenote.databinding.ActivityUpdateNotesBinding;

public class UpdateNotesActivity extends AppCompatActivity {
    ActivityUpdateNotesBinding binding;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        binding.title.setText(i.getStringExtra("title"));
        binding.description.setText(i.getStringExtra("description"));
        id = i.getStringExtra("id");

        binding.updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.title.getText().toString().trim()) && !TextUtils.isEmpty(binding.description.getText().toString().trim())) {
                    Database db = new Database(UpdateNotesActivity.this);
                    db.updateNotes(binding.title.getText().toString(), binding.description.getText().toString(), id);
                    Intent intent = new Intent(UpdateNotesActivity.this, MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(UpdateNotesActivity.this, "Both Fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}