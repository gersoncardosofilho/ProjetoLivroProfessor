/**
 * Copyright (C) 2003-2017, Foxit Software Inc..
 * All Rights Reserved.
 * <p>
 * http://www.foxitsoftware.com
 * <p>
 * The following code is copyrighted and is the proprietary of Foxit Software Inc.. It is not allowed to
 * distribute any parts of Foxit Mobile PDF SDK to third party or public without permission unless an agreement
 * is signed between Foxit Software Inc. and customers to explicitly grant customers permissions.
 * Review legal.txt for additional license and legal information.
 */
package com.foxit.uiextensions.controls.propertybar.imp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foxit.uiextensions.R;
import com.foxit.uiextensions.utils.AppDisplay;


public class FontAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mFontNames;
    private boolean[] mFontChecked;

    public FontAdapter(Context context, String[] fontNames, boolean[] fontChecked) {
        this.mContext = context;
        this.mFontNames = fontNames;
        this.mFontChecked = fontChecked;
    }

    @Override
    public int getCount() {
        return mFontNames.length;
    }

    @Override
    public Object getItem(int position) {
        return mFontNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pb_fontstyle_fontitem, null, false);

            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            if (AppDisplay.getInstance(mContext).isPad()) {
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.ux_list_item_height_1l_pad);
            } else {
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.ux_list_item_height_1l_phone);
            }
            convertView.setLayoutParams(layoutParams);
            int padding = (int) mContext.getResources().getDimension(R.dimen.ux_horz_left_margin_phone);
            convertView.setPadding(padding, 0, padding, 0);

            holder.pb_tv_fontItem = (TextView) convertView.findViewById(R.id.pb_tv_fontItem);
            holder.pb_iv_fontItem_selected = (ImageView) convertView.findViewById(R.id.pb_iv_fontItem_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pb_tv_fontItem.setText(mFontNames[position]);
        holder.pb_iv_fontItem_selected.setImageResource(R.drawable.pb_selected);
        if (mFontChecked[position]) {
            holder.pb_tv_fontItem.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_button_colour));
            holder.pb_iv_fontItem_selected.setVisibility(View.VISIBLE);
        } else {
            holder.pb_tv_fontItem.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body1_gray));
            holder.pb_iv_fontItem_selected.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class ViewHolder {
        private TextView pb_tv_fontItem;
        private ImageView pb_iv_fontItem_selected;
    }
}
