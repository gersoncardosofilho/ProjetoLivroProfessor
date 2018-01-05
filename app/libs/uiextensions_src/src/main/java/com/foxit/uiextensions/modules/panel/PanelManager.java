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
package com.foxit.uiextensions.modules.panel;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.foxit.uiextensions.R;
import com.foxit.uiextensions.controls.panel.PanelHost;
import com.foxit.uiextensions.controls.panel.PanelSpec;
import com.foxit.uiextensions.controls.panel.impl.PanelHostImpl;
import com.foxit.uiextensions.utils.AppDisplay;
import com.foxit.uiextensions.utils.LayoutConfig;

public class PanelManager implements IPanelManager {

    private Context mContext;
    private ViewGroup mRootView;
    private PanelHost mPanel;
    private PopupWindow mPanelPopupWindow;
    private IPanelManager.OnShowPanelListener mShowListener = null;

    public PanelManager(Context context, ViewGroup parent, PopupWindow.OnDismissListener dismissListener) {
        mContext = context;
        mRootView = parent;
        mPanel = new PanelHostImpl(mContext, new PanelHost.ICloseDefaultPanelCallback() {
            @Override
            public void closeDefaultPanel(View v) {
                if (mPanelPopupWindow != null && mPanelPopupWindow.isShowing()){
                    mPanelPopupWindow.dismiss();
                }
            }
        });
        setPanelView(mPanel.getContentView(), dismissListener);
    }

    private void setPanelView(final View view, PopupWindow.OnDismissListener dismissListener) {
        mPanelPopupWindow = new PopupWindow(view,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mPanelPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00FFFFFF));
        mPanelPopupWindow.setAnimationStyle(R.style.View_Animation_LtoR);
        if (dismissListener != null) {
            mPanelPopupWindow.setOnDismissListener(dismissListener);

//            mPanelPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    mStateChangeListener.onStateChanged(mReader.getState(), mReader.getState());
//                }
//            });
        }
    }

    @Override
    public PanelHost getPanel() {
        return mPanel;
    }

    public PopupWindow getPanelWindow() {
        return mPanelPopupWindow;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if (mPanelPopupWindow != null && mPanelPopupWindow.isShowing()) {
//            hidePanel();
//            showPanel();
//        }
//    }

    @Override
    public void setOnShowPanelListener(OnShowPanelListener listener) {
        mShowListener = listener;
    }

    @Override
    public void showPanel() {
        if (mPanel.getCurrentSpec() == null) {
            showPanel(PanelSpec.PanelType.Outline);
        } else {
            showPanel(mPanel.getCurrentSpec().getPanelType());
        }
    }

    @Override
    public void showPanel(PanelSpec.PanelType panelType) {

        int viewWidth = mRootView.getWidth();
        int viewHeight = mRootView.getHeight();
        int width;
        int height;
        boolean bVertical = mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (bVertical) {
            height = Math.max(viewWidth, viewHeight);
            width = Math.min(viewWidth, viewHeight);
        } else {
            height = Math.min(viewWidth, viewHeight);
            width = Math.max(viewWidth, viewHeight);
        }
        if (AppDisplay.getInstance(mContext).isPad()) {
            float scale = LayoutConfig.RD_PANEL_WIDTH_SCALE_V;
            if (width > height) {
                scale = LayoutConfig.RD_PANEL_WIDTH_SCALE_H;
            }
            width = (int) (AppDisplay.getInstance(mContext).getScreenWidth() * scale);
        }
        mPanelPopupWindow.setWidth(width);
        mPanelPopupWindow.setHeight(height);
        mPanelPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // need this, else lock screen back will show input keyboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPanelPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
        resetPanelFocus(panelType);
        mPanelPopupWindow.showAtLocation(mRootView, Gravity.LEFT | Gravity.TOP, 0, 0);
//        mStateChangeListener.onStateChanged(mReader.getState(), mReader.getState()); // TODO: 2017/5/29
        if (mShowListener != null) {
            mShowListener.onShow();
        }
    }

    private void resetPanelFocus(PanelSpec.PanelType panelType) {
        if (panelType != null) {
            mPanel.setCurrentSpec(panelType);
        }
    }

    @Override
    public void hidePanel() {
        if (mPanelPopupWindow.isShowing()) {
            mPanelPopupWindow.dismiss();
        }
    }
}
