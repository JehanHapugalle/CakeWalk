package com.example.thecakewalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {

    Button log;
    EditText editText1;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editText1=(EditText)findViewById(R.id.Aemail);
        editText2=(EditText)findViewById(R.id.Apass);
        log=(Button)findViewById(R.id.Alogin);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText1.getText().toString().equals("admin") && editText2.getText().toString().equals("admin")){
                    Toast.makeText(AdminLogin.this, "Successfully sign in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AdminLogin.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}