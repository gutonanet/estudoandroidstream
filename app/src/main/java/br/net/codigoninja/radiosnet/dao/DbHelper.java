package br.net.codigoninja.radiosnet.dao;

/**
 * Created by guton on 12/02/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "bancoApp";
    private final String CREATE_TABLE = "CREATE TABLE Radios (ID INTEGER PRIMARY KEY, Nome TEXT NOT NULL, URL TEXT, UF TEXT NOT NULL);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Radios");
        onCreate(db);
    }
}