
package com.my.math_quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.my.math_quiz.R.string;
import com.my.math_quiz.views.TitleBar;
import com.my.math_quiz.views.TitleBar.TitleBarListener;
import com.my.math_quiz_multiplayer_stuff.TCPIPClient;
import com.my.math_quiz_multiplayer_stuff.TCPIPClient.TCPIPClientListenerBeforeGame;
import com.my.math_quiz_multiplayer_stuff.TCPIPServer;

public class MultiPlayerActivityJoin extends Activity implements TitleBarListener,TCPIPClientListenerBeforeGame {

	TitleBar titleBar=null;
	EditText ipAdress;
	EditText portNumber;
	Toast toast;
	Button connectButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_player_w_join);
		
		titleBar=(TitleBar)findViewById(R.id.TBtitleBar);
		titleBar.setTitleBarListener(this);
		titleBar.setTitle(getString(R.string.multi_player_join));
		titleBar.setRightImage(BitmapFactory.decodeResource(getResources(), R.drawable.action_settings));
		
		
		ipAdress=(EditText)findViewById(R.id.MPWJWEditIp);
		portNumber=(EditText)findViewById(R.id.MPWJWEditPort);
		
		ipAdress.setText(ApplicationClass.getLastIPAdress());
		portNumber.setText(ApplicationClass.getLastPortNumber()+"");
		
		
		ipAdress.addTextChangedListener(new TextWatcher() {
			@Override	public void onTextChanged(CharSequence s, int start, int before, int count) {	}
			@Override	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {	}
			
			@Override
			public void afterTextChanged(Editable s) {
					ApplicationClass.saveIPAdress(s.toString());
			}
		});
		
		portNumber.addTextChangedListener(new TextWatcher() {
			@Override	public void onTextChanged(CharSequence s, int start, int before, int count) {	}
			@Override	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {	}
			
			@Override
			public void afterTextChanged(Editable s) {
				try{
				ApplicationClass.savePortNumber(Integer.parseInt(s.toString()));
				}catch(Exception e){}
			}
		});
		toast= Toast.makeText(this,getString(R.string.successfulConnectionToServer),Toast.LENGTH_LONG);
		
		
		
		
		connectButton=(Button)findViewById(R.id.MPWJConnect);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onResume() {
		super.onResume();
		TCPIPClient.setTCPIPClientListener(this);
	}
	
	
	/**
	 * On click method defined in .xml 
	 * */
	public void MPWJbuttonClicked(View v){
			Log.d("clDebuging","connect cliekce");
			toast.setText(string.connectiongToServer);
			toast.show();
			
			TCPIPClient.setIpAdressAndPort(ipAdress.getText()+"", Integer.parseInt(portNumber.getText()+""));
			Log.d("clDebuging","connecting to server: "+ipAdress.getText()+":"+ portNumber.getText()+"");
			TCPIPClient.connectToServer();
				
			
		
	}
	@Override
	public void finish() {
		TCPIPClient.removeTCPIPClientListener(this);
		TCPIPClient.killConnection();
		super.finish();
	}
	/**BEGIN the title bar listener methods*/
	@Override
	public void onLeftButtonClick() {
		this.finish();
	}

	@Override
	public void onRightButtonClick() {
		Intent intent = new Intent(MultiPlayerActivityJoin.this, PreferenceActivity.class);
		startActivity(intent);
	}
	/**END the title bar listener methods*/

	
	/**BEGIN the TCPIPClientListenerBeforeGame methods*/
	@Override
	public void onCommandToStartGame() {
		Intent intent = new Intent(MultiPlayerActivityJoin.this, MultiPlayerActivityJoinGame.class);
		startActivity(intent);
	}

	@Override
	public void onRequestingClientNickname() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerStoped() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnection(boolean wasSuccsessful) {
		if(wasSuccsessful){
			toast.setText(string.successfulConnectionToServer);
			toast.show();
		}else{
			toast.setText(string.unSuccessfulConnectionToServer);
			toast.show();
		}
		
	}
	/**END the TCPIPClientListenerBeforeGame methods*/

}
