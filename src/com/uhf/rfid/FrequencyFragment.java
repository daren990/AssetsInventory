package com.uhf.rfid;

import com.uhf.constants.Constants.RFID_18K6C_COUNTRY_REGION;
import com.uhf.constants.Constants.Result;
import com.uhf.rfid.R;
import com.uhf.structures.Frequency_Region;
import com.uhf.structures.Rfid_Value;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class FrequencyFragment extends Fragment{

	private  EditText editFrequencyTabPinDian;
	private  Spinner spinFrequencyTabRegion;
	private  Button btnFrequencyRegion;
	private  Button btnFrequencyPinDian;
	private  Button btnFrequencyAdd;
	private  Button btnFrequencyMinus;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.frequency_tab, null);		
	}	
	@Override       
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		editFrequencyTabPinDian = (EditText)getView().findViewById(R.id.editFrequencyPinDian);		
		spinFrequencyTabRegion = (Spinner)getView().findViewById(R.id.spinFrequencyRegion);
		btnFrequencyRegion = (Button)getView().findViewById(R.id.btnFrequencyRegion);
		btnFrequencyPinDian = (Button)getView().findViewById(R.id.btnFrequencyPinDian);
		btnFrequencyAdd = (Button)getView().findViewById(R.id.btnFrequencyAdd);
		btnFrequencyMinus = (Button)getView().findViewById(R.id.btnFrequencyMinus);
		GetConfig();
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnFrequencyRegion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				RFID_18K6C_COUNTRY_REGION  region = null;
				int uRegionIndex = (int) spinFrequencyTabRegion.getSelectedItemId();
				switch(uRegionIndex){
				case 0:
					region = RFID_18K6C_COUNTRY_REGION.China840_845;
					break;
				case 1:
					region = RFID_18K6C_COUNTRY_REGION.China920_925;
					break;
				case 2:
					region = RFID_18K6C_COUNTRY_REGION.Open_Area902_928;
				}
				int result = MainActivity.link.Radio_MacSetRegion(region);
				if (result == Result.RFID_STATUS_OK.getValue())
				{
					MainActivity.DisConnect();
					result = MainActivity.InitRadio();
					if(result == Result.RFID_STATUS_OK.getValue()){
						MainActivity.DisplayResult("设置区域成功");
						Common.callAlarmAsSuccess(getActivity());
					}
				}
				else
				{
					MainActivity.DisplayResult(result+ "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}	
			}
		});
		btnFrequencyPinDian.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				RFID_18K6C_COUNTRY_REGION country =null;
				if (spinFrequencyTabRegion.getSelectedItemId() == 0 || spinFrequencyTabRegion.getSelectedItemId() == 1){
					country = RFID_18K6C_COUNTRY_REGION.China840_845;
				}
				else if(spinFrequencyTabRegion.getSelectedItemId() == 2){					
					country = RFID_18K6C_COUNTRY_REGION.Open_Area902_928;  
				}
				double singleFrequency = Double.parseDouble(editFrequencyTabPinDian.getText().toString());
				int result = Result.RFID_STATUS_OK.getValue();
				result = MainActivity.link.Radio_SetSingleFrequency(singleFrequency, country);
				if (result == Result.RFID_STATUS_OK.getValue()){
					MainActivity.DisConnect();
					result = MainActivity.InitRadio();
					if(result == Result.RFID_STATUS_OK.getValue()){
						MainActivity.DisplayResult("设置频点成功");
						Common.callAlarmAsSuccess(getActivity());
					}			
				}
				else
				{
					MainActivity.DisplayResult(result + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}
			}
		});
		btnFrequencyAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				double singleFrequency = Double.parseDouble(editFrequencyTabPinDian.getText().toString());
				singleFrequency += 0.25;
				editFrequencyTabPinDian.setText(String.valueOf(singleFrequency));
			}
		});
		btnFrequencyMinus.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				double singleFrequency = Double.parseDouble(editFrequencyTabPinDian.getText().toString());
				if ((singleFrequency - 0.25) > 0)
					singleFrequency -= 0.25;
				else
					return;
				editFrequencyTabPinDian.setText(String.valueOf(singleFrequency));
			}

		});
	}

	public void GetConfig(){
		if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
			return;
		do
		{
			Rfid_Value rfid_value = new Rfid_Value();
			int result = Result.RFID_STATUS_OK.getValue();		
			//定频
			double FrequencyValue = MainActivity.link.Radio_GetSingleFrequency(rfid_value);
			if (rfid_value.value == Result.RFID_STATUS_OK.getValue())
			{	
				editFrequencyTabPinDian.setText(String.valueOf(FrequencyValue));
			}
			else
			{
				break;
			}

			//频段
			Frequency_Region region = new Frequency_Region();
			result = MainActivity.link.Radio_MacGetRegion(region);
			if (result == Result.RFID_STATUS_OK.getValue())
			{
				if (region.iLowFrequency == 840 && region.iHeighFrequency == 845){
					spinFrequencyTabRegion.setSelection(0);
				}            
				else if (region.iLowFrequency == 920 && region.iHeighFrequency == 925){
					spinFrequencyTabRegion.setSelection(1);
				}                   
				else if (region.iLowFrequency == 902 && region.iHeighFrequency == 928){
					spinFrequencyTabRegion.setSelection(2);
				}            
			}
			else
			{
				break;
			}
		}
		while(false);
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
