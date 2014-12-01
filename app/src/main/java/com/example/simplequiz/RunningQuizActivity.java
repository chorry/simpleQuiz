package com.example.simplequiz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RunningQuizActivity extends ActionBarActivity {
	private int currentQuestion;
	private int maxQuestions;
	private List<DbQuestion> QuestionList;
	private int answersCorrect = 0;
	private int answersWrong   = 0;
	private DbQuestionDataSource dbQuestionDataSource;
	public List<String> questionAnswers;

	public ListView answersView;

    public int timeLimit     = 0;
    public int questionLimit = 0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle b = getIntent().getExtras();
        this.setTimeLimit( b.getInt("Time_limit") );
        this.setQuestionLimit( b.getInt("Questions_limit") );

        //default is checkbox
        answersView = (ListView) findViewById(R.id.answers_listview);

        //AnswersCheckBoxAdapter adapter = new AnswersCheckBoxAdapter(this, questionAnswers);
        //answersView.setAdapter( adapter );

        this.init( );
    }


    public void setTimeLimit ( int a)
    {
        this.timeLimit = a;
        if (this.timeLimit > 0 )
        {
            //TODO: start timer
        }
    }

    public void setQuestionLimit ( int a )
    {
        this.questionLimit = a;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void init()
    {
        dbQuestionDataSource = new DbQuestionDataSource(this);
        QuestionList   = dbQuestionDataSource.getRandomQuestions(Integer.toString(this.questionLimit));
        dbQuestionDataSource.close();

    	this.initQuestionList();
    	currentQuestion = 0;
    	this.showQuestion();
    }
    
    public void initQuestionList()
    {
    	maxQuestions = QuestionList.size();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void checkAnswer()
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);
        if (qElement.getType().equals("CheckBox") )
        {
            checkCheckBoxAnswer();
        }
        else
        {
            checkEditTextAnswer();
        }
    }

    public void checkEditTextAnswer()
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);
        StringBuilder result = new StringBuilder();


        List<String> currentAnswers =
                Arrays.asList(
                        ((AnswersEditTextAdapter) answersView.getAdapter())
                                .getEnteredText()
                                .split("\\s*,\\s*")
                );
        Log.e("debug", "textFound:" + ((AnswersEditTextAdapter) answersView.getAdapter()).getEnteredText() );
        //split string to ListArray
        Log.e("debug","curAnswers:");

        for(Iterator<String> i = currentAnswers.iterator(); i.hasNext(); )
        {
            String item = i.next();
            Log.e("debug","answers:[" + item + "]");
        }



        Toast.makeText(RunningQuizActivity.this, Boolean.toString( qElement.compareAnswer( currentAnswers )), Toast.LENGTH_SHORT).show();
        result.delete(0, result.length());

    }

    public void checkCheckBoxAnswer()
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);

        List<String> currentAnswers = new ArrayList<String>();
    	StringBuilder result = new StringBuilder();

        for(
        		int i=0;
        		i < ((AnswersCheckBoxAdapter) answersView.getAdapter()).mCheckStates.size();
        		i++
        	)
        {
        	Log.v("example","mCheckId=" + Integer.toString(i));
            if(
                    ((AnswersCheckBoxAdapter) answersView.getAdapter())
                            .mCheckStates.get(i)
              )
            {
            	result.append("box[" + Integer.toString(i) + "]:" + qElement.getAnswers().get(i));
                result.append("\n");
                currentAnswers.add(qElement.getAnswers().get(i));
            }
        }

        Log.v("example","again:" + result.toString());
        Toast.makeText(RunningQuizActivity.this, Boolean.toString( qElement.compareAnswer( currentAnswers )), Toast.LENGTH_SHORT).show();
        result.delete(0, result.length());

    }

    public void nextQuestionClick(View view)
    {
        this.nextQuestion();
    }
    public void prevQuestionClick(View view)
    {
        this.prevQuestion();
    }

    public void nextQuestion()
    {
        checkAnswer();
        currentQuestion++;
        if (currentQuestion == QuestionList.size() )
            currentQuestion = 0;
        showQuestion();
    }

    public void prevQuestion()
    {
        checkAnswer();
        currentQuestion--;
        if (currentQuestion < 0  )
            currentQuestion = QuestionList.size() -1;

        showQuestion();
    }

    public void showQuestion()
    {

    	TextView questionView = (TextView) findViewById(R.id.text_question);

        DbQuestion qElement = QuestionList.get(currentQuestion);
    	
    	questionView.setText( qElement.getQuestion() );
    	
    	this.questionAnswers = qElement.getAnswers();

        this.updateAdapter( qElement.getType() );

        if (qElement.getType().equals("CheckBox") )
        {
            ((AnswersCheckBoxAdapter) answersView.getAdapter()).updateAnswers(qElement.getAnswers());

        }

        if (qElement.getType().equals("EditText") )
        {
            ((AnswersEditTextAdapter) answersView.getAdapter()).updateAnswers();
        }
    }


    public void updateAdapter(String questionType)
    {

        if (questionType.equals("EditText") ) //&& ( answersView.getAdapter() instanceof AnswersCheckBoxAdapter) )
        {
            Log.e("debug","new Edit adapter");
            AnswersEditTextAdapter adapter = new AnswersEditTextAdapter(this);
            answersView.setAdapter( adapter );
            adapter.notifyDataSetChanged();
        }

        if (questionType.equals("CheckBox") ) //&& ( answersView.getAdapter() instanceof AnswersEditTextAdapter) )
        {
            Log.e("debug","new Box adapter");
            AnswersCheckBoxAdapter adapter = new AnswersCheckBoxAdapter(this, questionAnswers);
            answersView.setAdapter( adapter );
            adapter.notifyDataSetChanged();
        }
    }

    public void updateStatusText()
    {
    	TextView t = (TextView) findViewById(R.id.text_answer);
    	t.setText( Integer.toString(currentQuestion+1) + " of " + Integer.toString(maxQuestions)  + "\n" + 
    	"Wrong:" + Integer.toString(answersWrong) + ", Correct:"+ Integer.toString(answersCorrect));
    }

    
    
}
