package com.nd.calendar.Control;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.calendar.CommData.CityInfo;
import com.nd.weather.widget.R;

// 城市列表
public class DragListAdapter extends ArrayAdapter<CityInfo> {

    public final static int MODE_NORMAL = 0;
    public final static int MODE_DELETE = 1;
    public final static int MODE_ORDER = 2;
    private int mMode = MODE_NORMAL;

    private LayoutInflater mInflater;
    List<CityInfo> m_listInfo;

    public DragListAdapter(Context context, List<CityInfo> objects) {

        super(context, 0, objects);
        m_listInfo = objects;
        mInflater = LayoutInflater.from(context);
    }

    // 设置显示模式
    public int getMode() {
        return mMode;
    }

    public void setMode(int iMode) {
        this.mMode = iMode;
    }

    class ViewHolder {
        TextView textView;
        TextView tvNote;
        ImageView imageNormal;
        ImageView imageDrag;
        ImageView imagePosition;
        CheckBox checkDel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vh = null;

        if (view == null) {
            view = mInflater.inflate(R.layout.weather_drag_list_item, parent, false);
            vh = new ViewHolder();
            vh.textView = (TextView) view.findViewById(R.id.drag_list_item_text);
            vh.tvNote = (TextView) view.findViewById(R.id.drag_list_item_note);
            vh.imageNormal = (ImageView) view.findViewById(R.id.normal_list_item_image);
            vh.imageDrag = (ImageView) view.findViewById(R.id.drag_list_item_image);
            vh.imagePosition = (ImageView) view.findViewById(R.id.position_list_item_image);
            vh.checkDel = (CheckBox) view.findViewById(R.id.drag_list_item_chk_delete);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        CityInfo info = m_listInfo.get(position);

        if (mMode == MODE_DELETE) { // 删除模式
            vh.checkDel.setTag(position);
            vh.imageDrag.setVisibility(View.GONE);
            vh.checkDel.setVisibility(View.VISIBLE);
            vh.checkDel.setChecked(info.isDeleteState());
        } else { // 调整模式
            vh.checkDel.setVisibility(View.GONE);
            if (mMode == MODE_ORDER) {
                vh.imageDrag.setVisibility(View.VISIBLE);
            } else {
                vh.imageDrag.setVisibility(View.GONE);
            }
        }

        // 当前是gps定位出来的显示gps标记
        if (info.getFromGps() == 1) {
            vh.imageNormal.setVisibility(View.GONE);
            vh.imagePosition.setVisibility(View.VISIBLE);
        } else {
            vh.imagePosition.setVisibility(View.GONE);
            vh.imageNormal.setVisibility(View.VISIBLE);
        }

        vh.textView.setText(info.getName());
        vh.tvNote.setText("[" + info.getProvName() + "]");
        return view;

    }

    public void setDeleteCheck(View v) {
        ViewHolder vh = (ViewHolder) v.getTag();
        vh.checkDel.setChecked(!vh.checkDel.isChecked());
        int position = (Integer) vh.checkDel.getTag();
        if (getCount() > position) {
            getItem(position).setDeleteState(vh.checkDel.isChecked());
        }
    }

}