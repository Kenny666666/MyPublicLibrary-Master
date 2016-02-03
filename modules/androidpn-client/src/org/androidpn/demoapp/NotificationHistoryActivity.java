package org.androidpn.demoapp;

import java.util.ArrayList;
import java.util.List;

import org.androidpn.client.Constants;
import org.androidpn.client.NotificationDetailsActivity;
import org.androidpn.client.NotificationHistory;
import org.litepal.crud.DataSupport;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 显示历史消息的activity
 * @author hugs
 *
 */
public class NotificationHistoryActivity extends Activity {
	private ListView mListView;
	private NotificationHistoryAdapter mAdapter;
	private List<NotificationHistory> mList = new ArrayList<NotificationHistory>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_history);
		//查询出所有历史消息
		mList = DataSupport.findAll(NotificationHistory.class);
		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View a, int index,long arg3) {
				NotificationHistory history = mList.get(index);
				
		        Intent intent = new Intent(NotificationHistoryActivity.this,NotificationDetailsActivity.class);
	            intent.putExtra(Constants.NOTIFICATION_API_KEY, history.getApiKey());
	            intent.putExtra(Constants.NOTIFICATION_TITLE, history.getTitle());
	            intent.putExtra(Constants.NOTIFICATION_MESSAGE, history.getMessage());
	            intent.putExtra(Constants.NOTIFICATION_URI, history.getUri());
	            intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, history.getImageUrl());
	            startActivity(intent);
			}
			
		});
		mAdapter = new NotificationHistoryAdapter(this, 0, mList);
		mListView.setAdapter(mAdapter);
		//注册菜单
		registerForContextMenu(mListView);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//1、groupId,2、菜单itemId,3、排序,4、显示的文字
		menu.add(0,0,0,"删除");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId()==0) {
			AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
			int index = menuInfo.position;
			NotificationHistory history = mList.get(index);
			history.delete();
			mList.remove(index);
			mAdapter.notifyDataSetChanged();
		}
		return super.onContextItemSelected(item);
	}
	
	class NotificationHistoryAdapter extends ArrayAdapter<NotificationHistory>{

		public NotificationHistoryAdapter(Context context,int textViewResourceId, List<NotificationHistory> objects) {
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NotificationHistory history = getItem(position);
			View view;
			if (convertView == null) {
				//inflate第二个参数的意思：他是一个int型(指传布局名)，给当前布局再加入一个父布局，没需求一般传Null
				view = LayoutInflater.from(getContext()).inflate(R.layout.notification_history_item, null);
			}else {
				view = convertView;
			}
			TextView titleTextView = (TextView)view.findViewById(R.id.tv_title);
			TextView timeTextView = (TextView)view.findViewById(R.id.tv_time);
			titleTextView.setText(history.getTitle());
			timeTextView.setText(history.getTime());
			
			return view;
		}
	}
}
