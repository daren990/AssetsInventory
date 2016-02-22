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
	private static final String TAG = "��ӡ������Ϣ";// ׼����TAG��ʶ����LOG���������������LogCat���е���

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
		
		bntExport.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!InventoryFragment.m_bStopInventoryThread){
					textView.setText("����ֹͣ�̵�");
					return;
				}
				// �ڵ���֮ǰ�õ�ǰʱ�����ֵ��ǵ��������ݣ�����PC�˵����ݴ���
				Date date = new Date();
				long time = date.getTime();
				AssetDAOImpl assetDAOImpl = new AssetDAOImpl(getBaseContext());
				List<Asset> assetList = assetDAOImpl.findAll();
				for (Asset asset : assetList) {
					asset.setPcFlag(time);
					assetDAOImpl.update(asset);
				}
				// ��ò��������ݿ��еı����������ݵ�����Excel���
				DBHelper dbOpenHandler = new DBHelper(getBaseContext());
				SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
				DatabaseDump testDatabaseDump = new DatabaseDump(db, null);
				testDatabaseDump.exportData();
				DisConnect();
				textView.setText("�����ɹ�");
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
	
//	public void initSpinner(Context context) {
//		//��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���  
//		AssetDAOImpl assetDAOImpl = new AssetDAOImpl(context);
//        list = assetDAOImpl.getAllAddress();
//        myTextView = (TextView)findViewById(R.id.txtAddressValue);  
//        mySpinner = (Spinner)findViewById(R.id.AddressSpinner);
//        //�ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��   
//        adapter = new  ArrayAdapter<String>(this ,android.R.layout.simple_spinner_item, list);
//        //��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��   
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //���Ĳ�������������ӵ������б���   
//        mySpinner.setAdapter(adapter); 
//        //���岽��Ϊ�����б����ø����¼�����Ӧ���������Ӧ�˵���ѡ��   
//        mySpinner.setOnItemSelectedListener(new  Spinner.OnItemSelectedListener(){  
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
//                // TODO Auto-generated method stub   
//                /* ����ѡmySpinner ��ֵ����myTextView ��*/   
//                myTextView.setText(adapter.getItem(arg2));  
//                /* ��mySpinner ��ʾ*/   
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