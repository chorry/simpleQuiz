package com.example.simplequiz;

public class UserAnswers<T>  {
    public T userAnswers;

    public T getUserAnswers()
    {
        return this.userAnswers;
    }

    public void setUserAnswers(T userAnswers)
    {
        this.userAnswers = userAnswers;
    }
}
