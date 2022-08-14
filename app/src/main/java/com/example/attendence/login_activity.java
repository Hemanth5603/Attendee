package com.example.attendence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class login_activity extends AppCompatActivity {
    Timer timer;
    String name1 = "";
    String email1 = "";
    private loginDBHelper loginDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(login_activity.this, R.color.custom_blue1));

        EditText username = (EditText) findViewById(R.id.username);
        TextView inputer = (TextView) findViewById(R.id.inputer);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        Button back = (Button) findViewById(R.id.back);
        Button Register = (Button) findViewById(R.id.Register);
        Intent i = new Intent(this, getstarted.class);

        loginDBHelper = new loginDBHelper(this);


        /*SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String FirstTime = pref.getString("FirstTimeInstall", "");


        if (FirstTime.equals("Yes")) {
            Intent inte = new Intent(this, HomeScreen.class);
            startActivity(inte);
            finish();
        } else {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }*/

        Intent intent = new Intent(this, HomeScreen.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
                overridePendingTransition(R.anim.left_anim, R.anim.rightout_anim);
                finish();
            }
        });


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final LoadingDialog loadingDialog = new LoadingDialog(login_activity.this);


                loadingDialog.startLoadingDialig();
                String name = username.getText().toString();
                String em = email.getText().toString();
                String pass = password.getText().toString();

                if (name.equals("") || em.equals("") || pass.equals("")) {
                    inputer.setText("Please enter all Details...");

                } else {

                    loginDBHelper.registerUser(new UserData(name, em, pass));
                    Toast.makeText(login_activity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();


                }
                username.setText("");
                email.setText("");
                password.setText("");




                Cursor cur = loginDBHelper.getinfo();
                if (cur.getCount() == 0)
                    Toast.makeText(login_activity.this, "Not Registered Successfully", Toast.LENGTH_SHORT).show();

                while (cur.moveToNext()) {
                    name1 = cur.getString(1).toString();
                    email1 = cur.getString(2).toString();
                }

                intent.putExtra("name1", name1);
                intent.putExtra("email1", email1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 1500);


                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 1500);

            }
        });
    }


    public void sendData(){
        Intent inti = new Intent(this,HomeScreen.class);
        Cursor cur = loginDBHelper.getinfo();
        if (cur.getCount() == 0)
            Toast.makeText(login_activity.this, "Not Registered Successfully", Toast.LENGTH_SHORT).show();

        while (cur.moveToNext()) {
            name1 = cur.getString(1).toString();
            email1 = cur.getString(2).toString();
        }

        inti.putExtra("name1", name1);
        inti.putExtra("email1", email1);

    }




    private void senddatatofrag(){
        Fragment fragment = new HomeFragment();
        Bundle bun = new Bundle();
        bun.putString("namii","Hemanth");

        //fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();fragmentTransaction.replace(R.id.container,fragment).commit();

    }

}