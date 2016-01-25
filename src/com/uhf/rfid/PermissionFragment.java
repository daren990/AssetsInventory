package com.uhf.rfid;

import com.uhf.constants.Constants.Result;
import com.uhf.rfid.R;
import com.uhf.structures.Rfid_Value;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PermissionFragment extends Fragment{

	private Button btnPerRead;
	private Button btnPerUpdataPwd;
	private Button btnPerUpgradeProgram;
	private TextView txtperAccPwd;
	private TextView txtperKillPwd;
	private EditText editNewPwd;
	private Spinner spinperPwdType;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.permission_tab, null);		
	}	

	@Override       
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		btnPerRead = (Button)getView().findViewById(R.id.btnperRead);
		btnPerUpdataPwd = (Button)getView().findViewById(R.id.btnperUpdataPwd);
		btnPerUpgradeProgram = (Button)getView().findViewById(R.id.btnperUpgradeProgram);
		txtperAccPwd = (TextView)getView().findViewById(R.id.txtperAccPwd);
		txtperKillPwd = (TextView)getView().findViewById(R.id.txtperKillPwd);
		editNewPwd = (EditText)getView().findViewById(R.id.editperNewPwd);
		spinperPwdType = (Spinner)getView().findViewById(R.id.spinperPwdType);
		setListener();

	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnPerRead.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				do
				{
					String value = "";
					String powerPwd = "00000000";
					if(!Common.IsHex(powerPwd))
					{
						MainActivity.DisplayResult("授权密码格式不对");
						Common.callAlarmAsFailure(getActivity());
						break;
					}
					int cs_ImpowerPassword = Integer.parseInt(powerPwd);
					char[] cTemp;
					Rfid_Value rfid_value = new Rfid_Value();
					cTemp = MainActivity.link.Radio_ReadTag(4,0,0, cs_ImpowerPassword, rfid_value);
					if (rfid_value.value == Result.RFID_STATUS_OK.getValue())
					{
						value = MainActivity.link.c2hexs(cTemp, cTemp.length);
						if (value.length() == 16)
						{
							MainActivity.DisplayResult("获取密码成功");
							String cs_DestoryPassword = value.substring(0, 8);
							String cs_AccessPassword = value.substring(8, 16);
							txtperAccPwd.setText(cs_AccessPassword);
							txtperKillPwd.setText(cs_DestoryPassword);
							Common.callAlarmAsSuccess(getActivity());
						}
						else
						{
							MainActivity.DisplayResult("获取密码失败");
							Common.callAlarmAsFailure(getActivity());
						}
					}
					else
					{
						MainActivity.DisplayResult(rfid_value.value+ "常规错误");
					}
				}
				while (false);
			}

		});

		btnPerUpdataPwd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				int status = Result.RFID_STATUS_OK.getValue();
				int ui_Offset = (int) spinperPwdType.getSelectedItemId();
				String cs_ReadyToChangePAssword = editNewPwd.getText().toString();
				String powerPwd = "00000000";
				do
				{
					if (cs_ReadyToChangePAssword == null || cs_ReadyToChangePAssword.equals("") )
					{
						MainActivity.DisplayResult("值不能为空");
						Common.callAlarmAsFailure(getActivity());
						break;
					}
					if (!Common.IsHex(cs_ReadyToChangePAssword) || !Common.IsHex(powerPwd))
					{
						MainActivity.DisplayResult("密码格式有误");
						Common.callAlarmAsFailure(getActivity());
						break;
					}
					if ((cs_ReadyToChangePAssword.length() % 4) != 0 || cs_ReadyToChangePAssword.length() / 4 != 2)
					{
						status = Result.ACTION_RFID_18K6C_TAG_WRITE_ERROR_DATA.getValue();
						MainActivity.DisplayResult("密码格式或长度错误");
						Common.callAlarmAsFailure(getActivity());
						break;
					}
					int cs_ImpowerPassword = Integer.parseInt(powerPwd);
					char[] writeData = MainActivity.link.s2char(cs_ReadyToChangePAssword);

					if (Result.ACTION_RFID_18K6C_TAG_WRITE_ERROR_DATA.getValue() == status)
					{
						break;
					}
					status = MainActivity.link.Radio_WriteTag(2, (ui_Offset * 2), 0, cs_ImpowerPassword, writeData);
					if (Result.RFID_STATUS_OK.getValue() == status)
					{
						if (ui_Offset == 0)
						{
							MainActivity.DisplayResult("修改销毁密码成功");
						}
						else
						{
							MainActivity.DisplayResult("修改访问密码成功");
						}
						Common.callAlarmAsSuccess(getActivity());
					}
					else
					{
						if (ui_Offset == 0)
						{
							MainActivity.DisplayResult(status + "常规错误");
						}
						else
						{
							MainActivity.DisplayResult(status + "常规错误");
						}
						Common.callAlarmAsFailure(getActivity());
					}
				}
				while (false);
			}

		});
		btnPerUpgradeProgram.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int status = 0;
				status = MainActivity.link.Radio_UpgradeProgram();
				if(status == Result.RFID_STATUS_OK.getValue()){
					MainActivity.DisConnect();
					status = MainActivity.InitRadio();
					if(status == Result.RFID_STATUS_OK.getValue()){
						MainActivity.DisplayResult("恢复出厂设置成功");	
						Common.callAlarmAsSuccess(getActivity());
					}
				}
				else{
					MainActivity.DisplayResult(status + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}
			}

		});
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
