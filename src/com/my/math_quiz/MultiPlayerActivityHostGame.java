
package com.my.math_quiz;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.math_quiz.interfaces.LevelDataIN;
import com.my.math_quiz.utils.LevelDescripction;
import com.my.math_quiz.utils.Task;
import com.my.math_quiz.views.InGameBottomButtoms;
import com.my.math_quiz.views.InGameBottomButtoms.BottomButtonListener;
import com.my.math_quiz.views.ResultBottomButtoms;
import com.my.math_quiz.views.ResultBottomButtoms.ResultBottomButtonListener;
import com.my.math_quiz.views.TitleBar;
import com.my.math_quiz.views.TitleBar.TitleBarListener;
import com.my.math_quiz_multiplayer_stuff.Client;
import com.my.math_quiz_multiplayer_stuff.TCPIPServer;
import com.my.math_quiz_multiplayer_stuff.TCPIPServer.TCPIPServerListenerInGame;

public class MultiPlayerActivityHostGame extends Activity implements TitleBarListener,TCPIPServerListenerInGame,ResultBottomButtonListener,BottomButtonListener {

	TitleBar titleBar=null;
	
	LevelDescripction levelDescripction;
	LevelDataIN levelData;
	ArrayList<Task> tasks;

	int numberOfTasksInRound;
	int selectedLevel;
	
	int currentTaskPosition;
	
