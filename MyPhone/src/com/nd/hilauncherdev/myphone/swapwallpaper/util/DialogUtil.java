package com.nd.hilauncherdev.myphone.swapwallpaper.util;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nd.android.pandahome2.R;

/**
 * dilaog工具类
 * 
 * @author youy
 * 
 */
public class DialogUtil {
	/**
	 * 回调
	 * 
	 * @author youy
	 * 
	 */
	public static interface SelectedDialogCallBack {
		public void getData(KeyValue value, List<KeyValue> list);
	}

	public static class KeyValue {
		public String displayName;
		public String value;
		public boolean isCheck;
		public Object tag;
	}

	public static void showSimpleDialog(final Context context, final int titleId, final List<KeyValue> list, final SelectedDialogCallBack callBack) {

		Dialog dialog = new Dialog(context, R.style.Theme_CustomDialog) {
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.net_traffic_simple_dialog);
				TextView title = (TextView) findViewById(R.id.dialog_title);
				if (titleId == -1) {
					title.setVisibility(View.GONE);
				} else {
					title.setText(titleId);
				}
				ListView listView = (ListView) findViewById(R.id.dialog_data);
				listView.setAdapter(new DataAdapter(context, list));
			}

			class DataAdapter extends BaseAdapter {
				private LayoutInflater inflater;
				private List<KeyValue> list;

				public DataAdapter(Context context, List<KeyValue> list) {
					inflater = LayoutInflater.from(context);
					this.list = list;
				}

				@Override
				public View getView(final int pos, View view, ViewGroup paramViewGroup) {
					final ViewHolder holder;
					if (view == null) {
						holder = new ViewHolder();
						view = inflater.inflate(R.layout.net_traffic_simple_dialog_item, null);
						holder.textView = (TextView) view.findViewById(R.id.dialog_select_item_desc);
						holder.imageView = (ImageView) view.findViewById(R.id.dialog_select_item);
						view.setTag(holder);
					} else {
						holder = (ViewHolder) view.getTag();
					}
					final KeyValue keyValue = list.get(pos);
					holder.textView.setText(keyValue.displayName);
					if (keyValue.isCheck) {
						holder.imageView.setImageResource(R.drawable.myphone_radiobtn_selected);
					} else {
						holder.imageView.setImageResource(R.drawable.myphone_radiobtn_unselected);
					}
					view.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View paramView) {
							callBack.getData(keyValue, list);
							dismiss();
						}
					});
					return view;
				}

				@Override
				public int getCount() {
					return list == null ? 0 : list.size();
				}

				@Override
				public Object getItem(int paramInt) {
					return null;
				}

				@Override
				public long getItemId(int paramInt) {
					return 0;
				}

				class ViewHolder {
					public TextView textView;
					public ImageView imageView;
				}

			}
		};
		dialog.show();
	}
}
