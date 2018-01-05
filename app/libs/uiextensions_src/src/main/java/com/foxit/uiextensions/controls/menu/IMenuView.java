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

import android.view.View;

/**
 * Control {@link MoreMenuModule} group and group's submenus display and hide,
 * as well as add group ,or group submenus to the {@link MoreMenuModule}
 * <br/><br/>
 * You can use it through {@link MoreMenuModule#getMenuView()}
 */
public interface IMenuView {

    /**
     * Add a group
     * <p>
     * Note1: We use this tag to sort/add/remove/get... group,so the tag must be unique when initializing the MenuGroupImpl .
     * Note2: The tag must be less than 100, or more than 150, because the tag between 100 and 150 has been used or may be used in the future.
     *
     * @param group the group info.
     */
    void addMenuGroup(MenuGroupImpl group);

    /**
     * According to the tag to remove group.
     *
     * @param tag the group id and is unique ,it may be the existing tag {@link MoreMenuConfig#GROUP_FILE },{@link MoreMenuConfig#GROUP_FORM},
     *            {@link MoreMenuConfig#GROUP_PROTECT} or you custom tag.
     */
    void removeMenuGroup(int tag);

    /**
     * According to the tag Set the enabled state of this group.
     *
     * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE}, or {@link View#GONE}.
     *                   <p/>
     * @param tag        the group id and is unique ,it may be the existing tag {@link MoreMenuConfig#GROUP_FILE },{@link MoreMenuConfig#GROUP_FORM},
     *                   {@link MoreMenuConfig#GROUP_PROTECT} or you custom tag.
     */
    void setGroupVisibility(int visibility, int tag);

    /**
     * According to the tag get the group info.
     *
     * @param tag the group id and is unique ,it may be the existing tag {@link MoreMenuConfig#GROUP_FILE },{@link MoreMenuConfig#GROUP_FORM},
     *            {@link MoreMenuConfig#GROUP_PROTECT } or you custom tag.
     * @return the group info
     */
    MenuGroupImpl getMenuGroup(int tag);

    /**
     * Add the item to the group according to the groupTag.
     * <p>
     * Note: We use this tag to sort/get/remove... item,so the tag must be unique when initializing the MenuItemImpl.
     *
     * @param groupTag the group id and is unique ,it may be the existing tag {@link MoreMenuConfig#GROUP_FILE },{@link MoreMenuConfig#GROUP_FORM},
     *                 {@link MoreMenuConfig#GROUP_PROTECT} or you custom tag.
     *                 <p>
     * @param item     the item info
     */
    void addMenuItem(int groupTag, MenuItemImpl item);

    /**
     * Remove item by grouptag and itemtag
     *
     * @param groupTag the group id and is unique ,it may be the existing tag {@link MoreMenuConfig#GROUP_FILE },{@link MoreMenuConfig#GROUP_FORM},
     *                 {@link MoreMenuConfig#GROUP_PROTECT } or you custom tag.
     *                 <p/>
     * @param itemTag  the item id ,it belongs to the group and is unique
     *                 you can customize it, but you have tomake sure that the tag is unique,and we use this to sort item.
     *                 <p/>
     *                 The relationship between item and group is as follows:
     *                 <p/>
     *                 {@link MoreMenuConfig#GROUP_FILE }
     *                 GROUP_FILE: [
     *                 {@link MoreMenuConfig#ITEM_DOCINFO,
     *                 {@link MoreMenuConfig#ITEM_REDUCE_FILE_SIZE
     *                 ]
     *                 <p/>
     *                 {@link MoreMenuConfig#GROUP_PROTECT}
     *                 GROUP_PROTECT: [
     *                 {@link MoreMenuConfig#ITEM_PASSWORD,
     *                 {@link MoreMenuConfig#ITEM_CETIFICATE,
     *                 {@link MoreMenuConfig#ITEM_AD_RMD,
     *                 {@link MoreMenuConfig#ITEM_SIGN_CERTIFY,
     *                 {@link MoreMenuConfig#ITEM_REMOVESECURITY_PASSWORD,
     *                 {@link MoreMenuConfig#ITEM_REMOVESECURITY_PUBKEY,
     *                 {@link MoreMenuConfig#ITEM_REMOVESECURITY_RMS,
     *                 {@link MoreMenuConfig#ITEM_CPDFDRM,
     *                 {@link MoreMenuConfig#ITEM_REMOVESECURITY_CPDFDRM
     *                 ],
     *                 <p/>
     *                 {@link MoreMenuConfig#GROUP_FORM},
     *                 GROUP_FORM: [
     *                 {@link MoreMenuConfig#ITEM_RESET_FORM,
     *                 {@link MoreMenuConfig#ITEM_IMPORT_FORM,
     *                 {@link MoreMenuConfig#ITEM_EXPORT_FORM
     *                 ]
     * @see MoreMenuConfig
     */
    void removeMenuItem(int groupTag, int itemTag);

    /**
     * According to the groupTag and itemTag Set the enabled state of this item.
     *
     * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE}, or {@link View#GONE}.
     *                   <p>
     * @param groupTag   the group id and is unique ,it may be the existing tag {@link MoreMenuConfig#GROUP_FILE },{@link MoreMenuConfig#GROUP_FORM},
     *                   {@link MoreMenuConfig#GROUP_PROTECT } or you custom tag.
     *                   <p>
     * @param itemTag    the item id ,it belongs to the group and is unique
     *                   you can customize it, but you have tomake sure that the tag is unique,and we use this to sort item.
     *                   <p>
     *                   The relationship between item and group is as follows:
     *                   <p>
     *                   {@link MoreMenuConfig#GROUP_FILE }
     *                   GROUP_FILE: [
     *                   {@link MoreMenuConfig#ITEM_DOCINFO,
     *                   {@link MoreMenuConfig#ITEM_REDUCE_FILE_SIZE
     *                   ]
     *                   <p>
     *                   {@link MoreMenuConfig#GROUP_PROTECT}
     *                   GROUP_PROTECT: [
     *                   {@link MoreMenuConfig#ITEM_PASSWORD,
     *                   {@link MoreMenuConfig#ITEM_CETIFICATE,
     *                   {@link MoreMenuConfig#ITEM_AD_RMD,
     *                   {@link MoreMenuConfig#ITEM_SIGN_CERTIFY,
     *                   {@link MoreMenuConfig#ITEM_REMOVESECURITY_PASSWORD,
     *                   {@link MoreMenuConfig#ITEM_REMOVESECURITY_PUBKEY,
     *                   {@link MoreMenuConfig#ITEM_REMOVESECURITY_RMS,
     *                   {@link MoreMenuConfig#ITEM_CPDFDRM,
     *                   {@link MoreMenuConfig#ITEM_REMOVESECURITY_CPDFDRM
     *                   ],
     *                   <p>
     *                   {@link MoreMenuConfig#GROUP_FORM},
     *                   GROUP_FORM: [
     *                   {@link MoreMenuConfig#ITEM_RESET_FORM,
     *                   {@link MoreMenuConfig#ITEM_IMPORT_FORM,
     *                   {@link MoreMenuConfig#ITEM_EXPORT_FORM
     *                   ]
     * @see MoreMenuConfig
     */
    void setItemVisibility(int visibility, int groupTag, int itemTag);

    /**
     * Note: This method is only used within RDK
     */
    View getContentView();
}
