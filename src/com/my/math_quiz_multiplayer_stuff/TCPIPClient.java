
package com.my.math_quiz_multiplayer_stuff;

import java.io.DataOutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.my.math_quiz.ApplicationClass;
import com.my.math_quiz.R;
import com.my.math_quiz.utils.Equations;
import com.my.math_quiz.utils.Task;

public class TCPIPClient {
	public static final char speratorCharacter=5;
	/**
	 * This is listeners for all action that client activity need before game
	 * */
	public interface TCPIPClientListenerBeforeGame{
		public void onCommandToStartGame();
		public void onRequestingClientNickname();
		public void onServerStoped();
		public void onConnection(boolean wasSuccsessful);
	}
	/**
	 * This is listeners for all action that client activity need in game
	 * */
	public interface TCPIPClientListenerInGame{
		public void onRequestingClientNickname();
		public void onNumberOfGames(int numberOfGames);
		public void onRequestToClearAllDataFromOldTasks();
		public void onRequestToDisplayEndScreen(String text);
		public void onRequestToDisplayGameScreen();
		public void onUserScoresRecived(int userId,int score);
		public void onUserDataRecived(int userId,String nickname);
		public void onCommandToDisplaySpecificTask(int taskNumber);
		public void onCommandToDisplayCorrectAnswer(int taskNumber);
		public void onSelectedAnswerOfOtherUser(int task, int userId,int answer);
		public void onTaskReciveFromServer(int taskNumber,Task task);
		public void onServerStoped();
		
	}
	
	static String ipAdress;
	static int port;
	static WeakReference<TCPIPClientListenerBeforeGame> listenerBG;
	static WeakReference<TCPIPClientListenerInGame> listenerIG;

	static Socket socket=new Socket();;
	static DataOutputStream dataOutputStream;
	static ServerReadingThread serverReadingRunable;
	static Thread serverReadingThread;
	static Thread threadForOpeningConnection;
//	static RunableForOpeningConnection runableForConnectionOpening;
/**
 * This method save port number and ipAdress to use it you must call connectToServer or reconnectToServer method
 *  @param ipAdresss is ipAdress of server, to which you want to connect
 *  @param port is the port number of server, to which you want to connect
 * */
	public static void setIpAdressAndPort(String ipAdresss,int portt){
		ipAdress=ipAdresss;
		port=portt;
	}
	
	/**This method kill connection to server
     * first is trying to close socket then killing runableFor reading class 
     * and then the tread whit method stop and also close output stream
     * 
     */
	public static void killConnection(){
			try{
				threadForOpeningConnection.stop();
			}catch(Exception e){}
		  try{
			  serverReadingRunable.kill();
			}catch(Exception e){}
			try{
				dataOutputStream.close();
			}catch(Exception e){}
			try{
				socket.close();
			}catch(Exception e){}
			try{
				serverReadingThread.stop();
			}catch(Exception e){}
    }
	
	/**
	 * This method reconnect this device to server
	 * First is trying to kill old connection if is any then is setting socket and thread for reading
	 * */
	public static void reconnectToServer(){
		killConnection();
		new OpeningConnection().execute();
//		try{
//			  runableForConnectionOpening=new RunableForOpeningConnection();
//			  threadForOpeningConnection=new Thread(runableForConnectionOpening);
//			  threadForOpeningConnection.start();
//		}catch(Exception e){Log.d("clDebuging","error on trying connecting1: "+e);}
	}

	
	
//	//I must add this to background because android don't allow to do this in main thread
//	 private static class RunableForOpeningConnection implements Runnable
//	 {
//		
//		 public RunableForOpeningConnection()
//		 {		 }
//        public  void run()
//        {
//        	Looper.prepare();
//        	try{
//        		socket=new Socket();
////        		socket.bind(null);
//        	 socket.connect(new InetSocketAddress(ipAdress, port));
////   			 socket = new Socket(ipAdress, port);
//        	
//        		
////        		SocketAddress sockaddr = new InetSocketAddress("192.168.1.10", 1234);
////            	socket=new Socket();//adrr,Integer.parseInt(IpPort));
////            	socket.connect(sockaddr,5000);
//            	
//   		     dataOutputStream = new DataOutputStream(socket.getOutputStream());
//   		     serverReadingRunable=new ServerReadingThread(socket.getInputStream());
//   		     serverReadingThread=new Thread(serverReadingRunable);
//   		     serverReadingThread.start();
//   		     
//   		
//   		     if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onConnection(true);
//   			
//        	}catch(Exception e){
//        		  if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onConnection(false);
//        		Log.d("clDebuging","error on trying connecting2: "+e);
//        	}
//        }
//     }
//	
//	
	//I must add this to background because android don't allow to do this in main thread
	 private static class OpeningConnection extends AsyncTask<Void, Void, Boolean> {
	        @Override
	        protected Boolean doInBackground(Void... args) {
	        	try{
		        	 socket=new Socket();
  	        	     socket.connect(new InetSocketAddress(ipAdress, port));
		   		     dataOutputStream = new DataOutputStream(socket.getOutputStream());
		   		     serverReadingRunable=new ServerReadingThread(socket.getInputStream());
		   		     serverReadingThread=new Thread(serverReadingRunable);
		   		     serverReadingThread.start();
		   		     return true;
	        	}catch(Exception e){
	        		Log.d("clDebuging","error on trying connecting2: "+e);
	        	}
	        	return false;
	 		}
	          
	        @Override
	        protected void onPostExecute(Boolean result) {
	        	  if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onConnection(result);
	        }

	        @Override
	        protected void onPreExecute() {}

	        @Override
	        protected void onProgressUpdate(Void... values) {}
	    }
	
	
	
	
	
