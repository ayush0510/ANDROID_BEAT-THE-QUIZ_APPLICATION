
package com.my.math_quiz_multiplayer_stuff;

import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.my.math_quiz.ApplicationClass;
import com.my.math_quiz.R;
import com.my.math_quiz.utils.Task;

public class TCPIPServer {
	
//TODO on start game I must end accepting new clients and on end game I must resume accepting new clients
	
	public static final char speratorCharacter=5;
	
	/**
	 * This is listeners for all action that server activity need before game start
	 * */
	public interface TCPIPServerListenerBeforeGame{
		/**
		 * This method tigger when we accept new client or when old client leave
		 * @param number the new number of clients
		 * @accepted boolean value is true if we accept new client, if we lose old client id this valuie false
		 * */
		public void onNumberOfClientsChanged(int number,boolean accepted);
		public void onSuccessfulStartedServer(boolean wasSuccessful);
	}
	/**
	 * This is listeners for all action that server activity need when game already run
	 * */
	public interface TCPIPServerListenerInGame{
		/**
		 * This method tigger when we accept new client or when old client leave
		 * @param number the new number of clients
		 * @accepted boolean value is true if we accept new client, if we lose old client id this valuie false
		 * */
		public void onNumberOfClientsChanged(int number,boolean accepted);
		public void onRequestForNumberOfGame();
		public void onRequestForPlayerNickname(int playerId);
		public void onRequestForNumberOfPlayers();
		public void onSelectedAnswerRecived(int taskNumber,int selectedAnsver,int clientId);
		public void onRequestForTaskDescription(int taskNumber);
		public void onPlayerNickname(String string, int playerId);
		
	}
    static HashMap<Integer,Client> clients=null;
    static WeakReference<TCPIPServerListenerBeforeGame> listenerBG;
    static WeakReference<TCPIPServerListenerInGame> listenerIG;
    static ClientAcceptorThread serverThreadRunable;
    static Thread serverThread;
    static ServerSocket socket;
    private static  int port = 21111;
   
    /**
     * This method save port number to use it you must call restart or start server method
     * @param portt is the port number which you want to set
     * */
    public static void setPort(int portt){
    	port=portt;
    }
    
    /**
     * This method restart server
     * First try to kill sever then create insance of clients
     * then create socket and thread in which accept clients 
     * Time out is set to 10000ms
     * */
    public static void restartTcpServer() {
    	Log.d("srDebuging","killing server");
    	killServer();
    	Log.d("srDebuging","server kiled");
    	clients=new HashMap<Integer, Client>();
    	try{
	    	socket=new ServerSocket(port);
	    	Log.d("srDebuging","start server on port: "+port);
	    	socket.setSoTimeout(10000);
	    	serverThreadRunable=new ClientAcceptorThread(socket);
	    	serverThread=new Thread(serverThreadRunable);
	    	serverThread.start();
	    	if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onSuccessfulStartedServer(true);
    	}catch(Exception e){
    		Log.d("srDebuging","error on trying start server: "+e);
    		if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onSuccessfulStartedServer(false);
    	}
    }
    /**
     * Method to start server,
     * is the same functionality like restartTcpServer
     * in booth way method try to kill server first just to avoid memory leaks and others problems
     * */
    public static void startTcpServer(){
    	restartTcpServer();
    }

    /**This method kill server
     * first is trying to kill all clients then server socket
     * then runable class and at leas the tread whit method stop */
    public static void killServer(){
    	if(clients!=null){
	    	for(Client client:clients.values()){
	    		client.killClient();
	    	}
    	}
    	Log.d("srDebuging","klientsss killed");
    	clients=null;
    	try{
			socket.close();
		}catch(Exception e){}
		try{
			serverThreadRunable.kill();
		}catch(Exception e){}
		try{
			serverThread.stop();
		}catch(Exception e){}
    }
    
    /**
     * This method save TCPIPServerListener before game as week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void setTCPIPServerListener(TCPIPServerListenerBeforeGame list){
    	listenerBG=new WeakReference<TCPIPServer.TCPIPServerListenerBeforeGame>(list);
    }
    /**
     * This method save TCPIPServerListener in game as week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void setTCPIPServerListener(TCPIPServerListenerInGame list){
    	listenerIG=new WeakReference<TCPIPServer.TCPIPServerListenerInGame>(list);
    }
    /**
     * This method remove TCPIPServerListener before game from week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void removeTCPIPServerListener(TCPIPServerListenerBeforeGame list){
    	listenerBG=null;
    }
    /**
     * This method remove TCPIPServerListener in game from week reference.
     * So user don't need to wory about memory leaks.
     * */
    public static void removeTCPIPServerListener(TCPIPServerListenerInGame list){
    	listenerIG=null;
    }
    
    /**
     * This is handler which handle all messages from accepting new clients thread
     * and form thread which read all data from clients 
     * This handler tigers the listener methods
     * */
    public static Handler handler=new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			Log.d("reciveServer","I recive: "+msg.obj);
			
			String[] data=null;
			int playerId=-8;
			Log.d("messageFromClient","messg: "+msg.obj);
			if(msg.what<1000){
				data=((ObjectForMessageFromClient)msg.obj).getText().split(Pattern.quote(speratorCharacter+""));
				playerId=((ObjectForMessageFromClient)msg.obj).clientId;
			}
			
