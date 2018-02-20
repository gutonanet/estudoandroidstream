package br.net.codigoninja.radiosnet.dao;

/**
 * Created by guton on 12/02/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "bancoApp";
    private final String CREATE_TABLE_CIDADES = "CREATE TABLE Cidades (ID INTEGER PRIMARY KEY, UF TEXT NOT NULL, NOME TEXT NOT NULL);";
    private final String CREATE_TABLE_GENEROS = "CREATE TABLE Generos (ID INTEGER PRIMARY KEY, NOME TEXT NOT NULL);";
    private final String CREATE_TABLE_RADIOS = "CREATE TABLE Radios (ID INTEGER PRIMARY KEY, NOME TEXT NOT NULL, URL TEXT, ID_GENERO INTEGER NOT NULL, ID_CIDADE INTEGER NOT NULL, FAVORITO INTEGER);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CIDADES);
        db.execSQL(CREATE_TABLE_GENEROS);
        db.execSQL(CREATE_TABLE_RADIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Radios");
        db.execSQL("DROP TABLE IF EXISTS Cidades");
        db.execSQL("DROP TABLE IF EXISTS Generos");
        onCreate(db);
    }
}