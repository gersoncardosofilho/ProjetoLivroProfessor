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
package com.foxit.uiextensions.controls.propertybar.imp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.foxit.uiextensions.R;
import com.foxit.uiextensions.UIExtensionsManager;
import com.foxit.uiextensions.controls.propertybar.MoreTools;
import com.foxit.uiextensions.utils.AppDisplay;

import java.util.HashMap;
import java.util.Map;

public class MoreToolsImpl implements MoreTools {
    private Context mContext;
    private ViewGroup mRootView;
    private View mLl_root;
    private LinearLayout mLl_content;
    private LinearLayout mLl_arrow;
    private ImageView mIv_arrow_bottom;

    private ImageView mMt_iv_highlight;
    private ImageView mMt_iv_underline;
    private ImageView mMt_iv_squiggly;
    private ImageView mMt_iv_strikeout;
    private ImageView mMt_iv_inserttext;
    private ImageView mMt_iv_replace;


    private ImageView mMt_iv_circle;
    private ImageView mMt_iv_square;
    private ImageView mMt_iv_pencil;
    private ImageView mMt_iv_eraser;
    private ImageView mMt_iv_line;
    private ImageView mMt_iv_arrow;

    private LinearLayout mMt_ll_typewriter;
    private ImageView mMt_iv_typewriter;
    private TextView mMt_tv_typewriter;

    private LinearLayout mMt_ll_note;
    private ImageView mMt_iv_note;
    private TextView mMt_tv_note;

    private LinearLayout mMt_ll_stamp;
    private ImageView mMt_iv_stamp;
    private TextView mMt_tv_stamp;
    private LinearLayout mMt_ll_fileattach;
    private ImageView mMt_iv_fileattach;
    private TextView mMt_tv_fileattach;

    private boolean mShowMask = false;
    private int mPadWidth;
    private PopupWindow mPopupWindow;
    private Map<Integer, IMT_MoreClickListener> mListeners;
    private IMT_DismissListener mDismissListener;
    private UIExtensionsManager.Config mModuleConfig;

