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

	public void save(TUsers tusers) {// �����¼
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();// ȡ�����ݿ����
		db.execSQL("insert into t_users (username,pass) values(?,?)", new Object[] { tusers.getUsername(), tusers.getPass() });
		db.close();// �ǵùر����ݿ����
	}

	public void delete(Integer id) {// ɾ����¼
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("delete from t_users where id=?", new Object[] { id.toString() });
		db.close();
	}

	public void update(TUsers tusers) {// �޸ļ�¼
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("update t_users set username=?,pass=? where" + " id=?", new Object[] { tusers.getUsername(), tusers.getPass(), tusers.getId() });
		db.close();
	}

	public TUsers find(Integer id) {// ����ID���Ҽ�¼
		TUsers tusers = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from t_users where id=?", new String[] { id.toString() });
		if (cursor.moveToFirst()) {// ����ȡ������
			tusers = new TUsers();
			tusers.setId(cursor.getInt(cursor.getColumnIndex("id")));
			tusers.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			tusers.setPass(cursor.getString(cursor.getColumnIndex("pass")));

		}
		db.close();
		return tusers;
	}

	public List<TUsers> findAll() {// ��ѯ���м�¼
		List<TUsers> lists = new ArrayList<TUsers>();
		TUsers tusers = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// Cursor cursor=db.rawQuery("select * from t_users limit ?,?", new
		// String[]{offset.toString(),maxLength.toString()});
		// //����֧������MYSQL��limit��ҳ����

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

	public long getCount() {//ͳ�����м�¼��
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from t_users ", null);
		cursor.moveToFirst();
		db.close();
		return cursor.getLong(0);
	}

}
