package com.example.simplequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbQuestionDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {
            DbHelper .COLUMN_ID,
            DbHelper .COLUMN_QUESTION,
            DbHelper .COLUMN_QUESTION_TYPE,
            DbHelper .COLUMN_ANSWERS,
            DbHelper .COLUMN_ANSWER_CORRECT,
    };

    public DbQuestionDataSource(Context context) {
        Log.i("DBDS","Making dbHelper");
        dbHelper = new DbHelper(context);
        this.open();
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DbQuestion createQuestion(String question, String answers, String answers_correct, int question_type) {
        ContentValues values = new ContentValues();

        values.put(DbHelper.COLUMN_QUESTION, question);
        values.put(DbHelper.COLUMN_QUESTION_TYPE, question_type);
        values.put(DbHelper.COLUMN_ANSWERS, answers);
        values.put(DbHelper.COLUMN_ANSWER_CORRECT, answers_correct);

        long insertId = database.insert(DbHelper.TABLE_QUESTIONS, null, values);
        Cursor cursor = database.query(
                DbHelper.TABLE_QUESTIONS,
                allColumns,
                DbHelper.COLUMN_ID + " = " + insertId,
                null,
                null, null, null);
        cursor.moveToFirst();
        DbQuestion newQuestion = cursorToQuestion(cursor);
        cursor.close();
        return newQuestion;
    }



    public List<DbQuestion> getAllQuestions() {
        List<DbQuestion> questions = new ArrayList<DbQuestion>();

        Cursor cursor = database.query(DbHelper.TABLE_QUESTIONS,
                allColumns,
                DbHelper.COLUMN_QUESTION_TYPE+"=?",
                new String[] { "1" },
                null, //groupBy
                null, //having
                null); //orderBy

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DbQuestion question = cursorToQuestion(cursor);
            questions.add(question);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return questions;
    }

    public List<DbQuestion> getRandomQuestions( String amount )
    {
        List<DbQuestion> questions = new ArrayList<DbQuestion>();

        Cursor cursor = database.query(DbHelper.TABLE_QUESTIONS,
                allColumns,
                null,
                null,
                null, //groupBy
                null, //having
                "RANDOM()",
                amount);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DbQuestion question = cursorToQuestion(cursor);
            questions.add(question);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return questions;
    }

    private DbQuestion cursorToQuestion(Cursor cursor) {
        DbQuestion question = new DbQuestion();
        //question.setId(cursor.getLong(0));
        question.setQuestion(cursor.getString(1));
        question.setQuestion_type(cursor.getInt(2));

        List<String> currentAnswers =
                Arrays.asList(
                        cursor.getString(3).split("\\s*,\\s*")
                );
        question.setAnswers( currentAnswers );

        List<String> correctAnswers =
                Arrays.asList(
                        cursor.getString(4).split("\\s*,\\s*")
                );

        question.setAnswer_correct( correctAnswers);

        return question;
    }
}
