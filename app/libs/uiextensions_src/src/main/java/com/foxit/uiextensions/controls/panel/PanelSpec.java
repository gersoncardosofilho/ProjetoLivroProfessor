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
package com.foxit.uiextensions.controls.panel;

import android.view.View;

import com.foxit.uiextensions.Module;

public interface PanelSpec {

    /**  Panel types. */
    enum PanelType{
        /** Reading bookmark panel type. */
        ReadingBookmarks(0, Module.MODULE_NAME_BOOKMARK),
        /** Outline panel type. */
        Outline(1, Module.MODULE_NAME_OUTLINE),
        /** Annotation panel type. */
        Annotations(2, Module.MODULE_NAME_ANNOTPANEL) ,
        /** Attachment panel type. */
        Attachments(3, Module.MODULE_NAME_FILE_PANEL);

        private int mTag;
        private String mModuleName;

        PanelType(int tag, String moduleName){
            this.mTag = tag;
            this.mModuleName = moduleName;
        }

        public int getTag(){
            return mTag;
        }

        public String getModuleName(){
            return mModuleName;
        }
    };

    int getIcon();

    PanelType getPanelType();

    View getTopToolbar();

    View getContentView();

    void onActivated();

    void onDeactivated();
}
