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

import android.view.View;
import android.view.animation.Animation.AnimationListener;

import com.my.math_quiz.ApplicationClass;

public class OneAnimationStep{
	public ArrayList<OneItemToAnimate> itemsToDisplay=null;
	public ArrayList<OneItemToAnimate> itemsTohide=null;
	private AnimationListener animationListener;
	public OneAnimationStep(AnimationListener listener){
		animationListener=listener;
		
	}
	public OneAnimationStep(View view1,boolean addListener1,AnimationListener listener){
		animationListener=listener;
		addItemToDisplay(view1,addListener1);
	}

	public OneAnimationStep(View view1,boolean addListener1,View view2,boolean addListener2,AnimationListener listener){
		animationListener=listener;
		addItemToDisplay(view1,addListener1);
		addItemToDisplay(view2,addListener2);
	}
	public OneAnimationStep(View view1,boolean addListener1,View view2,boolean addListener2,View view3, boolean addListener3,AnimationListener listener){
		animationListener=listener;
		addItemToDisplay(view1,addListener1);
		addItemToDisplay(view2,addListener2);
		addItemToDisplay(view3,addListener3);
	}

	public void executeAnimation(){
		
		if(itemsToDisplay!=null){
			for(OneItemToAnimate item:itemsToDisplay){
				item.view.clearAnimation();
				if(item.addListener){
					item.view.startAnimation(ApplicationClass.getFadeInAnimation(animationListener));		
				}
				else{
					item.view.startAnimation(ApplicationClass.getFadeInAnimation(null));		
				}
			}
			
		}
		if(itemsTohide!=null){
			for(OneItemToAnimate item:itemsTohide){
				item.view.clearAnimation();
				if(item.addListener){
					item.view.startAnimation(ApplicationClass.getFadeOutAnimation(animationListener));		
				}
				else{
					item.view.startAnimation(ApplicationClass.getFadeOutAnimation(null));		
				}
			}
		}
		
		
	}
	
	
	
	public void addItemToDisplay(View view, boolean addListener){
		if(itemsToDisplay==null){
			itemsToDisplay=new ArrayList<OneItemToAnimate>();
		}
		itemsToDisplay.add(new OneItemToAnimate(view, addListener));
	}
	
	public void addItemToHide(View view, boolean addListener){
		if(itemsTohide==null){
			itemsTohide=new ArrayList<OneItemToAnimate>();
		}
		itemsTohide.add(new OneItemToAnimate(view, addListener));
	}
	class OneItemToAnimate{
		
		public boolean addListener;
		public View view;
		
		public OneItemToAnimate(View view, boolean addListener){
			this.addListener=addListener;
			this.view=view;
		}
	}
}