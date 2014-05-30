
package com.my.math_quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.my.math_quiz.adapters.LevelsDisplayedAdapter;
import com.my.math_quiz.utils.LevelDescripction;
import com.my.math_quiz.views.TitleBar;
import com.my.math_quiz.views.TitleBar.TitleBarListener;

public class LevelsDisplayedActivity extends Activity implements TitleBarListener{
	TitleBar titleBar=null;
	ListView list=null;
	
	
	public static final int MODE_BEFORE_SINGLE_PLAYER_GAME=1;
	public static final int MODE_MULTIPLAYER_SELECTION_ONE_DEVICE=2;
	public static final int MODE_MULTIPLAYER_SELECTION_WLAN=3;
//	public static final int MODE_TUTORIAL_SELECTION=4;
	
	public static final String KEY_FOR_MODE_PARAMETER="displayingLevelsMode";
	public static final String KEY_FOR_SINGLE_PLAYER_RESULT="finishdLevel";
	public static final String KEY_FOR_LEVEL_WHICH_TUTORIAL_WAS_DISPLAYED="keyforlevelwhictwd";
	
	private int selectedMode;
	
	LevelsDisplayedAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_player);
		
		Intent myIntent = getIntent();
		selectedMode = myIntent.getIntExtra(KEY_FOR_MODE_PARAMETER,0);
		
		
		
		titleBar=(TitleBar)findViewById(R.id.TBtitleBar);
		titleBar.setTitleBarListener(this);
		titleBar.setRightImage(BitmapFactory.decodeResource(getResources(), R.drawable.action_settings));
		switch (selectedMode) {
			case MODE_BEFORE_SINGLE_PLAYER_GAME:
				titleBar.setTitle(getString(R.string.single_player));
				break;
			case MODE_MULTIPLAYER_SELECTION_ONE_DEVICE:
				titleBar.setTitle(getString(R.string.multi_player_one_device));
				break;
			case MODE_MULTIPLAYER_SELECTION_WLAN:
				titleBar.setTitle(getString(R.string.multi_player_wlan));
				break;
		//	case MODE_TUTORIAL_SELECTION:
		//		titleBar.setTitle(getString(R.string.tutorials));
		//		break;
			default: break;
		}
		
		
		list=(ListView)findViewById(R.id.SPlistView);
		
		adapter=new LevelsDisplayedAdapter(this,0,ApplicationClass.getLevelDescriptions(),selectedMode);
		list.setAdapter(adapter);
	
		list.setOnItemClickListener(onItemClickListene);
		ApplicationClass.dao.fealLevelsDescriptionsWithScores();
	}
	
	OnItemClickListener onItemClickListene=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
			LevelDescripction lds=ApplicationClass.getLevelDescription(position);
			Intent intent;
			switch (selectedMode) {
				case MODE_BEFORE_SINGLE_PLAYER_GAME:
//				TODO you have to count the tutorial if user first time open the level
				//	if(lds.wasAlreadyOpend()==true){
						intent = new Intent(LevelsDisplayedActivity.this, SingelPlayerGameActivity.class);
						intent.putExtra("EXTRA_SELECTED_LEVEL", position);
						startActivityForResult(intent,55);
				//	}
				//	else{
						//we must show tutorial
					//	startTutorial(position,true,MODE_START_BEFORE_GAME);
				//	}
					
				break;
				case MODE_MULTIPLAYER_SELECTION_ONE_DEVICE:
					Intent intent2 = new Intent(LevelsDisplayedActivity.this, MultiPlayerOneDeviceGameActivity.class);
					intent2.putExtra("EXTRA_SELECTED_LEVEL", position);
					startActivity(intent2);
				break;
				case MODE_MULTIPLAYER_SELECTION_WLAN:
					Intent intent3 = new Intent(LevelsDisplayedActivity.this, MultiPlayerActivityHostGame.class);
					intent3.putExtra("EXTRA_SELECTED_LEVEL", position);
					startActivity(intent3);
				break;
		//		case MODE_TUTORIAL_SELECTION:
		//			startTutorial(position,false,MODE_START_FROM_TUTORIAL);
		//		break;	
			default:
				break;
			}

		}
	};
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
	}
	public static final String KEY_FOR_MODE_PARAMATER="keyformodeinonetutorial";
	public static final String KEY_FOR_SELECTED_LEVEL="selectedlevel";
	public static final int MODE_START_BEFORE_GAME=1;
	//public static final int MODE_START_FROM_TUTORIAL=2;

/*
	private void startTutorial(int position,boolean beforeGame,int mode){
		Intent intent=null;
		if(position==0){
			intent = new Intent(LevelsDisplayedActivity.this, TutorialLevel0.class);
			intent.putExtra(KEY_FOR_MODE_PARAMATER,mode);
		}
		else if(position==3){
			intent = new Intent(LevelsDisplayedActivity.this, TutorialLevel3.class);
			intent.putExtra(KEY_FOR_MODE_PARAMATER,mode);
		}else if(position==10){
			intent = new Intent(LevelsDisplayedActivity.this, TutorialLevel10.class);
			intent.putExtra(KEY_FOR_MODE_PARAMATER, mode);
		}
		else if(position==1||position==2||position==4){
			intent = new Intent(LevelsDisplayedActivity.this, TutorialLevelEquationType.class);
			intent.putExtra(KEY_FOR_MODE_PARAMATER, mode);
			intent.putExtra(KEY_FOR_SELECTED_LEVEL, position);
		}else if(position>=5){
			intent = new Intent(LevelsDisplayedActivity.this, TutorialLevelMultiRow.class);
			intent.putExtra(KEY_FOR_MODE_PARAMATER, mode);
			intent.putExtra(KEY_FOR_SELECTED_LEVEL, position);
		}
		if(beforeGame){
			startActivityForResult(intent,79);
		}else{
			startActivity(intent);
		}
	
	}
	*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("activiFinish","result coede is: "+resultCode);
		if (requestCode == 55) {
		     if(resultCode == RESULT_OK){      
		         int result=data.getIntExtra(KEY_FOR_SINGLE_PLAYER_RESULT,-1);
		         if(result!=-1){
		        	 ApplicationClass.getLevelDescription(result).updateMaximumScores();
		        	 adapter.notifyDataSetChanged();
		         }
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		else if(requestCode==79){
			if(resultCode==RESULT_OK){
				int finishedLevel=data.getIntExtra(KEY_FOR_LEVEL_WHICH_TUTORIAL_WAS_DISPLAYED, 0);
				Intent 	intent = new Intent(LevelsDisplayedActivity.this, SingelPlayerGameActivity.class);
				intent.putExtra("EXTRA_SELECTED_LEVEL", finishedLevel);
				startActivityForResult(intent,55);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}
	/**BEGIN the title bar listener methods*/
	@Override
	public void onLeftButtonClick() {
		this.finish();
	}

	@Override
	public void onRightButtonClick() {
		Log.d("starting","start preference activity");
		Intent intent = new Intent(LevelsDisplayedActivity.this, PreferenceActivity.class);
		startActivity(intent);
		
	}
	/**END the title bar listener methods*/


}
