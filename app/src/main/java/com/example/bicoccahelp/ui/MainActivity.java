package com.example.bicoccahelp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.utils.ServiceLocator;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
