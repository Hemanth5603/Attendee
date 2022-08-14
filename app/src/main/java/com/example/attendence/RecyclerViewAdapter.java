package com.example.attendence;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Person> personlist;
    private String Classname1 = "";

    public RecyclerViewAdapter(Context context, ArrayList<Person> personlist,String Classname) {
        this.context = context;
        this.personlist = personlist;
        this.Classname1 = Classname;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_person,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Person person = personlist.get(position);
        holder.textView.setText(person.getRoll());
        holder.checkBox.setChecked(person.isSelected());
        //Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),android.R.anim.fade_in);
        //holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {return personlist.size();}



    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView textView;
        private TextView custom_row_classname;
        private ConstraintLayout rowItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.checkBox = itemView.findViewById(R.id.checkBox);
            this.textView = itemView.findViewById(R.id.textView);
            this.rowItem = itemView.findViewById(R.id.rowitem);
            this.custom_row_classname = itemView.findViewById(R.id.custom_row_classname);

            custom_row_classname.setText(Classname1);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = ((CheckBox) v).isChecked();

                    if(isChecked){
                        personlist.get(getAbsoluteAdapterPosition()).setSelected(true);
                    }else{
                        personlist.get(getAbsoluteAdapterPosition()).setSelected(false);
                    }
                    notifyDataSetChanged();
                    for(int i=0;i<personlist.size();i++){
                        Log.d("Tag",personlist.toString());
                    }
                }
            });

            /*rowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, String.valueOf(personlist.get(getAbsoluteAdapterPosition()) ), Toast.LENGTH_SHORT).show();
                }
            });*/

        }
    }
}
