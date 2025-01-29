package com.example.smartspine;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PostureHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostureHistoryAdapter adapter;
    private List<PostureData> postureDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load posture data (this should be replaced with actual data loading logic)
        postureDataList = loadPostureData();

        adapter = new PostureHistoryAdapter(postureDataList);
        recyclerView.setAdapter(adapter);
    }

    private List<PostureData> loadPostureData() {
        // Placeholder data, replace with actual data loading logic
        List<PostureData> data = new ArrayList<>();
        data.add(new PostureData("Good Posture", "2024-06-10 10:00:00"));
        data.add(new PostureData("Leaning Forward", "2024-06-10 10:10:00"));
        data.add(new PostureData("Good Posture", "2024-06-10 10:20:00"));
        data.add(new PostureData("Leaning Left", "2024-06-10 10:30:00"));
        return data;
    }
}
