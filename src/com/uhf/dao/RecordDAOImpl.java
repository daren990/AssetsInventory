package com.uhf.dao;

import java.util.ArrayList;
import java.util.List;

import com.uhf.util.DBHelper;
import com.uhf.vo.Asset;
import com.uhf.vo.Record;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecordDAOImpl {
	private DBHelper dbOpenHandler;
	
	public RecordDAOImpl(Context context) {
		this.dbOpenHandler = new DBHelper(context);
	}
	
	/**
	 * 保存记录
	 * @param record 盘点记录
	 */
	public void save(Record record) {// 插入记录
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();// 取得数据库操作
		db.execSQL("insert into assetRecord (id, name, assetType, assetNo, address, custodian, status, labelId, cTime, pStatus) values(?,?,?,?,?,?,?,?,?,?)", 
				new Object[] {record.getId(), record.getName(), record.getAssetType(), record.getAssetNo(), record.getAddress(), record.getCustodian(), 
						record.getStatus(), record.getLabelId(), record.getcTime(), record.getpStatus()});
		db.close();// 记得关闭数据库操作
	}
	
	/**
	 * 删除记录
	 * @param id
	 */
	public void delete(Integer id) {// 删除纪录
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("delete from assetRecord where id=?", new Object[] {id.toString()});
		db.close();
	}
	
	/**
	 * 修改记录
	 * @param asset
	 */
	public void update(Record record) {// 修改纪录
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("update assetRecord set name=?,assetType=?,assetNo=?,address=?,custodian=?,status=?,labelId=?,cTime=?,pStatus=? where" + " id=?", 
				new Object[] {record.getName(), record.getAssetType(), record.getAssetNo(), record.getAddress(), record.getCustodian(), 
						record.getStatus(), record.getLabelId(), record.getId(), record.getcTime(), record.getpStatus()});
		db.close();
	}
	
	/**
	 * 根据ID查找盘点记录
	 * @param id
	 * @return
	 */
	public Record find(Integer id) {// 根据ID查找纪录
		Record record = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from assetRecord where id=?", new String[] {id.toString()});
		if (cursor.moveToFirst()) {
			record = new Record();
			record.setId(cursor.getInt(cursor.getColumnIndex("id")));
			record.setName(cursor.getString(cursor.getColumnIndex("name")));
			record.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			record.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			record.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			record.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
			record.setcTime(cursor.getString(cursor.getColumnIndex("cTime")));
			record.setpStatus(cursor.getInt(cursor.getColumnIndex("pStatus")));
		}
		db.close();
		return record;
	}
	
	/**
	 * 通过标签的ID获得对应的盘点记录
	 * @param labelId
	 * @return
	 */
	public Record findByLabelId(String labelId) {// 根据labelId查找纪录
		Record record = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from assetRecord where labelId=?", new String[] {labelId});
		if (cursor.moveToFirst()) {// 依次取出数据
			record = new Record();
			record.setId(cursor.getInt(cursor.getColumnIndex("id")));
			record.setName(cursor.getString(cursor.getColumnIndex("name")));
			record.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			record.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			record.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			record.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			record.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			record.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
			record.setcTime(cursor.getString(cursor.getColumnIndex("cTime")));
			record.setpStatus(cursor.getInt(cursor.getColumnIndex("pStatus")));
		}
		db.close();
		return record;
	}
	
	/**
	 * 获得所有的盘点记录数据
	 * @return
	 */
	public List<Record> findAll() {// 查询所有记录
		List<Record> lists = new ArrayList<Record>();
		Record record = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from assetRecord ", null);
		while (cursor.moveToNext()) {
			record = new Record();
			record.setId(cursor.getInt(cursor.getColumnIndex("id")));
			record.setName(cursor.getString(cursor.getColumnIndex("name")));
			record.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			record.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			record.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			record.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			record.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			record.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
			record.setcTime(cursor.getString(cursor.getColumnIndex("cTime")));
			record.setpStatus(cursor.getInt(cursor.getColumnIndex("pStatus")));
			lists.add(record);
		}
		db.close();
		return lists;
	}
}
