package com.uhf.rfid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uhf.util.DBHelper;

import com.uhf.constants.Constants.Result;
import com.uhf.linkage.Linkage;
import com.uhf.rfid.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView)findViewById(R.id.txtmainstatusvalue);
		btnmainstart = (Button)findViewById(R.id.btnmainstart);
		btnmainend = (Button)findViewById(R.id.btnmainend);
		rgs = (RadioGroup) findViewById(R.id.tabs_rg);   
		initView();
		setListener();
		initData();
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
}    