package com.example.simplequiz;

import java.util.List;

/**
 * Created by alexc_000 on 29.11.2014.
 */
public class DbQuestion {
    private String question;
    private List<String> answers;
    private List<String> answer_correct;
    private int question_type;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getAnswer_correct() {
        return answer_correct;
    }

    public void setAnswer_correct(List<String> answer_correct) {
        this.answer_correct = answer_correct;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    @Override
    public String toString() {
        return question;
    }

    public boolean compareAnswer(List<String> a)
    {
        int correct = 0;
        if (a.size() < 1)
            return false;

        for (int i=0; i<a.size(); i++)
        {
            if ( this.answer_correct.contains(a.get(i)) )
            {
                ++correct;
            }
        }

        return (correct == a.size());
    }

    public String getType()
    {
        switch (this.question_type)
        {
            case 1:
                return "EditText";
            default:
                return "CheckBox";
        }
    }
}
