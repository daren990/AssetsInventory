package com.uhf.dao;

import java.util.ArrayList;
import java.util.List;

import com.uhf.util.DBHelper;
import com.uhf.vo.Asset;
import com.uhf.vo.TUsers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AssetDAOImpl {
	private DBHelper dbOpenHandler;

	public AssetDAOImpl(Context context) {
		this.dbOpenHandler = new DBHelper(context);
	}

	public void save(Asset asset) {// �����¼
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();// ȡ�����ݿ����
		db.execSQL("insert into asset (name, assetType, assetNo, address, custodian, status, labelId) values(?,?,?,?,?,?,?)", new Object[] {asset.getName(), asset.getAssetType(), asset.getAssetNo(), asset.getAddress(), asset.getCustodian(), asset.getStatus(), asset.getLabelId()});
		db.close();// �ǵùر����ݿ����
	}

	public void delete(Integer id) {// ɾ����¼
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("delete from asset where id=?", new Object[] {id.toString()});
		db.close();
	}

	public void update(Asset asset) {// �޸ļ�¼
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("update asset set name=?,assetType=?,assetNo=?,address=?,custodian=?,status=?,labelId=? where" + " id=?", new Object[] {asset.getName(), asset.getAssetType(), asset.getAssetNo(), asset.getAddress(), asset.getCustodian(), asset.getStatus(), asset.getLabelId(), asset.getId()});
		db.close();
	}

	public Asset find(Integer id) {// ����ID���Ҽ�¼
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from asset where id=?", new String[] {id.toString()});
		if (cursor.moveToFirst()) {// ����ȡ������
			asset = new Asset();
			asset.setId(cursor.getInt(cursor.getColumnIndex("id")));
			asset.setName(cursor.getString(cursor.getColumnIndex("name")));
			asset.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			asset.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			asset.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			asset.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			asset.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			asset.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
		}
		db.close();
		return asset;
	}
	
	/**
	 * ͨ����ǩ��ID��ö�Ӧ���ʲ���¼
	 * @param labelId
	 * @return
	 */
	public Asset findByLabelId(String labelId) {// ����ID���Ҽ�¼
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from asset where labelId=?", new String[] {labelId});
		if (cursor.moveToFirst()) {// ����ȡ������
			asset = new Asset();
			asset.setId(cursor.getInt(cursor.getColumnIndex("id")));
			asset.setName(cursor.getString(cursor.getColumnIndex("name")));
			asset.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			asset.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			asset.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			asset.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			asset.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			asset.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
		}
		db.close();
		return asset;
	}
	
	/**
	 * ͨ���ʲ����ƻ�ö�Ӧ���ʲ���¼
	 * @param name
	 * @return
	 */
	public Asset findByName(String name) {// ����ID���Ҽ�¼
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from asset where name=?", new String[] {name});
		if (cursor.moveToFirst()) {// ����ȡ������
			asset = new Asset();
			asset.setId(cursor.getInt(cursor.getColumnIndex("id")));
			asset.setName(cursor.getString(cursor.getColumnIndex("name")));
			asset.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			asset.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			asset.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			asset.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			asset.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			asset.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
		}
		db.close();
		return asset;
	}
	
	/**
	 * ͨ���ʲ���Ż�ö�Ӧ���ʲ���¼
	 * @param name
	 * @return
	 */
	public Asset findByAssetNo(String assetNo) {// ����ID���Ҽ�¼
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// ���α�Cursor���մ����ݿ������������
		Cursor cursor = db.rawQuery("select * from asset where assetNo=?", new String[] {assetNo});
		if (cursor.moveToFirst()) {// ����ȡ������
			asset = new Asset();
			asset.setId(cursor.getInt(cursor.getColumnIndex("id")));
			asset.setName(cursor.getString(cursor.getColumnIndex("name")));
			asset.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			asset.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			asset.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			asset.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			asset.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			asset.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
		}
		db.close();
		return asset;
	}

	public List<Asset> findAll() {// ��ѯ���м�¼
		List<Asset> lists = new ArrayList<Asset>();
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// Cursor cursor=db.rawQuery("select * from t_users limit ?,?", new
		// String[]{offset.toString(),maxLength.toString()});
		// //����֧������MYSQL��limit��ҳ����

		Cursor cursor = db.rawQuery("select * from asset ", null);
		while (cursor.moveToNext()) {
			asset = new Asset();
			asset.setId(cursor.getInt(cursor.getColumnIndex("id")));
			asset.setName(cursor.getString(cursor.getColumnIndex("name")));
			asset.setAssetType(cursor.getString(cursor.getColumnIndex("assetType")));
			asset.setAssetNo(cursor.getString(cursor.getColumnIndex("assetNo")));
			asset.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			asset.setCustodian(cursor.getString(cursor.getColumnIndex("custodian")));
			asset.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			asset.setLabelId(cursor.getString(cursor.getColumnIndex("labelId")));
			lists.add(asset);
		}
		db.close();
		return lists;
	}

	public long getCount() {//ͳ�����м�¼��
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from asset ", null);
		cursor.moveToFirst();
		db.close();
		return cursor.getLong(0);
	}
}