	Bitmap taskIndicatorCorrectAnswer;
	Bitmap taskIndicatorWrongAnswer;
	Bitmap taskIndicatorNotSelectedAnswer;
	Bitmap taskIndicatorCurrent;
	
	
	TextView texViewForTaskText=null;
	InGameBottomButtoms bottomButtonsAnswer=null;
	LinearLayout layoutForIndicators;
	ImageView[] imageViews;
	RelativeLayout gameViewContainer;
	RelativeLayout scoreViewContainer;
	WebView scoreText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_player_wlan_game);
		
		titleBar=(TitleBar)findViewById(R.id.TBtitleBar);
		titleBar.setTitleBarListener(this);
		titleBar.setTitle(getString(R.string.multi_player_host));
		titleBar.setRightImage(BitmapFactory.decodeResource(getResources(), R.drawable.action_settings));
		

		Intent myIntent = getIntent();
		selectedLevel = myIntent.getIntExtra("EXTRA_SELECTED_LEVEL",0);
		levelDescripction= ApplicationClass.getLevelDescription(selectedLevel);
		levelData=levelDescripction.getLevelData();

		taskIndicatorCorrectAnswer=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_correct_answer);
		taskIndicatorWrongAnswer=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_wrong_answer);
		taskIndicatorNotSelectedAnswer=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_not_selected);
		taskIndicatorCurrent=BitmapFactory.decodeResource(getResources(), R.drawable.task_indicator_current);
		
		
		bottomButtonsAnswer=(InGameBottomButtoms)this.findViewById(R.id.MPODGVbootomButtons);
		texViewForTaskText=(TextView)this.findViewById(R.id.MPODGVtextView);
		layoutForIndicators=(LinearLayout)this.findViewById(R.id.MPODGlayoutForIndicator);
		gameViewContainer=(RelativeLayout)this.findViewById(R.id.MPODGgameModeStuff);
		scoreViewContainer=(RelativeLayout)this.findViewById(R.id.MPODScoreModeStuff);
		scoreText=(WebView)this.findViewById(R.id.MPODGScoretextView);
		((ResultBottomButtoms)this.findViewById(R.id.MPODGVscoreButtons)).setListener(this);
		bottomButtonsAnswer.setListener(this);
		
		TCPIPServer.requestClientsNickname();
		restartGame();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onResume() {
		super.onResume();
		TCPIPServer.setTCPIPServerListener(this);
	}

	@Override
	public void finish() {
		TCPIPServer.removeTCPIPServerListener(this);
		super.finish();
	}
	HashMap<Integer,Client> clients;
	/**
	 * score two dimension table for two player 
	 * -1 wrong
	 * 0 not answered yet
	 * 1 correct answer
	 * */
	int[] myScores;
	private int getSumScore() {
		int tmp=0;
		for(int i=0; i<myScores.length; i++){
			if(myScores[i]>0)
				tmp+=myScores[i];
		}
		return tmp;
	}
	/**
	 * @return in score i have wrong answer|not answer|correct answer|and player id
	 * it is 1dimensional table four fields 1*4
	 * */
	public int[] getScoresTable(){
		int []scorT=new int[4];
		scorT[0]=0;
		scorT[1]=0;
		scorT[2]=0;
		scorT[3]=-1;
		for(int i=0; i<myScores.length; i++){
			if(myScores[i]==-1)
				scorT[0]++;
			else if(myScores[i]==0)
				scorT[1]++;
			else if(myScores[i]==1)
				scorT[2]++;
		}
		return scorT;
	}
		
	private void restartGame(){
		currentTaskPosition=0;
		gameViewContainer.setVisibility(View.VISIBLE);
		scoreViewContainer.setVisibility(View.INVISIBLE);
		numberOfTasksInRound=ApplicationClass.getMPCurrentNumberOfGamesInOneRound();
		
		TCPIPServer.sendRequestToClearAllDataFromOldTasks();
		TCPIPServer.sendNumberOfGames(numberOfTasksInRound);
		TCPIPServer.sendRequestToDisplayGameScreen();
		
		clients=TCPIPServer.getClients();
		for(Client cl:clients.values()){
			cl.resetScore(numberOfTasksInRound);
		}
		myScores=new int [numberOfTasksInRound];
		
		levelData.clearLevelData();
		tasks=	levelData.getTests(numberOfTasksInRound);
		
		
		
		layoutForIndicators.removeAllViews();
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
		
		for(int i=0; i<numberOfTasksInRound; i++){
			TCPIPServer.sendTaskToAllClients(tasks.get(i), i);
		}
		
		displayDataFromSpecificTest(0);
	}
	
	
	/**This method display text of task and set answers to buttons and also set progress bars to right position*/
	private void displayDataFromSpecificTest(int position){
		if(position>0){
			imageViews[position-1].setImageBitmap(myScores[position-1]==-1?taskIndicatorWrongAnswer:(myScores[position-1]==0?taskIndicatorNotSelectedAnswer:taskIndicatorCorrectAnswer));
		}
		if(position<numberOfTasksInRound)
		{
			
		
			hasAnyoneAnswerCorrect=false;
			hasAnyoneAnswerToThisTaskYet=false;
			Task currentTask=tasks.get(currentTaskPosition);

			
			TCPIPServer.sendSignalToSwitchToOtherTask(position);
			
			currentTaskPosition=position;
			
			texViewForTaskText.setText(currentTask.getText());
			bottomButtonsAnswer.seButtontTexts(currentTask.getAnswers());
			bottomButtonsAnswer.resetFirstSelectedAnswer();
			
			imageViews[position].setImageBitmap(taskIndicatorCurrent);
		}
		else{
			
			gameViewContainer.setVisibility(View.INVISIBLE);
			scoreViewContainer.setVisibility(View.VISIBLE);

			int[][] scores=new int[clients.size()+1][4];
			scores[0]=getScoresTable();
			int i=1;
			for(Client cl:clients.values()){
				scores[i]=cl.getScoresTable();
				i++;
			}
			//in score i have wrong answer|not answer|correct answer|and player id
			//sorting the table
			for(i=0; i<scores.length; i++){
				for(int j=i+1; j<scores.length; j++){
					if(scores[i][2]<scores[j][2]){
						int[]tmp=scores[i];
						scores[i]=scores[j];
						scores[j]=tmp;
					}
				}
			}
			
			
			//convertin table to html text
			String text="<table border=\"1\" align=\"center\">";
			text+="<tr align=\"center\"><td>"+
				getString(R.string.muPlace)+"</td><td>"+
				getString(R.string.muPlayer)+"</td><td>"+
				getString(R.string.muWrong)+"</td><td>"+
				getString(R.string.muNotanswerd)+"</td><td>"+
				getString(R.string.muCorrect)+"</td></tr>";
			for(i=0; i<scores.length; i++){
				text+="<tr align=\"center\">";
				text+="<td>"+(i+1)+"</td>";
				if(scores[i][3]==-1){
					text+="<td>"+ApplicationClass.getNickName()+"</td>";
				}else{
					text+="<td>"+clients.get(scores[i][3]).getNickname()+"</td>";
				}
				text+="<td>"+scores[i][0]+"</td>";
				text+="<td>"+scores[i][1]+"</td>";
				text+="<td>"+scores[i][2]+"</td>";
				text+="</tr>";
				
			}
			text+="</table>";
			
			TCPIPServer.sendRequestToDisplayEndScreen(text);
			scoreText.loadData("<html><body>"+getString(R.string.muYouAchived)+getSumScore()+"</br></br>"+text+"</body></html>","text/html","utf-8");
		}
	}
	/**BEGIN the title bar listener methods*/
	@Override
	public void onLeftButtonClick() {
		this.finish();
	}

	@Override
	public void onRightButtonClick() {
		Intent intent = new Intent(MultiPlayerActivityHostGame.this, PreferenceActivity.class);
		startActivity(intent);
	}
	/**END the title bar listener methods*/

	
	/**BEGIN the TCPIPServerListenerInGame methods*/
	@Override
	public void onNumberOfClientsChanged(int number, boolean accepted) {
		if(number==0){
			Toast.makeText(this,getString(R.string.muNoMoreClients),Toast.LENGTH_LONG).show();
			this.finish();
		}
	}

	@Override
	public void onRequestForNumberOfGame() {
		TCPIPServer.sendNumberOfGames(numberOfTasksInRound);
	}

	@Override
	public void onRequestForPlayerNickname(int playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestForNumberOfPlayers() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSelectedAnswerRecived(int taskNumber, int selectedAnsver,	int clientId) {
		TCPIPServer.sendSelectdAnswerOfUserToOClients(taskNumber, clientId, selectedAnsver);
		onAnswer(clientId,selectedAnsver==tasks.get(taskNumber).getCorrectAnswer());
	}

	@Override
	public void onRequestForTaskDescription(int taskNumber) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPlayerNickname(String nickname, int playerId) {
		clients.get(playerId).setNickname(nickname);
		
	}
	/**END the TCPIPServerListenerInGame methods*/
	
	
	/**START the ResultBottomButtonListener methods*/
	@Override
	public void onAgainButtonClicked() {
		restartGame();
		
	}

	@Override
	public void onFinishButtonClicked() {
		this.finish();
		
	}

	@Override
	public void onShareButtonClicked() {
		// TODO Auto-generated method stub
		
	}
	/**END the ResultBottomButtonListener methods*/

	
	/**START the BottomButtonListener methods*/
	/**@param position the position of button on which user clicked*/
	boolean hasAnyoneAnswerCorrect;
	boolean hasAnyoneAnswerToThisTaskYet;
	@Override
	public void onButtonClick(InGameBottomButtoms buttoms, int position) {
		//if I didnt answer yet and other guy isn't answer correct
		if(currentTaskPosition<myScores.length&&myScores[currentTaskPosition]==0&&hasAnyoneAnswerCorrect==false){
			Task t=tasks.get( currentTaskPosition);
			if(buttoms.setCollors(position, t.getCorrectAnswer(),false)){
				onAnswer(-1,position==t.getCorrectAnswer());
			}
		}
	}
	/**END the BottomButtonListener methods*/
	private void onAnswer(int player,boolean wasCorrect){
//		Log.d("onbuttonclick","buttonclickes");
//		displayDataFromSpecificTest(++currentTaskPosition);
		if(player==-1){
			//this mena that is me
			myScores[currentTaskPosition]=wasCorrect?1:-1;
		}
		else{
			//this mean that one of clients answer this
			clients.get(player).setScores(currentTaskPosition,wasCorrect?1:-1);
		}
		if(wasCorrect){
			//we clicke correct solution and even if we frt clicked or second we go to next round
			beforeMovindToNextPage.removeCallbacks(runableBeforePageSwitching);
			handlerToNextPage.removeCallbacks(runablePageSwitching);
			moveDelayToPage(ApplicationClass.getMPDelayOnCorrectAnswerInMiliS());
			TCPIPServer.sendSignalToDisplayCorrectAnsswer(currentTaskPosition);
			hasAnyoneAnswerCorrect=true;
		}else if(hasAnyoneAnswerToThisTaskYet==false){
			//we clicked first and wrong
			startTimingBeforeSwitchingToNextpage();
		}
		hasAnyoneAnswerToThisTaskYet=true;
	}
	final Handler handlerToNextPage = new Handler();
	private void moveDelayToPage(int delay){
		handlerToNextPage.postDelayed(runablePageSwitching, delay);
	}
	final Runnable runablePageSwitching=new Runnable() {
		  @Override
		  public void run() {
			displayDataFromSpecificTest(++currentTaskPosition);
		  }
	};
	
	final Handler beforeMovindToNextPage=new Handler();
	private void startTimingBeforeSwitchingToNextpage(){
		beforeMovindToNextPage.postDelayed(runableBeforePageSwitching, ApplicationClass.getMPRemainTimeToAnswer());
	}
	final Runnable runableBeforePageSwitching=new Runnable() {
		  @Override
		  public void run() {
			  	handlerToNextPage.removeCallbacks(runablePageSwitching);
				moveDelayToPage(ApplicationClass.getMPDelayOnWrongAnswerInMiliS());
				TCPIPServer.sendSignalToDisplayCorrectAnsswer(currentTaskPosition);
				bottomButtonsAnswer.setCorrectCollorToSpecificButton(tasks.get(currentTaskPosition).getCorrectAnswer());
				hasAnyoneAnswerCorrect=true;
		  }
	};


	
}
