package com.example.simplequiz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunningQuizActivity extends ActionBarActivity {

    private DbQuestionDataSource dbQuestionDataSource;
	private List<DbQuestion>     QuestionList;
    private ArrayList            AnswersList;

    private int currentQuestion;
    private int maxQuestions;

	private int answersCorrect = 0;
	private int answersWrong   = 0;

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

        answersView = (ListView) findViewById(R.id.answers_listview);

        this.init( );

        this.showQuestion();
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

        maxQuestions = QuestionList.size();
    	currentQuestion = 0;

        this.initUserAnswers();
    }

    public void initUserAnswers()
    {
        this.AnswersList = new ArrayList( this.questionLimit );
        for (int i=0; i<this.questionLimit; i++)
        {
            this.AnswersList.add(null);
        }
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


        List<String> currentAnswers =
                Arrays.asList(
                        ((AnswersEditTextAdapter) answersView.getAdapter())
                                .getEnteredText()
                                .split("\\s*,\\s*")
                );

        UserAnswers<String> ua = new UserAnswers<String>();
        ua.setUserAnswers( ((AnswersEditTextAdapter) answersView.getAdapter())
                .getEnteredText() );

        saveUserAnswersEditText(currentQuestion, ua);

        Toast.makeText(RunningQuizActivity.this, Boolean.toString( qElement.compareAnswer( currentAnswers )), Toast.LENGTH_SHORT).show();

    }

    public void checkCheckBoxAnswer()
    {
        DbQuestion qElement = QuestionList.get(currentQuestion);

        List<String> currentAnswers = new ArrayList<String>();

        for(
            int i=0;
        	i < ((AnswersCheckBoxAdapter) answersView.getAdapter()).mCheckStates.size();
        	i++
        )
        {
            if(
                ((AnswersCheckBoxAdapter) answersView.getAdapter()).mCheckStates.get(i)
            )
            {
                currentAnswers.add(qElement.getAnswers().get(i));
            }
        }

        UserAnswers<SparseBooleanArray> ua = new UserAnswers<SparseBooleanArray>();
        ua.setUserAnswers( ((AnswersCheckBoxAdapter) answersView.getAdapter()).mCheckStates );
        saveUserAnswersCheckBox(currentQuestion, ua);

        Toast.makeText(RunningQuizActivity.this, Boolean.toString( qElement.compareAnswer( currentAnswers )), Toast.LENGTH_SHORT).show();

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

    public void saveUserAnswersEditText(int num, UserAnswers<String> o)
    {
        this.AnswersList.set(num, o);
    }

    public void saveUserAnswersCheckBox(int num, UserAnswers<SparseBooleanArray> o)
    {
        this.AnswersList.set(num, o);
    }

    public SparseBooleanArray loadUserAnswersCheckBox(int num)
    {
        SparseBooleanArray a1 = null;
        if ( this.AnswersList.get(num) != null)
        {
            a1 = ((UserAnswers<SparseBooleanArray>) this.AnswersList.get(num)).getUserAnswers();
        }
        return a1;
    }

    public String loadUserAnswersEditText(int num)
    {
        String a1 = null;
        if (this.AnswersList.get(num) != null)
        {
            a1 = ((UserAnswers<String>) this.AnswersList.get(num)).getUserAnswers();
        }
        return a1;
    }



    public void showQuestion()
    {


    	TextView questionView = (TextView) findViewById(R.id.text_question);

        DbQuestion qElement = QuestionList.get(currentQuestion);
    	
    	questionView.setText( qElement.getQuestion() );
    	
        this.updateAdapter( qElement.getType(), qElement.getAnswers() );

        if (qElement.getType().equals("CheckBox") )
        {
            ((AnswersCheckBoxAdapter) answersView.getAdapter()).updateAnswers(qElement.getAnswers());
            if (this.loadUserAnswersCheckBox(currentQuestion) != null)
            {
                ((AnswersCheckBoxAdapter) answersView.getAdapter()).setCheckedStates(this.loadUserAnswersCheckBox(currentQuestion));
            }

        }

        if (qElement.getType().equals("EditText") )
        {
            String a = "";
            if (this.loadUserAnswersEditText(currentQuestion) != null)
            {
                a = this.loadUserAnswersEditText(currentQuestion);
            }
            ((AnswersEditTextAdapter) answersView.getAdapter()).updateAnswers(a);
        }
    }


    public void updateAdapter(String questionType, List<String> questionAnswers)
    {

        if (questionType.equals("EditText") ) //&& ( answersView.getAdapter() instanceof AnswersCheckBoxAdapter) )
        {
            AnswersEditTextAdapter adapter = new AnswersEditTextAdapter(this);
            answersView.setAdapter( adapter );
            adapter.notifyDataSetChanged();
        }

        if (questionType.equals("CheckBox") ) //&& ( answersView.getAdapter() instanceof AnswersEditTextAdapter) )
        {
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
