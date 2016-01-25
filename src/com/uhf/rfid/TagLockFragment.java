package com.uhf.rfid;

import com.uhf.constants.Constants.RFID_18K6C_TAG_MEM_PERM;
import com.uhf.constants.Constants.RFID_18K6C_TAG_PWD_PERM;
import com.uhf.constants.Constants.Result;
import com.uhf.rfid.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TagLockFragment extends Fragment{

	private Button btnTagLockMake;
	private Spinner  spinTagLockAccPwdArea;
	private Spinner spinTagLockKillPwdArea;
	private Spinner spinTagLockEPC;
	private Spinner spinTagLockTID;
	private Spinner spinTagLockUSER;
	private EditText editTagLockAccPwd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.taglock_tab, null);		
	}	
	@Override       
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);	
		btnTagLockMake = (Button)getView().findViewById(R.id.btntaglockmake);
		spinTagLockAccPwdArea = (Spinner)getView().findViewById(R.id.spintagLockAccPwdArea);
		spinTagLockKillPwdArea = (Spinner)getView().findViewById(R.id.spintaglockKillPwdArea);
		spinTagLockEPC = (Spinner)getView().findViewById(R.id.spintaglockEpc);
		spinTagLockTID = (Spinner)getView().findViewById(R.id.spintaglockTid);
		spinTagLockUSER =(Spinner)getView().findViewById(R.id.spintaglockUser);
		editTagLockAccPwd = (EditText)getView().findViewById(R.id.edittaglockAccPwd);
		setListener();

	}
	private void setListener() {
		// TODO Auto-generated method stub
		btnTagLockMake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				String editAccPwd = editTagLockAccPwd.getText().toString().trim().replace(" ", "");
				if (editAccPwd == null || editTagLockAccPwd.equals(""))
				{
					MainActivity.DisplayResult("密码不能为空");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				if (!Common.IsHex(editAccPwd))
				{
					MainActivity.DisplayResult("密码格式不对");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				int accesspassword = Integer.parseInt(editAccPwd);
				int result = MainActivity.link.Radio_LockTag(accesspassword,
						GetPasswordChoicePermission(spinTagLockAccPwdArea),
						GetPasswordChoicePermission(spinTagLockKillPwdArea),
						GetMemoryChoicePermission(spinTagLockEPC),
						GetMemoryChoicePermission(spinTagLockTID),
						GetMemoryChoicePermission(spinTagLockUSER));

				if (result == Result.RFID_STATUS_OK.getValue())
				{
					MainActivity.DisplayResult("执行成功");
					Common.callAlarmAsSuccess(getActivity());
				}
				else
				{
					MainActivity.DisplayResult(result + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}

			}

		});
	}

	private int GetPasswordChoicePermission(Spinner objComb)
	{
		switch ((int)objComb.getSelectedItemId())
		{
		case 0:
			return RFID_18K6C_TAG_PWD_PERM.NO_CHANGE.getValue();
		case 1:
			return RFID_18K6C_TAG_PWD_PERM.ACCESSIBLE.getValue();
		case 2:
			return RFID_18K6C_TAG_PWD_PERM.ALWAYS_ACCESSIBLE.getValue();
		case 3:
			return RFID_18K6C_TAG_PWD_PERM.SECURED_ACCESSIBLE.getValue();
		case 4:
			return RFID_18K6C_TAG_PWD_PERM.ALWAYS_NOT_ACCESSIBLE.getValue();
		default:
			return RFID_18K6C_TAG_PWD_PERM.NO_CHANGE.getValue();
		}
	}

	private int GetMemoryChoicePermission(Spinner objComb)
	{
		switch ((int)objComb.getSelectedItemId())
		{
		case 0:
			return RFID_18K6C_TAG_MEM_PERM.NO_CHANGE.getValue();
		case 1:
			return RFID_18K6C_TAG_MEM_PERM.WRITEABLE.getValue();
		case 2:
			return RFID_18K6C_TAG_MEM_PERM.ALWAYS_WRITEABLE.getValue();
		case 3:
			return RFID_18K6C_TAG_MEM_PERM.SECURED_WRITEABLE.getValue();
		case 4:
			return RFID_18K6C_TAG_MEM_PERM.ALWAYS_NOT_WRITEABLE.getValue();
		default:
			return RFID_18K6C_TAG_MEM_PERM.NO_CHANGE.getValue();
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
