
package com.my.math_quiz.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

import com.my.math_quiz.ApplicationClass;

public class DatabaseHelper  extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 11;
	public static final String DATABASE_NAME = "math_quiz.db";

	private SQLiteDatabase db; 
	private SQLiteStatement stmtInsertScoreData;
	private SQLiteStatement stmtInsertLevelData;
	
	private boolean wasDatabaseJustCreated=false;
	public DatabaseHelper() {
		super(ApplicationClass.applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
		db=this.getWritableDatabase();
		stmtInsertScoreData=db.compileStatement(QUERY_FOR_INSER_SCORE_DATA);
		stmtInsertLevelData=db.compileStatement(QUERY_FOR_INSER_LEVEL_DATA);

		
		if(wasDatabaseJustCreated){
			setAllLevelsToNotOpend();
		}
	}
	private void setAllLevelsToNotOpend(){
		//I insert into new DB false for all 20 levels (if one level was already opened I marked false) 
		Log.d("savingDB","adding to db0");
		 for(int i=0; i<ApplicationClass.NUMBER_OF_LEVELS; i++){
			 insertLevelDataToDatabase(i);
		 }
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(SQL_CREATE_LEVEL_DATA);
		 db.execSQL(SQL_CREATE_SCORE_DATA);
		 wasDatabaseJustCreated=true;
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_LEVEL_DATA);
		db.execSQL(SQL_DELETE_SCORE_DATA);
		onCreate(db);		
		
	}
	@Override
	public void close(){
		db.close();
		try{super.clone();}catch(Exception e){}
	}
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String REAL_TYPE = " REAL";
	private static final String NUMERIC_TYPE = " NUMERIC";
	private static final String COMMA_SEP = ",";

	
	public abstract class FeedLevelData implements BaseColumns{
		public static final String TABLE_NAME="leveldata";
		public static final String COLUMN_LEVEL_ID="levelid";
		public static final String COLUMN_WAS_LEVEL_ALREADY_OPEND="wasopend";
	}
	
	public abstract class FeedScoreData implements BaseColumns{
		public static final String TABLE_NAME="scoredata";
		public static final String COLUMN_SCORE_ID="scoreid";
		public static final String COLUMN_LEVEL_ID="levelid";		
		public static final String COLUMN_NUMBER_OF_TESTS="numberoftests";
		public static final String COLUMN_TIME_SPENT="spenttime";
		public static final String COLUMN_SCORE_ACHIVED="scoreachived";
		public static final String COLUMN_PLAYING_DATE="date";
	}
	
	private static final String SQL_CREATE_LEVEL_DATA =
		    "CREATE TABLE " + FeedLevelData.TABLE_NAME + " (" +
		    FeedLevelData.COLUMN_LEVEL_ID + INTEGER_TYPE+" PRIMARY KEY,"+// AUTOINCREMENT," +
		    FeedLevelData.COLUMN_WAS_LEVEL_ALREADY_OPEND+NUMERIC_TYPE +" DEFAULT  0"+
		    " )";
	
	private static final String SQL_CREATE_SCORE_DATA =
		    "CREATE TABLE " + FeedScoreData.TABLE_NAME + " (" +
		    FeedScoreData.COLUMN_SCORE_ID + INTEGER_TYPE+" PRIMARY KEY AUTOINCREMENT," +
		    FeedScoreData.COLUMN_LEVEL_ID+ INTEGER_TYPE + COMMA_SEP +
		    FeedScoreData.COLUMN_NUMBER_OF_TESTS+INTEGER_TYPE+ COMMA_SEP+
		    FeedScoreData.COLUMN_TIME_SPENT+INTEGER_TYPE+ COMMA_SEP+
		    FeedScoreData.COLUMN_SCORE_ACHIVED+INTEGER_TYPE+ COMMA_SEP+
		    FeedScoreData.COLUMN_PLAYING_DATE+NUMERIC_TYPE+
		    " )";
	
	public static final String QUERY_FOR_INSER_LEVEL_DATA="INSERT INTO "+FeedLevelData.TABLE_NAME+" ("+
			FeedLevelData.COLUMN_LEVEL_ID+
			" ) VALUES (?)";
	
	
	public static final String QUERY_FOR_INSER_SCORE_DATA="INSERT INTO "+FeedScoreData.TABLE_NAME+" ("+
			FeedScoreData.COLUMN_LEVEL_ID+", "+
			FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+
			FeedScoreData.COLUMN_TIME_SPENT+", "+
			FeedScoreData.COLUMN_SCORE_ACHIVED+", "+
			FeedScoreData.COLUMN_PLAYING_DATE+
			" ) VALUES (?,?,?,?,?)";
	
	
	private void insertLevelDataToDatabase(int level){
		Log.d("whatNull","isS:"+stmtInsertLevelData);
		 stmtInsertLevelData.bindString(1, level+"");
		 stmtInsertLevelData.execute();
		 stmtInsertLevelData.clearBindings();
	}
	
	
	public long insertScoreData(LevelEntity entity){
		Log.d("inserting","inserting score data in helper");
		stmtInsertScoreData.bindString(1, entity.getLevel()+"");
		stmtInsertScoreData.bindString(2, entity.getNumberOfGames()+"");
		stmtInsertScoreData.bindString(3, entity.getTimeInMIliseconds()+"");
		stmtInsertScoreData.bindString(4, entity.getScore()+"");
		stmtInsertScoreData.bindString(5, entity.getDate().getTime()+"");
		long id=stmtInsertScoreData.executeInsert();
		stmtInsertScoreData.clearBindings();
		return id;
	}
	
	
	private static final String SQL_DELETE_LEVEL_DATA = "DROP TABLE IF EXISTS " + FeedLevelData.TABLE_NAME;
	private static final String SQL_DELETE_SCORE_DATA = "DROP TABLE IF EXISTS " + FeedScoreData.TABLE_NAME;
	
	public static final String QUERY_FOR_ALL_LEVELDATAS="SELECT * FROM "+FeedLevelData.TABLE_NAME+" ORDER BY "+FeedLevelData.COLUMN_LEVEL_ID;
