
package com.my.math_quiz;

import java.nio.charset.Charset;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.my.math_quiz.database.DAOImplementation;
import com.my.math_quiz.utils.Generator;
import com.my.math_quiz.utils.LevelDescripction;

public class ApplicationClass extends Application{

	
	public static final int NUMBER_OF_LEVELS=20;
	
	private static LevelDescripction[] levelsDescriptions=new LevelDescripction[NUMBER_OF_LEVELS];
	private static Point displaySize=null;

	public static final int NUMBER_TASK_LEVEL_MINIMUM=10;
	public static final int NUMBER_TASK_LEVEL_MEDINUM=20;
	public static final int NUMBER_TASK_LEVEL_MAXIMUM=30;
	
	
	public static final Charset charset = Charset.forName("UTF-8");
	private static final byte [] endBytes={10,11,12,13};
	public static String endCharacters=new String(endBytes);
	
	public static Context applicationContext;

	public static DAOImplementation dao;
	
	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		MyPreferences.reloadPreferences();
		dao=new DAOImplementation();
		Generator.inicilizeStringForLanguage(applicationContext);
		/**We read from DB all indicators if specific level was opened or not*/
		ArrayList<Boolean> levelsIndicators=dao.getOpendIndicatorsFromAllLevels();
		for(int i=0; i<NUMBER_OF_LEVELS; i++){
			levelsDescriptions[i]=new LevelDescripction(i,levelsIndicators.get(i));
		}
		
	}
	
	public static int getMaximumNumberOfGamesInOneRound(){
		return NUMBER_TASK_LEVEL_MAXIMUM;
	}

	public static LevelDescripction[] getLevelDescriptions() {
		return levelsDescriptions;
	}
	public static int getSPCurrentNumberOfGamesInOneRound(){
		return MyPreferences.spNumberOfGames;
	}
	public static int getSPDelayOnCorrectAnswerInMiliS(){
		return MyPreferences.spDellayCorrect;
	}
	public static int getSPDelayOnWrongAnswerInMiliS(){
		return MyPreferences.spDellayWrong;
	}
	
	public static int getMPCurrentNumberOfGamesInOneRound(){
		return MyPreferences.mpNumberOfGames;
	}
	public static int getMPDelayOnCorrectAnswerInMiliS(){
		return MyPreferences.mpDellayCorrect;
	}
	public static int getMPDelayOnWrongAnswerInMiliS(){
		return MyPreferences.mpDellayWrong;
	}
	public static int getMPRemainTimeToAnswer(){
		return MyPreferences.mpRemainTimeToAnswer;
	}
	
	public static String getNickName(){
		return MyPreferences.mpNicKName;
	}
	public static String getLastIPAdress(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		return sharedPrefs.getString("pref_key_ip_adress","192.168.1.1");
	}
	public static int getLastPortNumber(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		return sharedPrefs.getInt("pref_key_port_number",1111);
	}
	public static void saveIPAdress(String ipAdress){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		Editor edit=sharedPrefs.edit();
		edit.putString("pref_key_ip_adress", ipAdress);
		edit.apply();
	}
	public static void savePortNumber(int portNumber){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		Editor edit=sharedPrefs.edit();
		edit.putInt("pref_key_port_number", portNumber);
		edit.apply();	
	}
	public static Point getDisplaySize(){
		if(displaySize==null){
			DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
			displaySize=new Point(metrics.widthPixels,metrics.heightPixels);
		}
		return displaySize;
	}
	public static LevelDescripction getLevelDescription(int level) {
		return levelsDescriptions[level];
	}

	public static void settingsChanged() {
		MyPreferences.reloadPreferences();
	}
	
	
	static class MyPreferences{
		public static int spDellayCorrect;
		public static int spDellayWrong;
		public static int spNumberOfGames;
		
		public static int mpDellayCorrect;
		public static int mpDellayWrong;
		public static int mpNumberOfGames;
		public static int mpRemainTimeToAnswer;
		public static String mpNicKName="";
		
		public static void reloadPreferences(){
			if(applicationContext!=null){
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
				spDellayCorrect=Integer.parseInt(sharedPrefs.getString("pref_key_delay_correct","0"));
				spDellayWrong=Integer.parseInt(sharedPrefs.getString("pref_key_delay_wrong","500"));
				spNumberOfGames=Integer.parseInt(sharedPrefs.getString("pref_key_number_of_test","10"));
				
				mpDellayCorrect=Integer.parseInt(sharedPrefs.getString("pref_key_mp_delay_correct","0"));
				mpDellayWrong=Integer.parseInt(sharedPrefs.getString("pref_key_mp_delay_wrong","500"));
				mpNumberOfGames=Integer.parseInt(sharedPrefs.getString("pref_key_mp_number_of_test","10"));
				mpRemainTimeToAnswer=Integer.parseInt(sharedPrefs.getString("pref_key_mp_time_remain_to_answer","2000"));
				mpNicKName=sharedPrefs.getString("pref_key_mp_nickname",applicationContext.getString(R.string.seNicknameDefault));
			}
		}
		
		
	}
	public static float convertDpToPixel(float dp){
	    Resources resources = applicationContext.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	public static float convertPixelsToDp(float px){
	    Resources resources = applicationContext.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	/**
	 * This method convert the IP address from INT to STRING with DOTS
	 * @param  ip is IP number in integer
	 * @return ip number in string in readable format
	 * */
	public static  String intToIp(int ip) {
		   return  (ip & 0xFF) + "." +
				   ((ip >> 8 ) & 0xFF) + "." +
				   ((ip >> 16 ) & 0xFF) + "." +
				   ((ip >> 24 ) & 0xFF )  ;
		}
	private static final int animationDuration=1000;
	/**
	 * Method create new instance of fade in animation
	 * @param listener is the animation listener which will be set to the animation, if you don't want to set listener just place null to this place
	 * @return new instance of fade in animation
	 * */
	public static Animation getFadeInAnimation(AnimationListener listener){
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setDuration(animationDuration);
		fadeIn.setFillEnabled(true);
		fadeIn.setFillAfter(true);
		if(listener!=null)
			fadeIn.setAnimationListener(listener);
		return fadeIn;
	}
	
	/**
	 * Method create new instance of fade OUT animation
	 * @param listener is the animation listener which will be set to the animation, if you don't want to set listener just place null to this place
	 * @return new instance of fade in animation
	 * */
	public static Animation getFadeOutAnimation(AnimationListener listener){
		Animation fadeIn = new AlphaAnimation(1, 0);
		fadeIn.setDuration(animationDuration);
		fadeIn.setFillEnabled(true);
		fadeIn.setFillAfter(true);
		if(listener!=null)
			fadeIn.setAnimationListener(listener);
		return fadeIn;
	}
	/**
	 * Method create new instance of fade in animation which is devided in two parts (which part first or last we wan we chose with @param firstHalf)
	 * @param firstHalf With this param we chose which part of animation we want, if we set to true we will get first part else it will return the last part
	 * @param listener is the animation listener which will be set to the animation, if you don't want to set listener just place null to this place
	 * 
	 * @return new instance of fade in animation
	 * */
	public static Animation getHalfFadeInAnimation(boolean firstHalf,AnimationListener listener){
		
		Animation fadeIn;
		if(firstHalf){
			fadeIn= new AlphaAnimation(0, 0.7f);
			fadeIn.setDuration((int)(animationDuration*0.7));
		}
		else{
			fadeIn= new AlphaAnimation(0.7f, 1);
			fadeIn.setDuration((int)(animationDuration*0.3));
		}
		fadeIn.setFillEnabled(true);
		fadeIn.setFillAfter(true);
		if(listener!=null)
			fadeIn.setAnimationListener(listener);
		return fadeIn;
		
	}
	
	/**
	 * Method create new instance of fade out animation which is devided in two parts (which part first or last we wan we chose with @param firstHalf)
	 * @param firstHalf With this param we chose which part of animation we want, if we set to true we will get first part else it will return the last part
	 * @param listener is the animation listener which will be set to the animation, if you don't want to set listener just place null to this place
	 * 
	 * @return new instance of fade in animation
	 * */
	public static Animation getHalfFadeOutAnimation(boolean firstHalf,AnimationListener listener){
		
		Animation fadeOut;
		if(firstHalf){
			fadeOut= new AlphaAnimation(1, 0.7f);
			fadeOut.setDuration((int)(animationDuration*0.3));
		}
		else{
			fadeOut= new AlphaAnimation(0.7f, 0);
			fadeOut.setDuration((int)(animationDuration*0.7));
		}
		fadeOut.setFillEnabled(true);
		fadeOut.setFillAfter(true);
		if(listener!=null)
			fadeOut.setAnimationListener(listener);
		return fadeOut;
		
	}
}
