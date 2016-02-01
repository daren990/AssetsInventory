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

	public void save(Asset asset) {// 插入记录
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();// 取得数据库操作
		db.execSQL("insert into asset (name, assetType, assetNo, address, custodian, status, labelId) values(?,?,?,?,?,?,?)", new Object[] {asset.getName(), asset.getAssetType(), asset.getAssetNo(), asset.getAddress(), asset.getCustodian(), asset.getStatus(), asset.getLabelId()});
		db.close();// 记得关闭数据库操作
	}

	public void delete(Integer id) {// 删除纪录
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("delete from asset where id=?", new Object[] {id.toString()});
		db.close();
	}

	public void update(Asset asset) {// 修改纪录
		SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
		db.execSQL("update asset set name=?,assetType=?,assetNo=?,address=?,custodian=?,status=?,labelId=? where" + " id=?", new Object[] {asset.getName(), asset.getAssetType(), asset.getAssetNo(), asset.getAddress(), asset.getCustodian(), asset.getStatus(), asset.getLabelId(), asset.getId()});
		db.close();
	}

	public Asset find(Integer id) {// 根据ID查找纪录
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from asset where id=?", new String[] {id.toString()});
		if (cursor.moveToFirst()) {// 依次取出数据
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
	 * 通过标签的ID获得对应的资产记录
	 * @param labelId
	 * @return
	 */
	public Asset findByLabelId(String labelId) {// 根据ID查找纪录
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from asset where labelId=?", new String[] {labelId});
		if (cursor.moveToFirst()) {// 依次取出数据
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
	 * 通过资产名称获得对应的资产记录
	 * @param name
	 * @return
	 */
	public Asset findByName(String name) {// 根据ID查找纪录
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from asset where name=?", new String[] {name});
		if (cursor.moveToFirst()) {// 依次取出数据
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
	 * 通过资产编号获得对应的资产记录
	 * @param name
	 * @return
	 */
	public Asset findByAssetNo(String assetNo) {// 根据ID查找纪录
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// 用游标Cursor接收从数据库检索到的数据
		Cursor cursor = db.rawQuery("select * from asset where assetNo=?", new String[] {assetNo});
		if (cursor.moveToFirst()) {// 依次取出数据
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

	public List<Asset> findAll() {// 查询所有记录
		List<Asset> lists = new ArrayList<Asset>();
		Asset asset = null;
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		// Cursor cursor=db.rawQuery("select * from t_users limit ?,?", new
		// String[]{offset.toString(),maxLength.toString()});
		// //这里支持类型MYSQL的limit分页操作

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

	public long getCount() {//统计所有记录数
		SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from asset ", null);
		cursor.moveToFirst();
		db.close();
		return cursor.getLong(0);
	}
}
