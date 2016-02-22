package com.uhf.dao;

import com.uhf.util.DBHelper;
import com.uhf.vo.Asset;
import com.uhf.vo.Department;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DepartmentDAOImpl {
	private DBHelper dbOpenHandler;
	
	public DepartmentDAOImpl(Context context) {
		this.dbOpenHandler = new DBHelper(context);
	}
	
	/**
	 * ͨ���������ƻ�ö�Ӧ�Ĳ���
	 * @param name
	 * @return Department
	 */
	public Department findByName(String name) {// ����ID���Ҽ�¼
		Department department = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from department where name=?", new String[] {name});
		if (cursor.moveToFirst()) {// ����ȡ������
			department = new Department();
			department.setId(cursor.getString(cursor.getColumnIndex("id")));
			department.setParentId(cursor.getString(cursor.getColumnIndex("id")));
			department.setName(cursor.getString(cursor.getColumnIndex("name")));
			department.setType(cursor.getInt(cursor.getColumnIndex("type")));
			department.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			department.setSequenceNum(cursor.getInt(cursor.getColumnIndex("sequenceNum")));
			department.setMeno(cursor.getString(cursor.getColumnIndex("meno")));
		}
		db.close();
		return department;
	}
}
