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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.foxit.uiextensions.R;
import com.foxit.uiextensions.controls.toolbar.BaseBar;
import com.foxit.uiextensions.controls.toolbar.impl.BaseItemImpl;
import com.foxit.uiextensions.controls.toolbar.impl.TopBarImpl;
import com.foxit.uiextensions.utils.AppDisplay;
import com.foxit.uiextensions.utils.AppResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Class <CODE>MenuViewImpl</CODE> represents the more menu
 * Now, the more menu only include file group, which contains a menu item "properties".
 */
public class MenuViewImpl implements IMenuView {

    private Context mContext;
    private View mView;
    private ArrayList<MenuGroupImpl> mMenuGroups;

    private LinearLayout mMenuList_ly;
    private RelativeLayout mMenuTitleLayout;
    private BaseBar mMenuTitleBar;
    private ComparatorGroupByTag comparator;

    private Map<Integer, Integer> mGroupVisibleMap = new HashMap<Integer, Integer>();

    public interface MenuCallback {
        void onClosed();
    }

    private MenuCallback mCallback;

    public MenuViewImpl(Context context, MenuCallback callback) {
        mContext = context;
        mCallback = callback;
        mView = View.inflate(mContext, R.layout.view_menu_more, null);
        mMenuList_ly = (LinearLayout) mView.findViewById(R.id.menu_more_content_ly);
        mMenuGroups = new ArrayList<MenuGroupImpl>();
        comparator = new ComparatorGroupByTag();

        initMenuTitleView();
    }

    private void initMenuTitleView() {
        mMenuTitleBar = new TopBarImpl(mContext);
        mMenuTitleBar.setBackgroundResource(R.color.ux_text_color_subhead_colour);
        BaseItemImpl mTitleTextItem = new BaseItemImpl(mContext);
        mTitleTextItem.setText(AppResource.getString(mContext, R.string.action_more));
        mTitleTextItem.setTextSize(AppDisplay.getInstance(mContext).px2dp(mContext.getResources().getDimension(R.dimen.ux_text_height_title)));
        mTitleTextItem.setTextColorResource(R.color.ux_text_color_menu_light);
        if (AppDisplay.getInstance(mContext).isPad()) {
            mMenuTitleBar.addView(mTitleTextItem, BaseBar.TB_Position.Position_LT);
        } else {
            BaseItemImpl mMenuCloseItem = new BaseItemImpl(mContext);
            mMenuCloseItem.setImageResource(R.drawable.cloud_back);
            mMenuCloseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onClosed();
                    }
                }
            });
            mMenuTitleBar.addView(mMenuCloseItem, BaseBar.TB_Position.Position_LT);
            mMenuTitleBar.addView(mTitleTextItem, BaseBar.TB_Position.Position_LT);
        }
        mMenuTitleLayout = (RelativeLayout) mView.findViewById(R.id.menu_more_title_ly);
        mMenuTitleLayout.removeAllViews();
        mMenuTitleLayout.addView(mMenuTitleBar.getContentView());
    }

    @Override
    public void addMenuGroup(MenuGroupImpl menuGroup) {
        if (menuGroup == null)
            return;

        if (mMenuGroups.contains(menuGroup))
            return;

        if (null != mGroupVisibleMap.get(menuGroup.getTag())) {

            int visibility = mGroupVisibleMap.get(menuGroup.getTag()).intValue();
            if (View.VISIBLE == visibility) {
                menuGroup.getView().setVisibility(View.VISIBLE);
            } else if (View.INVISIBLE == visibility) {
                menuGroup.getView().setVisibility(View.INVISIBLE);
            } else {
                menuGroup.getView().setVisibility(View.GONE);
            }
        }

        mMenuGroups.add(menuGroup);
        Collections.sort(mMenuGroups, comparator);

        resetView();
    }

    private class ComparatorGroupByTag implements Comparator<Object> {
        @Override
        public int compare(Object lhs, Object rhs) {
            if (lhs instanceof MenuGroupImpl && rhs instanceof MenuGroupImpl) {
                MenuGroupImpl lItem = (MenuGroupImpl) lhs;
                MenuGroupImpl rItem = (MenuGroupImpl) rhs;
                return lItem.getTag() - rItem.getTag();
            } else {
                return 0;
            }
        }
    }

    @Override
    public void removeMenuGroup(int tag) {
        if (mMenuGroups.size() > 0) {
            for (MenuGroupImpl group : mMenuGroups) {
                if (group.getTag() == tag) {
                    mMenuGroups.remove(group);
                    mMenuList_ly.removeView(group.getView());
                    return;
                }
            }
        }
    }

    @Override
    public void setGroupVisibility(int visibility, int tag) {
        if (mMenuGroups.size() > 0) {
            for (MenuGroupImpl group : mMenuGroups) {
                if (group.getTag() == tag) {
                    group.getView().setVisibility(visibility);
                    return;
                }
            }
        }

        mGroupVisibleMap.put(tag, visibility);
    }

    @Override
    public MenuGroupImpl getMenuGroup(int tag) {
        if (mMenuGroups.size() > 0) {
            for (MenuGroupImpl group : mMenuGroups) {
                if (group.getTag() == tag) {
                    return group;
                }
            }
        }

        return null;
    }

    @Override
    public void addMenuItem(int groupTag, MenuItemImpl item) {
        if (mMenuGroups.size() > 0) {
            for (MenuGroupImpl group : mMenuGroups) {
                if (group.getTag() == groupTag) {
                    //add item
                    group.addItem(item);
                    return;
                }
            }
        }
    }

    @Override
    public void removeMenuItem(int groupTag, int itemTag) {
        if (mMenuGroups.size() > 0) {
            for (MenuGroupImpl group : mMenuGroups) {
                if (group.getTag() == groupTag) {
                    //remove
                    group.removeItem(itemTag);
                }
            }
        }
    }

    @Override
    public void setItemVisibility(int visibility, int groupTag, int itemTag) {
        if (mMenuGroups.size() > 0) {
            for (MenuGroupImpl group : mMenuGroups) {
                if (group.getTag() == groupTag) {
                    group.setItemVisibility(visibility, itemTag);
                    return;
                }
            }
        }
    }

    @Override
    public View getContentView() {
        return mView;
    }

    private void resetView() {
        mMenuList_ly.removeAllViews();
        for (MenuGroupImpl group : mMenuGroups) {
            mMenuList_ly.addView(group.getView());
        }
    }
}
