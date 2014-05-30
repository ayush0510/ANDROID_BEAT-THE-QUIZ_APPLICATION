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
package com.my.math_quiz.utils;

import java.util.ArrayList;

import android.util.Log;

import com.my.math_quiz.ApplicationClass;
import com.my.math_quiz.interfaces.LevelDataIN;

public class LevelDescripction {

	public String levelName;
	private int level;
	public String levelDescription;
	/**Here I have times and best scores for each number of test in round seperate like for 20 for 30 for 50 test in round*/
	public int[] levelTimes=new int[3];
	public int[] levelScores=new int[3];
	/**This I have to know if I should show tutorial or not*/
	private boolean wasAlreadyOpend;
	
	public LevelDescripction(int level,boolean wasAlreadyOpend){
		this.wasAlreadyOpend=wasAlreadyOpend;
		this.level=level;
		switch(level){
			case 0: levelName=Generator.get0levelName();levelDescription=Generator.get0levelDescription();break;
			case 1: levelName=Generator.get1levelName();levelDescription=Generator.get1levelDescription();break;
			case 2: levelName=Generator.get2levelName();levelDescription=Generator.get2levelDescription();break;
			case 3: levelName=Generator.get3levelName();levelDescription=Generator.get3levelDescription();break;
			case 4: levelName=Generator.get4levelName();levelDescription=Generator.get4levelDescription();break;
			case 5: levelName=Generator.get5levelName();levelDescription=Generator.get5levelDescription();break;
			case 6: levelName=Generator.get6levelName();levelDescription=Generator.get6levelDescription();break;
			case 7: levelName=Generator.get7levelName();levelDescription=Generator.get7levelDescription();break;
			case 8: levelName=Generator.get8levelName();levelDescription=Generator.get8levelDescription();break;
			case 9: levelName=Generator.get9levelName();levelDescription=Generator.get9levelDescription();break;
			case 10: levelName=Generator.get10levelName();levelDescription=Generator.get10levelDescription();break;
			case 11: levelName=Generator.get11levelName();levelDescription=Generator.get11levelDescription();break;
			case 12: levelName=Generator.get12levelName();levelDescription=Generator.get12levelDescription();break;
			case 13: levelName=Generator.get13levelName();levelDescription=Generator.get13levelDescription();break;
			case 14: levelName=Generator.get14levelName();levelDescription=Generator.get14levelDescription();break;
			case 15: levelName=Generator.get15levelName();levelDescription=Generator.get15levelDescription();break;
			case 16: levelName=Generator.get16levelName();levelDescription=Generator.get16levelDescription();break;
			case 17: levelName=Generator.get17levelName();levelDescription=Generator.get17levelDescription();break;
			case 18: levelName=Generator.get18levelName();levelDescription=Generator.get18levelDescription();break;
			case 19: levelName=Generator.get19levelName();levelDescription=Generator.get19levelDescription();break;
		default:break;
		}
	}
	
	public int getLevel(){
		return level;
	}
	
	/**@return true if user already opened this level else false (we have this to show tutorial at first time)*/
	public boolean wasAlreadyOpend(){
		return wasAlreadyOpend;
	}
	/**This method set varible wasAlreadyOpend to true and also save changes to DB*/
	public void setWasAlreadyOpendOnTrue(){
		if(wasAlreadyOpend==false){
			wasAlreadyOpend=true;
			ApplicationClass.dao.markLevelAsOpend(level);
		}
	}
	
	/**This method create new instance of method for level data so task will be new and everything else
	 * @return new instance of levelDataInterface*/
	public LevelDataIN getLevelData() {
		return new LevelData(level);
	}
	
	
	
	
	private class LevelData implements LevelDataIN{
		private long startTime;
		private long endTime;
		private long length;
		
		private boolean isTiming=false;
		private boolean isStopTiming=true;
		private int level;
		
		ArrayList<Task> tasks;
		
		public LevelData(int level){
			this.level=level;
		}
		/**@return the arrayList of test for this specific level, if we call this method first time, 
		 * the levels will be automatically generated for this specific level*/
		@Override
		public ArrayList<Task> getTests(int number){
			if(tasks==null||tasks.size()!=number){
				tasks=generateTask(level, number);
			}
			return tasks;
		}
		/**This method set tasks to null and at next time calling getTask the task will be generated at new,
		 * on calling this method is also reseted timer, so you should call method @startTimingLevel when you want start timing*/
		@Override
		public void clearLevelData(){
			tasks=null;
			startTime=0;
			endTime=0;
			length=0;
			isTiming=false;
			isStopTiming=true;
		}
		
		@Override
		public  int getNumberOfUnsolvedTests(){
			int numberOfUnsolved=0;
			if(tasks==null){
				Log.d("exceptionMY","we request the numnber of unsoved test bu the tasks are set to null");
				return 1;
			}
			for(Task task:tasks){
				if(task.getSelectedAnswer()==-1){
					numberOfUnsolved++;
				}
			}
			return numberOfUnsolved;
		}
		
