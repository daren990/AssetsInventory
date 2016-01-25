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
	private static final String TAG = "��ӡ������Ϣ";// ׼����TAG��ʶ����LOG���������������LogCat���е���

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
	 * ����
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
					textView.setText("���ӳɹ�");
				}
				else
				{
					textView.setText("����ʧ��"+result);
				}
			}
		});


		btnmainend.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!InventoryFragment.m_bStopInventoryThread){
					textView.setText("����ֹͣ�̵�");
					return;
				}
			/*	if(m_InitStatus){
					DisConnect();
					textView.setText("�Ͽ��ɹ�");
				}
*/
				DisConnect();
				textView.setText("�Ͽ��ɹ�");
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
	 * �˳���
	 */
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event)  
	{  
		if (keyCode == KeyEvent.KEYCODE_BACK )  
		{  
			// �����˳��Ի���  
			AlertDialog isExit = new AlertDialog.Builder(this).create();  
			// ���öԻ������  
			isExit.setTitle("ϵͳ��ʾ");  
			// ���öԻ�����Ϣ  
			isExit.setMessage("ȷ��Ҫ�˳���");  
			// ���ѡ��ť��ע�����  
			isExit.setButton("ȷ��", listener);  
			isExit.setButton2("ȡ��", listener);  
			// ��ʾ�Ի���  
			isExit.show();  
		}       
		return false;  
	}  
	/**�����Ի��������button����¼�*/  
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	{  
		public void onClick(DialogInterface dialog, int which)  
		{  
			switch (which)  
			{  
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳����� 
				if(!InventoryFragment.m_bStopInventoryThread){
					InventoryFragment.m_bStopInventoryThread = true;
					link.CancelOperation();
				}				
				if(m_InitStatus){
					DisConnect();
				}
				finish();
				break;  
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���  
				break;  
			default:  
				break;  
			}  
		}  
	}; 

	/**
	 * ��ʾ���
	 * @param result
	 */
	public static void DisplayResult(String result){
		textView.setText(result);
	}
	
	/**
	 * ��ʼ�����ݿ�
	 */
	public void initData() {
		DBHelper dbHandler = new DBHelper(this.getBaseContext());
		try {
			Log.i(TAG, "��ʼ�����ݿ⿪ʼ");
			dbHandler.createDataBase();
			Log.i(TAG, "��ʼ�����ݿ����");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}    