package com.example.exchallenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.exchallenger.Helpers.LocalSaveHelper;
import com.example.exchallenger.Helpers.MainHelper;

import java.util.List;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        List<Map<String, Object>> map = new LocalSaveHelper(getApplicationContext()).getListMap("exercises");
        for (Map<String, Object> child : map)
        {
            Log.d(MainHelper.TAG, "Name => " + child.get("name"));
        }
    }
}
