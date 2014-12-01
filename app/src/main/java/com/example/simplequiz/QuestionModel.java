package com.example.simplequiz;

import java.util.Arrays;
import java.util.List;

public class QuestionModel extends GenericQuestionModel
{
	private String   question;
	private String[] answers;
	private String[] answer_correct;
	private int      type;

	public QuestionModel(String q, String[] a, String[] c, int type)  {
		this.setQuestion(q);
		this.setAnswers(a);
		this.setCorrectAnswer(c);
		this.setType(type);
	}

	public void setType(int t)
	{
		this.type = t;
	}

	public String getType()
	{
		switch (this.type)
		{
			case 1:
				return "EditText";
			default:
				return "CheckBox";
		}
	}
	public void setQuestion(String q)
	{
		this.question = q;
	}

	public String getQuestion()
	{
		return this.question;
	}

	public void setAnswers(String [] a)
	{
		this.answers = a;
	}

	public String[] getAnswers()
	{
		return this.answers;
	}

	public void setCorrectAnswer(String[] n)
	{
		this.answer_correct = n;
	}

	public boolean compareAnswer(List<String> a)
    {
        int correct = 0;
        for (int i=0; i<a.size(); i++)
        {
            if ( Arrays.asList(this.answer_correct).contains(a.get(i)) )
            {
                ++correct;
            }
        }

        if (correct == a.size())
            return true;
        return false;
    }

	public String[] getCorrectAnswer()
	{
		return this.answer_correct;
	}

	public int pow( int base, int exponent)
	{   // Does not work for negative exponents. (But that would be leaving the range of int)
	    if (exponent == 0) return 1;  // base case;
	    int temp = pow(base, exponent/2);
	    if (exponent % 2 == 0)
	        return temp * temp;
	    else
	        return (base * temp * temp);
	}


}
