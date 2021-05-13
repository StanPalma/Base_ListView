package com.example.base_listview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Base extends SQLiteOpenHelper {
    public Base(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Metodo onCreate para crear la Base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS Compras(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS Compras";
        db.execSQL(query);
        onCreate(db);
    }
}
