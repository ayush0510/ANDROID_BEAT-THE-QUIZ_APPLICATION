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
package com.my.math_quiz.views;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPagerWithCustomSpeedOfSwitchingPages extends ViewPager {

	
	private final int fastSpeed=80;
	private int mDuration = fastSpeed;
	private boolean isEnablePageSwiping=true;
	public void setSlowSpeed(){
		mDuration = 200;
	}
	public ViewPagerWithCustomSpeedOfSwitchingPages(Context context) {
		super(context);
		init(context);
	}
	public ViewPagerWithCustomSpeedOfSwitchingPages(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private void init(Context context){
		Interpolator sInterpolator = new AccelerateInterpolator();
		try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true); 
            FixedSpeedScroller scroller = new FixedSpeedScroller(this.getContext(), sInterpolator);
            // scroller.setFixedDuration(5000);
            mScroller.set(this, scroller);
         } catch (NoSuchFieldException e) {Log.d("exception",e+"");
        } catch (IllegalArgumentException e) {Log.d("exception",e+"");
        } catch (IllegalAccessException e) {Log.d("exception",e+"");
        }
	}
	/**This method disable switching pages on view pager with finger. It is only done programmatically*/
	public void disablePageSwitchingWithFinger(){
		isEnablePageSwiping=false;
	}
	/**This method disable switching pages on view pager with finger. Programmatically option steal can be used*/
	public void enablePageSwitchingWithFinger(){
		isEnablePageSwiping=true;
	}
	@Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
       if(isEnablePageSwiping) return super.onInterceptHoverEvent(event);
		// Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(isEnablePageSwiping)return super.onTouchEvent(event);
        // Never allow swiping to switch between pages
        return false;
    }
	class FixedSpeedScroller extends Scroller {
	
	    
	    
	    public FixedSpeedScroller(Context context) {
	        super(context);
	    }
	    public FixedSpeedScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }
	
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	        mDuration=fastSpeed;
	    }
	
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	        mDuration=fastSpeed;
	    }
	}

}
