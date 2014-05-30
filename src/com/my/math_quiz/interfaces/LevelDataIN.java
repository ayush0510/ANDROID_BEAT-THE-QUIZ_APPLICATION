
package com.my.math_quiz.interfaces;

import java.util.ArrayList;

import com.my.math_quiz.utils.Task;

public interface LevelDataIN {
	/**@return the arrayList of test for this specific level, if we call this method first time, 
	 * the levels will be automatically generated for this specific level*/
	public ArrayList<Task> getTests(int number);
	
	/**This method set tasks to null and at next time calling getTask the task will be generated at new,
	 * on calling this method is also reseted timer, so you should call method @startTimingLevel when you want start timing*/
	public void clearLevelData();
	
	/**@return the number of tests in this level which are unsolved*/
	public  int getNumberOfUnsolvedTests();
	
	
	/**@return the score from this level (probably is just number of correct answers in level)*/
	public  int getScoreAchived();
	
	/**This method start timing time from zero. The start timer will only star 
	 * if clocked is stopped previous or. isen't start already. 
	 * The time will be set to zero*/
	public void startTimingLevel();
	
	/**This method stop clock, and save current length for continue timing,
	 *  clock must already run else is no effect from this method (start should be called before)*/
	public void pauseTimingLevel();
	
	/**This method  continue timing form last saved time,
	 *  clock must already run else is no effect from this method (start should be called before)*/
	public void resumTimingLevel();
	
	/**This method if timer is running we now stop timer and we can continue him only with method start
	 * clock must already run else is no effect from this method (start should be called before)*/
	public void stopTimingLevel();
	
	/**@return the time (in milliseconds) spent for solving all test in this level 
	 * if level is not previous stopped method will return -1 */
	public long getDurationOfLevel();
	
	/**this method return next not solved test position 
	 * @param currentPosition position from where it method will look for not solved test
	 * @return the position of next not solved test, from currentPosition, if is no more test for solve 
	 * from currentPosition it return test before currentPostion and if event ther is no more test it retun -1 */
	public int getNextNotSolvedTestPosition(int currentPosition);

}
