
package com.my.math_quiz.database;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

import com.my.math_quiz.ApplicationClass;

public class LevelEntity {


	private long id=-1;
	
	private int level;
	private int numberOfGames;
	private int timeInMIliseconds;
	private int score;
	
	private Date date;
	
	public LevelEntity(int id,int level,int numberOfGames,int timeInMiliseconds, int score,Date date){
		this.id=id;
		setData(level, numberOfGames, timeInMiliseconds, score,date);
	}
	
//	public LevelEntity(int level,int numberOfGames,int timeInMiliseconds, int score,Date date){
//		id=-1;
//		setData(level, numberOfGames, timeInMiliseconds, score,date);
//	}
	public LevelEntity(int level,int numberOfGames,int timeInMiliseconds, int score){
		id=-1;
		setData(level, numberOfGames, timeInMiliseconds, score,Calendar.getInstance().getTime());
	}
	
	public void setData(int level,int numberOfGames,int timeInMiliseconds, int score,Date date){
//		if(date==null)
//			date=Calendar.getInstance().getTime();
//		else
//			this.date=date;
		this.date=date;
		this.level=level;
		this.numberOfGames=numberOfGames;
		this.timeInMIliseconds=timeInMiliseconds;
		this.score=score;
	}

	public long getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getNumberOfGames() {
		return numberOfGames;
	}

	public int getTimeInMIliseconds() {
		return timeInMIliseconds;
	}

	public int getScore() {
		return score;
	}

	public Date getDate() {
		return date;
	}
	
	/**This method save current entity to DB if it hasn't goot ID already*/
	public boolean saveToDB(){
		if(id!=-1){
			//TOODO maaybe change it to update
			return false;
		}
		else{
			this.id=ApplicationClass.dao.insertLevelEntityToDatabase(this);
			return true;
		}
	}
	

}
