package com.uhf.rfid;

import com.uhf.constants.Constants.Result;
import com.uhf.rfid.R;
import com.uhf.structures.Rfid_Value;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;


public class PowerFragment extends Fragment{
	private Button btnPowerTabSet;
	private Button btnPowerTabLianJie;
	private SeekBar seekPowerValue;
	private TextView txtPowerTabGetValue;
	private TextView txtPowerTabCurrentValue;
	private Spinner spinPowerTabLianjie;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.power_tab, null);		
	}	
	@Override       
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);	
		btnPowerTabSet = (Button)getView().findViewById(R.id.btnpowerSet);
		btnPowerTabLianJie = (Button)getView().findViewById(R.id.btnpowerLianjie);
		seekPowerValue = (SeekBar)getView().findViewById(R.id.seekpowerValue);
		txtPowerTabGetValue = (TextView)getView().findViewById(R.id.txtpowerGetValue);
		txtPowerTabCurrentValue = (TextView)getView().findViewById(R.id.txtpowerCurrentValue);
		spinPowerTabLianjie =(Spinner)getView().findViewById(R.id.spinpowerLianJie);
		getConfig();
		setListener();
	}
	private void setListener() {
		// TODO Auto-generated method stub
		btnPowerTabSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				int status = Result.RFID_STATUS_OK.getValue();
				int iu32 = (int)seekPowerValue.getProgress();

				status = MainActivity.link.Radio_SetAntennaPower(iu32*10);
				if (status == Result.RFID_STATUS_OK.getValue())
				{
					MainActivity.DisConnect();
					status = MainActivity.InitRadio();
					if(status == Result.RFID_STATUS_OK.getValue()){
						MainActivity.DisplayResult("修改功率成功");	
						txtPowerTabGetValue.setText(String.valueOf(iu32)); 
						txtPowerTabCurrentValue.setText(String.valueOf(iu32));
						Common.callAlarmAsSuccess(getActivity());
					}
				}
				else
				{
					MainActivity.DisplayResult(status + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}		
			}

		});
		btnPowerTabLianJie.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				int status = MainActivity.link.Radio_SetCurrentLinkProfile((int)spinPowerTabLianjie.getSelectedItemId());

				if (status == Result.RFID_STATUS_OK.getValue()){
					MainActivity.DisplayResult("设置链接成功");	
					Common.callAlarmAsSuccess(getActivity());

				}  
				else{
					MainActivity.DisplayResult(status + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}
			}

		});
		seekPowerValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{  
			public void onProgressChanged(SeekBar arg0,int progress,boolean fromUser)  
			{   
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				if(progress < 13){
					seekPowerValue.setProgress(13);
					return;
				}
				txtPowerTabGetValue.setText(String.valueOf(progress));  
			}              
			@Override  
			public void onStartTrackingTouch(SeekBar seekBar) {  
				// TODO Auto-generated method stub              
			}  
			@Override  
			public void onStopTrackingTouch(SeekBar seekBar) {  
				// TODO Auto-generated method stub  

			}  
		});  

	}
	/*
	 * 获取配置
	 * */
	public  void getConfig(){
		if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
			return;
		do
		{
			Rfid_Value rfid_value = new Rfid_Value();
			//链接
			int iProfile = 0;
			iProfile = MainActivity.link.Radio_GetCurrentLinkProfile(rfid_value);
			if (rfid_value.value == Result.RFID_STATUS_OK.getValue())
			{
				if (iProfile == 0){
					spinPowerTabLianjie.setSelection(0);
				}		
				else if (iProfile == 1){
					spinPowerTabLianjie.setSelection(1);
				}			
				else if (iProfile == 2){
					spinPowerTabLianjie.setSelection(2);
				}			
				else if (iProfile == 3){
					spinPowerTabLianjie.setSelection(3);
				}		
			}
			else
			{
				break;
			}

			//天线功率
			int AntennaPower = MainActivity.link.Radio_GetAntennaPower(rfid_value);
			if (rfid_value.value == Result.RFID_STATUS_OK.getValue())
			{
				seekPowerValue.setProgress(AntennaPower/10);
				txtPowerTabGetValue.setText(String.valueOf(seekPowerValue.getProgress()));
				txtPowerTabCurrentValue.setText(String.valueOf(seekPowerValue.getProgress()));
			}
		}while(false);
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

