package com.uhf.rfid;
import com.uhf.constants.Constants.MemoryBank;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ReadWriteFragment extends Fragment{

	private Button btnReadWriteTabRead;
	private Button btnReadWriteTabWrite;
	private Spinner spinReadWriteArea;
	private EditText editReadWiteOffset;
	private EditText editReadWriteCount;
	private EditText editReadWritePwd;
	private TextView editReadWriteReadValue;
	private EditText editReadWriteWriteVlue;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		return inflater.inflate(R.layout.readwrite_tab, null);	
	}	
	@Override       
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		btnReadWriteTabRead = (Button)getView().findViewById(R.id.btnReadWriteReadValue);
		btnReadWriteTabWrite =(Button)getView().findViewById(R.id.btnReadWriteWriteValue);
		spinReadWriteArea = (Spinner)getView().findViewById(R.id.spinReadWriteArea);
		editReadWiteOffset = (EditText)getView().findViewById(R.id.editReadWriteOffset);
		editReadWriteCount = (EditText)getView().findViewById(R.id.editReadWriteCount);
		editReadWritePwd = (EditText)getView().findViewById(R.id.editReadWritePwd);
		editReadWriteReadValue =(TextView)getView().findViewById(R.id.editReadWriteReadValue);
		editReadWriteWriteVlue=(EditText)getView().findViewById(R.id.editReadWriteWriteValue);
		setListener();
	}
	private void setListener() {
		// TODO Auto-generated method stub
		btnReadWriteTabRead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				Rfid_Value result = new Rfid_Value(); 
				int accesspassword = 0;//密码
				int offset = 0;//起始地址
				int count = 0;//长度
				char[] ReadText;
				int bank = 0;
				String pwd = editReadWritePwd.getText().toString().trim().replace(" ", "");

				if (pwd == null || pwd.equals(""))
				{
					MainActivity.DisplayResult("值不能为空");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				if (!Common.IsHex(pwd))
				{
					MainActivity.DisplayResult("输入的值有误");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				accesspassword = Integer.parseInt(editReadWritePwd.getText().toString().trim().replace(" ", ""));
				offset = Integer.parseInt(editReadWiteOffset.getText().toString().trim().replace(" ", ""));
				count = Integer.parseInt(editReadWriteCount.getText().toString().trim().replace(" ", ""));
				int id = (int) spinReadWriteArea.getSelectedItemId();
				switch (id)
				{
				case 0:
					bank = MemoryBank.EPC.getValue();
					break;
				case 1:
					bank = MemoryBank.TID.getValue();
					break;
				case 2:
					bank = MemoryBank.USER.getValue();
					break;
				case 3:
					bank = MemoryBank.RESERVED.getValue();
					break;
				default:
					break;
				}
				ReadText = MainActivity.link.Radio_ReadTag(count, offset, bank, accesspassword, result);
				if (result.value == Result.RFID_STATUS_OK.getValue())
				{
					MainActivity.DisplayResult("读取成功");
					String strtt = "";
					strtt = MainActivity.link.c2hexs(ReadText,ReadText.length);
					editReadWriteReadValue.setText(strtt);
					Common.callAlarmAsSuccess(getActivity());
				}
				else
				{
					editReadWriteReadValue.setText("");
					MainActivity.DisplayResult(result.value + "常规错误");
					Common.callAlarmAsFailure(getActivity());
				}

			}
		});
		btnReadWriteTabWrite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!MainActivity.m_InitStatus || !InventoryFragment.m_bStopInventoryThread)
					return;
				int bank = 0;
				int offset = 0;
				int count = 0;
				int pwd = 0;
				int result = Result.RFID_STATUS_OK.getValue();
				String value = editReadWriteWriteVlue.getText().toString().trim().replace(" ", "");
				String textBoxAddressOffset = editReadWiteOffset.getText().toString().trim().replace(" ", "");
				String textBoxReadWriteLength = editReadWriteCount.getText().toString().trim().replace(" ", "");
				String txtpwd = editReadWritePwd.getText().toString().trim().replace(" ", "");
				if ((value == null || value.equals(""))
						|| (textBoxAddressOffset == null || textBoxAddressOffset.equals(""))
						|| (textBoxReadWriteLength == null || textBoxReadWriteLength.equals(""))
						|| (txtpwd == null || txtpwd.equals("")))
				{
					MainActivity.DisplayResult("值不能为空");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				if (!Common.IsHex(value) || !Common.IsHex(textBoxAddressOffset) || !Common.IsHex(textBoxReadWriteLength) || !Common.IsHex(txtpwd))
				{
					MainActivity.DisplayResult("输入的值有误");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				offset = Integer.parseInt(textBoxAddressOffset);
				count = Integer.parseInt(textBoxReadWriteLength);
				pwd = Integer.parseInt(txtpwd);
				int id = (int) spinReadWriteArea.getSelectedItemId();
				switch (id)
				{
				case 0:
					bank = MemoryBank.EPC.getValue();
					break;
				case 1:
					bank = MemoryBank.TID.getValue();
					break;
				case 2:
					bank =MemoryBank.USER.getValue();
					break;
				case 3:
					bank = MemoryBank.RESERVED.getValue();
					break;
				default:
					break;
				}
				if (id == 1)
				{
					MainActivity.DisplayResult("TID不可写入");
					Common.callAlarmAsFailure(getActivity());
					return;
				}

				if ((value.length() % 4) != 0)
				{
					MainActivity.DisplayResult("写入长度有误");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				if ((id == 0 && offset == 0) || (id == 0 && offset == 1))
				{
					MainActivity.DisplayResult("起始地址有误");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				if ((value.length() / 4) != count)
				{
					MainActivity.DisplayResult("写入长度有误");
					Common.callAlarmAsFailure(getActivity());
					return;
				}
				char[] WriteText=  MainActivity.link.s2char(value);
				result = MainActivity.link.Radio_WriteTag(count, offset, bank, pwd, WriteText);
				if (result == Result.RFID_STATUS_OK.getValue())
				{
					MainActivity.DisplayResult("写入成功");
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