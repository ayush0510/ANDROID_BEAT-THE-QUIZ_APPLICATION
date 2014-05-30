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

import java.util.Random;

import android.content.Context;

import com.my.math_quiz.R;
import com.my.math_quiz.utils.Generator.SolutionGeneratorInterface;
//TODO look at this link for displaying symbols now I don0t have time
//	http://stackoverflow.com/questions/14112548/the-cuberoot-symbol-in-java-string
public  class Generator {

	public static interface SolutionGeneratorInterface{
		public int getOneSolution();
		public void setRealSolution(int realSolution);
		
	}
	public static Random rnd=new Random(System.currentTimeMillis());
	public static int getRandomInArea(int from, int to){
		to=Math.abs(to);
		return rnd.nextInt((int)(to-from))+from;
	}
	public static int[] getSolutionList(int correctSolution,SolutionGeneratorInterface generator)
	{
		generator.setRealSolution(correctSolution);
		int[] answers=new int[4];
		
		int wrongAnswer;
		double randValue=Math.random();
		if(randValue<0.25){
			answers[0]=correctSolution;
		}else{
			do{
				wrongAnswer=generator.getOneSolution();
			}while(wrongAnswer==correctSolution);
			answers[0]=wrongAnswer;
		}
		
		if(randValue>=0.25&&randValue<0.5){
			answers[1]=correctSolution;
		}else{
			do{
				wrongAnswer=generator.getOneSolution();
			}while(wrongAnswer==correctSolution||wrongAnswer==answers[0]);
			answers[1]=wrongAnswer;
		}
		
		
		if(randValue>=0.5&&randValue<0.75){
			answers[2]=correctSolution;
		}else{
			do{
				wrongAnswer=generator.getOneSolution();
			}while(wrongAnswer==correctSolution||wrongAnswer==answers[0]||wrongAnswer==answers[1]);
			answers[2]=wrongAnswer;
		}
		
		
		if(randValue>=0.75){
			answers[3]=correctSolution;
		}else{
			do{
				wrongAnswer=generator.getOneSolution();
			}while(wrongAnswer==correctSolution||wrongAnswer==answers[0]||wrongAnswer==answers[1]||wrongAnswer==answers[2]);
			answers[3]=wrongAnswer;
		}
		return answers;
	
	}
	public static Equations getPowerTwoEquation(int toNumber){
		int firstNumber=getRandomInArea(0,toNumber);
		Equations equation=new Equations(firstNumber+"<sup><small>2</small></sup> ", firstNumber*firstNumber);
		return equation;
	}
	public static Equations getPowerTwoEquation(Equations equation){
		equation=new Equations(equation.getText()+"<sup><small>2</small></sup> ", equation.getValue()*equation.getValue());
		return equation;
	}
	
	
	public static Equations getMulEquation(int toNumber){
		int firstNumber=getRandomInArea(0,toNumber);
		int secondNUmber=getRandomInArea(0,toNumber);
		Equations equation=new Equations(firstNumber+" * "+secondNUmber, firstNumber*secondNUmber);
		return equation;
	}
	public static Equations getMulEquation(Equations equation,int toNumber){
		int secondNUmber=getRandomInArea(0,toNumber);
		equation=new Equations(equation.getText()+" * "+secondNUmber, equation.getValue()*secondNUmber);
		return equation;
	}
	public static Equations getDivEquation(int toNumber){
		int firstNumber=getRandomInArea(0,toNumber);
		int secondNUmber=getRandomInArea(1,toNumber);
		Equations equation=new Equations((firstNumber*secondNUmber)+" / "+secondNUmber, (firstNumber*secondNUmber)/secondNUmber);
		return equation;
	}
	public static Equations getDivEquation(Equations equation){
		int leftSide=equation.getValue();
		int secondNUmber=getRandomInArea(1,(int)leftSide);
		int i=0;
		while(i<5||leftSide%secondNUmber!=0){
			secondNUmber=getRandomInArea(1,(int)leftSide);
			i++;
		}
		if(leftSide%secondNUmber!=0){
			secondNUmber=(int)(leftSide/2)+2;
			while(leftSide%secondNUmber!=0){
				secondNUmber--;
			}
		}
		equation=new Equations(equation.getText()+" / "+secondNUmber, equation.getValue()/secondNUmber);
		return equation;
	}
	public static Equations getSqrtEquation(int toNumber){
		int firstNumber=getRandomInArea(0,toNumber);
		Equations equation=new Equations("\u221a<small>"+(firstNumber*firstNumber)+"</small>", firstNumber);
		return equation;
	}

