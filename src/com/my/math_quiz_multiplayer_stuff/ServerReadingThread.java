
package com.my.math_quiz_multiplayer_stuff;

import java.io.DataInputStream;
import java.io.InputStream;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.my.math_quiz.ApplicationClass;

class ServerReadingThread implements Runnable{

	/**
	 * This class is for reading data from server and then send message to TCPIPClient
	 * */
	/**
	 * XXX-......................................
	 * first 3 is type
	 * then is message text
	 * */
	
	
	private boolean work;
//	private InputStream inputSream;
	DataInputStream dataInputStream;
	String readingBuffer="";
	public ServerReadingThread(InputStream inputSream){
		work=true;
		dataInputStream= new DataInputStream(inputSream);
	}
	
	public void kill(){
		work=false;
		
		try{
			dataInputStream.close();
		}catch(Exception e){
			dataInputStream=null;
		}
	}
	@Override
	public void run() {
		//TODO this looper stuff is not good yet
//		Looper.prepare();
		Looper.prepare();
		while(work==true&&dataInputStream!=null){
			try{
				byte[] info=new byte[1024];
				Log.d("clDebuging","STARTreading");
				int number= dataInputStream.read(info);
				Log.d("clDebuging","number on reading:"+number);
				if(number==-1){
					work=false;
					dataInputStream.close();
//					Log.d("clDebuging","before toast:"+number);
//					Toast.makeText(ApplicationClass.applicationContext, "Connection to servere lose", Toast.LENGTH_SHORT).show();

					Message tmp=TCPIPClient.handler.obtainMessage();
					tmp.what=1009;
					TCPIPClient.handler.sendMessage(tmp);
				
				}else{
					readingBuffer+=new String(info).substring(0,number);
					Log.d("bufferr",readingBuffer);
					int possition=readingBuffer.indexOf(ApplicationClass.endCharacters);
					while(possition>-1){
						String line=readingBuffer.substring(0,possition);
						readingBuffer=readingBuffer.substring(possition+ApplicationClass.endCharacters.length());
						
						if(line!=null){
							Message tmp=TCPIPServer.handler.obtainMessage();
							tmp.what=Integer.parseInt(line.substring(0,3));
							tmp.obj=line.substring(3);
							Log.d("line","what: "+tmp.what+"obj: "+tmp.obj);
							TCPIPClient.handler.sendMessage(tmp);
						}
						possition=readingBuffer.indexOf(ApplicationClass.endCharacters);
					}
				}
			}catch(Exception e){
				Log.d("clDebuging","exception while reading"+e);
			}
			Log.d("clDebuging","ENDreading");
//			Looper.loop();
		}
	}

}
