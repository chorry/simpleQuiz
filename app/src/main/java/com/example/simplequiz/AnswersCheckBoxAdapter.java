package com.example.simplequiz;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

public class AnswersCheckBoxAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private List<String> answers;
    public SparseBooleanArray mCheckStates;
    LayoutInflater inflater;
    
    // the context is needed to inflate views in getView()
    public AnswersCheckBoxAdapter(
    		Context context,
            List<String> answers
    		)
    {
        Log.e("debug","CheckBox constructed");
        this.inflater = LayoutInflater.from(context);
        this.updateAnswers(answers);
    }
   
    @Override
    public boolean hasStableIds()
    {
    	return true;    	
    }
    
    public void updateAnswers(List<String> answers) {
        this.answers = answers;
        if (answers != null)
        {
            if (mCheckStates == null)
            {
                mCheckStates = new SparseBooleanArray(answers.size());
            }
        	mCheckStates.clear();// V/example
            Log.v("example adapter","New mCheckStates=" + Integer.toString(mCheckStates.size()));
        }
        notifyDataSetChanged();
    }

    public SparseBooleanArray getCheckedStates() {
        return mCheckStates;
    }

    @Override
    public int getCount() {
    	if ( this.answers == null)
    	{
    		return 0;
    	}
    	
        return answers.size();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.activity_answers_checkbox, parent, false);
			holder.answerCheckBox = (CheckBox) convertView.findViewById(R.id.answerCheckBox);
			holder.answerCheckBox.setChecked(false);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
    	String answer =  answers.get(position);
    	
        //((CheckBox) convertView.findViewById(R.id.answerCheckBox)).setText( answer );
        Log.v("example adapter","answer stuff for pos"+Integer.toString(position));
    	holder.answerCheckBox.setText(answer);
        holder.answerCheckBox.setTag(position);
        holder.answerCheckBox.setChecked( mCheckStates.get(position, false) );
        this.setChecked(position, holder.answerCheckBox.isChecked()); //lame workaroun
        holder.answerCheckBox.setOnCheckedChangeListener(this);
        Log.v("example adapter","answer stuff for pos is over");

        return convertView;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
    	Log.v("example adapter","setChecked mCheckId" + Integer.toString(position));
        mCheckStates.put(position, isChecked);
    }

    public void toggle(int position) {
        Log.v("example adapter","toggle");
        this.setChecked(position, !isChecked(position));
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
    	Log.v("example adapter", Integer.toString( (Integer) buttonView.getTag() ) + " is " + Boolean.toString(isChecked));    	
         mCheckStates.put((Integer) buttonView.getTag(), isChecked);    
    }
    
	@Override
	public String getItem(int position) {
		return this.answers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}	

	private class ViewHolder {
		CheckBox answerCheckBox;
	}
}
