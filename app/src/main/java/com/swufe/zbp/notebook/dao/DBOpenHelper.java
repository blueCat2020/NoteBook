package com.swufe.zbp.notebook.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION=2;	//已经做出更新
	private static final String DBNAME="notebook.db";

	public DBOpenHelper(Context context){
		super(context,DBNAME,null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create  table tb_note " +
				"(id INTEGER  PRIMARY KEY AUTOINCREMENT  NOT NULL,noteType varchar(20),title varchar(200)," +
				"context Text,year int,month int,day int,clock int )");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void droptable(SQLiteDatabase db){
		db.execSQL("drop table tb_note");
	}
}
