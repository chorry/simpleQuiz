package com.example.simplequiz;

/**
 * Created by alutsenko on 28.11.14.
 */
public class GenericQuestionModel<T1, T2> {
    protected String   question;
    protected T1 answers;
    protected T2 answer_correct;
    private int      type;

    public GenericQuestionModel() {
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(T1 answers) {
        this.answers = answers;
    }

    public void setAnswer_correct(T2 answer_correct) {
        this.answer_correct = answer_correct;
    }

}