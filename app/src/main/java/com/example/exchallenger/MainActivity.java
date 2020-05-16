package com.example.exchallenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.exchallenger.Helpers.MainHelper;
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Models.User;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testingHelper();
    }
    // this method for testing helper
    private void testingHelper()
    {
        new UserHelper().getUsersInfo("uniqueID_1", new UserHelper.GetUserInfo() {
            @Override
            public void onRead(User user) {
                Log.d(MainHelper.TAG, user.getName());
                copyUser(user);
            }
        });
    }

    public void copyUser(User user)
    {
        User newUser = new User(user);
        new UserHelper().addNewUser(newUser, new AddListener() {
            @Override
            public void onAdd() {
                Log.d(MainHelper.TAG, "Success Adding");
            }
        });
    }
}
