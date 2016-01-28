package com.uhf.rfid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.uhf.vo.Asset;
import com.uhf.constants.Constants.InvMode;
import com.uhf.constants.Constants.Result;
import com.uhf.dao.AssetDAOImpl;
import com.uhf.rfid.R;
import com.uhf.structures.Rfid_Value;
import com.uhf.structures.St_Inv_Data;
import com.uhf.structures.Single_Inventory_Time_Config;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class InventoryFragment extends Fragment {
	private static final String TAG = "这个是测试类";// 准备好TAG标识用于LOG输出，方便我们用LogCat进行调试

	private Button btninventoryStart;
	private Button btninventoryStop;
	private Button btninventoryClear;
	private ListView list_radio;
//	private TextView textView_tagcountvalue;
//	private TextView textView_SpeedValue;
//	private TextView text_View_tagtotal;
	private TextView txtAssetsCountValue;
	private boolean m_bStopOpenRadioThread;// 是否启动盘点
	public static boolean m_bStopInventoryThread;// 是否停止盘点
	private boolean m_bClear;
	private double m_InventorySpendTime;
	private double m_lStartTime;
	private St_Inv_Data[] stInvData;// 盘点的数据
	private int m_TagTotalCount;// 标签总数
	private String Data = "Data";// 标签数据
	private String Count = "Count";// 访问标签的次数
	private String AssetNo = "AssetNo";// 资产编号
	private String AssetName = "AssetName";// 资产名称
	private String AssetCustodian = "AssetCustodian";// 保管员
	private int assetsCountValue;// 资产盘点总数
	private SimpleAdapter recptionSimpleAdapter;
	private ArrayList<Map<String, String>> receptionArrayList;
	private HashMap<String, String> EpcData;
	private Single_Inventory_Time_Config InvConfig;
	private double invDataTime;
	private double temptime;

	/**
	 * 初始化开始盘点的参数
	 * @author libb
	 *
	 */
	public static class InventoryStartPara {
		public static int uiInventoryMode;
		public static int iRunningTime;
		public static int iRestTime;

		InventoryStartPara() {
			uiInventoryMode = 0;
			iRunningTime = 0;
			iRestTime = 0;
		}
	}
	
	// 定义获得系统数据的线程
	private MyThread_InventoryThread mythread_Inventory = null;
	// 定义获得标签速率等数据的线程
	private MyThread_MyCounterThread mythread_MyCounter = null;
	// 定义获得资产总数的线程
	private MyThread_AssetsCountThread mythread_AssetsCount = null;
	private MyThread_OpenRadioThread mythread_OpenRadio = null;
	// 处理系统数据的类
	private Handler_Inventory handler_Inventory;
	// 处理标签速率等信息的类
	private Handler_MyCounter handler_MyCounter;
	// 处理资产总数信息的类
	private Handler_AssetsCount handler_AssetsCount;
	
	/**
	 * 初始化布局
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.inventory_tab, null);
	}

	/**
	 * 活动的创建
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btninventoryStart = (Button) getView().findViewById(
				R.id.btninventoryStart);
		btninventoryStop = (Button) getView().findViewById(
				R.id.btninventoryStop);
		btninventoryClear = (Button) getView().findViewById(
				R.id.btninventoryClear);

		list_radio = (ListView) getView().findViewById(R.id.listinventorydata);
//		textView_tagcountvalue = (TextView) getView().findViewById(
//				R.id.txtinventoryTagCountValue);
//		textView_SpeedValue = (TextView) getView().findViewById(
//				R.id.txtinventorySpeedValue);
//		text_View_tagtotal = (TextView) getView().findViewById(
//				R.id.txtinventoryTagTotalValue);
		
		txtAssetsCountValue = (TextView) getView().findViewById(R.id.txtAssetsCountValue);
		
		m_bStopOpenRadioThread = false;
		m_bStopInventoryThread = true;
		receptionArrayList = new ArrayList<Map<String, String>>();
		EpcData = new HashMap<String, String>();
		InvConfig = new Single_Inventory_Time_Config();
		stInvData = new St_Inv_Data[512];
		initView();
		setListener();
	}

	/**
	 * 设置监听器
	 */
	private void setListener() {
		// TODO Auto-generated method stub
		btninventoryStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!MainActivity.m_InitStatus) {
					return;
				}
				if (!m_bStopInventoryThread)
					return;
				m_bStopOpenRadioThread = false;
				m_bStopInventoryThread = false;

				// 盘点模式
				int inv_mode = 0;

				Rfid_Value rfid_value = new Rfid_Value();
				inv_mode = MainActivity.link.Radio_GetInvMode(rfid_value);
				if (rfid_value.value == Result.RFID_STATUS_OK.getValue()) {
					if (inv_mode == InvMode.HighSpeedMode.getValue()) {
						InventoryStartPara.uiInventoryMode = 0;
					} else if (inv_mode == InvMode.IntelligentMode.getValue()) {
						InventoryStartPara.uiInventoryMode = 1;
					} else if (inv_mode == InvMode.LowPowerMode.getValue()) {
						InventoryStartPara.uiInventoryMode = 2;
					} else if (inv_mode == InvMode.UserDefined.getValue()) {
						InventoryStartPara.uiInventoryMode = 3;
						if (MainActivity.link
								.Radio_GetSingleInvTimeConfig(InvConfig) != 0) {
							return;
						}
					}
				}
				handler_Inventory = new Handler_Inventory();
				handler_MyCounter = new Handler_MyCounter();
				handler_AssetsCount = new Handler_AssetsCount();

				mythread_OpenRadio = new MyThread_OpenRadioThread();
				mythread_OpenRadio.start();

				mythread_Inventory = new MyThread_InventoryThread();
				mythread_Inventory.start();

				mythread_MyCounter = new MyThread_MyCounterThread();
				mythread_MyCounter.start();
				
				mythread_AssetsCount = new MyThread_AssetsCountThread();
				mythread_AssetsCount.start();

				MainActivity.DisplayResult("开始盘点");
			}
		});
		btninventoryStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!MainActivity.m_InitStatus) {
					return;
				}
				m_bStopOpenRadioThread = true;
				m_bStopInventoryThread = true;
				int nStatus = MainActivity.link.CancelOperation();
				if (nStatus != Result.RFID_STATUS_OK.getValue()) {
					MainActivity.DisplayResult("停止盘点操作执行失败" + nStatus);
				} else {
					MainActivity.DisplayResult("盘点停止");
				}
				handler_Inventory.removeMessages(111);
				handler_MyCounter.removeMessages(222);
				handler_AssetsCount.removeMessages(333);
			}
		});
		btninventoryClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClearCtlItemInfo();
			}
		});

	}

	class MyThread_OpenRadioThread extends Thread {
		public void run() {
			int result = 0;
			if (InventoryStartPara.uiInventoryMode != 3) {
				result = MainActivity.link.Inventory();
				/*
				 * if(result != 0){ Rfid_Value value = new Rfid_Value();
				 * MainActivity.link.Radio_GetErrorCode(value); int errorcode =
				 * value.value; Log.e("inventorystop",
				 * String.valueOf(errorcode)); }
				 */
			} else {
				if (InvConfig.iWorkTime == 0 && InvConfig.iRestTime == 0) {
					InvConfig.iWorkTime = 100;
					InvConfig.iRestTime = 900;
				}
				/*
				 * Log.e("inventorystop", String.valueOf(InvConfig.iWorkTime));
				 * Log.e("inventorystop", String.valueOf(InvConfig.iRestTime));
				 */
				result = MainActivity.link.Inventory(InvConfig.iWorkTime,
						InvConfig.iRestTime);
				/*
				 * if(result != 0){ Rfid_Value value = new Rfid_Value();
				 * MainActivity.link.Radio_GetErrorCode(value); int errorcode =
				 * value.value; Log.e("inventorystop",
				 * String.valueOf(errorcode)); }
				 */
			}
			if (result != Result.RFID_STATUS_OK.getValue()) {
				m_bStopOpenRadioThread = true;
			}
		}
	}

	/**
	 * 获得系统的标签数据
	 * @author libb
	 *
	 */
	class MyThread_InventoryThread extends Thread {
		public void run() {
			try {
				invDataTime = new Date().getTime();
				while (true) {
					if (m_bStopInventoryThread) {
						break;
					}
					if (m_bClear) {
						m_lStartTime = new Date().getTime();
						m_InventorySpendTime = 0;
						m_bClear = false;
						continue;
					}
					// 获得盘点数据
					/*
					 * Rfid_Value rfid_value = new Rfid_Value();
					 * rfid_value.value = 0;
					 */
					int num;
					num = MainActivity.link.GetInvData(stInvData);
					if (num > 0) {				
						// 解析出EPC等数据
						String strEPCTemp = "";
						// 循环数据结果
						for (int i = 0; i < num; i++) {
							if (stInvData[i].nLength > 0
									&& stInvData[i].nLength < 32
									&& stInvData[i].RSSI < 0
									&& stInvData[i].RSSI > -150) {
								strEPCTemp = MainActivity.link.b2hexs(
										stInvData[i].INV_Data,
										stInvData[i].nLength);
								if(stInvData[i].tidLength>0)
								{
								strEPCTemp +="  ";
								strEPCTemp += MainActivity.link.b2hexs(
										stInvData[i].TID_Data,
										stInvData[i].tidLength);
								}
							}
							// 是否终止线程
							if (m_bStopOpenRadioThread) {
								break;
							}
							if (strEPCTemp == "") {
								continue;
							}
							if (EpcData.containsKey(strEPCTemp)) {
								Common.send(handler_Inventory, 111, 1,
										strEPCTemp);
							} else {
								Common.send(handler_Inventory, 111, 0,
										strEPCTemp);
								EpcData.put(strEPCTemp, strEPCTemp);
							}
						}//for
						
						temptime =  new Date().getTime();
						if(temptime - invDataTime>300)
						{
						 Common.callAlarmAsSuccess(getActivity());
						 invDataTime = new Date().getTime();
						}
						Thread.sleep(50);
					}//if
				}//while

			}// try
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//run
	}

	class MyThread_MyCounterThread extends Thread {
		public void run() {
			m_lStartTime = new Date().getTime();
			double m_lEndTime = 0;
			int str_TotalCount = 0;
			double str_Rate = 0;
			int str_TagCount = 0;
			while (true) {
				if (m_bStopOpenRadioThread) {
					Common.send(handler_MyCounter, 222, 0, str_TotalCount,
							str_Rate);
					break;
				}
				str_TagCount = receptionArrayList.size();
				str_TotalCount = m_TagTotalCount;
				m_lEndTime = new Date().getTime();

				str_Rate = Math
						.ceil((str_TotalCount * 1.0)
								/ ((m_InventorySpendTime + (m_lEndTime - m_lStartTime)) / 1000));
				// 更新
				Common.send(handler_MyCounter, 222, str_TagCount,
						str_TotalCount, str_Rate);
			}
			m_InventorySpendTime += m_lEndTime - m_lStartTime;
		}
	}
	
	class MyThread_AssetsCountThread extends Thread {
		public void run() {
			int str_AssetsCount = 0;
			str_AssetsCount = assetsCountValue;
			Common.send(handler_AssetsCount, 333, str_AssetsCount);
		}
	}

	/**
	 * 更新显示列表数据
	 * @param found 是否已经存在此标签的标识
	 * @param data 标签数据
	 */
	private synchronized void UpdatelistDataInsert(int found, String data) {
		AssetDAOImpl assetDAOImpl = new AssetDAOImpl(getActivity().getBaseContext());
		m_TagTotalCount = m_TagTotalCount + 1;
		if (found == 1) {
			for (int i = 0; i < receptionArrayList.size(); i++) {
				if (data.equals(receptionArrayList.get(i).get(Data))) {
					String t = String.valueOf(receptionArrayList.get(i).get(
							Count));
					if (TextUtils.isEmpty(t)) {
						t = "0";
					}
					t = String.valueOf(Integer.valueOf(t) + 1);
					receptionArrayList.get(i).put(Count, t);
					refesh();
					return;
				}
			}
		} else {
			String assetNo = null;
			String assetName = null;
			String assetCustodian = null;
			Asset asset = (Asset) assetDAOImpl.findByLabelId(data);
			/**
			 * 如果标签ID没有找到对应的资产信息，则此时资产信息不添加到资产信息显示列表中
			 */
			if (asset != null) {
				assetNo = asset.getAssetNo();
				assetName = asset.getName();
				assetCustodian = asset.getCustodian();
				assetsCountValue = assetsCountValue + 1;
				// 不包含此条数据
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(Data, data);
				hashMap.put(Count, "1");
				hashMap.put(AssetNo, assetNo);
				hashMap.put(AssetName, assetName);
				hashMap.put(AssetCustodian, assetCustodian);
				receptionArrayList.add(hashMap);
				refesh();
			}
		}

	}

	/**
	 * 更新标签查找的速率、数量、总的扫描次数
	 * @param str_TagCount
	 * @param str_TotalCount
	 * @param str_Rate
	 */
	public synchronized void UpdateRateCount(String str_TagCount,
			String str_TotalCount, String str_Rate) {
//		textView_SpeedValue.setText(str_Rate);
//		textView_tagcountvalue.setText(str_TagCount);
//		text_View_tagtotal.setText(str_TotalCount);
		txtAssetsCountValue.setText(str_TagCount);
	}
	
	/**
	 * 更新资产的总数
	 * @param assetsCount
	 */
	public synchronized void UpdateAssetsCount(String assetsCount) {
		txtAssetsCountValue.setText(assetsCount);
	}

	/**
	 * 清除显示的数据信息
	 */
	public void ClearCtlItemInfo() {
		if (!MainActivity.m_InitStatus) {
			return;
		}
		m_bClear = true;
		receptionArrayList.clear();
		EpcData.clear();
		m_TagTotalCount = 0;
		assetsCountValue = 0;
//		textView_SpeedValue.setText("0");
//		textView_tagcountvalue.setText("0");
//		text_View_tagtotal.setText("0");
		txtAssetsCountValue.setText("0");
		refesh();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		recptionSimpleAdapter = new SimpleAdapter(getActivity(),
				receptionArrayList, R.layout.inventory_list, new String[] {
						AssetNo, AssetName, AssetCustodian }, new int[] {
						R.id.idActivityMain_RecodesElement_AssetNo,
						R.id.idActivityMain_RecodesElement_AssetName, 
						R.id.idActivityMain_RecodesElement_AssetCustodian });
		list_radio.setAdapter(recptionSimpleAdapter);
		SimpleAdapter sAdapter = (SimpleAdapter) (list_radio).getAdapter();
		sAdapter.notifyDataSetChanged();
	}

	/**
	 * 刷新列表数据
	 */
	private synchronized void refesh() {
		SimpleAdapter sAdapter = (SimpleAdapter) (list_radio).getAdapter();
		sAdapter.notifyDataSetChanged();
	}

	/**
	 * 传递列表的数据信息
	 * @author libb
	 *
	 */
	class Handler_Inventory extends Handler {
		public Handler_Inventory() {
		}

		public Handler_Inventory(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 111) {
				UpdatelistDataInsert(msg.arg1, String.valueOf(msg.obj));
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 传递扫描的数量信息
	 * @author libb
	 *
	 */
	class Handler_MyCounter extends Handler {
		public Handler_MyCounter() {
		}

		public Handler_MyCounter(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 222) {
				UpdateRateCount(String.valueOf(msg.arg1),
						String.valueOf(msg.arg2), String.valueOf(msg.obj));
			}
			super.handleMessage(msg);
		}
	}
	
	/**
	 * 传递资产的总数信息
	 * @author libb
	 *
	 */
	class Handler_AssetsCount extends Handler {
		public Handler_AssetsCount() {
		}

		public Handler_AssetsCount(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 333) {
				UpdateAssetsCount(String.valueOf(msg.arg1));
			}
			super.handleMessage(msg);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