    public MoreToolsImpl(Context context, ViewGroup viewGroup, UIExtensionsManager.Config config) {
        this.mContext = context;
        mRootView = viewGroup;
        mListeners = new HashMap<Integer, IMT_MoreClickListener>();
        mPadWidth = AppDisplay.getInstance(mContext).dp2px(315.0f);
        mModuleConfig = config;
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        UIExtensionsManager.Config.AnnotConfig annotConfig = mModuleConfig.getAnnotConfig();

        mLl_root = LayoutInflater.from(mContext).inflate(R.layout.mt_moretools, null, false);

        mMt_iv_highlight = (ImageView) mLl_root.findViewById(R.id.mt_iv_highlight);
        int highlightVisibility = annotConfig.isLoadHighlight() ? View.VISIBLE : View.GONE;
        mMt_iv_highlight.setVisibility(highlightVisibility);

        mMt_iv_underline = (ImageView) mLl_root.findViewById(R.id.mt_iv_underline);
        int underlineVisibility = annotConfig.isLoadUnderline() ? View.VISIBLE : View.GONE;
        mMt_iv_underline.setVisibility(underlineVisibility);

        mMt_iv_squiggly = (ImageView) mLl_root.findViewById(R.id.mt_iv_squiggly);
        int squigglyVisibility = annotConfig.isLoadSquiggly() ? View.VISIBLE : View.GONE;
        mMt_iv_squiggly.setVisibility(squigglyVisibility);

        mMt_iv_strikeout = (ImageView) mLl_root.findViewById(R.id.mt_iv_strikeout);
        int strikeoutVisibility = annotConfig.isLoadStrikeout() ? View.VISIBLE : View.GONE;
        mMt_iv_strikeout.setVisibility(strikeoutVisibility);

        mMt_iv_inserttext = (ImageView) mLl_root.findViewById(R.id.mt_iv_insert);
        int insertVisibility = annotConfig.isLoadInsertText() ? View.VISIBLE : View.GONE;
        mMt_iv_inserttext.setVisibility(insertVisibility);

        mMt_iv_replace = (ImageView) mLl_root.findViewById(R.id.mt_iv_replace);
        int replaceVisibility = annotConfig.isLoadReplaceText() ? View.VISIBLE : View.GONE;
        mMt_iv_replace.setVisibility(replaceVisibility);

        mMt_iv_circle = (ImageView) mLl_root.findViewById(R.id.mt_iv_circle);
        int circleVisibility = annotConfig.isLoadDrawCircle() ? View.VISIBLE : View.GONE;
        mMt_iv_circle.setVisibility(circleVisibility);

        mMt_iv_square = (ImageView) mLl_root.findViewById(R.id.mt_iv_square);
        int squareVisibility = annotConfig.isLoadDrawSquare() ? View.VISIBLE : View.GONE;
        mMt_iv_square.setVisibility(squareVisibility);

        mMt_iv_pencil = (ImageView) mLl_root.findViewById(R.id.mt_iv_pencil);
        int pencilVisibility = annotConfig.isLoadDrawPencil() ? View.VISIBLE : View.GONE;
        mMt_iv_pencil.setVisibility(pencilVisibility);

        mMt_iv_eraser = (ImageView) mLl_root.findViewById(R.id.mt_iv_eraser);
        int eraserVisibility = annotConfig.isLoadEraser()? View.VISIBLE : View.GONE;
        mMt_iv_eraser.setVisibility(eraserVisibility);

        mMt_iv_line = (ImageView) mLl_root.findViewById(R.id.mt_iv_line);
        int lineVisibility = annotConfig.isLoadDrawLine() ? View.VISIBLE : View.GONE;
        mMt_iv_line.setVisibility(lineVisibility);

        mMt_iv_arrow = (ImageView) mLl_root.findViewById(R.id.mt_iv_arrow);
        int arrowVisibility = annotConfig.isLoadDrawArrow() ? View.VISIBLE : View.GONE;
        mMt_iv_arrow.setVisibility(arrowVisibility);

        mMt_ll_typewriter = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_typewriter);
        mMt_iv_typewriter = (ImageView) mLl_root.findViewById(R.id.mt_iv_typewriter);
        mMt_tv_typewriter = (TextView) mLl_root.findViewById(R.id.mt_tv_typewriter);
        int typewriterVisibility = annotConfig.isLoadTypewriter() ? View.VISIBLE : View.GONE;
        mMt_ll_typewriter.setVisibility(typewriterVisibility);

