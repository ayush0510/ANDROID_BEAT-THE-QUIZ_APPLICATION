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

import android.text.Html;
import android.text.Spanned;

public class Task {

	private Equations equation;
	int[] answers=new int[4];
	
	
	/**@selectedAnswer on answer clicked we save the answer and if we return back we set color to this button according to varible @isCorrectAnswer*/
	private int selectedAnswer;
	/**@correctAnswer number of corrrect answer from 0 to 4 in this specific task, we have this to compare later and other thing, so we don't need always go thru all answers in task*/
	private int correctAnswer;

	public Task(Equations equation,int[] answers){
		setEquationAndAnswers(equation,answers);
	}
	
	public void setEquationAndAnswers(Equations equation,int[] answers){
		selectedAnswer=-1;
		this.equation=equation;
		this.answers=answers;
		int result=equation.getValue();
		for(int i=0; i<answers.length; i++){
			if(result==answers[i]){
				correctAnswer=i;
				break;
			}
		}
	}
//	public int getResult(){
//		return equation.getValue();
//	}
	public int[] getAnswers(){
		return answers;
	}
	/**
	 * @return This method return html style of text 
	 * */
	public Spanned getText(){
		return Html.fromHtml(equation.getText());
	}
	/**
	 * @return This method return source text, html tags not applayed yet 
	 * */
	public String getSourceText(){
		return equation.getText();
	}
	public int getCorrectAnswer(){
		return correctAnswer;
	}
	/**Set the selected answer to specific task for single player only, in multipleyer we save answers to bottom buttons 
	 * @param selectedAnswer the number of answer which user select from 0 to 4
	 * @return if previous user didn't already select answer to this task we return true else we return false*/
	public boolean setSelectedAnswer(int selectedAnswer){
		if(this.selectedAnswer==-1){
			this.selectedAnswer=selectedAnswer;
			return true;
		}
		return false;
	}
	/**
	 * For single player only, in multipleyer we save answers to bottom buttons 
	 * */
	public int getSelectedAnswer(){
		return selectedAnswer;
	}

	public int getCorrectAnswerValue() {
		return equation.getValue();
	}
}
