package com.uhf.dao;

import java.util.ArrayList;
import java.util.List;

import com.uhf.util.DBHelper;
import com.uhf.vo.TUsers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDAOImpl {
	private DBHelper dbOpenHandler;

	public SQLiteDAOImpl(Context context) {
		this.dbOpenHandler = new DBHelper(context);
	}

	public void save(TUsers tusers) {// 插入记录
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();// 取得数据库操作
		db.execSQL("insert into t_users (username,pass) values(?,?)", new Object[] { tusers.getUsername(), tusers.getPass() });
		db.close();// 记得关闭数据库操作
	}

	public void delete(Integer id) {// 删除纪录
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("delete from t_users where id=?", new Object[] { id.toString() });
		db.close();
	}

	public void update(TUsers tusers) {// 修改纪录
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("update t_users set username=?,pass=? where" + " id=?", new Object[] { tusers.getUsername(), tusers.getPass(), tusers.getId() });
		db.close();
	}

	public TUsers find(Integer id) {// 根据ID查找纪录
		TUsers tusers = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from t_users where id=?", new String[] { id.toString() });
		if (cursor.moveToFirst()) {// 依次取出数据
			tusers = new TUsers();
			tusers.setId(cursor.getInt(cursor.getColumnIndex("id")));
			tusers.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			tusers.setPass(cursor.getString(cursor.getColumnIndex("pass")));

		}
		db.close();
		return tusers;
	}

	public List<TUsers> findAll() {// 查询所有记录
		List<TUsers> lists = new ArrayList<TUsers>();
		TUsers tusers = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// Cursor cursor=db.rawQuery("select * from t_users limit ?,?", new
		// String[]{offset.toString(),maxLength.toString()});
		// //这里支持类型MYSQL的limit分页操作

		Cursor cursor = db.rawQuery("select * from t_users ", null);
		while (cursor.moveToNext()) {
			tusers = new TUsers();
			tusers.setId(cursor.getInt(cursor.getColumnIndex("id")));
			tusers.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			tusers.setPass(cursor.getString(cursor.getColumnIndex("pass")));
			lists.add(tusers);
		}
		db.close();
		return lists;
	}

	public long getCount() {//统计所有记录数
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from t_users ", null);
		cursor.moveToFirst();
		db.close();
		return cursor.getLong(0);
	}

}
