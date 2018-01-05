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
package com.foxit.uiextensions.controls.toolbar;

import android.view.View;

import com.foxit.uiextensions.controls.toolbar.impl.BaseItemImpl;
import com.foxit.uiextensions.controls.toolbar.impl.CircleItemImpl;
import com.foxit.uiextensions.pdfreader.impl.PDFReader;

/**
 * Toolbar's Operation control class,you can use it show/hide/add/remove items on the toolbar .
 * (PS:Currently toolbar has only the topbar/bottombar on the main page )
 * <br/><br/>
 * you can use it through {@link PDFReader#getBarManager()}
 */
public interface IBarsHandler {

    interface IItemClickListener {

        /**
         * You actively add item to click callback
         */
        void onClick(View v);
    }

    enum BarName {
        TOP_BAR,
        BOTTOM_BAR;
    }

    /**
     * Add an custom item to the toolbar
     * The item is inserted before any previous element at the
     * specified location. If the location is equal to the size of this
     * {@link #getItemsCount}, the object is added at the end.<br/><br/>
     * Note 1: if you want addItem in the topbar ,the gravity should be {@link  BaseBar.TB_Position#Position_LT} or{@link BaseBar.TB_Position#Position_RB};
     * if you want addItem in the bottombar,the gravity should be {@link BaseBar.TB_Position#Position_CENTER},Otherwise it may they overlap.<br/><br/>
     * Note 2: If your item has set the tag {@link IBaseItem#setTag(int)},the tag must be unique and the tag must be less than 100, or more than 300,
     * because the tag between 100 and 300 has been used or may be used in the future{@link ToolbarItemConfig}.<br/><br/>
     * Note 3: A tool bar can only add up to seven items,if the items counts more than seven,more than part will not be displayed and reture false
     * Note 4: the text in item maxlength is eight,more than part  will not be displayed.<br/><br/>
     * Note 5: the index is relative to {@link  BaseBar.TB_Position},If you add or remove items, the index of item in the toolbar will change
     *
     * @param barName the toolbar name
     * @param gravity the location of item in the toolbar{@link BaseBar.TB_Position}
     * @param item    If you want to add item in topbar, you can use {@link BaseItemImpl}
     *                or if want to add item in bottombar, you can use{@link CircleItemImpl}
     * @param index   the position at which to add the item,starting from 0 ,less than or equal to{@link #getItemsCount(BarName, BaseBar.TB_Position)}
     *                and is relative to {@link  BaseBar.TB_Position}.
     * @return true means add success ,otherwise add failure.
     */
    boolean addItem(BarName barName, BaseBar.TB_Position gravity, IBaseItem item, int index);

    /**
     * Add an default item to the toolbar
     * The item is inserted before any previous element at the
     * specified location. If the location is equal to the size of this
     * {@link #getItemsCount}, the object is added at the end.<br/><br/>
     *
     * Note 1: if you want addItem in the topbar ,the gravity should be {@link  BaseBar.TB_Position#Position_LT} or{@link BaseBar.TB_Position#Position_RB};
     * if you want addItem in the bottombar,the gravity should be {@link BaseBar.TB_Position#Position_CENTER},Otherwise it may they overlap.<br/><br/>
     * Note 2: If your item has set the tag {@link IBaseItem#setTag(int)},the tag must be unique and the tag must be less than 100, or more than 300,
     * because the tag between 100 and 300 has been used or may be used in the future{@link ToolbarItemConfig}.<br/><br/>
     * Note 3: A tool bar can only add up to seven items,if the items counts more than seven,more than part will not be displayed and reture false<br/><br/>
     * Note 4: the top text in item maxlength is 15, the bottom text in item maxlength is 8,more than part  will not be displayed.<br/><br/>
     * Note 5: the index is relative to {@link  BaseBar.TB_Position},If you add or remove items, the index of item in the toolbar will change
     *
     * @param barName       the toolbar name
     * @param gravity       the location of item in the toolbar{@link BaseBar.TB_Position}
     * @param textId        the textid in item
     * @param resId         the iconid in item
     * @param index         the position at which to add the item,starting from 0 ,less than or equal to{@link #getItemsCount(BarName, BaseBar.TB_Position)}
     *                      and is relative to {@link  BaseBar.TB_Position}.
     * @param clickListener The callback that will run
     * @return true means add success ,otherwise add failure.
     */
    boolean addItem(BarName barName, BaseBar.TB_Position gravity, int textId, int resId, int index, IItemClickListener clickListener);

