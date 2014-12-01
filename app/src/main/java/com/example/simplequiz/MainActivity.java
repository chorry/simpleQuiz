package com.example.simplequiz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.example.simplequiz.MESSAGE";
	private int currentQuestion;
	private int maxQuestions;
	private List<DbQuestion> QuestionList;
	private int answersCorrect = 0;
	private int answersWrong   = 0;
	private DbQuestionDataSource dbQuestionDataSource;
	public List<String> questionAnswers;
	public ListView answersView;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //default is checkbox
        answersView = (ListView) findViewById(R.id.answers_listview);

        AnswersCheckBoxAdapter adapter = new AnswersCheckBoxAdapter(this, questionAnswers);
        answersView.setAdapter( adapter );
        //answersView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //AnswersEditTextAdapter adapter = new AnswersEditTextAdapter(this);
        //answersView.setAdapter( adapter );
        this.init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //This is to be moved to a SQlite someday
    public void init()
    {
        dbQuestionDataSource = new DbQuestionDataSource(this);
        QuestionList   = dbQuestionDataSource.getAllQuestions();
        dbQuestionDataSource.close();
    	this.initQuestionList();
    	currentQuestion = -1;
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkAnswer(View view)
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);
        if (qElement.getType() == "CheckBox")
        {
            checkCheckBoxAnswer(view);
        }
        else
        {
            checkEditTextAnswer(view);
        }

    }

    public void checkEditTextAnswer(View view)
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);
        EditText et;
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
        for (int i=0; i < currentAnswers.size(); i++)
        {
            Log.e("debug","answers:[" + currentAnswers.get(i) + "]");
        }


        Toast.makeText(MainActivity.this, Boolean.toString( qElement.compareAnswer( currentAnswers )), Toast.LENGTH_SHORT).show();
        result.delete(0, result.length());

        this.showQuestion();
    }

    public void checkCheckBoxAnswer(View view)
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);

    	CheckBox cb;
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
        Toast.makeText(MainActivity.this, Boolean.toString( qElement.compareAnswer( currentAnswers )), Toast.LENGTH_SHORT).show();
        result.delete(0, result.length());


		this.showQuestion();
    }
    
    
    public void showQuestion()
    {
    	currentQuestion++;
    	if (currentQuestion == QuestionList.size() )
    		currentQuestion = 0;

    	TextView questionView = (TextView) findViewById(R.id.text_question);

        DbQuestion qElement = QuestionList.get(currentQuestion);
    	
    	questionView.setText( qElement.getQuestion() );
    	
    	this.questionAnswers = qElement.getAnswers();

        this.updateAdapter( qElement.getType() );

        if (qElement.getType() == "CheckBox" )
        {
            ((AnswersCheckBoxAdapter) answersView.getAdapter()).updateAnswers(qElement.getAnswers());
        }

        if (qElement.getType() == "EditText" )
        {
            ((AnswersEditTextAdapter) answersView.getAdapter()).updateAnswers();
        }
    }


    public void updateAdapter(String questionType)
    {
        Log.e("debug","do we need a new adapter?");
        Log.e("debug", "Current is " + answersView.getAdapter().getClass().toString() + " , qType is " + questionType );

        if (questionType == "EditText" ) //&& ( answersView.getAdapter() instanceof AnswersCheckBoxAdapter) )
        {
            Log.e("debug","new Edit adapter");
            AnswersEditTextAdapter adapter = new AnswersEditTextAdapter(this);

            answersView.setAdapter( adapter );
            adapter.notifyDataSetChanged();
        }

        if (questionType == "CheckBox" ) //&& ( answersView.getAdapter() instanceof AnswersEditTextAdapter) )
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