	public static Equations getSumEquation(int toNumber){
		int firstNumber=getRandomInArea(0,toNumber);
		int secondNUmber=getRandomInArea(0,toNumber);
		Equations equation=new Equations(firstNumber+" + "+secondNUmber, firstNumber+secondNUmber);
		return equation;
	}
	public static Equations getSumEquation(Equations equation,int toNumber){
		int secondNUmber=getRandomInArea(0,toNumber);
		equation=new Equations(equation.getText()+" + "+secondNUmber, equation.getValue()+secondNUmber);
		return equation;
	}
	public static Equations getSubEquation(int toNumber){
		int firstNumber=getRandomInArea(0,toNumber);
		int secondNUmber=getRandomInArea(0,toNumber);
		Equations equation=new Equations(firstNumber+" - "+secondNUmber, firstNumber-secondNUmber);
		return equation;
	}
	
	
	public static Equations getSubEquation(Equations equation,int toNumber){
		int secondNUmber=getRandomInArea(0,toNumber);
		equation=new Equations(equation.getText()+" - "+secondNUmber, equation.getValue()-secondNUmber);
		return equation;
	}
	public static Equations getSumOrSub(Equations equation,int toNumber){
		if(Math.random()<0.50){
			return getSumEquation(equation,toNumber);
		}else{
			return getSubEquation(equation,toNumber);
		}
	}
	public static Equations getSumOrSub(int toNumber){
		if(Math.random()<0.50){
			return getSumEquation(toNumber);
		}else{
			return getSubEquation(toNumber);
		}
	}
	public static Equations getMulOrDiv(Equations equation,int toNumber){
		if(Math.random()<0.50){
			return getMulEquation(equation,toNumber);
		}else{
			return getDivEquation(equation);
		}
	}
	public static Equations getMulOrDiv(int toNumber){
		if(Math.random()<0.50){
			return getMulEquation(toNumber);
		}else{
			return getDivEquation(toNumber);
		}
	}
	public static Equations getPowOrSqrt(int toNumber){
		if(Math.random()<0.50){
			return getSqrtEquation(toNumber);
		}else{
			return getPowerTwoEquation(toNumber);
		}
	}
	private static  String textAfterLevelNumber=" stopnja";
	private static String[]levelDescriptions=null;
	
	public static void inicilizeStringForLanguage(Context context){
		textAfterLevelNumber=" "+context.getString(R.string.level);
		levelDescriptions=context.getResources().getStringArray(R.array.levelDescriptions);
	}
	
