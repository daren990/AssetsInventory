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
	 * 通过部门名称获得对应的部门
	 * @param name
	 * @return Department
	 */
	public Department findByName(String name) {// 根据ID查找纪录
		Department department = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from department where name=?", new String[] {name});
		if (cursor.moveToFirst()) {// 依次取出数据
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
