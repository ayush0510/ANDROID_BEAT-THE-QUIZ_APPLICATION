
package com.my.math_quiz_multiplayer_stuff;

import java.io.DataInputStream;
import java.io.InputStream;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.my.math_quiz.ApplicationClass;

class ClientReadingThread implements Runnable{

	/**
	 * This class is for reading data from client and then send message to TCPIPServer
	 * */
	/**
	 * First three characters will be number and that will tell me for what this message is and what is his meaning
	 * XXX-......................................
	 * first 3 is type
	 * then is message text
	 * */
	private boolean work;
//	private InputStream inputSream;
	DataInputStream dataInputStream;
	String readingBuffer="";
	
	int clientId;
	public ClientReadingThread(InputStream inputSream,int clientId){
		work=true;
//		this.inputSream=inputSream;
		dataInputStream= new DataInputStream(inputSream);
		this.clientId=clientId;
	}
	
	public void kill(){
//		Log.d("srDebuging","killing input buffer");
		work=false;
		try{
			dataInputStream.close();
		}catch(Exception e){
			dataInputStream=null;
			Log.d("srDebuging","killing input buffer error: "+e);
		}
		Log.d("srDebuging","killing input buffer end method");
	}
	@Override
	public void run() {
		Looper.prepare();
		while(work==true&&dataInputStream!=null){
			try{ 
				byte[] info=new byte[1024];
				Log.d("srDebuging","start reading");
				int number= dataInputStream.read(info);
				Log.d("srDebuging","number on reading:"+number);
				if(number==-1){
					work=false;
					dataInputStream.close();
					
					//message mean that client is not accessible
					Message tmp=TCPIPServer.handler.obtainMessage();
					tmp.what=1009;
					tmp.obj=clientId;
					TCPIPServer.handler.sendMessage(tmp); 
					
				}else{
					readingBuffer+=new String(info).substring(0,number);;
					int possition=readingBuffer.indexOf(ApplicationClass.endCharacters);
					while(possition>-1){
						String line=readingBuffer.substring(0,possition);
						readingBuffer=readingBuffer.substring(possition+ApplicationClass.endCharacters.length());
						if(line!=null){
							Message tmp=TCPIPServer.handler.obtainMessage();
							tmp.what=Integer.parseInt(line.substring(0,3));
							//we append id of client to message
							tmp.obj=new ObjectForMessageFromClient(clientId,line.substring(3));
							TCPIPServer.handler.sendMessage(tmp);
						}
						possition=readingBuffer.indexOf(ApplicationClass.endCharacters);
					}
				}
				
			}catch(Exception e){
				Log.d("srDebuging","error while reading from client: "+e);
			}
			Log.d("srDebuging","end reading");
		}
	}

}