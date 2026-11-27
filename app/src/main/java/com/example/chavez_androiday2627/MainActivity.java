package com.example.chavez_androiday2627;

import android.os.Bundle;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.chavez_androiday2627.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_STATION_CODE = "extra_station_code";
    public static final String EXTRA_STATUS = "extra_status";
    public static final String EXTRA_ISSUE_NOTE = "extra_issue_note";

    private ActivityMainBinding binding;
    private String currentCode = "PC-07";
    private String currentStatus = "Needs Check";
    private String currentNote = "Emulator slow to boot";

    private final String[] stationCodes = {"PC-07", "PC-08", "PC-09"};
    private final String[] stationStatuses = {"Needs Check", "Ready", "Ready"};
    private final String[] stationNotes = {"Emulator slow to boot", "No issues reported", "No issues reported"};
    private int stationIndex = 0;
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    currentStatus = result.getData().getStringExtra(EXTRA_STATUS);
                    currentNote = result.getData().getStringExtra(EXTRA_ISSUE_NOTE);
                    stationStatuses[stationIndex] = currentStatus;
                    stationNotes[stationIndex] = currentNote;
                    binding.tvStationStatus.setText(currentStatus);
                    binding.tvStationSpec.setText("Note: " + currentNote);
                    Toast.makeText(this, "Updated " + currentCode + " -> " + currentStatus, Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnInspectStation.setOnClickListener(v -> {
            Intent intent = new Intent(this, StationDetailActivity.class);
            intent.putExtra(EXTRA_STATION_CODE, currentCode);
            intent.putExtra(EXTRA_STATUS, currentStatus);
            intent.putExtra(EXTRA_ISSUE_NOTE, currentNote);
            detailLauncher.launch(intent);
        });

        binding.btnShareReport.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LabCheck Report: " + currentCode);
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Station: " + currentCode + "\nStatus: " + currentStatus + "\nNote: " + currentNote);
            startActivity(Intent.createChooser(shareIntent, "Share Station Status"));
        });

        // Enhancement 1: switch stations before inspecting
        binding.btnSwitchStation.setOnClickListener(v -> {
            stationIndex = (stationIndex + 1) % stationCodes.length;
            showStation();
        });

        // Enhancement 2: implicit intent to dial lab maintenance
        binding.btnCallSupport.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:+639123456789")); // replace with the real contact number
            startActivity(dialIntent);
        });
    }

    private void showStation() {
        currentCode = stationCodes[stationIndex];
        currentStatus = stationStatuses[stationIndex];
        currentNote = stationNotes[stationIndex];
        binding.tvStationCode.setText(currentCode + " • Android Studio SDK");
        binding.tvStationStatus.setText(currentStatus);
        binding.tvStationSpec.setText("Note: " + currentNote);
    }
}