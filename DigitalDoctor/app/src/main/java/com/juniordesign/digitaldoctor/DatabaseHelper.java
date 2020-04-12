package com.juniordesign.digitaldoctor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    // create constants necessary for database creation -- database name and version number
    private static final String DATABASE_NAME = "DigitalDoctor.db";
    private static final Integer DATABASE_VERSION = 1;

    // create constants necessary for table creation -- table name and columns
    static final String BODY_PART_TABLE = "body_part_specific_table";
    static final String GENERALIZED_SYMPTOM_TABLE = "generalized_symptom_table";
    static final String PREGNANCY_TABLE = "pregnancy_table";
    static final String CHILDHOOD_SYMPTOM_TABLE = "childhood_table";
    static final String DIAGNOSIS_TABLE = "diagnosis_table";

    static final String PRIMARY_AREA = "primary_area";
    static final String PRIMARY_SYMPTOM = "primary_symptom";
    static final String EXTRA_INFORMATION = "extra_info";
    static final String SYMPTOM_NAME = "symptom_name";
    static final String SYMPTOM_SEVERITY = "symptom_severity";
    static final String SYMPTOM_INFORMATION = "symptom_info";


    // This creates a database (without a factory), as the first version of the database
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    // creates the necessary tables for the database, through SQL as a String
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
    // this is necessary for if the database changes structure (columns shifted, etc), as
    // we will need to update the version of the database, which will delete the old version
    // and store the new one within system files. I do not anticipate using this.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + BODY_PART_TABLE);
        db.execSQL("drop table if exists " + GENERALIZED_SYMPTOM_TABLE);
        db.execSQL("drop table if exists " + PREGNANCY_TABLE);
        db.execSQL("drop table if exists " + CHILDHOOD_SYMPTOM_TABLE);
        db.execSQL("drop table if exists " + DIAGNOSIS_TABLE);
        onCreate(db);
    }

    // params -- reader - a BufferedReader linked to a text file
    //           tableName - a string for what table we are loading the text data into
    // returns -- boolean -> never used, may modify to void later
    public boolean loadAllData(BufferedReader reader, String tableName) {
        String line;
        try {
            while( null != ( line = reader.readLine() ) ){
                String[] values = formatLine( line );
                insertData(tableName, values);
            }
            reader.close();
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    // takes in a String and returns an array split at the @ symbols
    public String[] formatLine(String line) {
        return line.split("@", 0);
    }

    //Inserts data, completely generic. Expects a table name, and an array of values in the correct
    //ordering of the table
    // doesn't need to be a boolean currently - may modify later
    public boolean insertData(String tableName, String[] values) {
        SQLiteDatabase db = this.getWritableDatabase();
        //content values are the type of data that is needed to insert into the table, essentially
        //it just specifies that these Strings are now content for the database
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

    // removes all data from the database, currently used on first load to make certain we
    // are testing the correct values in the database
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists " + BODY_PART_TABLE);
        db.execSQL("drop table if exists " + GENERALIZED_SYMPTOM_TABLE);
        db.execSQL("drop table if exists " + PREGNANCY_TABLE);
        db.execSQL("drop table if exists " + CHILDHOOD_SYMPTOM_TABLE);
        db.execSQL("drop table if exists " + DIAGNOSIS_TABLE);
        onCreate(db);
    }

    //This is an example for a deletion. I don't anticipate needing any of these unless we put in
    //bad values to our system. For now, I have left it non-generic, as I don't think it is worth
    //the time to invest making it more generic currently.
    public Integer deleteData(String symptomName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BODY_PART_TABLE, "" + SYMPTOM_NAME + " = ?", new String[] {symptomName});
    }

    //ester - this is a generic select method that returns a cursor with all unique _select entries from tableName
    //where items in whereColumns match with items in whereMatches
    public Cursor getData(String _select, String tableName, ArrayList<String> whereColumns, ArrayList<String> whereMatches) {
        SQLiteDatabase db = this.getWritableDatabase();

        //whereString includes the SQL commands that test equivalencies between each item in whereColumns with
        //corresponding item in whereMatches
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < whereColumns.size(); i++) {
            sb.append(whereColumns.get(i));
            sb.append(" = '");
            sb.append(whereMatches.get(i));
            sb.append("'");
            if (i < whereColumns.size() - 1) {
                sb.append(" AND ");
            }
        }

        String whereString = sb.toString();
        Cursor cur;
        //if there are items in whereColumns, include a WHERE statement in the SQL command. otherwise, WHERE can be
        //omitted from SQL command
        if (whereColumns.size() > 0) {
            cur = db.rawQuery("select distinct (" + _select + ") from " + tableName + " where " +
                    whereString, null);
        } else {
            cur = db.rawQuery("select distinct (" + _select + ") from " + tableName, null);
        }

        return cur;
    }
}
