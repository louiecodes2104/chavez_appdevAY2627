package com.example.chavez_androiday2627;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chavez_androiday2627.databinding.ActivityStationDetailBinding;

public class StationDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStationDetailBinding binding =
                ActivityStationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] statuses = {"Ready", "Needs Check", "Issue Logged"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, statuses);
        binding.spStatus.setAdapter(adapter);

        String code = getIntent().getStringExtra(MainActivity.EXTRA_STATION_CODE);
        String status = getIntent().getStringExtra(MainActivity.EXTRA_STATUS);
        String note = getIntent().getStringExtra(MainActivity.EXTRA_ISSUE_NOTE);

        binding.tvDetailTitle.setText("Inspect Station: " + (code != null ? code : "PC-01"));
        if (note != null) {
            binding.etIssueNote.setText(note);
        }
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(status)) {
                binding.spStatus.setSelection(i);
                break;
            }
        }

        binding.btnSaveInspection.setOnClickListener(v -> {
            String updatedNote = binding.etIssueNote.getText().toString().trim();
            String selectedStatus = binding.spStatus.getSelectedItem().toString();
            if ("Issue Logged".equals(selectedStatus) && updatedNote.isEmpty()) {
                Toast.makeText(this, "Enter an issue note before saving.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent reply = new Intent();
            reply.putExtra(MainActivity.EXTRA_STATUS, selectedStatus);
            reply.putExtra(MainActivity.EXTRA_ISSUE_NOTE, updatedNote.isEmpty() ? "No issues reported" : updatedNote);
            setResult(RESULT_OK, reply);
            finish();
        });
    }
}
