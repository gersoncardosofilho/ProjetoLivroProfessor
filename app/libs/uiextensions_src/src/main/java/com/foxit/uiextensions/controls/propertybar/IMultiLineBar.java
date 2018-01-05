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
package com.foxit.uiextensions.controls.propertybar;

import android.view.View;

import com.foxit.uiextensions.pdfreader.impl.PDFReader;


/**
 * This is mainly used to control the display mode of the page, and you can use it to hide/display unnecessary functions.
 * <br/><br/>
 * you can use it through {@link PDFReader#getSettingBar()}
 */
public interface IMultiLineBar {
    /**
     * Note: This method is only used within RDK
     */
    public interface IML_ValueChangeListener {
        public void onValueChanged(int type, Object value);

        public void onDismiss();

        public int getType();
    }

    /**
     * Note: This method is only used within RDK
     */
    public static final int TYPE_LIGHT = 0x0001;
    /** Switch day and night mode */
    public static final int TYPE_DAYNIGHT = 0x0002;
    /** Control the brightness of the page */
    public static final int TYPE_SYSLIGHT = 0x0004;
    /** Single Page mode*/
    public static final int TYPE_SINGLEPAGE = 0x0008;
    /** Continuous Page mode*/
    public static final int TYPE_CONTINUOUSPAGE = 0x0010;
    /** the Page thumbanil*/
    public static final int TYPE_THUMBNAIL = 0x0020;
    /** Screen lock*/
    public static final int TYPE_LOCKSCREEN = 0x0040;
    /** Reflow mode*/
    public static final int TYPE_REFLOW = 0x0080;
    /** Crop mode*/
    public static final int TYPE_CROP = 0x0100;
    int TYPE_FACING_MODE = 0x0120;

    /**
     * Note: This method is only used within RDK
     */
    public void setProperty(int property, Object value);

    /**
     * Note: This method is only used within RDK
     */
    public boolean isShowing();

    /**
     * Note: This method is only used within RDK
     */
    public void show();

    /**
     * Set the enabled state of this view.
     *
     * @param type       the modules tag,  Please refer to {@link  # TYPE_XXX } values
     * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE}, or {@link View#GONE}.
     *                   <></>
     * @see #TYPE_DAYNIGHT
     * @see #TYPE_SYSLIGHT
     * @see #TYPE_SINGLEPAGE
     * @see #TYPE_CONTINUOUSPAGE
     * @see #TYPE_THUMBNAIL
     * @see #TYPE_LOCKSCREEN
     * @see #TYPE_REFLOW
     * @see #TYPE_CROP
     * @see #TYPE_FACING_MODE
     *
     */
    public void setVisibility(int type, int visibility);

    /**
     * Note: This method is only used within RDK
     */
    public void dismiss();

    /**
     * Note: This method is only used within RDK
     */
    public View getContentView();

    /**
     * Note: This method is only used within RDK
     */
    public void registerListener(IML_ValueChangeListener listener);

    /**
     * Note: This method is only used within RDK
     */
    public void unRegisterListener(IML_ValueChangeListener listener);
}
