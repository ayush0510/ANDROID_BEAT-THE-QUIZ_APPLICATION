
package com.my.math_quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.my.math_quiz.views.TitleBar;
import com.my.math_quiz.views.TitleBar.TitleBarListener;
import com.my.math_quiz_multiplayer_stuff.TCPIPClient;
import com.my.math_quiz_multiplayer_stuff.TCPIPServer;
import com.my.math_quiz_multiplayer_stuff.TCPIPServer.TCPIPServerListenerBeforeGame;

public class MultiPlayerActivityHost extends Activity implements TitleBarListener, TCPIPServerListenerBeforeGame{

	TitleBar titleBar=null;
	TextView ipAdress;
	EditText portNumber;
	TextView numberOfPlayers;
	
	Button serverButton;
	Button nextButton;
	Toast toast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_player_w_host);
		
		titleBar=(TitleBar)findViewById(R.id.TBtitleBar);
		titleBar.setTitleBarListener(this);
		titleBar.setTitle(getString(R.string.multi_player_host));
		titleBar.setRightImage(BitmapFactory.decodeResource(getResources(), R.drawable.action_settings));
		
		
		ipAdress=(TextView)findViewById(R.id.MPWHValueIp);
		portNumber=(EditText)findViewById(R.id.MPWHWEditPort);
		
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		String ip = ApplicationClass.intToIp(wm.getConnectionInfo().getIpAddress());
		
		ipAdress.setText(ip);
		portNumber.setText(ApplicationClass.getLastPortNumber()+"");
		
		numberOfPlayers=(TextView)findViewById(R.id.MPWHValueNumPlayers);
		numberOfPlayers.setText("0");
		toast= Toast.makeText(this,getString(R.string.successfulStartedServer),Toast.LENGTH_SHORT);
		
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
		
		serverButton=(Button)findViewById(R.id.MPWHStartServer);
		nextButton=(Button)findViewById(R.id.MPWJStartGame);
		nextButton.setEnabled(false);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		super.onResume();
		TCPIPServer.setTCPIPServerListener(this);
		onNumberOfClientsChanged(TCPIPServer.getNumberOfClients(),false);
	}



	/**
	 * On click method defined in .xml for booth of the buttons is the same method
	 * */
	public void MPWHbuttonClicked(View v){
		Log.d("srDebuging","bitton clicekd");
		if(v.getId()==R.id.MPWHStartServer){
			numberOfPlayers.setText("0");
			serverButton.setText(getString(R.string.muRestartServer));
			nextButton.setEnabled(false);
			TCPIPServer.setPort(Integer.parseInt(portNumber.getText()+""));
			TCPIPServer.restartTcpServer();
		}else{
			TCPIPServer.stopAcepptingNewClients();
			TCPIPServer.removeTCPIPServerListener(this);
			TCPIPServer.sendCommandToStartGame();
			Intent intent = new Intent(this, LevelsDisplayedActivity.class);
			intent.putExtra(LevelsDisplayedActivity.KEY_FOR_MODE_PARAMETER, LevelsDisplayedActivity.MODE_MULTIPLAYER_SELECTION_WLAN);
			startActivity(intent);
		}
	}

	@Override
	public void finish() {
		TCPIPServer.removeTCPIPServerListener(this);
		TCPIPServer.killServer();
		super.finish();
	}
	/**BEGIN the title bar listener methods*/
	@Override
	public void onLeftButtonClick() {
		this.finish();
	}

	@Override
	public void onRightButtonClick() {
		Intent intent = new Intent(MultiPlayerActivityHost.this, PreferenceActivity.class);
		startActivity(intent);
	}
	/**END the title bar listener methods*/


	/**BEGIN the TCPIPServerListenerBeforeGame methods*/
	@Override
	public void onNumberOfClientsChanged(int number, boolean accepted) {
		numberOfPlayers.setText(number+"");
		if(number>0){
			nextButton.setEnabled(true);
		}
		else{
			nextButton.setEnabled(false);
		}
		
	}



	@Override
	public void onSuccessfulStartedServer(boolean wasSuccessful) {
		if(wasSuccessful){
			toast.setText(R.string.successfulStartedServer);
		}else{
			toast.setText(R.string.unSuccessfulStartedServer);
		}
		toast.show();
	}

}