    /**
     * get the items count by {@link BarName} and {@link BaseBar.TB_Position}
     * <p>
     * Note:the item location depend on the barname and gravity
     *
     * @param barName the toolbar name
     * @param gravity the location of item in the toolbar{@link BaseBar.TB_Position}
     * @return the items count
     */
    int getItemsCount(BarName barName, BaseBar.TB_Position gravity);


    /**
     * Get the item by tag, but if you remove this item before you get it, it will return null.
     * <p>
     * {@link IBaseItem#getTag()},the tag must be unique and the tag must be less than 100, or more than 300,
     * because the tag between 100 and 300 has been used or may be used in the future{@link ToolbarItemConfig}.
     *
     * @param barName the toolbar name
     * @param gravity the location of item in the toolbar{@link BaseBar.TB_Position}
     * @param tag     the item id and is unique ,it may be the existing tag
     *                {
     *                TOP_BAR: [
     *                {@link ToolbarItemConfig#ITEM_TOPBAR_BACK_TAG,
     *                {@link ToolbarItemConfig#ITEM_TOPBAR_READINGMARK_TAG,
     *                {@link ToolbarItemConfig#ITEM_TOPBAR_SEARCH_TAG,
     *                {@link ToolbarItemConfig#ITEM_TOPBAR_MORE_TAG
     *                ],
     *                BOTTOM_BAR: [
     *                {@link ToolbarItemConfig#ITEM_BOTTOMBAR_LIST_TAG,
     *                {@link ToolbarItemConfig#ITEM_BOTTOMBAR_VIEW_TAG,
     *                {@link ToolbarItemConfig#ITEM_BOTTOMBAR_COMMENT_TAG,
     *                {@link ToolbarItemConfig#ITEM_BOTTOMBAR_SIGN_TAG
     *                ]
     *                }
     *                or you custom tag.
     * @return {@link IBaseItem}
     */
    IBaseItem getItem(BarName barName, BaseBar.TB_Position gravity, int tag);


    /**
     * Removes the item at the specified gravity in the toolbar.
     * <p>
     * Note 1: the remove location depend on the barname and gravity
     * Note 2: the index is relative to {@link  BaseBar.TB_Position},If you add or remove items, the index of item in the toolbar will change
     *
     * @param barName the toolbar name
     * @param gravity the location of item in the toolbar{@link BaseBar.TB_Position}
     * @param index   the position at which to add the item,starting from 0 ,less than or equal to{@link #getItemsCount(BarName, BaseBar.TB_Position)}
     *                and is relative to {@link  BaseBar.TB_Position}.
     * @return true means remove success,otherwise means remove failure
     */
    boolean removeItem(BarName barName, BaseBar.TB_Position gravity, int index);

    /**
     * Removes the item at the specified item in the toolbar.
     * <p>
     * Note:the remove location depend on the barname and gravity
     *
     * @param barName the toolbar name
     * @param gravity the location of item in the toolbar{@link BaseBar.TB_Position}
     * @param item    the specified item in the toolbar
     * @return true means remove success,otherwise means remove failure.
     */
    boolean removeItem(BarName barName, BaseBar.TB_Position gravity, IBaseItem item);

    /**
     * Removes all items  from the toolbar
     *
     * @param barName the toolbar name
     */
    void removeAllItems(BarName barName);

    /**
     * add custom toolbar by BarName
     *
     * @param barName the toolbar name
     * @param view    the custom view
     * @return true means add success,otherwise means add failure.
     */
    boolean addCustomToolBar(BarName barName, View view);

    /**
     * remove toolbar by BarName
     *
     * @param barName the toolbar name
     * @return true means remove success,otherwise means remove failure.
     */
    boolean removeToolBar(BarName barName);

    /**
     * Set the enabled state of this view,and if set the enable to true, the bar is visible, or if set the enable to false,the bar is hide.
     *
     * @param barName the toolbar name
     * @param enabled True if this view is visible, false otherwise.
     */
    void enableToolBar(BarName barName, boolean enabled);

    /**
     * Sets the background color for the toolbar.
     *
     * @param barName the toolbar name
     * @param color   the color of the background
     */
    void setBackgroundColor(BarName barName, int color);

    /**
     * Set the background to a given resource. The resource should refer to
     * a Drawable object or 0 to remove the background.
     *
     * @param barName the toolbar name
     * @param resid   The identifier of the resource.
     * @attr ref android.R.styleable#View_background
     */
    void setBackgroundResource(BarName barName, int resid);

}
