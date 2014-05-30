/*

    Copyright 2014 Jože Kulovic

    This file is part of Math-quiz.

    Math-quiz is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Math-quiz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Math-quiz.  If not, see http://www.gnu.org/licenses

*/
package com.my.math_quiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.my.math_quiz.R;

public class ResultBottomButtoms extends LinearLayout{
	public interface ResultBottomButtonListener{
		public void onAgainButtonClicked();
		public void onFinishButtonClicked();
		public void onShareButtonClicked();
	}
	
	
	
	Button againButton=null;
	Button shareButton=null;
	Button endButton=null;
	
	ResultBottomButtonListener listener=null;
	

	public ResultBottomButtoms(Context context) {
		super(context);


	}

	public ResultBottomButtoms(Context context, AttributeSet attrs) {
		super(context, attrs);
	

	}

	public ResultBottomButtoms(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}


	private void initViews(){
		if(againButton==null){
			againButton=(Button)findViewById(R.id.LPbuttonAgain);
			shareButton=(Button)findViewById(R.id.LPbuttonShare);
			endButton=(Button)findViewById(R.id.LPbuttonFinish);

			if(mustDisableAgain){
				againButton.setVisibility(View.INVISIBLE);
			}
			else{
				againButton.setOnClickListener(onClickListener);
			}
			
//			if(mustEnableShareButton){
//				shareButton.setVisibility(View.VISIBLE);
//				shareButton.setOnClickListener(onClickListener);
//			}else{
//				shareButton.setVisibility(View.GONE);
//			}
			
			endButton.setOnClickListener(onClickListener);


		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		initViews();
		
	}
	public void setListener(ResultBottomButtonListener listener){
		this.listener=listener;
	}
	
	OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(listener!=null){
				if(v.getId()==R.id.LPbuttonAgain){
					listener.onAgainButtonClicked();
				}else if(v.getId()==R.id.LPbuttonShare){
					listener.onShareButtonClicked();
				}else if(v.getId()==R.id.LPbuttonFinish){
					listener.onFinishButtonClicked();
				}
			}
			
		}
	};
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	private boolean mustDisableAgain=false;
	public void disableAgainButton() {
		mustDisableAgain=true;
		if(againButton!=null)
			againButton.setVisibility(View.INVISIBLE);
		
	}
//	private boolean mustEnableShareButton=false;
//	public void enableShaeButton(){
//		Log.d("enableShare","enable share button");
//		mustEnableShareButton=true;
//		if(shareButton!=null)
//			shareButton.setVisibility(View.VISIBLE);
//	}

}