			switch(msg.what){
				case 1:
					//we receive request data for specific task
				    //|taskID| request data for specific task
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestForTaskDescription(Integer.parseInt(data[0]));
					break;
				case 2:
					//we receive selected answer of one task
				    //|taskId|answer| {selected answer at one task}
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onSelectedAnswerRecived(Integer.parseInt(data[0]),Integer.parseInt(data[1]),playerId);
					break;
				case 3:
					//we receive request for number of players
				    //{request for number of players}
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestForNumberOfPlayers();
					break;
					//here was notification that client stoped
				case 5:
					//we receive request for nickname
				    // playerId {request for nickname}
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestForPlayerNickname(Integer.parseInt(data[0]));
					break;
				case 6:
					//we receive request for numberOfGames
				    //{request for number of games}
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onRequestForNumberOfGame();
					break;
				case 7:
					//we receive player nickname
				    //
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onPlayerNickname(data[0],playerId);
					break;
				case 1009: //mean that  that client is not accessible any more
					int clientId=(Integer)msg.obj;
					Client cli=clients.get(clientId);
					if(cli!=null){
						cli.killClient();
						clients.remove(clientId);
					}
					Toast.makeText(ApplicationClass.applicationContext,ApplicationClass.applicationContext.getString(R.string.muConnectionToClient)+ " "+clientId+" "+ApplicationClass.applicationContext.getString(R.string.muLose), Toast.LENGTH_SHORT).show();
					if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onNumberOfClientsChanged(clients.size(),false);
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onNumberOfClientsChanged(clients.size(),false);
					break;
				case 1010: //mean that we accept new client 
					Log.d("reciveServer","accepted client");
					Client cl=(Client)msg.obj;
					clients.put(cl.getPlayerId(),cl);
					if(listenerBG!=null&&listenerBG.get()!=null)listenerBG.get().onNumberOfClientsChanged(clients.size(),true);
					if(listenerIG!=null&&listenerIG.get()!=null)listenerIG.get().onNumberOfClientsChanged(clients.size(),true);
					break;
				default: break;
			}
			return false;
		}
	});
    public static void stopAcepptingNewClients(){
    	if(serverThreadRunable!=null)serverThreadRunable.stopAcepptingNewClients();
    }
    public static void startAcepptinNewClients(){
    	if(serverThreadRunable!=null)serverThreadRunable.startAcepptinNewClients();
    }
    
    public static HashMap<Integer,Client> getClients(){
    	return clients;
    }
    public static void sendTaskToAllClients(Task task,int taskNumber){
    	// id=1
	    //|taskNumber|expressiont|answer1|answer2|answer3|answer4|correctNumber
    	String data="001"+
    			taskNumber+speratorCharacter+
    			task.getSourceText()+speratorCharacter+
    			task.getAnswers()[0]+speratorCharacter+
    			task.getAnswers()[1]+speratorCharacter+
    			task.getAnswers()[2]+speratorCharacter+
    			task.getAnswers()[3]+speratorCharacter+
    			task.getCorrectAnswerValue();
    	sendDataToClients(data,-1);
    }
    public static void sendSelectdAnswerOfUserToOClients(int taskNumber,int userId,int selectedAnswer){
    	// id=2
	    //|taskNumber|userId|selectedAnswer|
    	String data="002"+
    			taskNumber+speratorCharacter+
    			userId+speratorCharacter+
    			selectedAnswer;
    	sendDataToClients(data,userId);
    }
    public static void sendSignalToDisplayCorrectAnsswer(int taskNumber){
    	// id=3
	    // |taskNumber|  {this is signal to display correct result for specific task}
    	String data="003"+taskNumber;
    	sendDataToClients(data,-1);
    }
    public static void sendSignalToSwitchToOtherTask(int taskNumber){
    	// id=4
	    //|taskNumber|  {this is signal to switch to specific task in general to new one}
    	String data="004"+taskNumber;
    	sendDataToClients(data,-1);
    }
    public static void sendUserDataToClients(int userId,String nickname){
    	// id=5
	    //|userID|nickname|
    	String data="005"+userId+speratorCharacter+nickname;
    	sendDataToClients(data,userId);
    }
    public static void sendUserScoreToClients(int userId,int score){
    	// id=6
	    //|userId|score|
    	String data="006"+userId+speratorCharacter+score;
    	sendDataToClients(data,userId);
    }
    public static void sendRequestToDisplayEndScreen(String textToDisplay){
    	// id=7
	    //{nothing else just command to display end screen}
    	String data="007"+textToDisplay;
    	sendDataToClients(data,-1);
    }
    public static void sendRequestToClearAllDataFromOldTasks(){
    	// id=8
	    //{nothing else just command to clear all data from odl tasks}
    	String data="008";
    	sendDataToClients(data,-1);
    }
  //here was notification that server stoped
    public static void requestClientsNickname(){
    	// id=10
	    //{request for nickname}
    	String data="010";
    	sendDataToClients(data,-1);
    }
    public static void sendNumberOfGames(int numberOfGames){
    	// id=11
	    //|numberOfGames|
    	String data="011"+numberOfGames;
    	sendDataToClients(data,-1);
    }
  //here server notifiy clients to switch to game
    public static void sendCommandToStartGame(){
    	// id=12
	    //{command for starting game}
    	String data="012";
    	sendDataToClients(data,-1);
    }
    public static void sendRequestToDisplayGameScreen(){
    	// id=13
	    //{nothing else just command to display end screen}
    	String data="013";
    	sendDataToClients(data,-1);
    }
    private static void sendDataToClients(String data,int userId){
    	if(clients!=null)
        	for(Client cl :clients.values()){
        		if(cl.getPlayerId()!=userId)
        			cl.sendData(data);
        	}
    }

	public static int getNumberOfClients() {
		if(clients==null)return 0;
		return clients.values().size();
	}
    
    
    
}