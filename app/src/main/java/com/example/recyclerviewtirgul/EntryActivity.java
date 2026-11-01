package com.example.recyclerviewtirgul;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);
        boolean isRemembered = sharedPref.getBoolean("is_remembered", false);

        if (isRemembered) {
            Intent intent = new Intent(EntryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entry2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button log = findViewById(R.id.logIn);
        Button sign = findViewById(R.id.signIn);
        CheckBox isRemember = findViewById(R.id.remember);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = findViewById(R.id.name);
                EditText txtPass= findViewById(R.id.pass);
                String strName = txtName.getText().toString();
                String strPass = txtPass.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String namePref = sharedPref.getString("Name",null);
                String passPref = sharedPref.getString("Password",null);

                if(namePref == null || passPref == null || !namePref.equals(strName) || !passPref.equals(strPass)){
                    Toast.makeText(getApplicationContext(), "The values are not correct", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean remember = isRemember.isChecked();

                if (remember) {
                    editor.putBoolean("is_remembered", true);
                    editor.apply();
                }

                Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.signin, null);
                builder.setView(dialogView);

                AlertDialog customDialog = builder.create();
                customDialog.show();

                Button btnCancel = dialogView.findViewById(R.id.cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "The action was canceled", Toast.LENGTH_LONG).show();
                        customDialog.dismiss();
                    }
                });

                Button btnSign = dialogView.findViewById(R.id.signIn);
                btnSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtName = dialogView.findViewById(R.id.name);
                        EditText txtPass = dialogView.findViewById(R.id.pass);
                        EditText txtPhone = dialogView.findViewById(R.id.phoneNumber);
                        EditText txtEmail = dialogView.findViewById(R.id.email);


                        String strName = txtName.getText().toString();
                        String strPass = txtPass.getText().toString();
                        String strPhone = txtPhone.getText().toString();
                        String strEmail = txtEmail.getText().toString();

                        SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Name",strName);
                        editor.putString("Password",strPass);
                        editor.putString("Phone",strPhone);
                        editor.putString("Email",strEmail);
                        editor.apply();

                        customDialog.dismiss();
                    }
                });
            }
        });
    }
}