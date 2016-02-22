package com.uhf.rfid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uhf.util.DBHelper;
import com.uhf.util.DatabaseDump;
import com.uhf.vo.Asset;
import com.uhf.constants.Constants.Result;
import com.uhf.dao.AssetDAOImpl;
import com.uhf.linkage.Linkage;
import com.uhf.rfid.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends FragmentActivity{
	private static final String TAG = "打印测试信息";// 准备好TAG标识用于LOG输出，方便我们用LogCat进行调试

	private RadioGroup rgs;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	public static boolean  m_InitStatus;
	public static Linkage link = new Linkage();
	public static TextView textView;
	private Button btnmainstart;
	private Button btnmainend;
	private Button bntExport;
//	/** Called when the activity is first created. */   
//    private  List<String> list = new ArrayList<String>();  
//    private  TextView myTextView;  
//    private  Spinner mySpinner;  
//    private  ArrayAdapter<String> adapter;  
//    private  Animation myAnimation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView)findViewById(R.id.txtmainstatusvalue);
		btnmainstart = (Button)findViewById(R.id.btnmainstart);
		btnmainend = (Button)findViewById(R.id.btnmainend);
		bntExport = (Button)findViewById(R.id.bntExport);
		rgs = (RadioGroup) findViewById(R.id.tabs_rg);   
		initView();
		setListener();
		initData();
//		initSpinner(this.getBaseContext());
	}
	private void initView(){	
		fragments.add(new InventoryFragment());
		fragments.add(new InventorySetFragment());
		fragments.add(new PowerFragment());
		fragments.add(new FrequencyFragment());
		fragments.add(new ReadWriteFragment());
		fragments.add(new PermissionFragment());
		fragments.add(new TagLockFragment());
	}
	/**
	 * 监听
	 */
	private void setListener() {

		// TODO Auto-generated method stub
		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
		tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
			@Override
			public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
				
			}
		});
		btnmainstart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(m_InitStatus) return;
				int result = InitRadio();
				if(result == Result.RFID_STATUS_OK.getValue())
				{					
					textView.setText("连接成功");
				}
				else
				{
					textView.setText("连接失败"+result);
				}
			}
		});


		btnmainend.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!InventoryFragment.m_bStopInventoryThread){
					textView.setText("请先停止盘点");
					return;
				}
				/*	if(m_InitStatus){
					DisConnect();
					textView.setText("断开成功");
				}
			 	*/
				DisConnect();
				textView.setText("断开成功");
			}
		});
		
		bntExport.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!InventoryFragment.m_bStopInventoryThread){
					textView.setText("请先停止盘点");
					return;
				}
				// 在导出之前用当前时间的数值标记导出的数据，方便PC端的数据处理
				Date date = new Date();
				long time = date.getTime();
				AssetDAOImpl assetDAOImpl = new AssetDAOImpl(getBaseContext());
				List<Asset> assetList = assetDAOImpl.findAll();
				for (Asset asset : assetList) {
					asset.setPcFlag(time);
					assetDAOImpl.update(asset);
				}
				// 获得操作的数据库中的表，并将其内容导出成Excel表格
				DBHelper dbOpenHandler = new DBHelper(getBaseContext());
				SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
				DatabaseDump testDatabaseDump = new DatabaseDump(db, null);
				testDatabaseDump.exportData();
				DisConnect();
				textView.setText("导出成功");
			}
		});
	}

	public  static  int  InitRadio() {
		// TODO Auto-generated method stub
		int result = 0;	
		if(!m_InitStatus){
			result =  link.Radio_Initialization();	
			if(result == Result.RFID_STATUS_OK.getValue()){
				m_InitStatus = true;
			}

			else
				m_InitStatus = false;		
		}
		return result;
	}
	public static void DisConnect(){
		link.DestroyRadioFuncIntegration();
		m_InitStatus = false;
		
	}

	/**
	 * 退出框
	 */
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event)  
	{  
		if (keyCode == KeyEvent.KEYCODE_BACK )  
		{  
			// 创建退出对话框  
			AlertDialog isExit = new AlertDialog.Builder(this).create();  
			// 设置对话框标题  
			isExit.setTitle("系统提示");  
			// 设置对话框消息  
			isExit.setMessage("确定要退出吗");  
			// 添加选择按钮并注册监听  
			isExit.setButton("确定", listener);  
			isExit.setButton2("取消", listener);  
			// 显示对话框  
			isExit.show();  
		}       
		return false;  
	}  
	/**监听对话框里面的button点击事件*/  
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	{  
		public void onClick(DialogInterface dialog, int which)  
		{  
			switch (which)  
			{  
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序 
				if(!InventoryFragment.m_bStopInventoryThread){
					InventoryFragment.m_bStopInventoryThread = true;
					link.CancelOperation();
				}				
				if(m_InitStatus){
					DisConnect();
				}
				finish();
				break;  
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
				break;  
			default:  
				break;  
			}  
		}  
	}; 

	/**
	 * 显示结果
	 * @param result
	 */
	public static void DisplayResult(String result){
		textView.setText(result);
	}
	
	/**
	 * 初始化数据库
	 */
	public void initData() {
		DBHelper dbHandler = new DBHelper(this.getBaseContext());
		try {
			Log.i(TAG, "初始化数据库开始");
			dbHandler.createDataBase();
			Log.i(TAG, "初始化数据库结束");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void initSpinner(Context context) {
//		//第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项  
//		AssetDAOImpl assetDAOImpl = new AssetDAOImpl(context);
//        list = assetDAOImpl.getAllAddress();
//        myTextView = (TextView)findViewById(R.id.txtAddressValue);  
//        mySpinner = (Spinner)findViewById(R.id.AddressSpinner);
//        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。   
//        adapter = new  ArrayAdapter<String>(this ,android.R.layout.simple_spinner_item, list);
//        //第三步：为适配器设置下拉列表下拉时的菜单样式。   
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //第四步：将适配器添加到下拉列表上   
//        mySpinner.setAdapter(adapter); 
//        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中   
//        mySpinner.setOnItemSelectedListener(new  Spinner.OnItemSelectedListener(){  
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
//                // TODO Auto-generated method stub   
//                /* 将所选mySpinner 的值带入myTextView 中*/   
//                myTextView.setText(adapter.getItem(arg2));  
//                /* 将mySpinner 显示*/   
//                arg0.setVisibility(View.VISIBLE);  
//            }  
//            public void onNothingSelected(AdapterView<?> arg0) {  
//                // TODO Auto-generated method stub   
//                myTextView.setText("NONE" );  
//                arg0.setVisibility(View.VISIBLE);  
//            }  
//        });
//	}
}    