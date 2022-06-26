package net.manish.mybscitsem06;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListModel>
{
    private final ArrayList<ListModel> objects;
    public ListAdapter(Context context, int textViewResourceId, ArrayList<ListModel> questions)
    {
        super(context, textViewResourceId, questions);
        objects = questions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.list_row, parent, false);
        TextView textQuestion = v.findViewById(R.id.txtQSTN);
        TextView textSelectedAnswer = v.findViewById(R.id.txtSelectedANS);
        TextView textActualAnswer = v.findViewById(R.id.txtActualANS);
        String dummy = "QUESTION :  " + objects.get(position).getQuestion();
        textQuestion.setText(dummy);
        dummy = "SELECTED ANSWER : " + objects.get(position).getSelectedAnswer();
        textSelectedAnswer.setText(dummy);
        dummy = "CORRECT ANSWER : " + objects.get(position).getActualAnswer();
        textActualAnswer.setText(dummy);
        return v;
    }
}
