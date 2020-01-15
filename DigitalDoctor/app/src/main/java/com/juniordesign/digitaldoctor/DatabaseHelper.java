package com.juniordesign.digitaldoctor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DigitalDoctor.db";
    public static final Integer DATABASE_VERSION = 1;

    public static final String BODY_PART_TABLE = "body_part_specific_table";
    public static final String GENERALIZED_SYMPTOM_TABLE = "generalized_symptom_table";
    public static final String PREGNANCY_TABLE = "pregnancy_table";
    public static final String CHILDHOOD_SYMPTOM_TABLE = "childhood_table";
    public static final String DIAGNOSIS_TABLE = "diagnosis_table";

    public static final String PRIMARY_AREA = "primary_area";
    public static final String PRIMARY_SYMPTOM = "primary_symptom";
    public static final String EXTRA_INFORMATION = "extra_info";
    public static final String SYMPTOM_NAME = "symptom_name";
    public static final String SYMPTOM_SEVERITY = "symptom_severity";
    public static final String SYMPTOM_INFORMATION = "symptom_info";

    public DatabaseHelper(@Nullable Context context) {
        // This creates a database (without a factory), as the first version of the database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table body_part_specific_table (primary_area TEXT, primary_symptom TEXT,
        // extra_info TEXT, symptom_name TEXT, PRIMARY KEY ("primary_symptom, symptom_name))
        db.execSQL("create table " + BODY_PART_TABLE + " (" + PRIMARY_AREA + " TEXT, " +
                PRIMARY_SYMPTOM + " TEXT, " + EXTRA_INFORMATION + " TEXT, " + SYMPTOM_NAME +
                " TEXT, PRIMARY KEY (" + PRIMARY_SYMPTOM + ", " + SYMPTOM_NAME + "))");

        // create table generalized_symptom_table + (primary_symptom TEXT, extra_info TEXT,
        // symptom_name TEXT, PRIMARY KEY (primary_symptom, symptom_name))
        db.execSQL("create table " + GENERALIZED_SYMPTOM_TABLE + " (" + PRIMARY_SYMPTOM + " TEXT, "
                + EXTRA_INFORMATION + " TEXT, " + SYMPTOM_NAME + " TEXT, PRIMARY KEY (" +
                PRIMARY_SYMPTOM + ", " + SYMPTOM_NAME + "))");

        // create table pregnancy_table + (primary_symptom TEXT, extra_info TEXT,
        // symptom_name TEXT, PRIMARY KEY (primary_symptom, symptom_name))
        db.execSQL("create table " + PREGNANCY_TABLE + " (" + PRIMARY_SYMPTOM + " TEXT, "
                + EXTRA_INFORMATION + " TEXT, " + SYMPTOM_NAME + " TEXT, PRIMARY KEY (" +
                PRIMARY_SYMPTOM + ", " + SYMPTOM_NAME + "))");

        // create table childhood_table + (primary_area TEXT, extra_info TEXT,
        // symptom_name TEXT, PRIMARY KEY (primary_area, symptom_name))
        db.execSQL("create table " + CHILDHOOD_SYMPTOM_TABLE + " (" + PRIMARY_AREA + " TEXT, "
                + EXTRA_INFORMATION + " TEXT, " + SYMPTOM_NAME + " TEXT, PRIMARY KEY (" +
                PRIMARY_AREA + ", " + SYMPTOM_NAME + "))");

        // create table diagnosis_table + (symptom_name TEXT, symptom_severity TEXT,
        // symptom_information TEXT, PRIMARY KEY (symptom_name))
        db.execSQL("create table " + DIAGNOSIS_TABLE + " (" + SYMPTOM_NAME + " TEXT, "
                + SYMPTOM_SEVERITY + " TEXT, " + SYMPTOM_INFORMATION + " TEXT, PRIMARY KEY (" +
                SYMPTOM_NAME + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + BODY_PART_TABLE);
        db.execSQL("drop table if exists " + GENERALIZED_SYMPTOM_TABLE);
        db.execSQL("drop table if exists " + PREGNANCY_TABLE);
        db.execSQL("drop table if exists " + CHILDHOOD_SYMPTOM_TABLE);
        db.execSQL("drop table if exists " + DIAGNOSIS_TABLE);
        onCreate(db);
    }

    public boolean insertData(String tableName, String[] values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (tableName.equals(BODY_PART_TABLE)) {
            contentValues.put(PRIMARY_AREA, values[0]);
            contentValues.put(PRIMARY_SYMPTOM, values[1]);
            contentValues.put(EXTRA_INFORMATION, values[2]);
            contentValues.put(SYMPTOM_NAME, values[3]);
        } else if (tableName.equals(GENERALIZED_SYMPTOM_TABLE) ||
                tableName.equals(PREGNANCY_TABLE)) {
            contentValues.put(PRIMARY_SYMPTOM, values[0]);
            contentValues.put(EXTRA_INFORMATION, values[1]);
            contentValues.put(SYMPTOM_NAME, values[2]);
        } else if (tableName.equals(CHILDHOOD_SYMPTOM_TABLE)) {
            contentValues.put(PRIMARY_AREA, values[0]);
            contentValues.put(EXTRA_INFORMATION, values[1]);
            contentValues.put(SYMPTOM_NAME, values[2]);
        } else if (tableName.equals(DIAGNOSIS_TABLE)) {
            contentValues.put(SYMPTOM_NAME, values[0]);
            contentValues.put(SYMPTOM_SEVERITY, values[1]);
            contentValues.put(SYMPTOM_INFORMATION, values[2]);
        } else {
            return false;
        }
        long result = db.insert(tableName, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableName, null);
        return res;
    }

    public Integer deleteData(String symptomName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BODY_PART_TABLE, "" + SYMPTOM_NAME + " = ?", new String[] {symptomName});
    }
}
