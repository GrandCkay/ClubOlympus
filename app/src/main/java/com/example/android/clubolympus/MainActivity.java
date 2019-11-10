package com.example.android.clubolympus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.clubolympus.data.ClubOlympusContract.MemberEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Member;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEMBER_LOADER = 123;
        MemberCursorAdapter memberCursorAdapter;

    ListView dataListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListView = findViewById(R.id.dataListView);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });

        memberCursorAdapter = new MemberCursorAdapter(this, null, false);
        dataListView.setAdapter(memberCursorAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);

                Uri currentMemberUri = ContentUris.withAppendedId(MemberEntry.CONTENT_URI, id);
                intent.setData(currentMemberUri);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(MEMBER_LOADER, null, this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayData();
//    }

//    private void displayData() {
//        String [] projection = {
//                MemberEntry._ID,
//                MemberEntry.COLUMN_FIRST_NAME,
//                MemberEntry.COLUMN_LAST_NAME,
//                MemberEntry.COLUMN_GENDER,
//                MemberEntry.COLUMN_SPORT
//        };
//
//        //  Делаем запрос к базе данных
//            Cursor cursor = getContentResolver().query(MemberEntry.CONTENT_URI,
//                    projection, null, null, null);
//
//        dataTextView.setText("All members\n\n");
//        dataTextView.append(MemberEntry._ID + " " +
//                MemberEntry.COLUMN_FIRST_NAME + " " +
//                MemberEntry.COLUMN_LAST_NAME + " " +
//                MemberEntry.COLUMN_GENDER + " " +
//                MemberEntry.COLUMN_SPORT);
//
//        // Получаем индекс каждой колонки из cursor и сохраняем значение типa int
//        int idColumnIndex = cursor.getColumnIndex(MemberEntry._ID);
//        int firstNameColumnIndex = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
//        int lastNameColumnIndex = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
//        int genderColumnIndex = cursor.getColumnIndex(MemberEntry.COLUMN_GENDER);
//        int sportColumnIndex = cursor.getColumnIndex(MemberEntry.COLUMN_SPORT);
//
//        //Перебераем все строки, получаем строки пока есть данные
//        while (cursor.moveToNext()) {
//            int currentId = cursor.getInt(idColumnIndex);
//            String currentFirstName = cursor.getString(firstNameColumnIndex);
//            String currentLastName = cursor.getString(lastNameColumnIndex);
//            int currentGender = cursor.getInt(genderColumnIndex);
//            String currentSport = cursor.getString(sportColumnIndex);
//
//            dataTextView.append("\n" +
//            currentId + " " +
//            currentFirstName + " " +
//            currentLastName + " " +
//            currentGender + " " +
//            currentSport);
//        }
//        cursor.close();
//
//
//        MemberCursorAdapter cursorAdapter = new MemberCursorAdapter(this, cursor, false);
//        dataListView.setAdapter(cursorAdapter);
//    }


    // Создаем отдельный (фоновом) поток обновления данных
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String [] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_SPORT
        };

        CursorLoader cursorLoader = new  CursorLoader(this, MemberEntry.CONTENT_URI,
                projection, null, null, null);

        return cursorLoader;
    }


    // Распологаем полученные данные в интерфейсе
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        memberCursorAdapter.swapCursor(cursor);

    }

    // Метод удаляем ненужные ссылки на измененные или удаленные данные чтобы не было утечки памяти
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        memberCursorAdapter.swapCursor(null);

    }
}
