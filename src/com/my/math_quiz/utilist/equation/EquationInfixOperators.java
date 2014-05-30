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

public class EquationInfixOperators extends Equation{

	
	
	private int operator;
	private Equation leftSide=null;
	private Equation rightSide=null;
	
	public EquationInfixOperators(Equation leftSide,Equation rigtSide,int operator){
		this.leftSide=leftSide;
		this.rightSide=rigtSide;
		this.operator=operator;
	}
	
	
	@Override
	public String getText() {
		String leftText=leftSide.getText();
		String rightText=rightSide.getText();
		switch(operator){
			case operatorDIV:
				return leftText+" / "+rightText;
			case operatorLOG:
				/**TODO*/
				return "";
			case operatorMUL:
				return leftText+" * "+rightText;
			case operatorSUB:
				return leftText+" - "+rightText;
			case operatorSUM:
				return leftText+" + "+rightText;
			default:
				return "";
		}
	}

	@Override
	public float getValue() {
		float leftValue=leftSide.getValue();
		float rightValue=rightSide.getValue();
		switch(operator){
			case operatorDIV:
				return leftValue/rightValue;
			case operatorLOG:
				/**TODO*/
				return 0;
			case operatorMUL:
				return leftValue*rightValue;
			case operatorSUB:
				return leftValue-rightValue;
			case operatorSUM:
				return leftValue+rightValue;
			default:
				return 0;
		}
	}





	@Override
	public float getValue(float newFirstPart) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getLeftPriority() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getLefttPart() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getRightPriority() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getRightPart() {
		// TODO Auto-generated method stub
		return 0;
	}

}
