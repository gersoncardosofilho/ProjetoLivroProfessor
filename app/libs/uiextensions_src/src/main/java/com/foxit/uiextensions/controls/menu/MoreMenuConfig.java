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

/**
 * The unique identifier of More menu group and group item {@link IMenuView} {@link MoreMenuModule}
 */
public class MoreMenuConfig {
    public static final int GROUP_FILE = 100;
    public static final int GROUP_PROTECT = 101;
    public static final int GROUP_FORM = 102;

    //Group_file
    /** Group_File_DocInfo */
    public static final int ITEM_DOCINFO = 0;
    /** Group_File_ReduceFileSize */
    public static final int ITEM_REDUCE_FILE_SIZE = 1;

    //Group_protect
    /** Group_Protect_Password */
    public static final int ITEM_PASSWORD = 0;
    /** Group_Protect_Certificate*/
    public static final int ITEM_CETIFICATE = 1;
    /** Group_Protect_Ad_Rmd */
    public static final int ITEM_AD_RMD = 2;
    /** Group_Protect_SignCertify */
    public static final int ITEM_SIGN_CERTIFY = 3;
    /** Group_Protect_Remove_Security_Password */
    public static final int ITEM_REMOVESECURITY_PASSWORD = 4;
    /** Group_Protect_Remove_Security_Pubkey */
    public static final int ITEM_REMOVESECURITY_PUBKEY = 5;
    /** Group_Protect_Romove_security_Rms */
    public static final int ITEM_REMOVESECURITY_RMS = 6;
    /** Group_Protect_CpdForm */
    public static final int ITEM_CPDFDRM = 7;
    /** Group_Protect_Remove_Security_CpdForm */
    public static final int ITEM_REMOVESECURITY_CPDFDRM = 8;

    //Group_form
    /** Group_From_Reset_Form */
    public static final int ITEM_RESET_FORM = 0;
    /** Group_From_Import_Form*/
    public static final int ITEM_IMPORT_FORM = 1;
    /** Group_From_Export_Form */
    public static final int ITEM_EXPORT_FORM = 2;
}
