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
package com.foxit.uiextensions.annots.link;

import android.content.Context;
import android.graphics.PointF;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.sdk.common.PDFError;
import com.foxit.sdk.common.PDFException;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.action.Destination;
import com.foxit.uiextensions.Module;
import com.foxit.uiextensions.UIExtensionsManager;
import com.foxit.uiextensions.utils.ToolUtil;

public class LinkModule implements Module {
    private LinkAnnotHandler mAnnotHandler;

    private Context mContext;
    private PDFViewCtrl mPdfViewCtrl;
    private PDFViewCtrl.UIExtensionsManager mUiExtensionsManager;

    public LinkModule(Context context, PDFViewCtrl pdfViewCtrl, PDFViewCtrl.UIExtensionsManager uiExtensionsManager) {
        mContext = context;
        mPdfViewCtrl = pdfViewCtrl;
        mUiExtensionsManager = uiExtensionsManager;
    }

    @Override
    public String getName() {
        return Module.MODULE_NAME_LINK;
    }

    public LinkAnnotHandler getAnnotHandler() {
        return mAnnotHandler;
    }

    @Override
    public boolean loadModule() {
        mAnnotHandler = new LinkAnnotHandler(mContext, mPdfViewCtrl);
        mPdfViewCtrl.registerDocEventListener(mDocEventListener);
        mPdfViewCtrl.registerPageEventListener(mAnnotHandler.getPageEventListener());
        mPdfViewCtrl.registerRecoveryEventListener(mRecoveryListener);

        if (mUiExtensionsManager != null && mUiExtensionsManager instanceof UIExtensionsManager) {
            ToolUtil.registerAnnotHandler((UIExtensionsManager) mUiExtensionsManager, mAnnotHandler);
            ((UIExtensionsManager) mUiExtensionsManager).registerModule(this);
        }
        return true;
    }

    @Override
    public boolean unloadModule() {
        mPdfViewCtrl.unregisterDocEventListener(mDocEventListener);
        mPdfViewCtrl.unregisterPageEventListener(mAnnotHandler.getPageEventListener());
        mPdfViewCtrl.unregisterRecoveryEventListener(mRecoveryListener);

        if (mUiExtensionsManager != null && mUiExtensionsManager instanceof UIExtensionsManager) {
            ToolUtil.unregisterAnnotHandler((UIExtensionsManager) mUiExtensionsManager, mAnnotHandler);
        }
        return true;
    }


    PDFViewCtrl.IDocEventListener mDocEventListener = new PDFViewCtrl.IDocEventListener() {
        @Override
        public void onDocWillOpen() {
        }

        @Override
        public void onDocOpened(PDFDoc pdfDoc, int i) {
            mAnnotHandler.isDocClosed = false;

            Destination destination = mAnnotHandler.getDestination();
            if (destination != null){
                try {
                    PointF destPt = mAnnotHandler.getDestinationPoint(destination);
                    PointF devicePt = new PointF();
                    if (!mPdfViewCtrl.convertPdfPtToPageViewPt(destPt, devicePt, destination.getPageIndex())) {
                        devicePt.set(0, 0);
                    }
                    mPdfViewCtrl.gotoPage(destination.getPageIndex(), devicePt.x, devicePt.y);
                    mAnnotHandler.setDestination(null);
                }catch (PDFException e){
                    if (e.getLastError() == PDFError.OOM.getCode()) {
                        mPdfViewCtrl.recoverForOOM();
                    }
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onDocWillClose(PDFDoc pdfDoc) {
            mAnnotHandler.isDocClosed = true;
            mAnnotHandler.clear();
        }

        @Override
        public void onDocClosed(PDFDoc pdfDoc, int i) {
        }

        @Override
        public void onDocWillSave(PDFDoc pdfDoc) {
        }

        @Override
        public void onDocSaved(PDFDoc pdfDoc, int i) {
        }
    };

    PDFViewCtrl.IRecoveryEventListener mRecoveryListener = new PDFViewCtrl.IRecoveryEventListener() {
        @Override
        public void onWillRecover() {
            mAnnotHandler.isDocClosed = true;
            mAnnotHandler.clear();
        }

        @Override
        public void onRecovered() {
        }
    };
}