	public static String get0levelName(){return "1."+textAfterLevelNumber;}
	public static String get0levelDescription(){return levelDescriptions[0];}// "Seštevanje do 10";}
	public static Task get0levelTask(){
		Equations equation=getSumEquation(10);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel0()));
	}
	
	public static String get1levelName(){return "2."+textAfterLevelNumber;}
	public static String get1levelDescription(){return levelDescriptions[1];}//"Seštevanje do 50";}
	public static Task get1levelTask(){
		Equations equation=getSumEquation(50);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel1()));
	}
	public static String get2levelName(){return "3."+textAfterLevelNumber;}
	public static String get2levelDescription(){return levelDescriptions[2];}//"Seštevanje do 100";}
	public static Task get2levelTask(){
		Equations equation=getSumEquation(100);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel2()));
	}
	public static String get3levelName(){return "4."+textAfterLevelNumber;}
	public static String get3levelDescription(){return levelDescriptions[3];}//"Odštevanje do 20";}
	public static Task get3levelTask(){
		//here I don't have negative results so I don atomatic generate numbers but I check first
		int firstNumber=getRandomInArea(0,20);
		int secondNUmber=getRandomInArea(firstNumber,20);
		Equations equation=new Equations(firstNumber+" - "+secondNUmber, firstNumber-secondNUmber);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel3()));
	}
	public static String get4levelName(){return "5."+textAfterLevelNumber;}
	public static String get4levelDescription(){return levelDescriptions[4];}//"Odštevanje do 50";}
	public static Task get4levelTask(){
		Equations equation=getSubEquation(50);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel4()));
	}
	public static String get5levelName(){return "6."+textAfterLevelNumber;}
	public static String get5levelDescription(){return levelDescriptions[5];}//"Seštevanje in odštevanje do 50";}
	
	public static Task get5levelTask(){
		Equations equation=getSumOrSub(50);
		if(Math.random()<0.25){
			//from three parts equation 
			equation=getSumOrSub(equation,50);
		}
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel5()));
	}
	public static String get6levelName(){return "7."+textAfterLevelNumber;}
	public static String get6levelDescription(){return levelDescriptions[6];}//"Seštevanje in odštevanje z oklepaji od 50";}
	public static Task get6levelTask(){
		Equations equation=getSumOrSub(50);
		double randVal=Math.random();
		if(randVal<0.2){
			return get6levelTask();
		}
		else if(randVal<0.6){
			//(a+-b)+-c
			equation.addBracked();
			equation=getSumOrSub(equation,50);
		}
		else {
			//a+-(b+-c)
			int firstNumber=getRandomInArea(0, 50);
			if(randVal<0.8){
				equation.setData(firstNumber+" + "+equation.getText(), firstNumber+equation.getValue());
			}
			else{
				equation.setData(firstNumber+" - "+equation.getText(), firstNumber-equation.getValue());
			}
		}
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel6()));
	}
	public static String get7levelName(){return "8."+textAfterLevelNumber;}
	public static String get7levelDescription(){return levelDescriptions[7];}//"Množenje do 10";}
	public static Task get7levelTask(){
		Equations equation=getMulEquation(10);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel7()));
	}
	public static String get8levelName(){return "9."+textAfterLevelNumber;}
	public static String get8levelDescription(){return levelDescriptions[8];}//"Množenje do 20";}
	public static Task get8levelTask(){
		Equations equation=getMulEquation(20);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel8()));
	}
	public static String get9levelName(){return "10."+textAfterLevelNumber;}
	public static String get9levelDescription(){return levelDescriptions[9];}//"Operacije (+,-,*) do 20";}
	public static Task get9levelTask(){
		
		
		Equations equation=getMulEquation(10);
		double randVal=Math.random();
		if(randVal<0.1){
			//a*b*c
			equation=getMulEquation(equation,10);
		}
		else if(randVal<0.2){
			return get7levelTask();
		}
		else if(randVal<0.60){
			//a+-b*c
			int firstNumber=getRandomInArea(0, 50);
			if(randVal<0.4){
				equation.setData(firstNumber+" + "+equation.getText(), firstNumber+equation.getValue());
			}
			else{
				equation.setData(firstNumber+" - "+equation.getText(), firstNumber-equation.getValue());
			}
			equation.addBracked();
			equation=getSumOrSub(equation,50);
		}
		else {
			//a*b+-c
			equation=getSumOrSub(equation,50);
		}
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel9()));
	}
	public static String get10levelName(){return "11."+textAfterLevelNumber;}
	public static String get10levelDescription(){return levelDescriptions[10];}//"Celoštevilsko delenje do 100";}
	public static Task get10levelTask(){
		Equations equation=getDivEquation(10);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel10()));
	}
	public static String get11levelName(){return "12."+textAfterLevelNumber;}
	public static String get11levelDescription(){return levelDescriptions[11];}//"Operacij (*,/) do 100";}
	public static Task get11levelTask(){
		Equations equation=getMulOrDiv(10);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel11()));
	}
	public static String get12levelName(){return "13."+textAfterLevelNumber;}
	public static String get12levelDescription(){return levelDescriptions[12];}//"Operacije (+,-,*,/) z oklepaji 20";}
	public static Task get12levelTask(){
		Equations equation=null;
		double randVal=Math.random();
		if(randVal<0.25){
			//a-+b*/c
			equation=getMulOrDiv(10);
			int firstNumber=getRandomInArea(0, 20);
			if(randVal<0.12){
				equation.setData(firstNumber+" + "+equation.getText(), firstNumber+equation.getValue());
			}
			else{
				equation.setData(firstNumber+" - "+equation.getText(), firstNumber-equation.getValue());
			}
		}
		else if(randVal<0.5){
			//a*/b-+c
			equation=getMulOrDiv(10);
			equation=getSumOrSub(equation,20);
			
		}
		else if(randVal<0.75){
			//(a-+b)*/c
			equation=getSumOrSub(20);
			equation.addBracked();
			getMulOrDiv(equation, 10);
			
		}
		else {
			//a*/(b-+c)
			equation=getSumOrSub(100);
			equation.addBracked();
			if(randVal<0.9){
				//a*(b-c)
				int firstNumber=getRandomInArea(0, 5);
				equation.setData(firstNumber+" * "+equation.getText(), firstNumber*equation.getValue());
				
			}
			else{
				//a/(b-c)
				int result=getRandomInArea(0, 10);
				int firstNUmber=equation.getValue()*result;
				equation.setData(firstNUmber+" / "+equation.getText(), result);
			}
			
		}
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel12()));
	}
	public static String get13levelName(){return "14."+textAfterLevelNumber;}
	public static String get13levelDescription(){return levelDescriptions[13];}//"Operacije (+,-,*,/) z oklepaji 50";}
	public static Task get13levelTask(){
		Equations equation=null;
		double randVal=Math.random();
		if(randVal<0.25){
			//a-+b*/c
			equation=getMulOrDiv(10);
			int firstNumber=getRandomInArea(0, 50);
			if(randVal<0.12){
				equation.setData(firstNumber+" + "+equation.getText(), firstNumber+equation.getValue());
			}
			else{
				equation.setData(firstNumber+" - "+equation.getText(), firstNumber-equation.getValue());
			}
		}
		else if(randVal<0.5){
			//a*/b-+c
			equation=getMulOrDiv(10);
			equation=getSumOrSub(equation,50);
			
		}
		else if(randVal<0.75){
			//(a-+b)*/c
			equation=getSumOrSub(100);
			equation.addBracked();
			getMulOrDiv(equation, 10);
			
		}
		else {
			//a*/(b-+c)
			equation=getSumOrSub(100);
			equation.addBracked();
			if(randVal<0.9){
				//a*(b-c)
				int firstNumber=getRandomInArea(0, 10);
				equation.setData(firstNumber+" * "+equation.getText(), firstNumber*equation.getValue());
				
			}
			else{
				//a/(b-c)
				int result=getRandomInArea(0, 20);
				int firstNUmber=equation.getValue()*result;
				equation.setData(firstNUmber+" / "+equation.getText(), result);
			}
			
		}
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel13()));
	}
	public static String get14levelName(){return "15."+textAfterLevelNumber;}
	public static String get14levelDescription(){return levelDescriptions[14];}//"Kvadrati do 10";}
	public static Task get14levelTask(){
		Equations equation=getPowerTwoEquation(10);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel14()));
	}
	public static String get15levelName(){return "16."+textAfterLevelNumber;}
	public static String get15levelDescription(){return levelDescriptions[15];}//"Kvadrati do 20";}
	public static Task get15levelTask(){
		Equations equation=getPowerTwoEquation(20);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel15()));
	}
	public static String get16levelName(){return "17."+textAfterLevelNumber;}
	public static String get16levelDescription(){return levelDescriptions[16];}//"Koreni do 100";}
	public static Task get16levelTask(){
		Equations equation=getSqrtEquation(10);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel16()));
	}
	public static String get17levelName(){return "18."+textAfterLevelNumber;}
	public static String get17levelDescription(){return levelDescriptions[17];}//"Koreni do 400";}
	public static Task get17levelTask(){
		Equations equation=getSqrtEquation(20);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel17()));
	}
	public static String get18levelName(){return "19."+textAfterLevelNumber;}
	public static String get18levelDescription(){return levelDescriptions[18];}//"Kvadrati in koreni";}
	public static Task get18levelTask(){
		Equations equation=getPowOrSqrt(20);
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel18()));
	}
	public static String get19levelName(){return "20."+textAfterLevelNumber;}
	public static String get19levelDescription(){return levelDescriptions[19];}//"Mešane operacije";}
	public static Task get19levelTask(){
		// here I have +-*/ ^2 and sqrt ()
		Equations equation=null;
		double randVal=Math.random();
		if(randVal<0.1){
			//three parts with +-*/()
			return get13levelTask();
		}else //if(randVal<0.2)
		{
			equation=getSqrtEquation(10);
			if(randVal<0.3){
				//sqrt(a)+- b+- c+- d
				equation=getSumOrSub(getSumOrSub(getSumOrSub(equation, 50),50),50);
			}
			else if(randVal<0.6){
				//sqrt(a)+- b+- (c+- d)
				Equations newEquation=getSumOrSub(equation, 50);
				equation=getSumOrSub(equation,50);
				newEquation.addBracked();
				if(randVal<0.45){
					equation.setData(equation.getText()+" + "+newEquation.getText(), equation.getValue()+newEquation.getValue());
				}else{
					equation.setData(equation.getText()+" - "+newEquation.getText(), equation.getValue()-newEquation.getValue());
				}
				
			}else{
				////sqrt(a)+- (b+- c)+- d
				Equations newEquation=getSumOrSub(equation, 50);
				newEquation.addBracked();
				if(randVal<0.45){
					equation.setData(equation.getText()+" + "+newEquation.getText(), equation.getValue()+newEquation.getValue());
				}else{
					equation.setData(equation.getText()+" - "+newEquation.getText(), equation.getValue()-newEquation.getValue());
				}
				equation=getSumOrSub(equation,50);
			}
		}
		/**TODO*/
//		else if(randVal<0.3){
//			//a*/ sqrt(b)*/ c*/ d
//		}else if(randVal<0.4){
//			//a+- b*/ pow(c) +- d
//		}else if(randVal<0.5){
//			//a*/ b c+- pow(d)
//		}else if(randVal<0.6){
//			//a*/ sqrt(b) +-pow(c)*/  d
//		}else if(randVal<0.7){
//			//a+- b */  pow(c+-d)
//		}else if(randVal<0.8){
//			//a +- b*/ c +-d
//		}else if(randVal<0.9){
//			//pow(a+-b)+- c*/ d
//		}else{
//			//a*/ b*/ c*/ d
//		}
		
		
		
		
		
		return new Task(equation,getSolutionList(equation.getValue(),new SlutionForLevel19()));
	}
}



