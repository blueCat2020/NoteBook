
package com.swufe.zbp.notebook.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swufe.zbp.notebook.model.Note;

public class NoteDAO {
//_id integer primary key,no integer autoincrement,note varchar(200))
	/**
	 * 创建DBOpenHelper对象
	 */
	private DBOpenHelper helper;
	/**
	 * 创建SQLiteDatabase对象
	 */
	private SQLiteDatabase db;
	public NoteDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);
		// 初始化DBOpenHelper对象
	}
	/**
	 * 添加便签信息
	 *
	 * @param tb_note
	 */
	public void add(Note tb_note) {

		db = helper.getWritableDatabase();
		// 初始化SQLiteDatabase对象
		db.execSQL("insert into tb_note (id,noteType,title,context,year ,month,day,clock) values (?,?,?,?,?,?,?,?)", new Object[] {
				tb_note.getId(), tb_note.getNoteType(),tb_note.getTitle(),tb_note.getContext(),tb_note.getYear(),tb_note.getMonth(),tb_note.getDay(),tb_note.getClock()})
		;
		db.close();
		// 执行添加便签信息操作
	}

	/**
	 * 更新便签信息
	 *
	 * @param tb_note
	 */
	public void update(Note tb_note) {

		db = helper.getWritableDatabase();
		// 初始化SQLiteDatabase对象
		db.execSQL("update tb_note set title = ?,context=?,year=?,month=?,day=?,clock=? where id = ? ", new Object[] {
				tb_note.getTitle(),tb_note.getContext(),tb_note.getYear(),tb_note.getMonth(),tb_note.getDay(),tb_note.getClock(),tb_note.getId()});
		// 执行修改便签信息操作
		db.close();
	}

	/**
	 * 查找便签信息
	 *
	 * @param title
	 * @return
	 */
	public List<Note> findByTitle(String title) {

		List<Note> list_note = new ArrayList<Note>();
		// 创建集合对象
		db = helper.getReadableDatabase();
		// 初始化SQLiteDatabase对象

		Cursor cursor = db.rawQuery("select * from tb_note where title like ?",
				new String[] {"%" +  title+ "%" });
		// 获取所有便签信息
		while (cursor.moveToNext())
		{
			// 将遍历到的便签信息添加到集合中
			Note note=null;
			long noteId =cursor.getLong(cursor.getColumnIndex("id"));
			String noteType=cursor.getString(cursor.getColumnIndex("noteType"));
			String noteTitle =cursor.getString(cursor.getColumnIndex("title"));
			String noteContext =cursor.getString(cursor.getColumnIndex("context"));
			int noteYear =cursor.getInt(cursor.getColumnIndex("year"));
			int noteMonth=cursor.getInt(cursor.getColumnIndex("month"));
			int noteDay=cursor.getInt(cursor.getColumnIndex("day"));
			int noteClock=cursor.getInt(cursor.getColumnIndex("clock"));
			note=new Note(noteType,noteTitle ,noteContext ,noteYear,noteMonth,noteDay,noteClock,noteId);
			list_note.add(note);
		}
		cursor.close();
		// 遍历所有的便签信息
		db.close();
		return list_note;
		// 返回集合
		// 如果没有信息，则返回null
	}
	/**
	 * 刪除某条便签信息
	 *
	 * @param id
	 */
	public void detele(long id) {


			StringBuffer sb = new StringBuffer();
			db = helper.getWritableDatabase();
			// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from tb_note where id =?",
					new Object[]{id});
			db.close();
		// 判断是否存在要删除的id
	}
	/**
	 * 刪除便签信息
	 *
	 * @param ids
	 */
	public void detele(Long[] ids) {
		if (ids.length > 0){
			StringBuffer sb = new StringBuffer();
			// 创建StringBuffer对象
			for (int i = 0; i < ids.length; i++){
				sb.append('?').append(',');
				// 将删除条件添加到StringBuffer对象中
			}// 遍历要删除的id集合
			sb.deleteCharAt(sb.length() - 1);
			// 去掉最后一个“,“字符
			db = helper.getWritableDatabase();
			// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from tb_note where id in (" + sb + ")",
					ids);
			db.close();
		}// 判断是否存在要删除的id
	}
	/**
	 * 获取所有便签信息
	 * @return
	 */
	public List<Note> getScrollData() {
		List<Note> lisTb_note = new ArrayList<Note>();
		// 创建集合对象
		db = helper.getReadableDatabase();
		// 初始化SQLiteDatabase对象

		Cursor cursor = db.rawQuery("select * from tb_note order by year desc,month desc,day desc,clock desc",
				new String[] { });
		// 获取所有便签信息
		while (cursor.moveToNext())
		{
			// 将遍历到的便签信息添加到集合中
			Note note=null;
			long noteId =cursor.getLong(cursor.getColumnIndex("id"));
			String noteType=cursor.getString(cursor.getColumnIndex("noteType"));
			String noteTitle =cursor.getString(cursor.getColumnIndex("title"));
			String noteContext =cursor.getString(cursor.getColumnIndex("context"));
			int noteYear =cursor.getInt(cursor.getColumnIndex("year"));
			int noteMonth=cursor.getInt(cursor.getColumnIndex("month"));
			int noteDay=cursor.getInt(cursor.getColumnIndex("day"));
			int noteClock=cursor.getInt(cursor.getColumnIndex("clock"));
			note=new Note(noteType,noteTitle ,noteContext ,noteYear,noteMonth,noteDay,noteClock,noteId);
			lisTb_note.add(note);
		}
		cursor.close();
		// 遍历所有的便签信息
		db.close();
		return lisTb_note;
		// 返回集合
	}

	/**
	 * 获取某类的便签信息
	 * @param noteType
	 * @return
	 */
	public List<Note> getScrollData(String  noteType) {
		List<Note> lisTb_note = new ArrayList<Note>();
		// 创建集合对象
		db = helper.getWritableDatabase();
		// 初始化SQLiteDatabase对象

		Cursor cursor = db.rawQuery("select * from tb_note where noteType=? order by year desc,month desc,day desc,clock desc",
				new String[] { String.valueOf(noteType)});
		// 获取所有便签信息
		while (cursor.moveToNext())
		{
			// 将遍历到的便签信息添加到集合中
			Note note=null;
			long noteId =cursor.getLong(cursor.getColumnIndex("id"));
			String noteTitle =cursor.getString(cursor.getColumnIndex("title"));
			String noteContext =cursor.getString(cursor.getColumnIndex("context"));
			int noteYear =cursor.getInt(cursor.getColumnIndex("year"));
			int noteMonth=cursor.getInt(cursor.getColumnIndex("month"));
			int noteDay=cursor.getInt(cursor.getColumnIndex("day"));
			int noteClock=cursor.getInt(cursor.getColumnIndex("clock"));
			note=new Note(noteType,noteTitle ,noteContext ,noteYear,noteMonth,noteDay,noteClock,noteId);
			lisTb_note.add(note);
		}
		cursor.close();
		// 遍历所有的便签信息
		db.close();
		return lisTb_note;
		// 返回集合
	}

	/**\
	 *获取某时间段的便签信息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Note> getScrollData(Date startTime, Date endTime) {
		List<Note> lisTb_note = new ArrayList<Note>();
		// 创建集合对象
		db = helper.getReadableDatabase();
		// 初始化SQLiteDatabase对象
		Calendar start= Calendar.getInstance();
		Calendar end= Calendar.getInstance();
		start.setTime(startTime);
        end.setTime(endTime);
		// 获取所有便签信息
		Cursor cursor = db.rawQuery("select * from tb_note where year BETWEEN ? AND ?  and  month BETWEEN ? AND ? and  day BETWEEN ? AND ? and year order by year desc,month desc,day desc,clock desc",
				new String[] { String.valueOf(start.get(Calendar.YEAR)),String.valueOf(end.get(Calendar.YEAR)),
						String.valueOf(start.get(Calendar.MONTH)+1),String.valueOf(start.get(Calendar.MONTH)+1),
						String.valueOf(start.get(Calendar.DAY_OF_MONTH)),String.valueOf(start.get(Calendar.DAY_OF_MONTH))});
		while (cursor.moveToNext())
		{
			// 将遍历到的便签信息添加到集合中
			Note note=null;
			long noteId =cursor.getLong(cursor.getColumnIndex("id"));
			String noteType=cursor.getString(cursor.getColumnIndex("noteType"));
			String noteTitle =cursor.getString(cursor.getColumnIndex("title"));
			String noteContext =cursor.getString(cursor.getColumnIndex("context"));
			int noteYear =cursor.getInt(cursor.getColumnIndex("year"));
			int noteMonth=cursor.getInt(cursor.getColumnIndex("month"));
			int noteDay=cursor.getInt(cursor.getColumnIndex("day"));
			int noteClock=cursor.getInt(cursor.getColumnIndex("clock"));
			note=new Note(noteType,noteTitle ,noteContext ,noteYear,noteMonth,noteDay,noteClock,noteId);
			lisTb_note.add(note);
		}
		cursor.close();
		// 遍历所有的便签信息
		db.close();
		return lisTb_note;
		// 返回集合
	}
	/**
	 * 获取总记录数
	 *
	 * @return
	 */
	public long getCount() {
		db = helper.getReadableDatabase();
		// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select count(id) from tb_note ",new String[]{});
		// 获取便签信息的记录数
		if (cursor.moveToNext())
		{
			return cursor.getLong(0);
			// 返回总记录数
		}
		// 判断Cursor中是否有数据
		cursor.close();
		db.close();
		return 0;
		// 如果没有数据，则返回0
	}

	/**
	 * 获取便签最大编号
	 *
	 * @return
	 */
	public long getMaxId() {
		db = helper.getReadableDatabase();
		// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select max(id) from tb_note",new String[]{});
		// 获取便签信息表中的最大编号
		while (cursor.moveToLast()) {

			return cursor.getLong(0);
			// 获取访问到的数据，即最大编号
		}
		// 访问Cursor中的最后一条数据
		cursor.close();
		db.close();
		return 0;
		// 如果没有数据，则返回0
	}

	/**
	 * 清空用户数据
	 */
	public void deleteData(){
		db = helper.getWritableDatabase();
		db.execSQL("delete  from tb_note ",new Object[]{});
		db.close();
		// 初始化SQLiteDatabase对象
	}
}