//	//TODO
//	public static final String QUERY_FOR_BEST_SCORE_AT_ALL_LEVELS="SELECT * FROM "+FeedScoreData.TABLE_NAME+" ORDER BY "+FeedScoreData.COLUMN_LEVEL_ID;
	public static final String QUERY_FOR_BEST_SCORE_AT_ALL_LEVELS=
		"SELECT "+
			"s."+FeedScoreData.COLUMN_LEVEL_ID+", "+	
			"s."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+	
			"s."+FeedScoreData.COLUMN_SCORE_ACHIVED+", "+
			"MIN("+"t."+FeedScoreData.COLUMN_TIME_SPENT+") AS "+FeedScoreData.COLUMN_TIME_SPENT+" "+
		"FROM ( "+					
			"SELECT "+
				"tt."+FeedScoreData.COLUMN_LEVEL_ID+", "+	
				"tt."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+
				"MAX("+"tt."+FeedScoreData.COLUMN_SCORE_ACHIVED+") AS "+FeedScoreData.COLUMN_SCORE_ACHIVED+" "+
			"FROM "+FeedScoreData.TABLE_NAME +" tt "+
				"GROUP BY "+
					"tt."+FeedScoreData.COLUMN_LEVEL_ID+", "+
					"tt."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+" "+
			  ") s "+
		"INNER JOIN "+FeedScoreData.TABLE_NAME +" t ON "+
			"s."+FeedScoreData.COLUMN_LEVEL_ID+"="+"t."+FeedScoreData.COLUMN_LEVEL_ID+" AND " +
			"s."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+"="+"t."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+" AND "+
			"s."+FeedScoreData.COLUMN_SCORE_ACHIVED+"="+"t."+FeedScoreData.COLUMN_SCORE_ACHIVED+" "+
		"GROUP BY "+
			"s."+FeedScoreData.COLUMN_LEVEL_ID+", "+
			"s."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+
			"s."+FeedScoreData.COLUMN_SCORE_ACHIVED
	;

	
	public Cursor getCursorFromAllLevelDatas(){
		checkIfIsNDBull();
		return db.rawQuery(DatabaseHelper.QUERY_FOR_ALL_LEVELDATAS,null);
	}
	
	public Cursor getCursorFromBestScoresFromAllLevels(){
		checkIfIsNDBull();
		return db.rawQuery(DatabaseHelper.QUERY_FOR_BEST_SCORE_AT_ALL_LEVELS,null);
	}
	public Cursor getCursorFromBestScoresFromSpecificLevel(int level) {
		checkIfIsNDBull();
		return db.rawQuery(
		"SELECT "+
			"s."+FeedScoreData.COLUMN_LEVEL_ID+", "+	
			"s."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+	
			"s."+FeedScoreData.COLUMN_SCORE_ACHIVED+", "+
			"MIN("+"t."+FeedScoreData.COLUMN_TIME_SPENT+") AS "+FeedScoreData.COLUMN_TIME_SPENT+" "+
		"FROM ( "+					
			"SELECT "+
				"tt."+FeedScoreData.COLUMN_LEVEL_ID+", "+	
				"tt."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+
				"MAX("+"tt."+FeedScoreData.COLUMN_SCORE_ACHIVED+") AS "+FeedScoreData.COLUMN_SCORE_ACHIVED+" "+
			"FROM "+FeedScoreData.TABLE_NAME +" tt "+
				"GROUP BY "+
					"tt."+FeedScoreData.COLUMN_LEVEL_ID+", "+
					"tt."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+" "+
			  ") s "+
		"INNER JOIN "+FeedScoreData.TABLE_NAME +" t ON "+
			"s."+FeedScoreData.COLUMN_LEVEL_ID+"="+"t."+FeedScoreData.COLUMN_LEVEL_ID+" AND " +
			"s."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+"="+"t."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+" AND "+
			"s."+FeedScoreData.COLUMN_SCORE_ACHIVED+"="+"t."+FeedScoreData.COLUMN_SCORE_ACHIVED+" "+
		"WHERE s."+FeedScoreData.COLUMN_LEVEL_ID+ "="+level+" "+
		"GROUP BY "+
			"s."+FeedScoreData.COLUMN_LEVEL_ID+", "+
			"s."+FeedScoreData.COLUMN_NUMBER_OF_TESTS+", "+
			"s."+FeedScoreData.COLUMN_SCORE_ACHIVED
				,null);
	}
	public void setLevelDataToAlreadyOpend(int level){
		ContentValues values = new ContentValues();
		values.put(FeedLevelData.COLUMN_WAS_LEVEL_ALREADY_OPEND, 1);
		String selection = FeedLevelData.COLUMN_LEVEL_ID + " = ?";
		String[] selectionArgs = {level+"" };
		
		checkIfIsNDBull();
		db.update(
				FeedLevelData.TABLE_NAME,
			    values,
			    selection,
			    selectionArgs);
	}
		
	public static final String QUERY_FOR_ALL_LEVELS="SELECT "+
			FeedLevelData.TABLE_NAME+"."+FeedLevelData.COLUMN_LEVEL_ID+", "+
			FeedLevelData.TABLE_NAME+"."+FeedLevelData.COLUMN_WAS_LEVEL_ALREADY_OPEND+", "+
			" FROM "+FeedLevelData.TABLE_NAME;
	
