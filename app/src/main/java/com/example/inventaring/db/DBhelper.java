package com.example.inventaring.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION =4;
    private static final String DATABASE_NAME="Inventaring.db";
    private static final String DATABASE_TABLE ="Inventario";

    public DBhelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_TABLE +"(" +
                "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "Clave TEXT NOT NULL ," +
                "PRODUCTO TEXT NOT NULL," +
                "CANTIDAD INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("Drop TABLE " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
