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
package com.foxit.uiextensions.controls.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxit.uiextensions.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Class <CODE>MenuGroupImpl</CODE> represents the menu group.
 * The menu group can contains a menu item.
 */
public class MenuGroupImpl {
    private int tag;
    public String title;
    private Context mContext;
    private ArrayList<MenuItemImpl> mMenuItems;
    private View mView;
    private LinearLayout mContentList_ly;
    private ComparatorItemByTag comparator;

    private Map<Integer, Integer> mItemVisibleMap = new HashMap<Integer, Integer>();

    public MenuGroupImpl(Context context, int tag, String title) {
        this.tag = tag;
        this.title = title;

        mContext = context;
        mMenuItems = new ArrayList<MenuItemImpl>();
        comparator = new ComparatorItemByTag();
        mView = View.inflate(mContext, R.layout.view_menu_more_group, null);
        mContentList_ly = (LinearLayout) mView.findViewById(R.id.menu_more_group_content_ly);

        //init title
        TextView titleTV = (TextView) mView.findViewById(R.id.menu_more_group_title);
        if (title == null) {
            title = "";
        }
        titleTV.setText(title);
    }

    public void addItem(MenuItemImpl item) {
        if (item == null)
            return;

        if (mMenuItems.contains(item))
            return;

        if (null != mItemVisibleMap.get(item.getTag())) {

            int visibility = mItemVisibleMap.get(item.getTag()).intValue();
            if (View.VISIBLE == visibility) {
                item.getView().setVisibility(View.VISIBLE);
            } else if (View.INVISIBLE == visibility) {
                item.getView().setVisibility(View.INVISIBLE);
            } else {
                item.getView().setVisibility(View.GONE);
            }
        }

        mMenuItems.add(item);
        Collections.sort(mMenuItems, comparator);

        resetItems();
    }

    private class ComparatorItemByTag implements Comparator<Object> {
        @Override
        public int compare(Object lhs, Object rhs) {
            if (lhs instanceof MenuItemImpl && rhs instanceof MenuItemImpl) {
                MenuItemImpl lItem = (MenuItemImpl) lhs;
                MenuItemImpl rItem = (MenuItemImpl) rhs;
                return lItem.getTag() - rItem.getTag();
            } else {
                return 0;
            }
        }
    }

    private void resetItems() {
        mContentList_ly.removeAllViews();
        for (MenuItemImpl item : mMenuItems) {
            addItemToMenu(item);
        }
    }

    public void removeItem(MenuItemImpl item) {
        if (mMenuItems.size() > 0) {
            mMenuItems.remove(item);
            mContentList_ly.removeView(item.getView());
        }

    }

    public void removeItem(int tag) {
        if (mMenuItems.size() > 0) {
            for (MenuItemImpl item : mMenuItems) {
                if (item.getTag() == tag) {
                    mContentList_ly.removeView(item.getView());
                    mMenuItems.remove(item);
                    return;
                }
            }
        }
    }

    public void setItemVisibility(int visibility, int tag) {
        if (mMenuItems.size() > 0) {
            for (MenuItemImpl item : mMenuItems) {
                if (item.getTag() == tag) {
                    item.getView().setVisibility(visibility);
                    return;
                }
            }
        }

        mItemVisibleMap.put(tag, visibility);
    }

    private void addItemToMenu(MenuItemImpl item) {
        if (item.getView().getParent() != null) {
            ((ViewGroup) item.getView().getParent()).removeView(item.getView());
        }
        mContentList_ly.addView(item.getView(), new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        item.setDividerVisible(true);
    }

    public int getTag() {
        return tag;
    }

    public View getView() {
        return mView;
    }

}