		/**this method return next not solved test position 
		 * @param currentPosition position from where it method will look for not solved test
		 * @return the position of next not solved test, from currentPosition, if is no more test for solve 
		 * from currentPosition it return test before currentPostion and if event ther is no more test it retun -1 */
		@Override
		public int getNextNotSolvedTestPosition(int currentPosition){
			if(tasks==null)
				return -1;
			
			for(int i=currentPosition+1; i<tasks.size(); i++){
				if(tasks.get(i).getSelectedAnswer()==-1){
					return i;
				}
			}
			
			for(int i=currentPosition-1; i>=0; i--){
				if(tasks.get(i).getSelectedAnswer()==-1){
					return i;
				}
			}
			return -1;
		}
		
		@Override
		public int getScoreAchived() {
			int score=0;
			if(tasks==null){
				Log.d("exceptionMY","we request the numnber of unsoved test bu the tasks are set to null");
				return -1;
			}
			for(Task task:tasks){
				if(task.getSelectedAnswer()==task.getCorrectAnswer()){
					score++;
				}
			}
			return score;
		}
		/**This method start timing time from zero. The start timer will only star 
		 * if clocked is stopped previous or. isen't start already. 
		 * The time will be set to zero*/
		@Override
		public void startTimingLevel(){
			if(isStopTiming==true){
				length=0;
				startTime=System.currentTimeMillis();
				isTiming=true;
				isStopTiming=false;
			}
		}
		/**This method stop clock, and save current length for continue timing,
		 *  clock must already run else is no effect from this method (start should be called before)*/
		@Override
		public void pauseTimingLevel(){
			if(isTiming&&isStopTiming==false){
				endTime=System.currentTimeMillis();
				length+=endTime-startTime;
				isTiming=false;
			}
		}
		
		/**This method  continue timing form last saved time,
		 *  clock must already run else is no effect from this method (start should be called before)*/
		@Override
		public void resumTimingLevel(){
			if(isTiming==false&&isStopTiming==false){
				startTime=System.currentTimeMillis();
				isTiming=true;
			}
		}
		
		/**This method if timer is running we now stop timer and we can continue him only with method start
		 * clock must already run else is no effect from this method (start should be called before)*/
		@Override
		public void stopTimingLevel(){
			if(isStopTiming==false){
				endTime=System.currentTimeMillis();
				length+=endTime-startTime;
				isTiming=false;
				isStopTiming=true;
			}
		}
		/**@return the time (in milliseconds) spent for solving all test in this level 
		 * if level is not previous stopped method will return -1 */
		@Override
		public long getDurationOfLevel(){
			if(isStopTiming==true){
				return length;
			}else{
				return -1;
			}
			
		}

		private ArrayList<Task> generateTask(int level,int numberOfTasks){
			ArrayList<Task> tasks=new ArrayList<Task>();
			for(int i=0; i<numberOfTasks; i++){
				switch(level){
				case 0: tasks.add(Generator.get0levelTask());	break;
				case 1: tasks.add(Generator.get1levelTask());	break;
				case 2: tasks.add(Generator.get2levelTask());	break;
				case 3: tasks.add(Generator.get3levelTask());	break;
				case 4: tasks.add(Generator.get4levelTask());	break;
				case 5: tasks.add(Generator.get5levelTask());	break;
				case 6: tasks.add(Generator.get6levelTask());	break;
				case 7: tasks.add(Generator.get7levelTask());	break;
				case 8: tasks.add(Generator.get8levelTask());	break;
				case 9: tasks.add(Generator.get9levelTask());	break;
				case 10: tasks.add(Generator.get10levelTask());	break;
				case 11: tasks.add(Generator.get11levelTask());	break;
				case 12: tasks.add(Generator.get12levelTask());	break;
				case 13: tasks.add(Generator.get13levelTask());	break;
				case 14: tasks.add(Generator.get14levelTask());	break;
				case 15: tasks.add(Generator.get15levelTask());	break;
				case 16: tasks.add(Generator.get16levelTask());	break;
				case 17: tasks.add(Generator.get17levelTask());	break;
				case 18: tasks.add(Generator.get18levelTask());	break;
				case 19: tasks.add(Generator.get19levelTask());	break;
				default:break;
				}
			}
			return tasks;
		}

	}



	/**This method set to this description recived time and score as best one at specific task
	 *@param numberOfTess 
	 *@param scoreAchived 
	 *@param timeSpend 
	 * */
	public void setBestTimeScore(int numberOfTest, int scoreAchived, int timeSpend) {
		switch(numberOfTest){
			case ApplicationClass.NUMBER_TASK_LEVEL_MINIMUM:
					levelTimes[0]=timeSpend;
					levelScores[0]=scoreAchived;
				break;
			case ApplicationClass.NUMBER_TASK_LEVEL_MEDINUM:
					levelTimes[1]=timeSpend;
					levelScores[1]=scoreAchived;
				break;
			case ApplicationClass.NUMBER_TASK_LEVEL_MAXIMUM:
					levelTimes[2]=timeSpend;
					levelScores[2]=scoreAchived;
				break;
		}
	}
	
	public void updateMaximumScores(){
		ApplicationClass.dao.updateSpecificLevelDescriptionWithScore(this);
	}
		
}
