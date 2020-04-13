package com.example.android.clubolympus.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ClubOlympusContract {

    // Создаем приватный конструктор данного класса чтоб исключить возможность создания этого класса с наружи этого класса
    private ClubOlympusContract() {    }


    // Создаем DATABASE_VERSION u DATABASE_NAME в классе ClubOlympusContract, так как база данных для всего приложения
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "olympus";


    /* Uri (Unified Resource Identifier) - уникальный постоянный индификатор ресурса
       content://com.example.android.clubolympus/members
       content:// - схема, стандартный префикс для доступа к контенту (scheme)
       com.example.android.clubolympus - уникальное значение которое будет использоваться (content authority)
        * обычно используется название пакета
       /members - информация о типе данных, имя таблицы с которой нужно работать (type of data)
        * если нужна определенная строка (например 34) то указывается так /members/34  * ("/#")
    */


    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.android.clubolympus";
    public static final String PATH_MEMBERS = "members";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTHORITY);


    public static final class MemberEntry implements BaseColumns {

        public static final String TABLE_NAME = "members";

        public static final String _ID = BaseColumns._ID;  //Используем констанку из интерфейка BaseColumns вместа создания своей константы
        public static final String COLUMN_FIRST_NAME = "firstName" ;
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SPORT = "sport";


        // Создаем константы пля переменной gender
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + AUTHORITY + "/" + PATH_MEMBERS; // метод возвращает значение МИМЕ для работы с несколькими строками таблицы
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + AUTHORITY + "/" + PATH_MEMBERS; // метод возвращает значение МИМЕ для работы с одной записью
    }
}
