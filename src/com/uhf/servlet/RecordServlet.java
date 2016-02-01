package com.uhf.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.uhf.dao.RecordDAOImpl;
import com.uhf.vo.Asset;
import com.uhf.vo.Record;

import android.content.Context;

public class RecordServlet {
	RecordDAOImpl recordDAOImpl = null;
	/**
	 * 插入没有盘点到的记录
	 */
	public void IsRecord(Asset asset, Context context) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss "); 
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间 
		String cTime = formatter.format(curDate); 
		recordDAOImpl = new RecordDAOImpl(context);
		Record record = recordDAOImpl.findByLabelId(asset.getLabelId());
		if (record != null) {
			record.setcTime(cTime);
			recordDAOImpl.update(record);
		} else {
			List<Record> listRecord = recordDAOImpl.findAll();
			record = new Record();
			record.setId(listRecord.size()+1);
			record.setName(asset.getName());
			record.setAssetNo(asset.getAssetNo());
			record.setAssetType(asset.getAssetType());
			record.setCustodian(asset.getCustodian());
			record.setAddress(asset.getAddress());
			record.setStatus(asset.getStatus());
			record.setLabelId(asset.getLabelId());
			record.setcTime(cTime);
			record.setpStatus(1);
			recordDAOImpl.save(record);
		}
	}
}
