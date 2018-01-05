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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.foxit.uiextensions.R;
import com.foxit.uiextensions.controls.toolbar.BaseBar;
import com.foxit.uiextensions.controls.toolbar.IBaseItem;
import com.foxit.uiextensions.utils.AppDisplay;
import com.foxit.uiextensions.utils.UIToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BaseBarImpl implements BaseBar {
    private static final int MAX_ITEMS_IN_THE_BAR = 7;

    protected int mDefaultWide;

    protected int mDefaultWide_HORIZONTAL = 60;
    protected int mDefaultWide_VERTICAL = 60;
    protected int mDefaultLength = ViewGroup.LayoutParams.MATCH_PARENT;
    protected int mDefaultSpace = 18;

    protected int mDefaultIntervalSpace = 16;
    protected int mIntervalSpace = 16;

    protected ArrayList<IBaseItem> mLT_Items;
    protected ArrayList<IBaseItem> mCenter_Items;
    protected ArrayList<IBaseItem> mRB_Items;

    protected BarRelativeLayoutImpl mRootLayout;
    protected LinearLayout mLTLayout;
    protected LinearLayout mCenterLayout;
    protected LinearLayout mRBLayout;

    protected RelativeLayout.LayoutParams mRootParams_HORIZONTAL;
    protected RelativeLayout.LayoutParams mRootParams_VERTICAL;

    protected RelativeLayout.LayoutParams mLTLayoutParams_HORIZONTAL;
    protected RelativeLayout.LayoutParams mLTLayoutParams_VERTICAL;
    protected RelativeLayout.LayoutParams mCenterLayoutParams_HORIZONTAL;
    protected RelativeLayout.LayoutParams mCenterLayoutParams_VERTICAL;
    protected RelativeLayout.LayoutParams mRBLayoutParams_HORIZONTAL;
    protected RelativeLayout.LayoutParams mRBLayoutParams_VERTICAL;

    protected LinearLayout.LayoutParams mLTItemParams_HORIZONTAL;
    protected LinearLayout.LayoutParams mLTItemParams_VERTICAL;
    protected LinearLayout.LayoutParams mCenterItemParams_HORIZONTAL;
    protected LinearLayout.LayoutParams mCenterItemParams_VERTICAL;
    protected LinearLayout.LayoutParams mRBItemParams_HORIZONTAL;
    protected LinearLayout.LayoutParams mRBItemParams_VERTICAL;
    protected LinearLayout.LayoutParams mEndParams;

    private String mName;
    private boolean mIsRefreshLayout = false;
    protected View mContentView;

    protected int mOrientation;
    protected boolean mInterval = false;
    protected boolean mNeedResetItemSize = false;
    protected ComparatorTBItemByIndex mItemComparator;

    protected boolean mIsPad = false;
    protected Context mContext = null;

    public BaseBarImpl(Context context) {
        this(context, HORIZONTAL, 0, 0, false);
    }

    protected BaseBarImpl(Context context, int orientation) {
        this(context, orientation, 0, 0, false);
    }

    protected BaseBarImpl(Context context, int orientation, boolean interval) {
        this(context, orientation, 0, 0, interval);
    }

    /**
     * if this is an interval bar,length and wide must use px
     *
     * @param orientation BaseBarImpl.HORIZONTAL or BaseBarImpl.VERTICAL<br/>
     * @param length      The default wide (level), or high (vertical).(<code>ViewGroup.LayoutParams.MATCH_PARENT<code/>,<code>ViewGroup.LayoutParams.WRAP_CONTENT<code/> or dp).
     * @param wide        The default high (level), or wide (vertical).(<code>ViewGroup.LayoutParams.MATCH_PARENT<code/>,<code>ViewGroup.LayoutParams.WRAP_CONTENT<code/> or dp).
     */
    protected BaseBarImpl(Context context, int orientation, int length, int wide, boolean interval) {
        mContext = context;
        //length, wide
        if (checkPad(context)) {
            mIsPad = true;
        } else {
            mIsPad = false;
        }
        initDimens();
        if (wide != 0) {
            mDefaultWide = wide;
        } else {
            if (orientation == HORIZONTAL) {
                mDefaultWide = dip2px_fromDimens(mDefaultWide_HORIZONTAL);
            } else {
                mDefaultWide = dip2px_fromDimens(mDefaultWide_VERTICAL);
            }
        }
        if (length != 0) {
            mDefaultLength = length;
        } else {
            mDefaultLength = dip2px(mDefaultLength);
        }
        mDefaultSpace = dip2px_fromDimens(mDefaultSpace);
        mDefaultIntervalSpace = dip2px_fromDimens(mDefaultIntervalSpace);
        mInterval = interval;
        mOrientation = orientation;
        mRootLayout = new BarRelativeLayoutImpl(context, this);
        mLTLayout = new LinearLayout(context);
        mCenterLayout = new LinearLayout(context);
        mRBLayout = new LinearLayout(context);

        mRootLayout.addView(mLTLayout);
        mRootLayout.addView(mCenterLayout);
        mRootLayout.addView(mRBLayout);

        mLT_Items = new ArrayList<IBaseItem>();
        mCenter_Items = new ArrayList<IBaseItem>();
        mRB_Items = new ArrayList<IBaseItem>();

        initOrientation(orientation);

        mItemComparator = new ComparatorTBItemByIndex();
    }

    protected boolean checkPad(Context context) {
        return AppDisplay.getInstance(context).isPad();
    }

    @Override
    public boolean addView(IBaseItem item, TB_Position position) {
        return addView(item, position, IBaseItem.SortType.Sort_By_Tag, -1);
    }

    @Override
    public boolean addView(IBaseItem item, TB_Position position, int index) {
        return addView(item, position, IBaseItem.SortType.Sort_By_Index, index);
    }

    private boolean addView(IBaseItem item, TB_Position position, IBaseItem.SortType sortType, int index) {
        if (item == null) {
            return false;
        }
        if (mContentView != null) {
            mRootLayout.removeView(mContentView);
            mContentView = null;
        }
        if (mInterval) {
            if (mCenter_Items.contains(item)) {
                return false;
            }

            sortItems(item, mCenter_Items);
            resetItemSize(mCenter_Items);
            if (mOrientation == HORIZONTAL) {
                if (mDefaultLength <= 0) {
                    mCenterItemParams_HORIZONTAL.setMargins(0, 0, mDefaultSpace, 0);
                    resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);
                    return true;
                }
                mCenterLayout.setPadding(mDefaultIntervalSpace, 0, mDefaultIntervalSpace, 0);
                int itemSpace = marginsItemSpace(mCenter_Items, mOrientation, 0);
                mCenterItemParams_HORIZONTAL.setMargins(0, 0, itemSpace, 0);
                resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);
            } else {
                if (mDefaultLength <= 0) {
                    mCenterItemParams_VERTICAL.setMargins(0, 0, 0, mDefaultSpace);
                    resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);
                    return true;
                }
                mCenterLayout.setPadding(0, mDefaultIntervalSpace, 0, mDefaultIntervalSpace);
                int itemSpace = marginsItemSpace(mCenter_Items, mOrientation, 0);
                mCenterItemParams_VERTICAL.setMargins(0, 0, 0, itemSpace);
                resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);
            }
            return true;
        }
        if (!mInterval) {//padding for normal bar
            if (mOrientation == HORIZONTAL) {
                mRootLayout.setPadding(mDefaultIntervalSpace, 0, mDefaultIntervalSpace, 0);
            } else {
                mRootLayout.setPadding(0, mDefaultIntervalSpace, 0, mDefaultIntervalSpace);
            }
        }
        if (TB_Position.Position_LT.equals(position)) {
            if (mLT_Items.contains(item)) {
                return false;
            }

            if (mLT_Items.size() + mRB_Items.size() + mCenter_Items.size() >= MAX_ITEMS_IN_THE_BAR) {
                UIToast.getInstance(mContext).show(mContext.getString(R.string.fx_add_beyond_the_limit_error, MAX_ITEMS_IN_THE_BAR));
                return false;
            }

            if (IBaseItem.SortType.Sort_By_Tag.equals(sortType)) {
                sortItems(item, mLT_Items);
            } else {
                if (isIndexOutOfBounds(ADD_ITEM, mLT_Items, index)) {
                    return false;
                }
                mLT_Items.add(index, item);
            }

            resetItemSize(mLT_Items);
            if (mOrientation == HORIZONTAL) {
                int itemSpace;
                if (mLT_Items.size() > 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenWidth();
                    }
                    itemSpace = marginsItemSpace(mLT_Items, mOrientation, mDefaultLength);
                } else {
                    itemSpace = mDefaultSpace;
                }
                mLTItemParams_HORIZONTAL.setMargins(0, 0, itemSpace, 0);
                resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_HORIZONTAL);
            } else {

                int itemSpace;
                if (mLT_Items.size() > 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenHeight();
                    }
                    itemSpace = marginsItemSpace(mLT_Items, mOrientation, mDefaultLength);
                } else {
                    itemSpace = mDefaultSpace;
                }
                mLTItemParams_VERTICAL.setMargins(0, 0, 0, itemSpace);
                resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_VERTICAL);
            }
            return true;
        } else if (TB_Position.Position_CENTER.equals(position)) {

            if (mCenter_Items.contains(item)) {
                return false;
            }

            if (mLT_Items.size() + mRB_Items.size() + mCenter_Items.size() >= MAX_ITEMS_IN_THE_BAR) {
                UIToast.getInstance(mContext).show(mContext.getString(R.string.fx_add_beyond_the_limit_error, MAX_ITEMS_IN_THE_BAR));
                return false;
            }

            if (IBaseItem.SortType.Sort_By_Tag.equals(sortType)) {
                sortItems(item, mCenter_Items);
            } else {

                if (isIndexOutOfBounds(ADD_ITEM, mCenter_Items, index)) {
                    return false;
                }
                mCenter_Items.add(index, item);
            }
            resetItemSize(mCenter_Items);
            if (mOrientation == HORIZONTAL) {

                int itemSpace;
                if (mCenter_Items.size() > 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenWidth();
                    }
                    itemSpace = marginsItemSpace(mCenter_Items, mOrientation, mDefaultLength);
                } else {
                    itemSpace = mDefaultSpace;
                }
                mCenterItemParams_HORIZONTAL.setMargins(0, 0, itemSpace, 0);
                resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);
            } else {
                int itemSpace;
                if (mCenter_Items.size() > 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenHeight();
                    }
                    itemSpace = marginsItemSpace(mCenter_Items, mOrientation, mDefaultLength);
                } else {
                    itemSpace = mDefaultSpace;
                }
                mCenterItemParams_VERTICAL.setMargins(0, 0, 0, itemSpace);
                resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);
            }
            return true;
        } else if (TB_Position.Position_RB.equals(position)) {
            if (mRB_Items.contains(item)) {
                return false;
            }

            if (mLT_Items.size() + mRB_Items.size() + mCenter_Items.size() >= MAX_ITEMS_IN_THE_BAR) {
                UIToast.getInstance(mContext).show(mContext.getString(R.string.fx_add_beyond_the_limit_error, MAX_ITEMS_IN_THE_BAR));
                return false;
            }

            if (IBaseItem.SortType.Sort_By_Tag.equals(sortType)) {
                sortItems(item, mRB_Items);
            } else {

                if (isIndexOutOfBounds(ADD_ITEM, mRB_Items, index)) {
                    return false;
                }
                mRB_Items.add(index, item);
            }
            resetItemSize(mRB_Items);
            if (mOrientation == HORIZONTAL) {
                int itemSpace;
                if (mRB_Items.size() > 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenWidth();
                    }
                    itemSpace = marginsItemSpace(mRB_Items, mOrientation, mDefaultLength);
                } else {
                    itemSpace = mDefaultSpace;
                }
                mRBItemParams_HORIZONTAL.setMargins(itemSpace, 0, 0, 0);
                resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_HORIZONTAL);
            } else {
                int itemSpace;
                if (mRB_Items.size() > 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenHeight();
                    }
                    itemSpace = marginsItemSpace(mRB_Items, mOrientation, mDefaultLength);
                } else {
                    itemSpace = mDefaultSpace;
                }
                mRBItemParams_VERTICAL.setMargins(0, itemSpace, 0, 0);
                resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_VERTICAL);
            }
            return true;
        }
        return false;
    }

    private void resetItemSize(ArrayList<IBaseItem> items) {
        if (!mNeedResetItemSize) {
            return;
        }
        int maxH = 0, maxW = 0;
        for (IBaseItem item : items) {
            item.getContentView().measure(0, 0);
            if (item.getContentView().getMeasuredHeight() > maxH) {
                maxH = item.getContentView().getMeasuredHeight();
            }
            if (item.getContentView().getMeasuredWidth() > maxW) {
                maxW = item.getContentView().getMeasuredWidth();
            }
        }
        for (IBaseItem item : items) {
            item.getContentView().setMinimumHeight(maxH);
            item.getContentView().setMinimumWidth(maxW);
        }
    }

    protected int marginsItemSpace(ArrayList<IBaseItem> items, int orientation, int newLength) {
        if (AppDisplay.getInstance(mContext).isPad()) {
            if (orientation == HORIZONTAL) {
                int itemSpace = 0;
                int itemsWidth = 0;
                int lastItemWidth = 0;
                if (items.size() >= 2) {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).getContentView().measure(0, 0);
                        itemsWidth += items.get(i).getContentView().getMeasuredWidth();
                        if (i == items.size() - 1) {
                            lastItemWidth = items.get(i).getContentView().getMeasuredWidth();
                        }
                    }
                    if (((itemsWidth - lastItemWidth) * 4 + lastItemWidth + mDefaultIntervalSpace * 2) < newLength) {
                        itemSpace = (itemsWidth / items.size()) * 3;
                        mIntervalSpace = (newLength - (itemsWidth + itemSpace * (items.size() - 1))) / 2;
                    } else {
                        itemSpace = (newLength - mDefaultIntervalSpace * 2 - itemsWidth) / (items.size() - 1);
                        mIntervalSpace = mDefaultIntervalSpace;
                    }
                }
                return itemSpace;
            } else {
                int itemSpace = 0;
                int itemsHeight = 0;
                int lastItemHeight = 0;
                if (items.size() >= 2) {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).getContentView().measure(0, 0);
                        itemsHeight += items.get(i).getContentView().getMeasuredHeight();
                        if (i == items.size() - 1) {
                            lastItemHeight = items.get(i).getContentView().getMeasuredHeight();
                        }
                    }
                    if (((itemsHeight - lastItemHeight) * 4 + lastItemHeight + mDefaultIntervalSpace * 2) < newLength) {
                        itemSpace = (itemsHeight / items.size()) * 3;
                        mIntervalSpace = (newLength - (itemsHeight + itemSpace * (items.size() - 1))) / 2;
                    } else {
                        itemSpace = (newLength - mDefaultIntervalSpace * 2 - itemsHeight) / (items.size() - 1);
                        mIntervalSpace = mDefaultIntervalSpace;
                    }
                }
                return itemSpace;
            }
        } else {
            if (orientation == HORIZONTAL) {
                int itemSpace = 0;
                int itemsWidth = 0;
                if (items.size() >= 2) {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).getContentView().measure(0, 0);
                        itemsWidth += items.get(i).getContentView().getMeasuredWidth();
                    }

                    int needWidth = itemsWidth + mDefaultIntervalSpace * 2 + mDefaultSpace * (items.size() - 1);
                    if (!mInterval && newLength > needWidth) {
                        itemSpace = mDefaultSpace;
                    } else {
                        itemSpace = (newLength - mDefaultIntervalSpace * 2 - itemsWidth) / (items.size() - 1);
                    }
                    mIntervalSpace = mDefaultIntervalSpace;
                }
                return itemSpace;
            } else {
                int itemSpace = 0;
                int itemsHeight = 0;
                if (items.size() >= 2) {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).getContentView().measure(0, 0);
                        itemsHeight += items.get(i).getContentView().getMeasuredHeight();
                    }

                    int needHeight = itemsHeight + mDefaultIntervalSpace * 2 + mDefaultSpace * (items.size() - 1);
                    if (!mInterval && newLength > needHeight) {
                        itemSpace = mDefaultSpace;
                    } else {
                        itemSpace = (newLength - mDefaultIntervalSpace * 2 - itemsHeight) / (items.size() - 1);
                    }
                    mIntervalSpace = mDefaultIntervalSpace;
                }
                return itemSpace;
            }
        }
    }

    protected void sortItems(IBaseItem item, ArrayList<IBaseItem> items) {
        items.add(item);
        Collections.sort(items, mItemComparator);
    }

    protected void resetItemsLayout(ArrayList<IBaseItem> items, final LinearLayout layout, LinearLayout.LayoutParams itemParams) {
        if (items == null || items.isEmpty() || layout == null) {
            return;
        }
        layout.removeAllViews();

        if (!mInterval && mRBLayout == layout) {
            for (IBaseItem item : items) {
                layout.addView(item.getContentView(), itemParams);
            }
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (i == items.size() - 1) {
                    layout.addView(items.get(i).getContentView(), mEndParams);
                    continue;
                }
                layout.addView(items.get(i).getContentView(), itemParams);
            }
        }
    }

    @Override
    public boolean removeItemByTag(int tag) {
        if (!mCenter_Items.isEmpty()) {
            for (IBaseItem item : mCenter_Items) {
                if (item.getTag() == tag) {
                    mCenterLayout.removeView(item.getContentView());
                    boolean isRemoveSuccess = mCenter_Items.remove(item);
                    if (!mInterval && isRemoveSuccess) {
                        resetLayout(TB_Position.Position_CENTER);
                    }
                    return isRemoveSuccess;
                }
            }
        }
        if (!mInterval) {
            if (!mLT_Items.isEmpty()) {
                for (IBaseItem item : mLT_Items) {
                    if (item.getTag() == tag) {
                        mLTLayout.removeView(item.getContentView());
                        boolean isRemoveSuccess = mLT_Items.remove(item);
                        if (isRemoveSuccess) {
                            resetLayout(TB_Position.Position_LT);
                        }
                        return isRemoveSuccess;
                    }
                }
            }
            if (!mRB_Items.isEmpty()) {
                for (IBaseItem item : mRB_Items) {
                    if (item.getTag() == tag) {
                        mRBLayout.removeView(item.getContentView());
                        boolean isRemoveSuccess = mRB_Items.remove(item);
                        if (isRemoveSuccess) {
                            resetLayout(TB_Position.Position_RB);
                        }
                        return isRemoveSuccess;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeItemByIndex(TB_Position position, int index) {

        if (TB_Position.Position_CENTER.equals(position)) {
            if (isIndexOutOfBounds(REMOVE_ITEM, mCenter_Items, index)) {
                return false;
            }

            IBaseItem item = mCenter_Items.get(index);
            mCenterLayout.removeView(item.getContentView());
            boolean isRemoveSuccess = mCenter_Items.remove(item);
            if (!mInterval && isRemoveSuccess) {
                resetLayout(TB_Position.Position_CENTER);
            }
            return isRemoveSuccess;
        }

        if (!mInterval) {
            if (TB_Position.Position_LT.equals(position)) {
                if (isIndexOutOfBounds(REMOVE_ITEM, mLT_Items, index)) {
                    return false;
                }

                IBaseItem item = mLT_Items.get(index);
                mLTLayout.removeView(item.getContentView());
                boolean isRemoveSuccess = mLT_Items.remove(item);
                if (isRemoveSuccess) {
                    resetLayout(TB_Position.Position_LT);
                }
                return isRemoveSuccess;
            }

            if (TB_Position.Position_RB.equals(position)) {
                if (isIndexOutOfBounds(REMOVE_ITEM, mRB_Items, index)) {
                    return false;
                }

                IBaseItem item = mRB_Items.get(index);
                mRBLayout.removeView(item.getContentView());
                boolean isRemoveSuccess = mRB_Items.remove(item);
                if (isRemoveSuccess) {
                    resetLayout(TB_Position.Position_RB);
                }
                return isRemoveSuccess;
            }
        }
        return false;
    }

    @Override
    public boolean removeItemByItem(IBaseItem item) {
        if (mCenter_Items.contains(item)) {
            mCenterLayout.removeView(item.getContentView());
            boolean isRemoveSuccess = mCenter_Items.remove(item);
            if (!mInterval && isRemoveSuccess) {
                resetLayout(TB_Position.Position_CENTER);
            }
            return isRemoveSuccess;
        }
        if (!mInterval) {
            if (mLT_Items.contains(item)) {
                mLTLayout.removeView(item.getContentView());
                boolean isRemoveSuccess = mLT_Items.remove(item);
                if (isRemoveSuccess) {
                    resetLayout(TB_Position.Position_LT);
                }
                return isRemoveSuccess;
            }

            if (mRB_Items.contains(item)) {
                mRBLayout.removeView(item.getContentView());
                boolean isRemoveSuccess = mRB_Items.remove(item);
                if (isRemoveSuccess) {
                    resetLayout(TB_Position.Position_RB);
                }
                return isRemoveSuccess;
            }
        }
        return false;
    }

    protected void resetLayout(TB_Position position) {
        if (TB_Position.Position_LT.equals(position)) {

            if (mOrientation == HORIZONTAL) {

                if (mLT_Items.size() >= 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenWidth();
                    }
                    int itemSpace = marginsItemSpace(mLT_Items, mOrientation, mDefaultLength);
                    mLTItemParams_HORIZONTAL.setMargins(0, 0, itemSpace, 0);
                    resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_HORIZONTAL);
                }
            } else {

                if (mLT_Items.size() >= 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenHeight();
                    }
                    int itemSpace = marginsItemSpace(mLT_Items, mOrientation, mDefaultLength);
                    mLTItemParams_VERTICAL.setMargins(0, 0, 0, itemSpace);
                    resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_VERTICAL);
                }
            }
        } else if (TB_Position.Position_CENTER.equals(position)) {

            if (mOrientation == HORIZONTAL) {

                if (mCenter_Items.size() >= 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenWidth();
                    }
                    int itemSpace = marginsItemSpace(mCenter_Items, mOrientation, mDefaultLength);
                    mCenterItemParams_HORIZONTAL.setMargins(0, 0, itemSpace, 0);
                    resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);
                }
            } else {

                if (mCenter_Items.size() >= 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenHeight();
                    }
                    int itemSpace = marginsItemSpace(mCenter_Items, mOrientation, mDefaultLength);
                    mCenterItemParams_VERTICAL.setMargins(0, 0, 0, itemSpace);
                    resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);
                }
            }
        } else {

            if (mOrientation == HORIZONTAL) {

                if (mRB_Items.size() >= 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenWidth();
                    }
                    int itemSpace = marginsItemSpace(mRB_Items, mOrientation, mDefaultLength);
                    mRBItemParams_HORIZONTAL.setMargins(itemSpace, 0, 0, 0);
                    resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_HORIZONTAL);
                }
            } else {

                if (mRB_Items.size() >= 4) {
                    if (mDefaultLength < 0) {
                        mDefaultLength = AppDisplay.getInstance(mContext).getScreenHeight();
                    }
                    int itemSpace = marginsItemSpace(mRB_Items, mOrientation, mDefaultLength);
                    mRBItemParams_VERTICAL.setMargins(0, itemSpace, 0, 0);
                    resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_VERTICAL);
                }
            }
        }
    }


    @Override
    public void removeAllItems() {
        mLTLayout.removeAllViews();
        mCenterLayout.removeAllViews();
        mRBLayout.removeAllViews();

        mLT_Items.clear();
        mCenter_Items.clear();
        mRB_Items.clear();
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setBarVisible(boolean visible) {
        if (visible) {
            mRootLayout.setVisibility(View.VISIBLE);
        } else {
            mRootLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View getContentView() {
        return mRootLayout;
    }

    @Override
    public IBaseItem getItem(TB_Position location, int tag) {
        if (TB_Position.Position_LT.equals(location)) {
            return getItem(mLT_Items, tag);
        } else if (TB_Position.Position_CENTER.equals(location)) {
            return getItem(mCenter_Items, tag);
        } else if (TB_Position.Position_RB.equals(location)) {
            return getItem(mRB_Items, tag);
        }
        return null;
    }

    private IBaseItem getItem(ArrayList<IBaseItem> items, int tag) {
        for (IBaseItem item : items) {
            if (item.getTag() == tag) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int getItemsCount(TB_Position location) {
        if (TB_Position.Position_LT.equals(location)) {
            return mLT_Items.size();
        } else if (TB_Position.Position_CENTER.equals(location)) {
            return mCenter_Items.size();
        } else if (TB_Position.Position_RB.equals(location)) {
            return mRB_Items.size();
        }
        return 0;
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation == HORIZONTAL) {
            mDefaultWide = dip2px_fromDimens(mDefaultWide_HORIZONTAL);
        } else {
            mDefaultWide = dip2px_fromDimens(mDefaultWide_VERTICAL);
        }
        initOrientation(orientation);
    }

    protected void initOrientation(int orientation, int length, int wide) {
        mDefaultWide = wide;
        mDefaultLength = length;
        initOrientation(orientation);
    }

    protected void refreshLayout() {
        mIsRefreshLayout = true;
    }

    protected void initOrientation(int orientation) {
        mOrientation = orientation;
        if (orientation == HORIZONTAL) {
            if (mRootParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mRootParams_HORIZONTAL = new RelativeLayout.LayoutParams(mDefaultLength, mDefaultWide);
            }
            mRootLayout.setLayoutParams(mRootParams_HORIZONTAL);

            if (mLTLayoutParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mLTLayoutParams_HORIZONTAL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
                mLTLayoutParams_HORIZONTAL.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            if (mCenterLayoutParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mCenterLayoutParams_HORIZONTAL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
                mCenterLayoutParams_HORIZONTAL.addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            if (mRBLayoutParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mRBLayoutParams_HORIZONTAL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
                mRBLayoutParams_HORIZONTAL.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            mLTLayout.setLayoutParams(mLTLayoutParams_HORIZONTAL);
            mCenterLayout.setLayoutParams(mCenterLayoutParams_HORIZONTAL);
            mRBLayout.setLayoutParams(mRBLayoutParams_HORIZONTAL);
            mLTLayout.setOrientation(LinearLayout.HORIZONTAL);
            mCenterLayout.setOrientation(LinearLayout.HORIZONTAL);
            mRBLayout.setOrientation(LinearLayout.HORIZONTAL);

            mEndParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
            if (mLTItemParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mLTItemParams_HORIZONTAL = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
                mLTItemParams_HORIZONTAL.setMargins(0, 0, mDefaultSpace, 0);
            }
            resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_HORIZONTAL);

            if (mCenterItemParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mCenterItemParams_HORIZONTAL = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
                mCenterItemParams_HORIZONTAL.setMargins(0, 0, mDefaultSpace, 0);
            }
            resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);

            if (mRBItemParams_HORIZONTAL == null || mIsRefreshLayout == true) {
                mRBItemParams_HORIZONTAL = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
                mRBItemParams_HORIZONTAL.setMargins(mDefaultSpace, 0, 0, 0);
            }
            resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_HORIZONTAL);

        } else {
            if (mRootParams_VERTICAL == null || mIsRefreshLayout == true) {
                mRootParams_VERTICAL = new RelativeLayout.LayoutParams(mDefaultWide, mDefaultLength);
            }
            mRootLayout.setLayoutParams(mRootParams_VERTICAL);
            if (mLTLayoutParams_VERTICAL == null || mIsRefreshLayout == true) {
                mLTLayoutParams_VERTICAL = new RelativeLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLTLayoutParams_VERTICAL.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            if (mCenterLayoutParams_VERTICAL == null || mIsRefreshLayout == true) {
                mCenterLayoutParams_VERTICAL = new RelativeLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
                mCenterLayoutParams_VERTICAL.addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            if (mRBLayoutParams_VERTICAL == null || mIsRefreshLayout == true) {
                mRBLayoutParams_VERTICAL = new RelativeLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRBLayoutParams_VERTICAL.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            mLTLayout.setLayoutParams(mLTLayoutParams_VERTICAL);
            mCenterLayout.setLayoutParams(mCenterLayoutParams_VERTICAL);
            mRBLayout.setLayoutParams(mRBLayoutParams_VERTICAL);
            mLTLayout.setOrientation(LinearLayout.VERTICAL);
            mCenterLayout.setOrientation(LinearLayout.VERTICAL);
            mRBLayout.setOrientation(LinearLayout.VERTICAL);

            mEndParams = new LinearLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (mLTItemParams_VERTICAL == null || mIsRefreshLayout == true) {
                mLTItemParams_VERTICAL = new LinearLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLTItemParams_VERTICAL.setMargins(0, 0, 0, mDefaultSpace);
            }
            resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_VERTICAL);

            if (mCenterItemParams_VERTICAL == null || mIsRefreshLayout == true) {
                mCenterItemParams_VERTICAL = new LinearLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
                mCenterItemParams_VERTICAL.setMargins(0, 0, 0, mDefaultSpace);
            }
            resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);

            if (mRBItemParams_VERTICAL == null || mIsRefreshLayout == true) {
                mRBItemParams_VERTICAL = new LinearLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRBItemParams_VERTICAL.setMargins(0, mDefaultSpace, 0, 0);
            }
            resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_VERTICAL);

        }
        mIsRefreshLayout = false;
    }

    @Override
    public void setBackgroundColor(int color) {
        if (mRootLayout != null) {
            mRootLayout.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int res) {
        if (mRootLayout != null) {
            mRootLayout.setBackgroundResource(res);
        }
    }

    @Override
    public void setInterval(boolean interval) {
        mInterval = interval;
        if (interval) {
            mLTLayout.setVisibility(View.GONE);
            mRBLayout.setVisibility(View.GONE);
            mRootLayout.setPadding(0, 0, 0, 0);
            if (mOrientation == HORIZONTAL) {
                mCenterLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                mCenterLayout.getLayoutParams().height = mDefaultWide;
                mEndParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mDefaultWide);
            } else {
                mCenterLayout.getLayoutParams().width = mDefaultWide;
                mCenterLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                mEndParams = new LinearLayout.LayoutParams(mDefaultWide, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    @Override
    public void setItemSpace(int space) {
        mDefaultSpace = space;
        if (mLT_Items != null && !mLT_Items.isEmpty()) {
            if (mOrientation == HORIZONTAL) {
                mLTItemParams_HORIZONTAL.setMargins(0, 0, mDefaultSpace, 0);
                resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_HORIZONTAL);
            } else {
                mLTItemParams_VERTICAL.setMargins(0, 0, 0, mDefaultSpace);
                resetItemsLayout(mLT_Items, mLTLayout, mLTItemParams_VERTICAL);
            }
        }
        if (mCenter_Items != null && !mCenter_Items.isEmpty()) {
            if (mOrientation == HORIZONTAL) {
                mCenterItemParams_HORIZONTAL.setMargins(0, 0, mDefaultSpace, 0);
                resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);
            } else {
                mCenterItemParams_VERTICAL.setMargins(0, 0, 0, mDefaultSpace);
                resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);
            }
        }
        if (mRB_Items != null && !mRB_Items.isEmpty()) {
            if (mOrientation == HORIZONTAL) {
                mRBItemParams_HORIZONTAL.setMargins(mDefaultSpace, 0, 0, 0);
                resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_HORIZONTAL);
            } else {
                mRBItemParams_VERTICAL.setMargins(0, mDefaultSpace, 0, 0);
                resetItemsLayout(mRB_Items, mRBLayout, mRBItemParams_VERTICAL);
            }
        }
    }

    @Override
    public void setWidth(int width) {
        mRootLayout.getLayoutParams().width = width;
    }

    @Override
    public void setHeight(int height) {
        mRootLayout.getLayoutParams().height = height;
    }

    @Override
    public void setContentView(View v) {
        removeAllItems();
        mRootLayout.setPadding(0, 0, 0, 0);
        mRootLayout.addView(v);
        mContentView = v;
    }

    @Override
    public void setInterceptTouch(boolean isInterceptTouch) {
        mRootLayout.setInterceptTouch(isInterceptTouch);
    }

    @Override
    public void setNeedResetItemSize(boolean needResetItemSize) {
        mNeedResetItemSize = needResetItemSize;
    }

    public void layout(int l, int t, int r, int b) {
        if (mInterval) {
            int w = Math.abs(r - l);
            int h = Math.abs(b - t);
            if (mOrientation == HORIZONTAL) {
                resetLength(w);
            } else {
                resetLength(h);
            }
        }
    }

    protected void resetLength(int newLength) {
        if (mOrientation == HORIZONTAL) {
            mCenterLayout.setGravity(Gravity.CENTER_VERTICAL);
            int itemSpace = marginsItemSpace(mCenter_Items, mOrientation, newLength);
            mCenterItemParams_HORIZONTAL.setMargins(0, 0, itemSpace, 0);
            mCenterLayout.setPadding(mIntervalSpace, 0, mIntervalSpace, 0);
            resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_HORIZONTAL);
        } else {
            mCenterLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            int itemSpace = marginsItemSpace(mCenter_Items, mOrientation, newLength);
            mCenterItemParams_VERTICAL.setMargins(0, 0, 0, itemSpace);
            mCenterLayout.setPadding(0, mIntervalSpace, 0, mIntervalSpace);
            resetItemsLayout(mCenter_Items, mCenterLayout, mCenterItemParams_VERTICAL);
        }
    }

    private void initDimens() {
        if (mIsPad) {
            mDefaultWide_HORIZONTAL = mContext.getResources().getDimensionPixelOffset(R.dimen.ux_toolbar_height_pad);
        } else {
            mDefaultWide_HORIZONTAL = mContext.getResources().getDimensionPixelOffset(R.dimen.ux_toolbar_height_phone);
        }
        mDefaultWide_VERTICAL = mDefaultWide_HORIZONTAL;
        mDefaultSpace = mContext.getResources().getDimensionPixelOffset(R.dimen.ux_toolbar_button_interval);
        if (mIsPad) {
            mDefaultIntervalSpace = (int) mContext.getResources().getDimension(R.dimen.ux_horz_left_margin_pad);
        } else {
            mDefaultIntervalSpace = (int) mContext.getResources().getDimension(R.dimen.ux_horz_left_margin_phone);
        }
    }

    public void measure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    private class ComparatorTBItemByIndex implements Comparator<Object> {
        @Override
        public int compare(Object lhs, Object rhs) {
            if (lhs instanceof IBaseItem && rhs instanceof IBaseItem) {
                IBaseItem lItem = (IBaseItem) lhs;
                IBaseItem rItem = (IBaseItem) rhs;
                return lItem.getTag() - rItem.getTag();
            } else {
                return 0;
            }
        }

    }

    protected int dip2px(int dip) {
        if (dip <= 0) {
            return dip;
        } else {
            return AppDisplay.getInstance(mContext).dp2px(dip);
        }
    }

    protected int dip2px_fromDimens(int dip) {
        return dip;
    }

    private static final int ADD_ITEM = 112;
    private static final int REMOVE_ITEM = 113;

    private boolean isIndexOutOfBounds(int type, ArrayList<IBaseItem> items, int index) {
        int size = (ADD_ITEM == type) ? items.size() : items.size() - 1;

        if (index < 0 || index > size) {
            int tip = (ADD_ITEM == type) ? R.string.fx_add_item_error : R.string.fx_remove_item_error;
//            UIToast.getInstance(mContext).show(tip);
            return true;
        }
        return false;
    }

}
