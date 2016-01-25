package com.uhf.rfid;

import com.uhf.constants.Constants.InvMode;
import com.uhf.constants.Constants.RFID_INVENTORY_TAG_AREA;
import com.uhf.constants.Constants.Result;
import com.uhf.rfid.R;
import com.uhf.rfid.InventoryFragment.InventoryStartPara;
import com.uhf.structures.Rfid_Value;
import com.uhf.structures.Single_Inventory_Time_Config;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class InventorySetFragment extends Fragment {

	private Button btnInventorySetMake;
	private Spinner spinInventorySetMode;
	private Spinner spinInventorySetArea;
	private EditText editInventorySetworktime;
	private EditText editInventorySetsleeptime;
	private Single_Inventory_Time_Config  InvConfig;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.inventoryset_tab, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnInventorySetMake = (Button) getView().findViewById(
				R.id.btninventorysetmake);
		spinInventorySetMode = (Spinner) getView().findViewById(
				R.id.spininventorymode);
		spinInventorySetArea = (Spinner) getView().findViewById(
				R.id.spininventorysetcontent);
		editInventorySetworktime = (EditText) getView().findViewById(
				R.id.editinventoryruntime);
		editInventorySetsleeptime = (EditText) getView().findViewById(
				R.id.editinventorysleeptime);
		InvConfig = new Single_Inventory_Time_Config();
		getConfig();
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnInventorySetMake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!MainActivity.m_InitStatus
						|| !InventoryFragment.m_bStopInventoryThread)
					return;
				int result = 0;
				RFID_INVENTORY_TAG_AREA RFID_INVENTORY_TAG;
				InvMode inv_mode = null;
				if (spinInventorySetMode.getSelectedItemId() == 0) {
					inv_mode = InvMode.HighSpeedMode;

				} else if (spinInventorySetMode.getSelectedItemId() == 1) {
					inv_mode = InvMode.IntelligentMode;

				} else if (spinInventorySetMode.getSelectedItemId() == 2) {
					inv_mode = InvMode.LowPowerMode;

				} else if (spinInventorySetMode.getSelectedItemId() == 3) {
					inv_mode = InvMode.UserDefined;
					InvConfig.iWorkTime = Integer
							.parseInt(editInventorySetworktime.getText()
									.toString());
					InvConfig.iRestTime = Integer
							.parseInt(editInventorySetsleeptime.getText()
									.toString());
					if (InvConfig.iRestTime < 50)

					{
						MainActivity.DisplayResult("休息时间必须大于50");
						return;

					}
				}
				if (spinInventorySetArea.getSelectedItemId() == 0) {
					RFID_INVENTORY_TAG = RFID_INVENTORY_TAG_AREA.RFID_INVENTORY_TAG_EPC;
				} else {
					RFID_INVENTORY_TAG = RFID_INVENTORY_TAG_AREA.RFID_INVENTORY_TAG_TID;
				}
				if(MainActivity.link.Radio_SetSingleInvTimeConfig(InvConfig) != 0)
				{
					return;
				}
				result = MainActivity.link.Radio_SetInvMode(inv_mode);
				if (result == Result.RFID_STATUS_OK.getValue()) {
					MainActivity.DisConnect();
					result = MainActivity.InitRadio();
					if (result == Result.RFID_STATUS_OK.getValue()) {
						MainActivity.DisplayResult("设置盘点模式成功");
						Common.callAlarmAsSuccess(getActivity());
					}
				} else {
					MainActivity.DisplayResult(result + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}
				result = MainActivity.link
						.Radio_SetInvTagArea(RFID_INVENTORY_TAG);
				if (result != Result.RFID_STATUS_OK.getValue()) {
					MainActivity.DisplayResult(result + "常规错误");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
			}
		});
	}

	/*
	 * 获取一系列配置
	 */

	public void getConfig() {
		if (!MainActivity.m_InitStatus
				|| !InventoryFragment.m_bStopInventoryThread)
			return;
		do {
			// 盘点模式
			int inv_mode = 0;
			Rfid_Value rfid_value = new Rfid_Value();
			inv_mode = MainActivity.link.Radio_GetInvMode(rfid_value);
			if (rfid_value.value == Result.RFID_STATUS_OK.getValue()) {

				if (inv_mode == InvMode.HighSpeedMode.getValue()) {
					spinInventorySetMode.setSelection(0);
				}

				else if (inv_mode == InvMode.IntelligentMode.getValue()) {
					spinInventorySetMode.setSelection(1);
				} else if (inv_mode == InvMode.LowPowerMode.getValue()) {
					spinInventorySetMode.setSelection(2);

				} else {
					spinInventorySetMode.setSelection(3);
					if(MainActivity.link.Radio_GetSingleInvTimeConfig(InvConfig) != 0)
					{
						return;
					}
				}
			} else {
				break;
			}
			editInventorySetworktime.setText(String
					.valueOf(InvConfig.iWorkTime));
			editInventorySetsleeptime.setText(String
					.valueOf(InvConfig.iRestTime));
		} while (false);
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
