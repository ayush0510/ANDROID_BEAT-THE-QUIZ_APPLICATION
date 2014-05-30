
package com.my.math_quiz;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.math_quiz.database.LevelEntity;
import com.my.math_quiz.interfaces.LevelDataIN;
import com.my.math_quiz.utils.LevelDescripction;
import com.my.math_quiz.utils.Task;
import com.my.math_quiz.views.InGameBottomButtoms;
import com.my.math_quiz.views.InGameBottomButtoms.BottomButtonListener;
import com.my.math_quiz.views.ResultBottomButtoms;
import com.my.math_quiz.views.ResultBottomButtoms.ResultBottomButtonListener;
import com.my.math_quiz.views.TitleBar;
import com.my.math_quiz.views.TitleBar.TitleBarListener;
import com.my.math_quiz.views.ViewPagerWithCustomSpeedOfSwitchingPages;

public class SingelPlayerGameActivity extends Activity implements BottomButtonListener,TitleBarListener,ResultBottomButtonListener{

	ViewPagerWithCustomSpeedOfSwitchingPages pager;
	LevelDescripction levelDescripction;
	LevelDataIN levelData;
	ArrayList<Task> tasks;
	LayoutInflater inflater;
	
	/**This are indicators images on which task are now which task we answer yet wrong or correct*/
	ImageView[] imageViews;
	int numberOfTasksInRound;
	int selectedLevel;
	TitleBar titleBar=null;
	
	Bitmap taskIndicatorCorrectAnswer;
	Bitmap taskIndicatorWrongAnswer;
	Bitmap taskIndicatorNotSelectedAnswer;
	Bitmap taskIndicatorCurrent;
	

