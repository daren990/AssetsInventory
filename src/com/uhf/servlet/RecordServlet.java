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
	 * ����û���̵㵽�ļ�¼
	 */
	public void IsRecord(Asset asset, Context context) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy��MM��dd�� HH:mm:ss "); 
		Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ�� 
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
