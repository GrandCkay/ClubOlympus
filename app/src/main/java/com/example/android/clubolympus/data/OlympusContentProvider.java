package com.example.android.clubolympus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.clubolympus.data.ClubOlympusContract.*;

// Создаем КонтентПровайдер для работы с базой данных

public class OlympusContentProvider extends ContentProvider {

    OlympusDbOpenHandler dbOpenHandler;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;

    // Creates a UriMatcher object
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ClubOlympusContract.AUTHORITY, ClubOlympusContract.PATH_MEMBERS, MEMBERS);  // Uri код для работы со всей таблицей (используем константы для исключения ошибки)
        uriMatcher.addURI(ClubOlympusContract.AUTHORITY, ClubOlympusContract.PATH_MEMBERS
                + "/#", MEMBER_ID); // Uri код для работы с одной строкой (используем константы для исключения ошибки)
    }


    @Override
    public boolean onCreate() {
        dbOpenHandler = new OlympusDbOpenHandler(getContext());
        return true;
    }

    /* Uri (Unified Resource Identifier) - уникальный постоянный индификатор ресурса
       content://com.example.android.clubolympus/members
       content:// - схема, стандартный префикс для доступа к контенту (scheme)
       com.example.android.clubolympus - уникальное значение которое будет использоваться (content authority)
        * обычно используется название пакета
       /members - информация о типе данных, имя таблицы с которой нужно работать (type of data)
        * если нужна определенная строка (например 34) то указывается так /members/34

       URL (Unified Resource Locator)
       //http://google.com  */


    // метод * Read
    @Override
         /* Вводимые параметры
            Uri - content://com.example.android.clubolympus/members/34
            projection (имена столбцов имена которых будет выведена) = { "lastName", "gender" ]
         */

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                // Проверка таблицы * content://com.example.android.clubolympus/members/
                // В cursor хранится все строки таблицы
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MEMBER_ID:
                // Проверка одной строки * content://com.example.android.clubolympus/members/34
                selection = MemberEntry._ID + "=?"; // отбор
                // selectionArgs - аргументы отбора, где ContentUris.parseId - преабразует последний сегмент после последнего слеша в число (34)
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                // Получаем из базы данных строку преобразовануго числа (ID)
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);  // обновление таблицы, при помощи uri определяем обращение к всей таблице или одной строки

        return cursor;
    }

    // метод *Create
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Проверка вводных данных
        String firstName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("You have to input first name");
        }

        String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("You have to input last name");
        }

        Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
        if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN ||
                gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
            throw new IllegalArgumentException("You have to input correct gender");
        }

        String sport = values.getAsString(MemberEntry.COLUMN_SPORT);
        if (sport == null) {
            throw new IllegalArgumentException("You have to input sport");
        }

        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                long id =  db.insert(MemberEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e("insertMethod", "Insertion of data in the table failed for " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion od data in the table failed for " + uri);
        }
    }

    // метод * Delete
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDelete;

        switch (match) {
            case MEMBERS:
                // Проверка таблицы * content://com.example.android.clubolympus/members/
                rowsDelete = db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEMBER_ID:
                // Проверка одной строки * content://com.example.android.clubolympus/members/34
                selection = MemberEntry._ID + "=?"; // отбор
                // selectionArgs - аргументы отбора, где ContentUris.parseId - преабразует последний сегмент после последнего слеша в число (34)
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                // Получаем из базы данных строку преобразовануго числа (ID)
                rowsDelete = db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't delete this URI " + uri);
        }

        if (rowsDelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDelete;
    }

    // метод * Update
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Проверка вводных данных
        if (values.containsKey(MemberEntry.COLUMN_FIRST_NAME)) {
            String firstName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }

        if (values.containsKey(MemberEntry.COLUMN_LAST_NAME)) {
            String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }

        if (values.containsKey(MemberEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
            if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN ||
                    gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }

        if (values.containsKey(MemberEntry.COLUMN_SPORT)) {
            String sport = values.getAsString(MemberEntry.COLUMN_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsUpdate;

        switch (match) {
            case MEMBERS:
                // Проверка таблицы * content://com.example.android.clubolympus/members/
                rowsUpdate = db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);

                if (rowsUpdate != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsUpdate;


            case MEMBER_ID:
                // Проверка одной строки * content://com.example.android.clubolympus/members/34
                selection = MemberEntry._ID + "=?"; // отбор
                // selectionArgs - аргументы отбора, где ContentUris.parseId - преабразует последний сегмент после последнего слеша в число (34)
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                // Получаем из базы данных строку преобразовануго числа (ID)
                rowsUpdate = db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);

            if (rowsUpdate != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rowsUpdate;

            default:
                throw new IllegalArgumentException("Can't update this URI " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                // Проверка таблицы * content://com.example.android.clubolympus/members/

                return MemberEntry.CONTENT_MULTIPLE_ITEM;
            case MEMBER_ID:
                // Проверка одной строки * content://com.example.android.clubolympus/members/34

                return MemberEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