/**This is level where: we sum positive number to 10*/
class SlutionForLevel0 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 20);
	}
}


/**This is level where: we sum positive number to 50*/
class SlutionForLevel1 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-10, 10)+realSolution;
	}
}

/**This is level where: we sum positive number to 100*/
class SlutionForLevel2 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-20, 20)+realSolution;
	}
}

/**This is level where: we calculet difference to 20*/
class SlutionForLevel3 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-10, 10)+realSolution;
	}
}

/**This is level where: we calculet difference to 50*/
class SlutionForLevel4 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-20, 20)+realSolution;
	}
}

/**This is level where: we calculet difference and sum to 50*/
class SlutionForLevel5 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-20, 20)+realSolution;
	}
}

/**This is level where: we calculet difference and sum with brackets to 50*/
class SlutionForLevel6 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-20, 20)+realSolution;
	}
}

/**This is level where: we multiply to 10*/
class SlutionForLevel7 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 100);
	}
}

/**This is level where: we multiply to 20*/
class SlutionForLevel8 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 400);
	}
}

/**This is level where: we (+-*)to 20*/
class SlutionForLevel9 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-20, 20)+realSolution;
	}
}

/**This is level where: we devide integer to 100*/
class SlutionForLevel10 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-10, 10)+realSolution;
	}
}

/**This is level where: we  (/*) to 100*/
class SlutionForLevel11 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-100, 100)+realSolution;
	}
}

/**This is level where:  we (+-/*) to 20 with brackets*/
class SlutionForLevel12 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-100, 100)+realSolution;
	}
}

/**This is level where:  we (+-/*) to 100 with brackets*/
class SlutionForLevel13 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-100, 100)+realSolution;
	}
}

/**This is level where: we pow2 to 10*/
class SlutionForLevel14 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 100);
	}
}

/**This is level where: we pow2 to 20*/
class SlutionForLevel15 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 400);
	}
}

/**This is level where: we square to 100*/
class SlutionForLevel16 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 10);
	}
}

/**This is level where: we square 400*/
class SlutionForLevel17 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(0, 20);
	}
}

/**This is level where:sqers and powers*/
class SlutionForLevel18 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-20, 20)+realSolution;
	}
}

/**This is level where: mix operations advance*/
class SlutionForLevel19 implements  SolutionGeneratorInterface{
	int realSolution;
	@Override
	public void setRealSolution(int realSolution) {
		this.realSolution=realSolution;
	}
	@Override
	public  int getOneSolution(){
		return	Generator.getRandomInArea(-100, 100)+realSolution;
	}
}