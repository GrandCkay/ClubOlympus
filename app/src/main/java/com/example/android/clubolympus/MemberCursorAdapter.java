package com.example.android.clubolympus;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.clubolympus.data.ClubOlympusContract;
import com.example.android.clubolympus.data.ClubOlympusContract.MemberEntry;

public class MemberCursorAdapter extends CursorAdapter {
    

    
    public MemberCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.member_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);
        TextView sportNameTextView = view.findViewById(R.id.sportNameTextView);
        TextView idNameTextView = view.findViewById(R.id.idNameTextView);
        TextView genderNameTextView = view.findViewById(R.id.genderNameTextView);

        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_FIRST_NAME));
        String  lastName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_LAST_NAME));
        String sportName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_SPORT));
        int idName = cursor.getInt(cursor.getColumnIndexOrThrow(MemberEntry._ID));
        int genderNameCursor = cursor.getInt(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_GENDER));

        String genderItem;
        if (genderNameCursor == 1){
            genderItem = "Male";
        } else if (genderNameCursor == 2){
            genderItem = "Female";
        } else {
            genderItem = "Unknown";
        }

        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        sportNameTextView.setText(sportName);
        idNameTextView.setText(String.valueOf(idName));
        genderNameTextView.setText(genderItem);
    }
}
