package org.androidpn.client;

import org.androidpn.demoapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
/**
 * 
 * @author 开机自启广播
 *
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//判断用户是否勾选了开机自启
		Toast.makeText(context, "推送已自动开启", 1).show();
		SharedPreferences pref = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (pref.getBoolean(Constants.SETTINGS_AUTO_START, true)) {
	        ServiceManager serviceManager = new ServiceManager(context);
	        serviceManager.setNotificationIcon(R.drawable.notification);
	        serviceManager.startService();
		}
	}

}
