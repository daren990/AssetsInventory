package com.uhf.rfid;

import com.uhf.rfid.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

public class Common {

	/**
	 * 判断十六进制
	 * @param str
	 * @return
	 */
	public  static boolean IsHex(String str)
	{
		boolean b = false;
		char[] c = str.toUpperCase().toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if ((c[i] >='0'  && c[i]<='9' ) || (c[i] >='A' && c[i] <='F'))
			{
				b = true;         
			}
			else
			{
				b = false;
				break;
			}
		}
		return b;
	}
	/**
	 * 失败提示音
	 * @param context
	 */
	public static void callAlarmAsFailure(Context context) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); // 播放提示音
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		int current = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		MediaPlayer player = MediaPlayer.create(context, R.raw.failure);
		player.setVolume((float) current / (float) max, (float) current / (float) max); // 设置提示音量
		player.start();// 播放提示音
	}
	/**
	 * 成功提示音
	 * @param context
	 */
	public static void callAlarmAsSuccess(Context context) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); // 播放提示音
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		int current = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		MediaPlayer player = MediaPlayer.create(context, R.raw.readcard);
		player.setVolume((float) current / (float) max, (float) current / (float) max); // 设置提示音量
		player.start();// 播放提示音
	}

	public static void send(Handler handler, int what) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	public static void send(Handler handler, int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	public static void send(Handler handler, int what, int arg1) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}

	public static void send(Handler handler, int what, int arg1, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	public static void send(Handler handler, int what, int arg1, int arg2) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}
	public static void send(Handler handler, int what, int arg1, int arg2,Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
}