        mMt_ll_note = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_note);
        mMt_iv_note = (ImageView) mLl_root.findViewById(R.id.mt_iv_note);
        mMt_tv_note = (TextView) mLl_root.findViewById(R.id.mt_tv_note);
        int noteVisibility = annotConfig.isLoadNote() ? View.VISIBLE : View.GONE;
        mMt_ll_note.setVisibility(noteVisibility);

        mMt_ll_stamp = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_stamp);
        mMt_iv_stamp = (ImageView) mLl_root.findViewById(R.id.mt_iv_stamp);
        mMt_tv_stamp = (TextView) mLl_root.findViewById(R.id.mt_tv_stamp);
        int stampVisibility = annotConfig.isLoadStamp() ? View.VISIBLE : View.GONE;
        mMt_ll_stamp.setVisibility(stampVisibility);

        mMt_ll_fileattach = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_fileattach);
        mMt_iv_fileattach = (ImageView) mLl_root.findViewById(R.id.mt_iv_fileattach);
        mMt_tv_fileattach = (TextView) mLl_root.findViewById(R.id.mt_tv_fileattach);
        int fileattachVisibility = annotConfig.isLoadFileattach() ? View.VISIBLE : View.GONE;
        mMt_ll_fileattach.setVisibility(fileattachVisibility);

        mLl_content = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_content);
        mLl_arrow = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_arrow);
        mIv_arrow_bottom = (ImageView) mLl_root.findViewById(R.id.mt_iv_arrow_bottom);

        LinearLayout mMt_ll_textMarkup = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_text_markup);
        boolean isShowTextMarkup = false;
        int textMarkupCount = mMt_ll_textMarkup.getChildCount();
        for (int i = 0; i < textMarkupCount; i++) {
            View view = mMt_ll_textMarkup.getChildAt(i);
            if (view.getVisibility() == View.VISIBLE) {
                isShowTextMarkup = true;
                break;
            }
        }
        int markupVisibility = isShowTextMarkup ? View.VISIBLE : View.GONE;
        mLl_root.findViewById(R.id.mt_ll_parent_markup).setVisibility(markupVisibility);

        LinearLayout mMt_ll_drawing = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_drawing);
        boolean isShowDrawing = false;
        int drawingCount = mMt_ll_drawing.getChildCount();
        for (int i = 0; i < drawingCount; i++) {
            View view = mMt_ll_drawing.getChildAt(i);
            if (view.getVisibility() == View.VISIBLE) {
                isShowDrawing = true;
                break;
            }
        }
        int drawParentVisibility = isShowDrawing ? View.VISIBLE : View.GONE;
        mLl_root.findViewById(R.id.mt_ll_parent_drawing).setVisibility(drawParentVisibility);

        LinearLayout mMt_ll_others = (LinearLayout) mLl_root.findViewById(R.id.mt_ll_others);
        boolean isShowOhers = false;
        int othersCount = mMt_ll_others.getChildCount();
        for (int i = 0; i < othersCount; i++) {
            View view = mMt_ll_others.getChildAt(i);
            if (view.getVisibility() == View.VISIBLE) {
                isShowOhers = true;
                break;
            }
        }
        int othersParentVisibility = isShowOhers ? View.VISIBLE : View.GONE;
        mLl_root.findViewById(R.id.mt_ll_parent_others).setVisibility(othersParentVisibility);

        if (mPopupWindow == null) {
            if (AppDisplay.getInstance(mContext).isPad()) {
                mPopupWindow = new PopupWindow(mLl_root, mPadWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLl_content.setBackgroundResource(R.drawable.dlg_title_bg_4circle_corner_white);

                int bottomArrowVisibility = isShowDrawing || isShowOhers || isShowTextMarkup ? View.VISIBLE : View.GONE;
                mLl_arrow.setVisibility(bottomArrowVisibility);
            } else {
                mPopupWindow = new PopupWindow(mLl_root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLl_content.setBackgroundResource(R.color.ux_color_white);
                mLl_arrow.setVisibility(View.GONE);
            }

            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (mDismissListener != null) {
                        mDismissListener.onMTDismiss();
                    }
//                    mRead.getMainFrame().hideMaskView();
                }
            });
            if (!AppDisplay.getInstance(mContext).isPad()) {
                mPopupWindow.setAnimationStyle(R.style.View_Animation_BtoT);
            }
        } else {
            mPopupWindow.setContentView(mLl_root);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.mt_iv_highlight) {
                    if (mListeners.get(MoreTools.MT_TYPE_HIGHLIGHT) != null) {
                        mListeners.get(MoreTools.MT_TYPE_HIGHLIGHT).onMTClick(MoreTools.MT_TYPE_HIGHLIGHT);
                    }
                } else if (id == R.id.mt_iv_underline) {
                    if (mListeners.get(MoreTools.MT_TYPE_UNDERLINE) != null) {
                        mListeners.get(MoreTools.MT_TYPE_UNDERLINE).onMTClick(MoreTools.MT_TYPE_UNDERLINE);
                    }
                } else if (id == R.id.mt_iv_squiggly) {
                    if (mListeners.get(MoreTools.MT_TYPE_SQUIGGLY) != null) {
                        mListeners.get(MoreTools.MT_TYPE_SQUIGGLY).onMTClick(MoreTools.MT_TYPE_SQUIGGLY);
                    }
                } else if (id == R.id.mt_iv_strikeout) {
                    if (mListeners.get(MoreTools.MT_TYPE_STRIKEOUT) != null) {
                        mListeners.get(MoreTools.MT_TYPE_STRIKEOUT).onMTClick(MoreTools.MT_TYPE_STRIKEOUT);
                    }
                } else if (id == R.id.mt_iv_circle) {
                    if (mListeners.get(MoreTools.MT_TYPE_CIRCLE) != null) {
                        mListeners.get(MoreTools.MT_TYPE_CIRCLE).onMTClick(MoreTools.MT_TYPE_CIRCLE);
                    }
                } else if (id == R.id.mt_iv_square) {
                    if (mListeners.get(MoreTools.MT_TYPE_SQUARE) != null) {
                        mListeners.get(MoreTools.MT_TYPE_SQUARE).onMTClick(MoreTools.MT_TYPE_SQUARE);
                    }
                } else if (id == R.id.mt_iv_note) {
                    if (mListeners.get(MoreTools.MT_TYPE_ANNOTTEXT) != null) {
                        mListeners.get(MoreTools.MT_TYPE_ANNOTTEXT).onMTClick(MoreTools.MT_TYPE_ANNOTTEXT);
                    }
                } else if (id == R.id.mt_iv_typewriter) {
                    if (mListeners.get(MoreTools.MT_TYPE_TYPEWRITER) != null) {
                        mListeners.get(MoreTools.MT_TYPE_TYPEWRITER).onMTClick(MoreTools.MT_TYPE_TYPEWRITER);
                    }
                } else if (id == R.id.mt_iv_stamp) {
                    if (mListeners.get(MoreTools.MT_TYPE_STAMP) != null) {
                        mListeners.get(MoreTools.MT_TYPE_STAMP).onMTClick(MoreTools.MT_TYPE_STAMP);
                    }
                } else if (id == R.id.mt_iv_insert) {
                    if (mListeners.get(MoreTools.MT_TYPE_INSERTTEXT) != null) {
                        mListeners.get(MoreTools.MT_TYPE_INSERTTEXT).onMTClick(MoreTools.MT_TYPE_INSERTTEXT);
                    }

                } else if (id == R.id.mt_iv_replace) {
                    if (mListeners.get(MoreTools.MT_TYPE_REPLACE) != null) {
                        mListeners.get(MoreTools.MT_TYPE_REPLACE).onMTClick(MoreTools.MT_TYPE_REPLACE);
                    }
                } else if (id == R.id.mt_iv_pencil) {
                    if (mListeners.get(MoreTools.MT_TYPE_INK) != null) {
                        mListeners.get(MoreTools.MT_TYPE_INK).onMTClick(MoreTools.MT_TYPE_INK);
                    }
                } else if (id == R.id.mt_iv_eraser) {
                    if (mListeners.get(MoreTools.MT_TYPE_ERASER) != null) {
                        mListeners.get(MoreTools.MT_TYPE_ERASER).onMTClick(MoreTools.MT_TYPE_ERASER);
                    }
                } else if (id == R.id.mt_iv_arrow) {
                    if (mListeners.get(MoreTools.MT_TYPE_ARROW) != null) {
                        mListeners.get(MoreTools.MT_TYPE_ARROW).onMTClick(MoreTools.MT_TYPE_ARROW);
                    }
                } else if (id == R.id.mt_iv_line) {
                    if (mListeners.get(MoreTools.MT_TYPE_LINE) != null) {
                        mListeners.get(MoreTools.MT_TYPE_LINE).onMTClick(MoreTools.MT_TYPE_LINE);
                    }
                } else if (id == R.id.mt_iv_fileattach) {
                    if (mListeners.get(MoreTools.MT_TYPE_FILEATTACHMENT) != null) {
                        mListeners.get(MoreTools.MT_TYPE_FILEATTACHMENT).onMTClick(MoreTools.MT_TYPE_FILEATTACHMENT);
                    }
                }
                dismiss();
            }
        };

        mMt_iv_highlight.setOnClickListener(listener);
        mMt_iv_underline.setOnClickListener(listener);
        mMt_iv_strikeout.setOnClickListener(listener);
        mMt_iv_squiggly.setOnClickListener(listener);
        mMt_iv_inserttext.setOnClickListener(listener);
        mMt_iv_replace.setOnClickListener(listener);

        mMt_iv_circle.setOnClickListener(listener);
        mMt_iv_square.setOnClickListener(listener);
        mMt_iv_pencil.setOnClickListener(listener);
        mMt_iv_eraser.setOnClickListener(listener);
        mMt_iv_line.setOnClickListener(listener);
        mMt_iv_arrow.setOnClickListener(listener);

        mMt_iv_typewriter.setOnClickListener(listener);
        mMt_iv_note.setOnClickListener(listener);
        mMt_iv_stamp.setOnClickListener(listener);
        mMt_iv_fileattach.setOnClickListener(listener);

    }

    @Override
    public void registerListener(IMT_MoreClickListener listener) {
        if (!mListeners.containsKey(listener.getType())) {
            this.mListeners.put(listener.getType(), listener);
        }
    }

    @Override
    public void unRegisterListener(IMT_MoreClickListener listener) {
        if (mListeners.containsKey(listener.getType())) {
            this.mListeners.remove(listener.getType());
        }
    }

    @Override
    public void setMTDismissListener(IMT_DismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    @Override
    public View getContentView() {
        return mLl_root;
    }

    @Override
    public void setButtonEnable(int buttonType, boolean enable) {
        if (buttonType == MT_TYPE_HIGHLIGHT) {
            mMt_iv_highlight.setEnabled(enable);
        } else if (buttonType == MT_TYPE_UNDERLINE) {
            mMt_iv_underline.setEnabled(enable);
        } else if (buttonType == MT_TYPE_SQUIGGLY) {
            mMt_iv_squiggly.setEnabled(enable);
        } else if (buttonType == MT_TYPE_STRIKEOUT) {
            mMt_iv_strikeout.setEnabled(enable);
        } else if (buttonType == MT_TYPE_CIRCLE) {
            mMt_iv_circle.setEnabled(enable);
        } else if (buttonType == MT_TYPE_SQUARE) {
            mMt_iv_square.setEnabled(enable);
        } else if (buttonType == MT_TYPE_INSERTTEXT) {
            mMt_iv_inserttext.setEnabled(enable);
        } else if (buttonType == MT_TYPE_REPLACE) {
            mMt_iv_replace.setEnabled(enable);
        } else if (buttonType == MT_TYPE_INK) {//pencil
            mMt_iv_pencil.setEnabled(enable);
        } else if (buttonType == MT_TYPE_ERASER) {
            mMt_iv_eraser.setEnabled(enable);
        } else if (buttonType == MT_TYPE_LINE) {
            mMt_iv_line.setEnabled(enable);
        } else if (buttonType == MT_TYPE_ARROW) {
            mMt_iv_arrow.setEnabled(enable);
        } else if (buttonType == MT_TYPE_ANNOTTEXT) {
            mMt_tv_note.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_gray));
            mMt_iv_note.setEnabled(enable);
        } else if (buttonType == MT_TYPE_TYPEWRITER) {
            if (enable) {
                mMt_tv_typewriter.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_dark));
                mMt_iv_typewriter.setEnabled(enable);
            } else {
                mMt_tv_typewriter.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_gray));
                mMt_iv_typewriter.setEnabled(enable);
            }
        } else if (buttonType == MT_TYPE_STAMP) {
            if (enable) {
                mMt_tv_stamp.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_dark));
            } else {
                mMt_tv_stamp.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_gray));
            }

            mMt_iv_stamp.setEnabled(enable);

        } else if (buttonType == MT_TYPE_FILEATTACHMENT) {
            if (enable) {

                mMt_tv_fileattach.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_dark));
                mMt_iv_fileattach.setEnabled(enable);

            } else {
                mMt_tv_fileattach.setTextColor(mContext.getResources().getColor(R.color.ux_text_color_body2_gray));
                mMt_iv_fileattach.setEnabled(enable);
            }
        }
    }

    @Override
    public void show(RectF rectF, boolean showMask) {
        if (mPopupWindow != null && !isShowing()) {
            mPopupWindow.setFocusable(true);

            int height = mRootView.getHeight();
            int width = mRootView.getWidth();
            if (AppDisplay.getInstance(mContext).isPad()) {

                mIv_arrow_bottom.measure(0, 0);

                int toRight;
                if (mPadWidth / 2 > width - rectF.right + (rectF.right - rectF.left) / 2) {
                    toRight = 0;
                    mLl_arrow.setPadding((int) (mPadWidth / 2.0f - mIv_arrow_bottom.getMeasuredWidth() / 2.0f
                            + mPadWidth / 2 - (width - rectF.right + (rectF.right - rectF.left) / 2)), 0, 0, 0);
                } else {
                    if (rectF.right - (rectF.right - rectF.left) / 2 > mPadWidth / 2.0f) {
                        toRight = (int) (width - rectF.right + (rectF.right - rectF.left) / 2 - mPadWidth / 2);
                        mLl_arrow.setPadding((int) (mPadWidth / 2.0f - mIv_arrow_bottom.getMeasuredWidth() / 2.0f), 0, 0, 0);
                    } else {
                        toRight = width - mPadWidth;
                        if (rectF.right - (rectF.right - rectF.left) / 2 > mIv_arrow_bottom.getMeasuredWidth() / 2.0f) {
                            mLl_arrow.setPadding((int) (rectF.right - (rectF.right - rectF.left) / 2 - mIv_arrow_bottom.getMeasuredWidth() / 2.0f), 0, 0, 0);
                        } else {
                            mLl_arrow.setPadding(0, 0, 0, 0);
                        }
                    }
                }
                mPopupWindow.showAtLocation(mRootView, Gravity.RIGHT | Gravity.BOTTOM, toRight, (int) (height - rectF.top));
            } else {
                mPopupWindow.setWidth(width);
                mPopupWindow.showAtLocation(mRootView, Gravity.LEFT | Gravity.BOTTOM, 0, AppDisplay.getInstance(mContext).getNavBarHeight());
            }

            mShowMask = showMask;
            if (mShowMask) {
//                mRead.getMainFrame().showMaskView();
            }
        }
    }

    @Override
    public boolean isShowing() {
        if (mPopupWindow != null) {
            return mPopupWindow.isShowing();
        } else {
            return false;
        }
    }

    @Override
    public void dismiss() {
        if (mPopupWindow != null && isShowing()) {
            mPopupWindow.setFocusable(false);
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void update(RectF rectF) {
        int height = mRootView.getHeight();
        int width = mRootView.getWidth();
        if (AppDisplay.getInstance(mContext).isPad()) {

            mIv_arrow_bottom.measure(0, 0);

            int toRight;
            if (mPadWidth / 2 > width - rectF.right + (rectF.right - rectF.left) / 2) {
                toRight = 0;
                mLl_arrow.setPadding((int) (mPadWidth / 2.0f - mIv_arrow_bottom.getMeasuredWidth() / 2.0f
                        + mPadWidth / 2 - (width - rectF.right + (rectF.right - rectF.left) / 2)), 0, 0, 0);
            } else {
                if (rectF.right - (rectF.right - rectF.left) / 2 > mPadWidth / 2.0f) {
                    toRight = (int) (width - rectF.right + (rectF.right - rectF.left) / 2 - mPadWidth / 2);
                    mLl_arrow.setPadding((int) (mPadWidth / 2.0f - mIv_arrow_bottom.getMeasuredWidth() / 2.0f), 0, 0, 0);
                } else {
                    toRight = width - mPadWidth;
                    if (rectF.right - (rectF.right - rectF.left) / 2 > mIv_arrow_bottom.getMeasuredWidth() / 2.0f) {
                        mLl_arrow.setPadding((int) (rectF.right - (rectF.right - rectF.left) / 2 - mIv_arrow_bottom.getMeasuredWidth() / 2.0f), 0, 0, 0);
                    } else {
                        mLl_arrow.setPadding(0, 0, 0, 0);
                    }
                }
            }

            mPopupWindow.update(toRight, (int) (height - rectF.top), -1, -1);
        } else {

            if (Build.VERSION.SDK_INT == 24) {
                int screenHeight = AppDisplay.getInstance(mContext).getScreenHeight();
                mLl_root.measure(0, 0);
                int barHeight = mLl_root.getMeasuredHeight();

                mPopupWindow.update(0, screenHeight - barHeight, width, -1);
            } else {
                mPopupWindow.update(0, 0, width, -1);
            }
        }
    }
}
