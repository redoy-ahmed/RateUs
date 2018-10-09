package com.example.redoy.rateus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rating_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Rating.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Rating.TABLE_NAME);
        onCreate(db);
    }

    public long insertRating(String date, int good, int average, int bad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Rating.COLUMN_DATE, date);
        values.put(Rating.COLUMN_GOOD, good);
        values.put(Rating.COLUMN_AVERAGE, average);
        values.put(Rating.COLUMN_BAD, bad);
        long id = db.insert(Rating.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public Rating getRating(String date) {

        SQLiteDatabase db = this.getReadableDatabase();

        Rating rating;

        Cursor cursor = db.query(Rating.TABLE_NAME,
                new String[]{Rating.COLUMN_DATE, Rating.COLUMN_GOOD, Rating.COLUMN_AVERAGE, Rating.COLUMN_BAD},
                Rating.COLUMN_DATE + "=?",
                new String[]{date}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            rating = new Rating(cursor.getString(cursor.getColumnIndex(Rating.COLUMN_DATE)), cursor.getInt(cursor.getColumnIndex(Rating.COLUMN_GOOD)), cursor.getInt(cursor.getColumnIndex(Rating.COLUMN_AVERAGE)), cursor.getInt(cursor.getColumnIndex(Rating.COLUMN_BAD)));
            cursor.close();
            return rating;
        }
        return null;
    }

    public List<Rating> getAllRating() {
        List<Rating> ratings = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Rating.TABLE_NAME + " ORDER BY " + Rating.COLUMN_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Rating rating = new Rating();
                rating.setDate(cursor.getString(cursor.getColumnIndex(Rating.COLUMN_DATE)));
                rating.setGood(cursor.getInt(cursor.getColumnIndex(Rating.COLUMN_GOOD)));
                rating.setAverage(cursor.getInt(cursor.getColumnIndex(Rating.COLUMN_AVERAGE)));
                rating.setBad(cursor.getInt(cursor.getColumnIndex(Rating.COLUMN_BAD)));

                ratings.add(rating);
            } while (cursor.moveToNext());
        }

        db.close();
        return ratings;
    }

    public int getRatingCount() {
        String countQuery = "SELECT  * FROM " + Rating.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateRating(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Rating.COLUMN_GOOD, rating.getGood());
        values.put(Rating.COLUMN_AVERAGE, rating.getAverage());
        values.put(Rating.COLUMN_BAD, rating.getBad());

        return db.update(Rating.TABLE_NAME, values, Rating.COLUMN_DATE + " = ?", new String[]{rating.getDate()});
    }

    public void deleteRating(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Rating.TABLE_NAME, Rating.COLUMN_DATE + " = ?", new String[]{rating.getDate()});
        db.close();
    }
}
