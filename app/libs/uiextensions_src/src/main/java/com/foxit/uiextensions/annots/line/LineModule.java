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
package com.foxit.uiextensions.annots.line;

import android.content.Context;
import android.graphics.Canvas;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.uiextensions.Module;
import com.foxit.uiextensions.ToolHandler;
import com.foxit.uiextensions.UIExtensionsManager;
import com.foxit.uiextensions.annots.AnnotHandler;
import com.foxit.uiextensions.controls.propertybar.imp.AnnotMenuImpl;
import com.foxit.uiextensions.controls.propertybar.imp.PropertyBarImpl;
import com.foxit.uiextensions.utils.ToolUtil;


public class LineModule implements Module {
	protected LineUtil mUtil;
	protected LineToolHandler mLineToolHandler;
	protected LineToolHandler mArrowToolHandler;
	protected LineAnnotHandler mLineAnnotHandler;

	Context mContext;
	PDFViewCtrl mPdfViewCtrl;
	private PDFViewCtrl.UIExtensionsManager mUiExtensionsManager;
	public LineModule(Context context, PDFViewCtrl pdfViewCtrl, PDFViewCtrl.UIExtensionsManager uiExtensionsManager) {
		mContext = context;
		mPdfViewCtrl = pdfViewCtrl;
		mUiExtensionsManager = uiExtensionsManager;
	}

	@Override
	public String getName() {
		return Module.MODULE_NAME_LINE;
	}

	@Override
	public boolean loadModule() {
		mUtil = new LineUtil(mContext, this);
		mLineAnnotHandler = new LineAnnotHandler(mContext, mPdfViewCtrl, mUtil);

		if (mUiExtensionsManager != null && mUiExtensionsManager instanceof UIExtensionsManager) {
			UIExtensionsManager.Config mModulesConfig = ((UIExtensionsManager) mUiExtensionsManager).getModulesConfig();
			UIExtensionsManager.Config.AnnotConfig annotConfig = mModulesConfig.getAnnotConfig();

			// tool line
			if (annotConfig.isLoadDrawLine()) {
				mLineToolHandler = new LineToolHandler(mContext, mPdfViewCtrl, mUtil, LineConstants.INTENT_LINE_DEFAULT);
				mLineAnnotHandler.mRealAnnotHandler.setToolHandler(mLineToolHandler);
				mLineToolHandler.mAnnotHandler = mLineAnnotHandler.mRealAnnotHandler;
				mLineAnnotHandler.setAnnotMenu(LineConstants.INTENT_LINE_DEFAULT, new AnnotMenuImpl(mContext, mPdfViewCtrl));
				mLineAnnotHandler.setPropertyBar(LineConstants.INTENT_LINE_DEFAULT, new PropertyBarImpl(mContext, mPdfViewCtrl));
				((UIExtensionsManager) mUiExtensionsManager).registerToolHandler(mLineToolHandler);
				mLineToolHandler.initUiElements();
			}

			// arrow line
			if (annotConfig.isLoadDrawArrow()) {
				mArrowToolHandler = new LineToolHandler(mContext, mPdfViewCtrl, mUtil, LineConstants.INTENT_LINE_ARROW);
				mLineAnnotHandler.mRealAnnotHandler.setToolHandler(mArrowToolHandler);
				mArrowToolHandler.mAnnotHandler = mLineAnnotHandler.mRealAnnotHandler;
				mLineAnnotHandler.setAnnotMenu(LineConstants.INTENT_LINE_ARROW, new AnnotMenuImpl(mContext, mPdfViewCtrl));
				mLineAnnotHandler.setPropertyBar(LineConstants.INTENT_LINE_ARROW, new PropertyBarImpl(mContext, mPdfViewCtrl));
				((UIExtensionsManager) mUiExtensionsManager).registerToolHandler(mArrowToolHandler);
				mArrowToolHandler.initUiElements();
			}

			ToolUtil.registerAnnotHandler((UIExtensionsManager) mUiExtensionsManager, mLineAnnotHandler);
			((UIExtensionsManager) mUiExtensionsManager).registerModule(this);
		}
		mPdfViewCtrl.registerDrawEventListener(mDrawEventListener);
		mPdfViewCtrl.registerRecoveryEventListener(memoryEventListener);

		return true;
	}

	@Override
	public boolean unloadModule() {
		mPdfViewCtrl.unregisterDrawEventListener(mDrawEventListener);
		mPdfViewCtrl.unregisterRecoveryEventListener(memoryEventListener);
		if (mUiExtensionsManager != null && mUiExtensionsManager instanceof UIExtensionsManager) {

			if(mArrowToolHandler != null){
				((UIExtensionsManager) mUiExtensionsManager).unregisterToolHandler(mArrowToolHandler);
				mArrowToolHandler.uninitUiElements();
			}
			if (mLineToolHandler != null){
				((UIExtensionsManager) mUiExtensionsManager).unregisterToolHandler(mLineToolHandler);
				mLineToolHandler.uninitUiElements();
			}
			ToolUtil.unregisterAnnotHandler((UIExtensionsManager) mUiExtensionsManager, mLineAnnotHandler);
		}
		return true;
	}

	public AnnotHandler getAnnotHandler() {
		return mLineAnnotHandler;
	}

	public ToolHandler getLineToolHandler() {
		return mLineToolHandler;
	}

	public ToolHandler getArrowToolHandler() {
		return mArrowToolHandler;
	}

	private PDFViewCtrl.IDrawEventListener mDrawEventListener = new PDFViewCtrl.IDrawEventListener() {

		@Override
		public void onDraw(int pageIndex, Canvas canvas) {
			mLineAnnotHandler.onDrawForControls(canvas);
		}
	};

	PDFViewCtrl.IRecoveryEventListener memoryEventListener = new PDFViewCtrl.IRecoveryEventListener() {
		@Override
		public void onWillRecover() {
		}

		@Override
		public void onRecovered() {
		}
	};
}
