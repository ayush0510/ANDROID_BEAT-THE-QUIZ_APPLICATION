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

package com.my.math_quiz.utilist.equation;

public abstract class Equation {
	abstract public String getText();
	abstract public float getValue();
	/**This is calculatin priorities if we sea that is bigger priority we fcalculate higher priority first and than we switch and return value on switch values*/
	abstract public int getLeftPriority();
	abstract public int getLefttPart();
	
	abstract public int getRightPriority();
	abstract public int getRightPart();
	
	abstract public float getValue(float newFirstPart);

	/**
	 * Priorities
	 * value=0
	 * integral odvod 5
	 * ()=10
	 *sqrt=	^x=15
	 * //*=20
	 * +- =25
	 * 
	 * */
	

	public static final int operatorSUM=0;
	public static final int operatorSUB=1;
	public static final int operatorDIV=2;
	public static final int operatorMUL=3;
	public static final int operatorLOG=4;
	
	public static final int operatorBracked=5;
	public static final int operatoSqr=6;
	public static final int operatorPow=7;
//	public static final int operatorIntegral=8;
//	public static final int operatorOdvo=9;
	public static final int value=10;
	
	public static int getPriorityOfOperator(int operator){
		switch(operator){
			
			case operatorSUM:return 5;
			case operatorSUB:return 5;
			case operatorDIV:return 10;
			case operatorMUL:return 10;
			case operatorLOG:return 30;
			case operatoSqr:return 50;
			case operatorPow:return 50;
			case operatorBracked:return 100;
			case value:return 999;
//			case operatorIntegral:return 1;
//			case operatorOdvo:return 1;
			default: return 0;
		}
	}
}