//	public static final String QUERY_FOR_ALL_TAGS="SELECT "+
//			FeedTag.TABLE_NAME+"."+FeedTag.COLUMN_TAG_COLOR+", "+
//			FeedTag.TABLE_NAME+"."+FeedTag.COLUMN_TAG_ID+", "+
//			FeedTag.TABLE_NAME+"."+FeedTag.COLUMN_TAG_NAME+", "+
//			"COUNT ("+FeedAlbumTagLinker.TABLE_NAME+"."+FeedAlbumTagLinker.COLUMN_ALBUM_ID+
//			") AS "+FeedTag.EXTRA_NUMBER_OF_ALBUMS_WITH_THAT_TAG+
//			" FROM "+FeedTag.TABLE_NAME+"  LEFT JOIN "+ FeedAlbumTagLinker.TABLE_NAME +" ON ("+
//			FeedTag.TABLE_NAME+"."+FeedTag.COLUMN_TAG_ID+"="+FeedAlbumTagLinker.TABLE_NAME+"."+FeedAlbumTagLinker.COLUMN_TAG_ID+") " +
//					"GROUP BY "+FeedTag.TABLE_NAME+"."+FeedTag.COLUMN_TAG_ID
//					;
	private void checkIfIsNDBull(){
		if(db==null){
			db=this.getWritableDatabase();
		}
	}

	
}
