package com.example.simplequiz;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "zce-questions.sqlite3";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_QUESTION_TYPE = "question_type";
    public static final String COLUMN_ANSWERS = "answers";
    public static final String COLUMN_ANSWER_CORRECT = "answer_correct";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
