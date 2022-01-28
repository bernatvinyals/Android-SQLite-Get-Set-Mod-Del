package com.example.gestisimpledarticles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class articleHelper extends SQLiteOpenHelper {
    // database version
    private static final int database_VERSION = 1;

    // database name
    private static final String database_NAME = "toDoListDataBase";

    public articleHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOLIST =
                "CREATE TABLE articlelist ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "codiArticle TEXT NOT NULL,"+
                        "description TEXT NOT NULL," +
                        "pvp REAL NOT NULL," +
                        "estoc REAL DEFAULT 0)";

        db.execSQL(CREATE_TODOLIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // De moment no fem res

    }
}
