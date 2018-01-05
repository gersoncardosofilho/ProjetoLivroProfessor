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
package com.foxit.uiextensions.pdfreader;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.uiextensions.DocumentManager;
import com.foxit.uiextensions.controls.propertybar.IMultiLineBar;
import com.foxit.uiextensions.controls.toolbar.IBarsHandler;

public interface IPDFReader {

    interface BackEventListener {
        /**
         * Called when the back button clicked.
         *
         * @return Return <code>true</code> to prevent this event from being propagated
         *         further, or <code>false</code> to indicate that you have not handled
         *         this event and it should continue to be propagated by Foxit.
         */
        boolean onBack();
    }

    boolean registerLifecycleListener(ILifecycleEventListener listener);

    boolean unregisterLifecycleListener(ILifecycleEventListener listener);

    boolean registerStateChangeListener(IStateChangeListener listener);

    boolean unregisterStateChangeListener(IStateChangeListener listener);

    IMainFrame getMainFrame();

    PDFViewCtrl.UIExtensionsManager getUIExtensionsManager();

    DocumentManager getDocMgr();

    PDFViewCtrl getDocViewer();

    IBarsHandler getBarManager();

    IMultiLineBar  getSettingBar();

    int getState();

    void changeState(int state);

    void backToPrevActivity();

    void setBackEventListener(BackEventListener listener);

    BackEventListener getBackEventListener();
}
