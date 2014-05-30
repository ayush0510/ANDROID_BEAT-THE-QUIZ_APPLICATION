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

public class Equations {
	String text;
	int result;
	public Equations(String text,int result){
		setData(text, result);
	}
	public void setData(String text,int result){
		this.text=text;
		this.result=result;
	}
	public int getValue(){
		return result;
	}
	public String getText(){
		return text;
	}
	public void addBracked() {
		text="("+text+")";
		
	}
}