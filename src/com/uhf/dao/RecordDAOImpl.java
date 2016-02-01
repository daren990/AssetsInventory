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
	 * �����¼
	 * @param record �̵��¼
	 */
	public void save(Record record) {// �����¼
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();// ȡ�����ݿ����
		db.execSQL("insert into assetRecord (id, name, assetType, assetNo, address, custodian, status, labelId, cTime, pStatus) values(?,?,?,?,?,?,?,?,?,?)", 
				new Object[] {record.getId(), record.getName(), record.getAssetType(), record.getAssetNo(), record.getAddress(), record.getCustodian(), 
						record.getStatus(), record.getLabelId(), record.getcTime(), record.getpStatus()});
		db.close();// �ǵùر����ݿ����
	}
	
	/**
	 * ɾ����¼
	 * @param id
	 */
	public void delete(Integer id) {// ɾ����¼
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("delete from assetRecord where id=?", new Object[] {id.toString()});
		db.close();
	}
	
	/**
	 * �޸ļ�¼
	 * @param asset
	 */
	public void update(Record record) {// �޸ļ�¼
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("update assetRecord set name=?,assetType=?,assetNo=?,address=?,custodian=?,status=?,labelId=?,cTime=?,pStatus=? where" + " id=?", 
				new Object[] {record.getName(), record.getAssetType(), record.getAssetNo(), record.getAddress(), record.getCustodian(), 
						record.getStatus(), record.getLabelId(), record.getId(), record.getcTime(), record.getpStatus()});
		db.close();
	}
	
	/**
	 * ����ID�����̵��¼
	 * @param id
	 * @return
	 */
	public Record find(Integer id) {// ����ID���Ҽ�¼
		Record record = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
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
	 * ͨ����ǩ��ID��ö�Ӧ���̵��¼
	 * @param labelId
	 * @return
	 */
	public Record findByLabelId(String labelId) {// ����labelId���Ҽ�¼
		Record record = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from assetRecord where labelId=?", new String[] {labelId});
		if (cursor.moveToFirst()) {// ����ȡ������
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
	 * ������е��̵��¼����
	 * @return
	 */
	public List<Record> findAll() {// ��ѯ���м�¼
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
