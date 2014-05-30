
package com.my.math_quiz.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.my.math_quiz.LevelsDisplayedActivity;
import com.my.math_quiz.R;
import com.my.math_quiz.utils.LevelDescripction;

public class LevelsDisplayedAdapter extends ArrayAdapter<LevelDescripction>{
	LayoutInflater inflater;
	int selectedMode;
	int elementResourceId;
	public LevelsDisplayedAdapter(Context context, int resource, LevelDescripction[] objects,int selectedMode) {
		super(context, resource, objects);
		inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		this.selectedMode=selectedMode;
	
		switch(selectedMode){
			case LevelsDisplayedActivity.MODE_BEFORE_SINGLE_PLAYER_GAME: elementResourceId=R.layout.element_single_player; break;
			case LevelsDisplayedActivity.MODE_MULTIPLAYER_SELECTION_ONE_DEVICE:elementResourceId=R.layout.element_multiplayer; break;
			case LevelsDisplayedActivity.MODE_MULTIPLAYER_SELECTION_WLAN: elementResourceId=R.layout.element_multiplayer; break;
		//	case LevelsDisplayedActivity.MODE_TUTORIAL_SELECTION: elementResourceId=R.layout.element_tutorial; break;
			
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if(selectedMode==LevelsDisplayedActivity.MODE_BEFORE_SINGLE_PLAYER_GAME){
				ItemHolderSPLG holder=null;
				LevelDescripction info=getItem(position);
				
				if(convertView==null){
					convertView=inflater.inflate(elementResourceId, null);
					holder=new ItemHolderSPLG(
							(TextView)convertView.findViewById(R.id.ESPtitle),
							(TextView)convertView.findViewById(R.id.ESPdescription),
							(TextView)convertView.findViewById(R.id.ESPtimeL),
							(TextView)convertView.findViewById(R.id.ESPtimeM),
							(TextView)convertView.findViewById(R.id.ESPtimeH),
							(TextView)convertView.findViewById(R.id.ESPscoreL),
							(TextView)convertView.findViewById(R.id.ESPscoreM),
							(TextView)convertView.findViewById(R.id.ESPscoreH));
					convertView.setTag(holder);
				}else{
					holder=(ItemHolderSPLG)convertView.getTag();
				}
				holder.title.setText(info.levelName);
				holder.description.setText(info.levelDescription);
				
				holder.timeL.setText(info.levelTimes[0]+"");
				holder.timeM.setText(info.levelTimes[1]+"");
				holder.timeH.setText(info.levelTimes[2]+"");
				
				
				holder.scoreL.setText(info.levelScores[0]+"");
				holder.scoreM.setText(info.levelScores[1]+"");
				holder.scoreH.setText(info.levelScores[2]+"");
				
	
		}else{
			ItemHolderGeneral holder=null;
			LevelDescripction info=getItem(position);
			
			if(convertView==null){
				convertView=inflater.inflate(elementResourceId, null);
				holder=new ItemHolderGeneral((TextView)convertView.findViewById(R.id.ESPtitle),(TextView)convertView.findViewById(R.id.ESPdescription));
				convertView.setTag(holder);
			}else{
				holder=(ItemHolderGeneral)convertView.getTag();
			}
			holder.title.setText(info.levelName);
			holder.description.setText(info.levelDescription);
		}
			
		return convertView;
	}
	
	
	@Override
	public LevelDescripction getItem(int position) {
		return super.getItem(position);
	}
	class ItemHolderGeneral{
		public TextView title;
		public TextView description;
		
		public ItemHolderGeneral(TextView title,TextView description){
			this.title=title;
			this.description=description;
		}
	}

	class ItemHolderSPLG extends ItemHolderGeneral{
		public TextView timeL;
		public TextView timeM;
		public TextView timeH;
		
		public TextView scoreL;
		public TextView scoreM;
		public TextView scoreH;
		
		public ItemHolderSPLG(TextView title,TextView description,TextView timeL,TextView timeM,TextView timeH,TextView scoreL,TextView scoreM,TextView scoreH){
			super(title, description);
		
			this.timeL=timeL;
			this.timeM=timeM;
			this.timeH=timeH;
			
			this.scoreL=scoreL;
			this.scoreM=scoreM;
			this.scoreH=scoreH;
		}
		
		
	}
}
