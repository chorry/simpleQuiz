package com.example.simplequiz;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class AnswersEditTextAdapter extends BaseAdapter implements TextWatcher {

    private String[] answers;
    private String   enteredText;
    private ViewHolder thisHolder;
    LayoutInflater inflater;
    
    // the context is needed to inflate views in getView()
    public AnswersEditTextAdapter(
    		Context context
    		)
    {
    	Log.e("debug","EditText constructed");
        this.inflater = LayoutInflater.from(context);
    }

    public void updateAnswers() {
        this.enteredText = "";
        notifyDataSetChanged();
    }

    public String getEnteredText()
    {
        return this.enteredText;
        //        "One,two, three , four \\, five \\,\\six";
//        EditText et = (EditText) findViewById(R.id.answerEditText);
//        return et.getText().toString() ;
    }


    @Override
    public int getCount() {
        return 1;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder thisHolder;
		if (convertView == null) 
		{
			thisHolder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.activity_answers_textedit, parent, false);
			thisHolder.answerEditText = (EditText) convertView.findViewById(R.id.answerEditText);
            thisHolder.answerEditText.setText("");
            thisHolder.answerEditText.addTextChangedListener( this );
			convertView.setTag(thisHolder);
		}
		else
		{
			thisHolder = (ViewHolder) convertView.getTag();
		}
    	String answer =  "";
        this.enteredText = thisHolder.answerEditText.getEditableText().toString();
        return convertView;
    }

    @Override
    public void afterTextChanged(Editable e) {
        enteredText=e.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
	public String getItem(int position) {
		return "replaceMe!";
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder
    {
		EditText answerEditText;
	}
}
