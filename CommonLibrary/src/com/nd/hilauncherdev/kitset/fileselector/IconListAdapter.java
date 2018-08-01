package com.nd.hilauncherdev.kitset.fileselector;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.fileselector.IconModel.IconPack;
import com.nd.hilauncherdev.kitset.util.IconUtils;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;

/**
 * 图标列表适配器
 */
public class IconListAdapter extends ArrayAdapter<SDCardFile> {

	private final LayoutInflater mInflater;

	private Context mContext;
	private Drawable defaultDrawable;

	public IconListAdapter(Context context, int textResourceId,
			List<SDCardFile> apps) {
		super(context, 0, apps);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		defaultDrawable = mContext.getResources().getDrawable(
				R.drawable.file_browser_image48);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SDCardFile file = getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.file_browser_list_item_icon_text,
					parent, false);
			// convertView.setBackgroundDrawable(R.drawable.)
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			// holder.text.setTextColor(colors)
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setTextColor(Color.BLACK);
		if (file.type > 0) {
			switch (file.type) {
			case SDCardFile.ICON_FILE_DIR:
				file.setIcon(mContext.getResources().getDrawable(
						R.drawable.file_browser_folder48));
				file.setTitle(mContext.getString(R.string.select_third_icons));
				break;
			case SDCardFile.ICON_FILE_PACK:
				file.setIcon(mContext.getResources().getDrawable(
						R.drawable.file_browser_folder48));
				if (file.getTag() instanceof IconPack) {
					file.setTitle(((IconPack) file.getTag()).name);
				}
				break;
			case SDCardFile.ICON_FILE:
				String tag = "" + file.getTag();
				Drawable d = IconFactory.getIconDrawable(mContext, tag);
				if (d == null) {
					d = mContext.getResources()
							.getDrawable(R.drawable.file_browser_unknow32);
				}
				file.setIcon(d);
				file.setTitle("");
				break;
			}
		} else {
			if (!file.filtered) {
				if (file.getIcon() == null) {
					if (!file.isNULLFile()) {
						File javaFile = file.getFile();
						String fileName = javaFile.getName();
						String prex = fileName.substring(fileName
								.lastIndexOf(".") + 1, fileName.length());
						if (javaFile.isDirectory()) {
							file.setIcon(mContext.getResources().getDrawable(
									R.drawable.file_browser_folder48));
						} else if (ThemeGlobal.SUFFIX_APT_THEME.equalsIgnoreCase(prex)) {
							file.setIcon(mContext.getResources().getDrawable(
									R.drawable.file_browser_theme_apt));
						} else if ("zip".equalsIgnoreCase(prex)) {
							file.setIcon(mContext.getResources().getDrawable(
									R.drawable.file_browser_zip32));
						} else if ("txt".equalsIgnoreCase(prex)) {
							file.setIcon(mContext.getResources().getDrawable(
									R.drawable.file_browser_text48));
						} else if ("pdf".equalsIgnoreCase(prex)) {
							file.setIcon(mContext.getResources().getDrawable(
									R.drawable.file_browser_pdf32));
						} else if ("xml".equalsIgnoreCase(prex)) {
							file.setIcon(mContext.getResources().getDrawable(
									R.drawable.file_browser_xml32));
						} else if ("jpg".equalsIgnoreCase(prex)
								|| "png".equalsIgnoreCase(prex)
								|| "gif".equalsIgnoreCase(prex)
								|| "a".equalsIgnoreCase(prex)
								|| "b".equalsIgnoreCase(prex)) {
							file.setIcon(mContext.getResources().getDrawable(R.drawable.file_browser_image48));
						} else {
							file.setIcon(mContext.getResources().getDrawable(R.drawable.file_browser_unknow32));
						}
					}
				}
				try {
					if (file.getIcon() != null) {
						file.setIcon(IconUtils.createIconDrawable(file
								.getIcon(), getContext()));
					}
				} catch (Exception e) {
					file.setIcon(defaultDrawable);
				}

				file.filtered = true;
			}
		}

		holder.text.setSingleLine(true);
		holder.text.setMarqueeRepeatLimit(100);
		holder.text.setEllipsize(TruncateAt.MARQUEE);
		// holder.icon.setImageDrawable(ThemeFormart.createFixIconDrawable(file.getFile().getAbsolutePath(),false));
		holder.icon.setImageDrawable(file.getIcon());
		String title = file.getTitle();
		if (title != null) {
			int ix = title.lastIndexOf('.');
			if (ix > 0) {
				title = title.substring(0, ix);
			}
			holder.text.setText(title);
		} else {
			holder.text.setText("");
		}
		if (file.type > 0) {
			holder.text.setTextColor(Color.BLUE);
		}
		// holder.text.setCompoundDrawablesWithIntrinsicBounds(file.getIcon(),
		// null, null, null);
		// convertView.setBackgroundResource(R.drawable.list_selector);
		return convertView;
	}

	static class ViewHolder {
		TextView text;

		ImageView icon;
	}
}