	LevelEntity levelEntiyFromThesGame=null;
	MyAdapterForSingelPLayerGameActivity adapterForViewPager;
	LinearLayout layoutForIndicators;
	/**additionalPage mean number of pages after last test, that we have for displaying scores if user answer to all tasks in one group of them*/
	int additionalPage=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_player_game);
		
		Intent myIntent = getIntent();
		selectedLevel = myIntent.getIntExtra("EXTRA_SELECTED_LEVEL",0);
		numberOfTasksInRound=ApplicationClass.getSPCurrentNumberOfGamesInOneRound();
		
		
		levelDescripction= ApplicationClass.getLevelDescription(selectedLevel);
		levelDescripction.setWasAlreadyOpendOnTrue();
		levelData=levelDescripction.getLevelData();
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		titleBar=(TitleBar)findViewById(R.id.TBtitleBar);
		titleBar.setTitleBarListener(this);
		titleBar.setRightImage(BitmapFactory.decodeResource(getResources(), R.drawable.action_settings));
		titleBar.setTitle(1+"/"+numberOfTasksInRound);
		
		pager=(ViewPagerWithCustomSpeedOfSwitchingPages)findViewById(R.id.ASPGViewPager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Task t;
				if(position<numberOfTasksInRound){
					titleBar.setTitle((position+1)+"/"+numberOfTasksInRound);
					imageViews[position].setImageBitmap(taskIndicatorCurrent);
					t=tasks.get(position);
					if(t.getSelectedAnswer()==-1)
						levelData.resumTimingLevel();
				}else{
					//last page
					titleBar.setTitle(getString(R.string.scores));
				}
				if(position>0){
					t=tasks.get(position-1);
					if(t.getSelectedAnswer()==-1)
						imageViews[position-1].setImageBitmap(taskIndicatorNotSelectedAnswer);
					else if(t.getSelectedAnswer()==t.getCorrectAnswer())
						imageViews[position-1].setImageBitmap(taskIndicatorCorrectAnswer);
					else
						imageViews[position-1].setImageBitmap(taskIndicatorWrongAnswer);
				}
				if(position<numberOfTasksInRound-1){
					t=tasks.get(position+1);
					if(t.getSelectedAnswer()==-1)
						imageViews[position+1].setImageBitmap(taskIndicatorNotSelectedAnswer);
					else if(t.getSelectedAnswer()==t.getCorrectAnswer())
						imageViews[position+1].setImageBitmap(taskIndicatorCorrectAnswer);
					else
						imageViews[position+1].setImageBitmap(taskIndicatorWrongAnswer);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				handler.removeCallbacks(runablePageSwitching);
//				Log.d("setA","scrol state "+state);
//				if(state>1){
//					for(int i=0; i<	pager.getChildCount(); i++){
//						View v=pager.getChildAt(i);
//						v.setEnabled(false);
//					}
//				}
			}
		});
	
		
		
		taskIndicatorCorrectAnswer=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_correct_answer);
		taskIndicatorWrongAnswer=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_wrong_answer);
		taskIndicatorNotSelectedAnswer=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_not_selected);
		taskIndicatorCurrent=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_current);
		
		layoutForIndicators=(LinearLayout)findViewById(R.id.ASPGRlayoutBelowTitleBar);
		
		restartLevel();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
	}
	private void restartLevel(){
		additionalPage=0;
		numberOfTasksInRound=ApplicationClass.getSPCurrentNumberOfGamesInOneRound();
		layoutForIndicators.removeAllViews();
		
		//TODO move this out of constructor to restart method because if i change the number of 
				//test in round i must also change the number and the sizes of indicators else I rrecive exception
		imageViews=new ImageView[numberOfTasksInRound];
		int oneIndicatorWidth=ApplicationClass.getDisplaySize().x/numberOfTasksInRound;
		int oneIndicatorHeight=ApplicationClass.getDisplaySize().x/ApplicationClass.getMaximumNumberOfGamesInOneRound();
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(oneIndicatorWidth,oneIndicatorHeight);
		
		for(int i=0; i<imageViews.length; i++){
			imageViews[i]=new ImageView(this);
			imageViews[i].setLayoutParams(layoutParams);
			imageViews[i].setImageBitmap(taskIndicatorNotSelectedAnswer);
			imageViews[i].setScaleType(ScaleType.CENTER_INSIDE);
			layoutForIndicators.addView(imageViews[i]);
		}
		imageViews[0].setImageBitmap(taskIndicatorCurrent);
				
		
	
		titleBar.setTitle(1+"/"+numberOfTasksInRound);
		levelData.clearLevelData();
		tasks=	levelData.getTests(numberOfTasksInRound);
		adapterForViewPager=new MyAdapterForSingelPLayerGameActivity(numberOfTasksInRound);
		pager.setAdapter(adapterForViewPager);
		levelData.startTimingLevel();
	}
	
	
	/**BEGIN the title bar listener methods*/
	@Override
	public void onLeftButtonClick() {
		// we finish this activity
		SingelPlayerGameActivity.this.finish();
		
	}
		
	@Override
	public void onRightButtonClick() {
		Intent intent = new Intent(SingelPlayerGameActivity.this, PreferenceActivity.class);
		startActivity(intent);
	}
	/**END the title bar listener methods*/

	/**BEGIN the ResultBotomButton listener methods*/
	@Override
	public void onAgainButtonClicked() {
		restartLevel();
	}

	@Override
	public void onFinishButtonClicked() {
		this.finish();
	}

	@Override
	public void onShareButtonClicked() {
		// TODO Auto-generated method stub
		
	}
	/**END the ResultBotomButton listener methods*/

	@Override
	protected void onDestroy() {
		levelData.stopTimingLevel();
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		levelData.pauseTimingLevel();
		super.onPause();
	}
	@Override
	protected void onPostResume() {
		levelData.resumTimingLevel();
		super.onPostResume();
	}
	@Override
	protected void onResume() {
		levelData.resumTimingLevel();
		super.onResume();
	}

	class MyAdapterForSingelPLayerGameActivity extends PagerAdapter{
		int numbrOfTests;
		View resultPage=null;
		public MyAdapterForSingelPLayerGameActivity(int numberOfTests){
			this.numbrOfTests=numberOfTests;
		}
		@Override
		public int getCount() {
			return numbrOfTests+additionalPage;
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return view==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
//			return super.instantiateItem(container, position);
			if(position<numberOfTasksInRound){
				View v = inflater.inflate(R.layout.view_single_player_game_one_page, null);
				TextView text=(TextView)v.findViewById(R.id.VSPGOPtextView);
				Task currentTask=tasks.get(position);
				
				text.setText(currentTask.getText());
				InGameBottomButtoms buttoms;
				buttoms=(InGameBottomButtoms)v.findViewById(R.id.BBBottomBUttons);
				buttoms.seButtontTexts(currentTask.getAnswers());
				buttoms.setListener(SingelPlayerGameActivity.this);
				buttoms.setCollors(currentTask.getSelectedAnswer(), currentTask.getCorrectAnswer(),true);
				buttoms.setPositionInTasks(position);
				pager.addView(v);
				return v;
			}
			else{
				if(resultPage==null){
					resultPage=inflater.inflate(R.layout.view_single_player_game_end_page, null);
				
					((TextView)resultPage.findViewById(R.id.LPscore2)).setText(levelEntiyFromThesGame.getScore()+"");
					((TextView)resultPage.findViewById(R.id.LPtime2)).setText(levelEntiyFromThesGame.getTimeInMIliseconds()+"");
					((TextView)resultPage.findViewById(R.id.LPtasks2)).setText(numberOfTasksInRound+"");
//					Log.d("score:","score:"+levelEntiyFromThesGame.getScore()+"/"+levelEntiyFromThesGame.getTimeInMIliseconds());
					
					((TextView)resultPage.findViewById(R.id.ESPtimeL)).setText(levelDescripction.levelTimes[0]+"");
					((TextView)resultPage.findViewById(R.id.ESPtimeM)).setText(levelDescripction.levelTimes[1]+"");
					((TextView)resultPage.findViewById(R.id.ESPtimeH)).setText(levelDescripction.levelTimes[2]+"");
					((TextView)resultPage.findViewById(R.id.ESPscoreL)).setText(levelDescripction.levelScores[0]+"");
					((TextView)resultPage.findViewById(R.id.ESPscoreM)).setText(levelDescripction.levelScores[1]+"");
					((TextView)resultPage.findViewById(R.id.ESPscoreH)).setText(levelDescripction.levelScores[2]+"");
					
//					((ResultBottomButtoms)resultPage.findViewById(R.id.BBResultBottomButtons)).enableShaeButton();
					((ResultBottomButtoms)resultPage.findViewById(R.id.BBResultBottomButtons)).setListener(SingelPlayerGameActivity.this);
					
				
				}
				pager.addView(resultPage);
				return resultPage;
			}
		}
		 @Override
		 public void destroyItem(View collection, int position, Object view) {
		           ((ViewPager) collection).removeView((View) view);
		 }
	}
	

	/**This is single method from bottButton listener*/
	/**@param position the position of button on which user clicked*/
	@Override
	public void onButtonClick(InGameBottomButtoms buttoms,int position) {
		Task t=tasks.get( buttoms.getPositionInTasks());
		levelData.pauseTimingLevel();
//		Log.d("setA",position+"/"+pager.getCurrentItem());
		if(t.setSelectedAnswer(position)){
//			Log.d("setB",position+"");
			buttoms.setCollors(t.getSelectedAnswer(), t.getCorrectAnswer(),true);
			if(additionalPage==0&&levelData.getNumberOfUnsolvedTests()==0){
				//now we finish all tasks in that level so we can calculate results
				levelData.stopTimingLevel();
				levelEntiyFromThesGame=new LevelEntity(selectedLevel,numberOfTasksInRound, (int)levelData.getDurationOfLevel(),levelData.getScoreAchived());
				levelEntiyFromThesGame.saveToDB();
				additionalPage=1;
				adapterForViewPager.notifyDataSetChanged();
				pager.setCurrentItem(numberOfTasksInRound,true);
				
			}else {
				int delay=0;
				if(t.getSelectedAnswer()==t.getCorrectAnswer()){
					//we answer correct to answer an we move forward to next task  or back to previous if isn't solved already
					delay=ApplicationClass.getSPDelayOnCorrectAnswerInMiliS();
				}else{
					//we answer wrong and it is other delay sett
					delay=ApplicationClass.getSPDelayOnWrongAnswerInMiliS();
				}
				moveDelayToPage(delay);
			}
		}
		//timer.
	}
	final Handler handler = new Handler();
	private void moveDelayToPage(int delay){
		handler.postDelayed(runablePageSwitching, delay);
	}
	final Runnable runablePageSwitching=new Runnable() {
		  @Override
		  public void run() {
			pager.setSlowSpeed();
			pager.setCurrentItem(levelData.getNextNotSolvedTestPosition(pager.getCurrentItem()),true);
		  }
	};
//	CountDownTimer timer=new CountDownTimer(5000,5000) {
//		@Override
//		public void onTick(long millisUntilFinished) {}
//		
//		@Override
//		public void onFinish() {
//			
//			
//		}
//	};
	@Override
	public void finish() {
		levelData.clearLevelData();
		//user didn0t finished game so we don0t need to update datas
		if(additionalPage==0){
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);        
		}else{
			Intent returnIntent = new Intent();
			returnIntent.putExtra(LevelsDisplayedActivity.KEY_FOR_SINGLE_PLAYER_RESULT,selectedLevel);
			setResult(RESULT_OK,returnIntent);  
		}
		super.finish();
	}

	

}
