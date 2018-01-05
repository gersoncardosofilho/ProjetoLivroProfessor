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
package com.foxit.uiextensions.controls.toolbar.impl;


import android.content.Context;
import android.text.InputFilter;
import android.view.View;

import com.foxit.uiextensions.R;
import com.foxit.uiextensions.controls.toolbar.BaseBar;
import com.foxit.uiextensions.controls.toolbar.IBaseItem;
import com.foxit.uiextensions.controls.toolbar.IBarsHandler;
import com.foxit.uiextensions.pdfreader.impl.MainFrame;
import com.foxit.uiextensions.utils.AppDisplay;

public class BaseBarManager implements IBarsHandler {
    private static final int BOTTOM_TEXT_MAX_LEGNTH = 8;
    private static final int TOP_TEXT_MAX_LEGNTH = 15;

    private MainFrame mMainFrame;
    private Context mContext;

    public BaseBarManager(Context context, MainFrame mainFrame) {
        mMainFrame = mainFrame;
        mContext = context;
    }

    @Override
    public boolean addItem(BarName barName, BaseBar.TB_Position gravity, IBaseItem item, int index) {
        if (null == barName || null == gravity || null == item) {
            return false;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            item.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TOP_TEXT_MAX_LEGNTH)});
            item.setText(item.getText());
            return mMainFrame.getTopToolbar().addView(item, gravity, index);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            item.setFilters(new InputFilter[]{new InputFilter.LengthFilter(BOTTOM_TEXT_MAX_LEGNTH)});
            item.setText(item.getText());
            return mMainFrame.getBottomToolbar().addView(item, gravity, index);
        }
        return false;
    }

    @Override
    public boolean addItem(BarName barName, BaseBar.TB_Position gravity, int textId, int resId, int index, final IItemClickListener clickListener) {
        if (null == barName || null == gravity) {
            return false;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            BaseItemImpl item = new BaseItemImpl(mContext);
            item.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TOP_TEXT_MAX_LEGNTH)});
            if (textId > 0)
                item.setText(textId);
            if (resId > 0)
                item.setImageResource(resId);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v);
                    }
                }
            });
            return mMainFrame.getTopToolbar().addView(item, gravity, index);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {

            int circleResId = R.drawable.rd_bar_circle_bg_selector;
            int textSize = mContext.getResources().getDimensionPixelSize(R.dimen.ux_text_height_toolbar);
            int textColorResId = R.color.ux_text_color_body2_dark;
            int interval = mContext.getResources().getDimensionPixelSize(R.dimen.ux_toolbar_button_icon_text_vert_interval);

            CircleItemImpl circleItem = new CircleItemImpl(mContext.getApplicationContext());
            circleItem.setFilters(new InputFilter[]{new InputFilter.LengthFilter(BOTTOM_TEXT_MAX_LEGNTH)});
            if (textId > 0)
                circleItem.setText(textId);
            if (resId > 0)
                circleItem.setImageResource(resId);
            circleItem.setCircleRes(circleResId);
            circleItem.setRelation(BaseItemImpl.RELATION_BELOW);
            circleItem.setInterval(interval);
            circleItem.setTextSize(AppDisplay.getInstance(mContext).px2dp(textSize));
            circleItem.setTextColorResource(textColorResId);
            circleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v);
                    }
                }
            });
            return mMainFrame.getBottomToolbar().addView(circleItem, gravity, index);
        }
        return false;
    }

    @Override
    public int getItemsCount(BarName barName, BaseBar.TB_Position gravity) {
        if (null == barName || null == gravity) {
            return 0;
        }
        if (BarName.TOP_BAR.equals(barName)) {
            return mMainFrame.getTopToolbar().getItemsCount(gravity);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            return mMainFrame.getBottomToolbar().getItemsCount(gravity);
        }
        return 0;
    }

    @Override
    public IBaseItem getItem(BarName barName, BaseBar.TB_Position gravity, int tag) {
        if (null == barName || null == gravity) {
            return null;
        }
        if (BarName.TOP_BAR.equals(barName)) {
            return mMainFrame.getTopToolbar().getItem(gravity, tag);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            return mMainFrame.getBottomToolbar().getItem(gravity, tag);
        }
        return null;
    }

    @Override
    public boolean removeItem(BarName barName, BaseBar.TB_Position gravity, int index) {
        if (null == barName || null == gravity) {
            return false;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            return mMainFrame.getTopToolbar().removeItemByIndex(gravity, index);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            return mMainFrame.getBottomToolbar().removeItemByIndex(gravity, index);
        }
        return false;
    }

    @Override
    public boolean removeItem(BarName barName, BaseBar.TB_Position gravity, IBaseItem item) {
        if (null == barName || null == gravity) {
            return false;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            return mMainFrame.getTopToolbar().removeItemByItem(item);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            return mMainFrame.getBottomToolbar().removeItemByItem(item);
        }
        return false;
    }

    @Override
    public void removeAllItems(BarName barName) {
        if (null == barName)
            return ;

        if (BarName.TOP_BAR.equals(barName)) {
            mMainFrame.getTopToolbar().removeAllItems();
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            mMainFrame.getBottomToolbar().removeAllItems();
        }
    }

    @Override
    public boolean addCustomToolBar(BarName barName, View view) {
        if (null == barName || null == view) {
            return false;
        }
        return mMainFrame.addCustomToolBar(barName, view);
    }

    @Override
    public boolean removeToolBar(BarName barName) {
        if (null == barName) {
            return false;
        }
        return mMainFrame.removeBottomBar(barName);
    }

    @Override
    public void enableToolBar(BarName barName, boolean enabled) {
        if (null == barName) {
            return;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            mMainFrame.enableTopToolbar(enabled);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            mMainFrame.enableBottomToolbar(enabled);
        }
    }

    @Override
    public void setBackgroundColor(BarName barName, int color) {
        if (null == barName) {
            return;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            mMainFrame.getTopToolbar().setBackgroundColor(color);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            mMainFrame.getBottomToolbar().setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(BarName barName, int resid) {
        if (null == barName) {
            return;
        }

        if (BarName.TOP_BAR.equals(barName)) {
            mMainFrame.getTopToolbar().setBackgroundResource(resid);
        } else if (BarName.BOTTOM_BAR.equals(barName)) {
            mMainFrame.getBottomToolbar().setBackgroundResource(resid);
        }
    }

}
