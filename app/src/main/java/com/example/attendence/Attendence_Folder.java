package com.example.attendence;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.T;


public class Attendence_Folder extends Fragment {

    public Attendence_Folder() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_back, container, false);

        String path = "";
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            path = bundle.getString("PATH");
        }else{
            path = "";
        }

        if(path.equals("")){
            //Toast.makeText(getActivity(), "Cannot Open !", Toast.LENGTH_SHORT).show();
        }else{
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
            //DocumentsContract.Document.MIME_TYPE_DIR
            startActivity(intent);
        }


        return view;
    }
}