	/**
	 * This method functionality is the same as of method reconnectToServer
	 * So first is trying to killConnection and the creating new connection to server
	 */
	public static void connectToServer(){
		 reconnectToServer();
	}
	 /**
     * This method save TCPIPServerListener before game as week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void setTCPIPClientListener(TCPIPClientListenerBeforeGame list){
    	listenerBG=new WeakReference<TCPIPClientListenerBeforeGame>(list);
    }
    /**
     * This method save TCPIPServerListener in game as week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void setTCPIPClientListener(TCPIPClientListenerInGame list){
    	listenerIG=new WeakReference<TCPIPClientListenerInGame>(list);
    }
    /**
     * This method remove TCPIPServerListener before game from week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void removeTCPIPClientListener(TCPIPClientListenerBeforeGame list){
    	listenerBG=null;
    }
    /**
     * This method remove TCPIPServerListener in game from week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void removeTCPIPClientListener(TCPIPClientListenerInGame list){
    	listenerIG=null;
    }
	/**
     * This is handler which handle all messages from reading thread from server
     * This handler tigers the listener methods
     * */
	  public static Handler handler=new Handler(new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				Log.d("reciveClient","I recive: "+msg.obj);
				
				String[] data=null;
				if(msg.what!=1009)
					data=((String)msg.obj).split(Pattern.quote(speratorCharacter+""));
				
				try{
					Log.d("afterSplit","-->"+data[0]);
				}catch(Exception e){}
				
			
				switch(msg.what){
					case 1:
						//we receive data of one task
					    //|taskNumber|expressiont|answer1|answer2|answer3|answer4|correctNumber
						Log.d("reciveTask","her correct valueIS "+Integer.parseInt(data[6]));
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onTaskReciveFromServer(
								Integer.parseInt(data[0]),
								new Task(new Equations(data[1],Integer.parseInt(data[6])),
										new int[]{Integer.parseInt(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4]),Integer.parseInt(data[5])} ));
						break;
					case 2:
						//we receive answer of one user
					    //|taskNumber|userId|selectedAnswer|
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onSelectedAnswerOfOtherUser(Integer.parseInt(data [0]),Integer.parseInt(data [1]),Integer.parseInt(data [2]));
						break;
					case 3:
						//we receive signal to display correct answer
					    // |taskNumber|  {this is signal to display correct result for specific task}
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onCommandToDisplayCorrectAnswer(Integer.parseInt(data [0]));
						break;
					case 4:
						//we receive signal to switch to specific task probably next
					    //|taskNumber|  {this is signal to switch to specific task in general to new one}
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onCommandToDisplaySpecificTask(Integer.parseInt(data [0]));
						break;
					case 5:
						//we receive nickname of one user
					    //|userID|nickname|
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onUserDataRecived(Integer.parseInt(data [0]),data [1]);
						break;
					case 6:
						//we receive score of one user
					    //|userId|score|
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onUserScoresRecived(Integer.parseInt(data [0]),Integer.parseInt(data [1]));
						break;
					case 7:
						//we receive command to display end screen
					    //{text to display}
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestToDisplayEndScreen(data [0]);
						break;
					case 8:
						//we receive to clear all data of old tasks
					    //{nothing else just command to clear all data from odl tasks}
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestToClearAllDataFromOldTasks();
						break;
				//here was notification that server stoped
					case 10:
						//we receive request for nickname
					    //{request for nickname}
						if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onRequestingClientNickname();
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestingClientNickname();
						break;
					case 11:
						//we receive number of games
					    // |numberOfGames|
						Log.d("iget",data[0]+"dd");
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onNumberOfGames(Integer.parseInt(data[0]));
						break;
					case 12:
						//we receive command for starting game
						if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onCommandToStartGame();
						break;
					case 13:
						//we receive command for starting game
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestToDisplayGameScreen();
						break;
					case 1009://mean that we lose connection with server
						killConnection();//just to be sure to kill all stuff from old server
						//we receive number of games
					    // |numberOfGames|
						if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onServerStoped();
						if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onServerStoped();
						Toast.makeText(ApplicationClass.applicationContext, ApplicationClass.applicationContext.getString(R.string.muConTOServerLose), Toast.LENGTH_SHORT).show();
						break;
					default: break;
				}
				return false;
			}

	  });
	  
	  public static void sendRequestForTaskDescription(int taskNumber){
		// id=1
		//|taskID| request data for specific taskc
		  String data="001"+taskNumber;
	      sendData(data);
	  }
	  public static void sendSelectedAnswer(int taskNumber,int selectedAnswer){
		// id=2
		//|taskId|answer| {selected answer at one task}
		  String data="002"+taskNumber+speratorCharacter+selectedAnswer;
	      sendData(data);
	  }
	  public static void sendRequestForNumberOfPlayers(){
		// id=3
	    //{request for numbet of players}
		  String data="003";
	      sendData(data);
	  }
	  //here was notification that client stoped
	  public static void sendRequestForPlayerNickname(int playerID){
		// id=5
	    // playerId {request for nickname}
		  String data="005"+playerID;
	      sendData(data);
	  }
	  public static void sendRequestForNumberOfGames(){
		// id=6
	    //{request for number of games}
		  String data="006";
	      sendData(data);
	  }
	  public static void sendNickname(String nickname){
			// id=7
		    //{sending my nickname}
			  String data="007"+nickname;
		      sendData(data);
		  }
	  
	  private static void sendData(String data){
			if(dataOutputStream!=null){
				try{
					data+=ApplicationClass.endCharacters;
					dataOutputStream.write(data.getBytes(ApplicationClass.charset));
					dataOutputStream.flush();
				}catch(Exception e){}
			}
		}
}
