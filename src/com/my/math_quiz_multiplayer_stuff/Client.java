
package com.my.math_quiz_multiplayer_stuff;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import com.my.math_quiz.ApplicationClass;

import android.util.Log;

public class Client {

	
	private int playerId;
	public int getPlayerId() {
		return playerId;
	}
	public String getNickname() {
		return nickname;
	}
	private String nickname;
	
	Socket socket;
	OutputStream outputStream;
//	BufferedReader(new InputStreamReader(inputSream));
	DataOutputStream dataOutputStream;
	ClientReadingThread readingRunable;
	Thread readingThread;
	
	public Client(int playerId,Socket socket,ClientReadingThread readingRunable,Thread readingThread){
		this.playerId=playerId;
		this.socket=socket;
		try{
			this.outputStream=socket.getOutputStream();
			this.dataOutputStream=new DataOutputStream(outputStream);
		}catch(Exception e){}
		this.readingRunable=readingRunable;
		this.readingThread=readingThread;
	}
	public void killClient(){
		Log.d("srDebuging","klients killed1");
		try{
			readingRunable.kill();
		}catch(Exception e){}
		Log.d("srDebuging","klients killed2");
		try{
			dataOutputStream.close();
		}catch(Exception e){}
		Log.d("srDebuging","klients killed3");
		try{
			outputStream.close();
		}catch(Exception e){}
		Log.d("srDebuging","klients killed4");
		try{
			socket.close();
		}catch(Exception e){}
		Log.d("srDebuging","klients killed5");
		try{
			readingThread.stop();
		}catch(Exception e){}
		Log.d("srDebuging","klients killed6");
	}
	public void sendData(String data){
		if(dataOutputStream!=null){
			try{
				data+=ApplicationClass.endCharacters;
				dataOutputStream.write(data.getBytes(ApplicationClass.charset));
				dataOutputStream.flush();
			}catch(Exception e){}
		}
	}
	int[]scores;
	public void resetScore(int numberOfTasksInRound) {
		/**
		 * score two dimension table for two player 
		 * -1 wrong
		 * 0 not answered yet
		 * 1 correct answer
		 * */
		scores=new int[numberOfTasksInRound];
		
	}
	public int getSumScore() {
		int tmp=0;
		for(int i=0; i<scores.length; i++){
			if(scores[i]>0)
				tmp+=scores[i];
		}
		return tmp;
	}
	public void setScores(int currentTaskPosition, int i) {
		scores[currentTaskPosition]=i;
	}
	/**
	 * @return in score i have wrong answer|not answer|correct answer|and player id
	 * it is 1dimensional table four fields 1*4
	 * */
	public int[] getScoresTable(){
		int []scorT=new int[4];
		scorT[0]=0;
		scorT[1]=0;
		scorT[2]=0;
		scorT[3]=playerId;
		for(int i=0; i<scores.length; i++){
			if(scores[i]==-1)
				scorT[0]++;
			else if(scores[i]==0)
				scorT[1]++;
			else if(scores[i]==1)
				scorT[2]++;
		}
		
		return scorT;
	}
	public void setNickname(String nickname) {
		this.nickname=nickname;
	}
}
