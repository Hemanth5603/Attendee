package com.example.attendence;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private String mystr;
    private TextView greetname;
    private LinearLayout takeattendence;
    Button takeattendencebutton;
    private Spinner spinner;
    EditText startroll,endroll,period;
    TextView inputerror;
    String Initial = "";
    String temp = "";
    String finalendroll = "";
    String indata = "";
    String choice = "";






    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        greetname = view.findViewById(R.id.greetname);
        takeattendence = view.findViewById(R.id.takeattendence);
        takeattendencebutton = view.findViewById(R.id.takeattendencebutton);
        startroll = view.findViewById(R.id.startroll);
        endroll = view.findViewById(R.id.endroll);
        period = view.findViewById(R.id.period);
        inputerror = view.findViewById(R.id.inputerror);
        spinner = view.findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this.getActivity(),R.array.classes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);





        Bundle bundle = this.getArguments();
        if(bundle!=null){
            indata = bundle.getString("usernames");
        }else{
            //greetname.setText("not data");
        }


        //greetname.setText("Hello " + indata + "!" );
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String currentDateandTime = sdf.format(new Date());
        int dat = Integer.parseInt(currentDateandTime);
        if(dat>4 && dat<12) greetname.setText("Good Morning ");
        else if(dat>=12 && dat<16) greetname.setText("Good Afternoon ");
        else if(dat>=16 && dat<21) greetname.setText("Good Evening ");
        else greetname.setText("Welcome !");

        Intent it = new Intent(getActivity(),attendence.class);
        Activity activity = getActivity();
        takeattendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Boolean inputcheck = false;
                if(startroll.getText().toString().trim().equals("") || endroll.getText().toString().trim().equals("")){
                    inputerror.setText("*Enter All Input Fields");
                }
                else {
                    temp = endroll.getText().toString();
                    convertendroll();
                    inputerror.setText("");
                    sendDataToAttendence();
                }
            }
        });
        takeattendencebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startroll.getText().toString().trim().equals("") || endroll.getText().toString().trim().equals("")){
                    inputerror.setText("*Enter All Input Fields");
                }
                else {
                    temp = endroll.getText().toString();
                    convertendroll();
                    inputerror.setText("");
                    sendDataToAttendence();}
            }
        });

        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        choice = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void convertendroll(){ // Also check for startRoll for ECE guys due to range 197 ************

        if(temp.indexOf('A')>=0 || temp.indexOf('a')>=0) Initial = "10";
        else if(temp.indexOf('B')>=0 || temp.indexOf('b')>=0) Initial = "11";
        else if(temp.indexOf('C')>=0 || temp.indexOf('c')>=0) Initial = "12";
        else if(temp.indexOf('D')>=0 || temp.indexOf('d')>=0) Initial = "13";
        else if(temp.indexOf('E')>=0 || temp.indexOf('e')>=0) Initial = "14";
        else if(temp.indexOf('F')>=0 || temp.indexOf('f')>=0) Initial = "15";
        else if(temp.indexOf('G')>=0 || temp.indexOf('g')>=0) Initial = "16";
        else if(temp.indexOf('H')>=0 || temp.indexOf('h')>=0) Initial = "17";
        else if(temp.indexOf('I')>=0 || temp.indexOf('i')>=0) Initial = "18";
        else if(temp.indexOf('J')>=0 || temp.indexOf('j')>=0) Initial = "19";
        else Initial = "";
        if(!Initial.equals("")){
            finalendroll =  Initial+temp.charAt(temp.length()-1);

        }else finalendroll =  temp;

    }

    public void sendDataToAttendence(){
        Intent it = new Intent(getActivity(),attendence.class);
        it.putExtra("PersonName",indata);
        it.putExtra("classname",choice);
        it.putExtra("startroll",startroll.getText().toString());
        it.putExtra("endroll",finalendroll);
        it.putExtra("period",period.getText().toString());

        getActivity().startActivity(it);
    }





}