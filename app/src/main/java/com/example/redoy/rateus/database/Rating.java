package com.example.redoy.rateus.database;

/**
 * Created by ravi on 20/02/18.
 */

public class Rating {
    public static final String TABLE_NAME = "ratings";

    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GOOD = "good";
    public static final String COLUMN_AVERAGE = "average";
    public static final String COLUMN_BAD = "bad";

    private int good;
    private int average;
    private int bad;
    private String date;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_DATE + " TEXT PRIMARY KEY, "
                    + COLUMN_GOOD + " INTEGER,"
                    + COLUMN_AVERAGE + " INTEGER,"
                    + COLUMN_BAD + " INTEGER"
                    + ")";

    Rating() {
    }

    public Rating(String date, int good, int average, int bad) {
        this.good = good;
        this.average = average;
        this.bad = bad;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }
}
