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
package com.foxit.uiextensions.modules.signature;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.sdk.common.PDFException;
import com.foxit.sdk.common.PDFPath;
import com.foxit.uiextensions.Module;
import com.foxit.uiextensions.R;
import com.foxit.uiextensions.ToolHandler;
import com.foxit.uiextensions.UIExtensionsManager;
import com.foxit.uiextensions.controls.propertybar.PropertyBar;
import com.foxit.uiextensions.controls.propertybar.imp.PropertyBarImpl;
import com.foxit.uiextensions.controls.toolbar.BaseBar;
import com.foxit.uiextensions.controls.toolbar.IBaseItem;
import com.foxit.uiextensions.controls.toolbar.CircleItem;
import com.foxit.uiextensions.controls.toolbar.PropertyCircleItem;
import com.foxit.uiextensions.controls.toolbar.impl.BaseItemImpl;
import com.foxit.uiextensions.controls.toolbar.impl.BottomBarImpl;
import com.foxit.uiextensions.controls.toolbar.impl.CircleItemImpl;
import com.foxit.uiextensions.controls.toolbar.impl.PropertyCircleItemImp;
import com.foxit.uiextensions.security.digitalsignature.DigitalSignatureModule;
import com.foxit.uiextensions.security.digitalsignature.DigitalSignatureUtil;
import com.foxit.uiextensions.security.digitalsignature.IDigitalSignatureCallBack;
import com.foxit.uiextensions.utils.AppDisplay;
import com.foxit.uiextensions.utils.AppResource;
import com.foxit.uiextensions.utils.AppUtil;
import com.foxit.uiextensions.utils.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SignatureDrawView {
    private static final int MSG_DRAW = 0x01;
    private static final int MSG_CLEAR = 0x02;
    private static final int MSG_COLOR = 0x04;
    private static final int MSG_DIAMETER = 0x08;
    private static final int MSG_RELEASE = 0x10;


    public interface OnDrawListener {
        boolean canDraw();

        void moveToTemplate();

        void onBackPressed();

        void result(Bitmap bitmap, Rect rect, int color, String dsgPath);
    }

    public void setOnDrawListener(OnDrawListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private ViewGroup mParent;
    private PDFViewCtrl mPdfViewCtrl;
    private SignatureToolHandler mToolHandler;
    private OnDrawListener mListener;
    private DrawView mDrawView;

    private View mViewGroup;
    private AppDisplay mDisplay;
    private CircleItem mBackItem;
    private IBaseItem mTitleItem;
    private CircleItem mClearItem;
    private CircleItem mSaveItem;
    private ViewGroup mDrawContainer;
    private PropertyBar mPropertyBar;

    private IBaseItem mCertificateItem;

    private RelativeLayout mSignCreateTopBarLayout;
    private RelativeLayout mSignCreateBottomBarLayout;

    private BaseBar mSignCreateTopBar;
    private BaseBar mSignCreateBottomBar;

    private View mMaskView;

    private String mKey;
    private Bitmap mBitmap;
    private Rect mRect = new Rect();
    private int mBmpHeight;
    private int mBmpWidth;
    private Rect mValidRect = new Rect();


    private boolean mCanDraw = false;

    private DigitalSignatureUtil mDsgUtil;
    private String mCurDsgPath;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case MSG_DRAW:
                    if (mDrawView == null) return;
                    mCanDraw = true;
                    mDrawView.invalidate();
                    break;
                case MSG_CLEAR:
                    mCanDraw = true;
                    mSaveItem.setEnable(false);
                    mDrawView.invalidate();
                    break;
                case MSG_COLOR:
                    mCanDraw = true;

                    break;
                case MSG_DIAMETER:
                    mCanDraw = true;
                    break;
                case MSG_RELEASE:
                    mCanDraw = false;
                    if (mBitmap != null && mBitmap.isRecycled()) {
                        mBitmap.recycle();
                    }
                    mBitmap = null;
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public SignatureDrawView(Context context, ViewGroup parent, PDFViewCtrl pdfViewCtrl) {
        mContext = context;
        mParent = parent;
        mPdfViewCtrl = pdfViewCtrl;
        mViewGroup = View.inflate(mContext, R.layout.rv_sg_create, null);
        mDisplay = AppDisplay.getInstance(mContext);
        mSignCreateTopBarLayout = (RelativeLayout) mViewGroup.findViewById(R.id.sig_create_top_bar_layout);
        mSignCreateBottomBarLayout = (RelativeLayout) mViewGroup.findViewById(R.id.sig_create_bottom_bar_layout);
        Module dsgModule = ((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).getModuleByName(Module.MODULE_NAME_DIGITALSIGNATURE);
        if (dsgModule != null) {
            mDsgUtil = ((DigitalSignatureModule) dsgModule).getDSG_Util();
        }
        SignatureModule sigModule = (SignatureModule)((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).getModuleByName(Module.MODULE_NAME_PSISIGNATURE);
        if (sigModule != null) {
            mToolHandler = (SignatureToolHandler) sigModule.getToolHandler();
        }
        initBarLayout();
        mDrawContainer = (ViewGroup) mViewGroup.findViewById(R.id.sig_create_canvas);
        mDrawView = new DrawView(mContext);
        mDrawContainer.addView(mDrawView);

    }

    private void initBarLayout() {
        if (mSignCreateTopBar != null) {
            return;
        }
        initTopBar();
        if (mDsgUtil != null) {
            if (mPdfViewCtrl.getDoc() == null) {
                initBottomBar();
            } else {
                initBottomBar();
            }
        }

        mSignCreateTopBarLayout.addView(mSignCreateTopBar.getContentView());
        if (mDsgUtil != null) {
            mSignCreateBottomBarLayout.addView(mSignCreateBottomBar.getContentView());
        }
    }

    private void initTopBar() {
        mSignCreateTopBar = new SignatureCreateSignTitleBar(mContext);
        mSignCreateTopBar.setBackgroundColor(mContext.getResources().getColor(R.color.ux_bg_color_toolbar_light));

        int circleRes = R.drawable.rd_sign_circle_selector;

        mBackItem = new CircleItemImpl(mContext);
        mBackItem.setImageResource(R.drawable.rd_sg_back_selector);
        mBackItem.setCircleRes(circleRes);
        mBackItem.setId(R.id.sig_create_back);
        mBackItem.setOnClickListener(mOnClickListener);

        mClearItem = new CircleItemImpl(mContext);
        mClearItem.setImageResource(R.drawable.rd_sg_clear_selector);
        mClearItem.setCircleRes(circleRes);
        mClearItem.setId(R.id.sig_create_delete);
        mClearItem.setOnClickListener(mOnClickListener);

        mSaveItem = new CircleItemImpl(mContext);
        mSaveItem.setImageResource(R.drawable.rd_sg_save_selector);
        mSaveItem.setCircleRes(circleRes);
        mSaveItem.setId(R.id.sig_create_save);
        mSaveItem.setOnClickListener(mOnClickListener);

        mPropertyBar = new PropertyBarImpl(mContext, mPdfViewCtrl);
        mProItem = new PropertyCircleItemImp(mContext) {
            @Override
            public void onItemLayout(int l, int t, int r, int b) {
                if (((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).getCurrentToolHandler() != null && ((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).getCurrentToolHandler().getType() == ToolHandler.TH_TYPE_SIGNATURE) {
                    if (mPropertyBar.isShowing()) {
                        Rect mProRect = new Rect();
                        mProItem.getContentView().getGlobalVisibleRect(mProRect);
                        mPropertyBar.update(new RectF(mProRect));
                    }
                }
            }
        };
        mProItem.setCircleRes(circleRes);
        mProItem.setId(R.id.sig_create_property);
        mProItem.setOnClickListener(mOnClickListener);

        mTitleItem = new BaseItemImpl(mContext);
        mTitleItem.setTextSize(18);
        mTitleItem.setText(AppResource.getString(mContext, R.string.rv_sign_create));
        if (!mDisplay.isPad()) {
            mSignCreateTopBar.setItemSpace(mDisplay.dp2px(16));
        }
        mSignCreateTopBar.addView(mBackItem, BaseBar.TB_Position.Position_LT);
        mSignCreateTopBar.addView(mTitleItem, BaseBar.TB_Position.Position_LT);
        mSignCreateTopBar.addView(mProItem, BaseBar.TB_Position.Position_RB);
        mSignCreateTopBar.addView(mClearItem, BaseBar.TB_Position.Position_RB);
        mSignCreateTopBar.addView(mSaveItem, BaseBar.TB_Position.Position_RB);

    }

    private void initBottomBar() {
        mSignCreateBottomBar = new BottomBarImpl(mContext);
        mSignCreateBottomBar.setBackgroundColor(mContext.getResources().getColor(R.color.ux_bg_color_toolbar_light));

        mCertificateItem = new BaseItemImpl(mContext);
        mCertificateItem.setImageResource(R.drawable.sg_cert_add_selector);
        mCertificateItem.setText(AppResource.getString(mContext, R.string.sg_cert_add_text));
        mCertificateItem.setTextSize(18);
        mCertificateItem.setRelation(IBaseItem.RELATION_RIGNT);
        mCertificateItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDsgUtil.addCertList(new IDigitalSignatureCallBack() {
                    @Override
                    public void onCertSelect(String path, String name) {
                        if (!AppUtil.isEmpty(path) && !AppUtil.isEmpty(name)) {
                            mCertificateItem.setDisplayStyle(IBaseItem.ItemType.Item_Text);
                            mCertificateItem.setText(AppResource.getString(mContext, R.string.sg_cert_current_name_title) + name);
                            mCurDsgPath = path;
                        } else {
                            mCertificateItem.setDisplayStyle(IBaseItem.ItemType.Item_Text_Image);
                            mCertificateItem.setText(AppResource.getString(mContext, R.string.sg_cert_add_text));
                            mCurDsgPath = null;
                        }
                    }
                });


            }
        });
        mSignCreateBottomBar.addView(mCertificateItem, BaseBar.TB_Position.Position_CENTER);
    }

    private PropertyCircleItem mProItem;

    private void addMask() {
        if (mMaskView == null) {
            mMaskView = mViewGroup.findViewById(R.id.sig_create_mask_layout);
            mMaskView.setBackgroundColor(mContext.getResources().getColor(R.color.ux_color_mask_background));

        }
        mPropertyBar.setDismissListener(mPropertyBarDismissListener);
        mMaskView.setVisibility(View.VISIBLE);
    }

    private void preparePropertyBar() {
        int[] colors = new int[PropertyBar.PB_COLORS_SIGN.length];
        System.arraycopy(PropertyBar.PB_COLORS_SIGN, 0, colors, 0, colors.length);
        colors[0] = PropertyBar.PB_COLORS_SIGN[0];
        mPropertyBar.setColors(colors);
        mPropertyBar.setProperty(PropertyBar.PROPERTY_COLOR, mToolHandler.getColor());
        mPropertyBar.setProperty(PropertyBar.PROPERTY_LINEWIDTH, translate2LineWidth(mToolHandler.getDiameter()));
        mPropertyBar.setArrowVisible(true);
        mPropertyBar.reset(getSupportedProperties());
        mPropertyBar.setPropertyChangeListener(propertyChangeListener);
    }

    private long getSupportedProperties() {
        return PropertyBar.PROPERTY_COLOR
                | PropertyBar.PROPERTY_LINEWIDTH;
    }

    private PropertyBar.DismissListener mPropertyBarDismissListener = new PropertyBar.DismissListener() {
        @Override
        public void onDismiss() {
            if (mMaskView != null) {
                mMaskView.setVisibility(View.INVISIBLE);
            }
        }
    };



    private PropertyBar.PropertyChangeListener propertyChangeListener = new PropertyBar.PropertyChangeListener() {
        @Override
        public void onValueChanged(long property, int value) {
            if (property == PropertyBar.PROPERTY_COLOR) {
                if (value == mToolHandler.getColor()) return;
                mToolHandler.setColor(value);
                setInkColor(value);
                mProItem.setCentreCircleColor(value);
            } else if (property == PropertyBar.PROPERTY_SELF_COLOR) {
                if (value == mToolHandler.getColor()) return;
                mToolHandler.setColor(value);
                setInkColor(value);
                mProItem.setCentreCircleColor(value);
            }
        }

        @Override
        public void onValueChanged(long property, float value) {
            if (property == PropertyBar.PROPERTY_LINEWIDTH) {
                if (mToolHandler.getDiameter() == unTranslate(value)) return;
                float diameter = unTranslate(value);
                mToolHandler.setDiameter(diameter);
                setInkDiameter(diameter);
            }
        }

        @Override
        public void onValueChanged(long property, String value) {

        }
    };

    private float unTranslate(float r) {
        if (r <= 1) {
            r = 1.4999f;
        }
        return (r - 1) / 2;
    }


    private float translate2LineWidth(float d) {
        return (2 * d + 1);
    }

    public View getView() {
        return mViewGroup;
    }


    public void resetLanguage() {
        if (mViewGroup != null) {

            if (mTitleItem != null) {
                mTitleItem.setText(AppResource.getString(mContext, R.string.rv_sign_create));
            }
        }
    }

    public void init(int width, int height, String dsgPath) {
        mSaveDialog = null;
        mBmpWidth = width;
        if (mDisplay.isPad()) {
            mBmpHeight = height - (int) AppResource.getDimension(mContext, R.dimen.ux_toolbar_height_pad);
        } else {
            mBmpHeight = height - (int) AppResource.getDimension(mContext, R.dimen.ux_toolbar_height_phone);
        }
        mValidRect.set(mDisplay.dp2px(3),
                mDisplay.dp2px(7),
                mBmpWidth - mDisplay.dp2px(3),
                mBmpHeight - mDisplay.dp2px(7));
        mKey = null;
        mRect.setEmpty();
        mSaveItem.setEnable(false);
        if (mToolHandler.getColor() == 0)
            mToolHandler.setColor(PropertyBar.PB_COLORS_SIGN[0]);
        mProItem.setCentreCircleColor(mToolHandler.getColor());
        if (mToolHandler.getDiameter() == 0) mToolHandler.setDiameter(3);

        if (mBitmap == null) {
            try {
                mBitmap = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Config.ARGB_8888);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                if (mListener != null) {
                    mListener.onBackPressed();
                }
                return;
            }
        }
        mBitmap.eraseColor(0xFFFFFFFF);
        mCanDraw = false;
        initCanvas();

        mCurDsgPath = dsgPath;
        setCertificateItem(mCurDsgPath);
    }


    public void init(int width, int height, String key, Bitmap bitmap, Rect rect, int color, float diameter, String dsgPath) {
        mSaveDialog = null;
        if (bitmap == null || rect == null) {
            init(width, height, dsgPath);
            return;
        }
        mBmpWidth = width;
        if (mDisplay.isPad()) {
            mBmpHeight = height - (int) AppResource.getDimension(mContext, R.dimen.ux_toolbar_height_pad);
        } else {
            mBmpHeight = height - (int) AppResource.getDimension(mContext, R.dimen.ux_toolbar_height_phone);
        }
        mValidRect.set(mDisplay.dp2px(3),
                mDisplay.dp2px(7),
                mBmpWidth - mDisplay.dp2px(3),
                mBmpHeight - mDisplay.dp2px(7));
        mKey = key;
        mRect.set(rect);
        mSaveItem.setEnable(true);
        if (mBitmap != null) {
            if (!mBitmap.isRecycled()) mBitmap.recycle();
            mBitmap = null;
        }
        int[] colors;
        try {
            mBitmap = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Config.ARGB_8888);
            colors = new int[mBmpWidth * mBmpHeight];
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            if (mListener != null) {
                mListener.onBackPressed();
            }
            return;
        }
        try {
            bitmap.getPixels(colors, 0, mBmpWidth, 0, 0, mBmpWidth, mBmpHeight);
            mBitmap.setPixels(colors, 0, mBmpWidth, 0, 0, mBmpWidth, mBmpHeight);
        } catch (Exception e) {
            int oldVerBmpHeight = height - mDisplay.dp2px(80);//for supper old version
            if (oldVerBmpHeight > bitmap.getHeight()) {
                bitmap.getPixels(colors, 0, mBmpWidth, 0, 0, mBmpWidth, bitmap.getHeight());
                mBitmap.setPixels(colors, 0, mBmpWidth, 0, 0, mBmpWidth, bitmap.getHeight());
            } else {
                bitmap.getPixels(colors, 0, mBmpWidth, 0, 0, mBmpWidth, oldVerBmpHeight);
                mBitmap.setPixels(colors, 0, mBmpWidth, 0, 0, mBmpWidth, oldVerBmpHeight);
            }

        }
        bitmap.recycle();
        bitmap = null;
        mToolHandler.setColor(color);
        mProItem.setCentreCircleColor(color);
        mToolHandler.setDiameter(diameter);
        mCanDraw = false;
        initCanvas();

        mCurDsgPath = dsgPath;
        setCertificateItem(mCurDsgPath);
    }

    public void unInit() {
        releaseCanvas();
        if (mSaveDialog != null && mSaveDialog.isShowing()) {
            try {
                mSaveDialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        mSaveDialog = null;
        mPropertyBar.setDismissListener(null);
    }

    private void setCertificateItem(String dsgPath) {
        if (mDsgUtil == null) {
            return;
        }

        if (mCertificateItem != null) {
            if (!AppUtil.isEmpty(dsgPath)) {
                mCertificateItem.setDisplayStyle(IBaseItem.ItemType.Item_Text);
                File file = new File(dsgPath);
                mCertificateItem.setText(AppResource.getString(mContext, R.string.sg_cert_current_name_title) + file.getName());
            } else {
                mCertificateItem.setDisplayStyle(IBaseItem.ItemType.Item_Text_Image);
                mCertificateItem.setText(AppResource.getString(mContext, R.string.sg_cert_add_text));
            }
        }
    }

    private AlertDialog mSaveDialog;

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (AppUtil.isFastDoubleClick()) return;
            int id = v.getId();
            if (R.id.sig_create_back == id) {
                if (mListener != null) {
                    mListener.onBackPressed();
                }
                return;
            }
            if (R.id.sig_create_property == id) {
                preparePropertyBar();
                addMask();
                Rect rect = new Rect();
                mProItem.getContentView().getGlobalVisibleRect(rect);
                mPropertyBar.show(new RectF(rect), false);
            }
            if (R.id.sig_create_delete == id) {
                clearCanvas();
                return;
            }
            if (R.id.sig_create_save == id) {
                if (mDrawView == null || mDrawView.getBmp() == null) return;

                saveSign();
                if (!(((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).getCurrentToolHandler() instanceof SignatureToolHandler)) {
                    ((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).setCurrentToolHandler(((SignatureModule) (((UIExtensionsManager) mPdfViewCtrl.getUIExtensionsManager()).getModuleByName(Module.MODULE_NAME_PSISIGNATURE))).getToolHandler());
                }
                return;
            }

        }
    };

    private void saveSign() {
        Bitmap bitmap = mDrawView.getBmp();
        if (mKey == null) {
            SignatureDataUtil.insertData(mContext, bitmap, mRect, mToolHandler.getColor(), mToolHandler.getDiameter(), mCurDsgPath);
        } else {
            SignatureDataUtil.updateByKey(mContext, mKey, bitmap, mRect, mToolHandler.getColor(), mToolHandler.getDiameter(), mCurDsgPath);
        }
        if (mListener != null) {
            mListener.result(bitmap, mRect, mToolHandler.getColor(), mCurDsgPath);
        }
    }

    private void adjustCanvasRect() {
        if (mBitmap == null) return;
        if (mRect.left < 0) mRect.left = 0;
        if (mRect.top < 0) mRect.top = 0;
        if (mRect.right > mBmpWidth) mRect.right = mBmpWidth;
        if (mRect.bottom > mBmpHeight) mRect.bottom = mBmpHeight;
    }


    private SignatureDrawEvent mDrawEvent;

    private void initCanvas() {
        if (mBitmap == null) return;

        mDrawEvent = new SignatureDrawEvent(mBitmap, SignatureConstants.SG_EVENT_DRAW, mToolHandler.getColor(), mToolHandler.getDiameter(), null);
        mDrawEvent.mType = SignatureConstants.SG_EVENT_DRAW;
        SignaturePSITask task = new SignaturePSITask(mDrawEvent, new Event.Callback() {
            @Override
            public void result(Event event, boolean success) {
                mHandler.sendEmptyMessage(MSG_DRAW);
            }
        });
        mPdfViewCtrl.addTask(task);
    }

    private void setInkColor(int color) {
        if (mDrawEvent == null) return;
        SignatureDrawEvent drawEvent = new SignatureDrawEvent();
        drawEvent.mType = SignatureConstants.SG_EVENT_COLOR;
        drawEvent.mColor = color;
        SignaturePSITask task = new SignaturePSITask(drawEvent, new Event.Callback() {
            @Override
            public void result(Event event, boolean success) {

                mHandler.sendEmptyMessage(MSG_COLOR);
            }
        });
        mPdfViewCtrl.addTask(task);
    }

    private void setInkDiameter(float diameter) {
        if (mDrawEvent == null) return;
        SignatureDrawEvent drawEvent = new SignatureDrawEvent();
        drawEvent.mType = SignatureConstants.SG_EVENT_THICKNESS;
        drawEvent.mThickness = diameter;
        SignaturePSITask task = new SignaturePSITask(drawEvent, new Event.Callback() {
            @Override
            public void result(Event event, boolean success) {
                mHandler.sendEmptyMessage(MSG_DIAMETER);
            }
        });
        mPdfViewCtrl.addTask(task);
    }

    private void clearCanvas() {
        if (mDrawEvent == null) return;
        mDrawEvent.mType = SignatureConstants.SG_EVENT_CLEAR;
        mBitmap.eraseColor(0xFFFFFFFF);
        mHandler.sendEmptyMessage(MSG_CLEAR);
    }

    private void addPoint(final List<PointF> points, final List<Float> pressures, final int flag) {
        try {
            for (int i = 0; i < points.size(); i++) {
                PointF point = points.get(i);
                Float pressure = pressures.get(i);
                SignaturePSITask.mPsi.addPoint(point, flag, pressure);
            }

            RectF rect = SignaturePSITask.mPsi.getContentsRect();
            Rect contentRect = new Rect((int) rect.left, (int) rect.top, (int) (rect.right + 0.5), (int) (rect.bottom + 0.5));
            if (mRect.isEmpty())
                mRect.set(contentRect);
            else
                mRect.union(contentRect);

            adjustCanvasRect();
            mSaveItem.setEnable(true);
            mDrawView.invalidate(contentRect);
        } catch (PDFException e) {
            e.printStackTrace();
        }
    }

    private void releaseCanvas() {
        if (mDrawEvent == null) return;
        mDrawEvent.mType = SignatureConstants.SG_EVENT_RELEASE;
        mHandler.sendEmptyMessage(MSG_RELEASE);
        mDrawEvent = null;
    }

    class DrawView extends View {

        private Paint mPaint;
        private boolean mMultiPointer;
        private PointF mPointTmp;

        public DrawView(Context context) {
            super(context);
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if (Build.VERSION.SDK_INT >= 11) {
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
            mPointTmp = new PointF();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (SignaturePSITask.mPsi != null) {
                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            }
        }

        private float getDistanceOfTwoPoint(PointF p1, PointF p2) {
            return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
        }

        private boolean mPointInvalid;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (!mCanDraw || mListener == null || !mListener.canDraw()) return false;
            int count = event.getPointerCount();
            PointF point = new PointF(event.getX(), event.getY());
            if (count > 1) {
                if (!mMultiPointer) {
                    mPointTmp.set(point);
                    mMultiPointer = true;
                }
                return false;
            }
            int action = event.getAction();
            float pressure = event.getPressure();
            if (pressure < 0.1) {
                pressure = 0.1f;
            }
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mValidRect.contains((int) event.getX(), (int) event.getY())) {
                        mPointInvalid = false;
                        List<PointF> points = new ArrayList<PointF>();
                        List<Float> pressures = new ArrayList<Float>();
                        points.add(point);
                        pressures.add(pressure);
                        addPoint(points, pressures, PDFPath.e_pointTypeMoveTo);
                        mPointTmp.set(point);
                    } else {
                        mPointInvalid = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMultiPointer) break;
                    if (mValidRect.contains((int) event.getX(), (int) event.getY())) {
                        if (!mPointInvalid) {
                            float delta = getDistanceOfTwoPoint(point, mPointTmp);
                            if (delta < 2) break;
                            List<PointF> points = new ArrayList<PointF>();
                            List<Float> pressures = new ArrayList<Float>();
                            for (int i = 0; i < event.getHistorySize(); i++) {
                                mPointTmp.set(event.getHistoricalX(i), event.getHistoricalY(i));
                                delta = getDistanceOfTwoPoint(point, mPointTmp);
                                if (delta < 2) continue;
                                points.add(new PointF(event.getHistoricalX(i), event.getHistoricalY(i)));
                                pressures.add(event.getHistoricalPressure(i));
                            }
                            points.add(point);
                            pressures.add(pressure);
                            addPoint(points, pressures, PDFPath.e_pointTypeLineTo);
                        } else {
                            mPointInvalid = false;
                            List<PointF> points = new ArrayList<PointF>();
                            List<Float> pressures = new ArrayList<Float>();
                            points.add(point);
                            pressures.add(pressure);
                            addPoint(points, pressures, PDFPath.e_pointTypeMoveTo);
                            mPointTmp.set(point);
                        }
                    } else {
                        mPointInvalid = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    List<PointF> points = new ArrayList<PointF>();
                    List<Float> pressures = new ArrayList<Float>();
                    pressures.add(pressure);
                    if (!mValidRect.contains((int) event.getX(), (int) event.getY())) {
                        points.add(mPointTmp);
                        addPoint(points, pressures, PDFPath.e_pointTypeLineToCloseFigure);
                        break;
                    }
                    if (mPointInvalid) {
                        points.add(mPointTmp);
                        addPoint(points, pressures, PDFPath.e_pointTypeLineToCloseFigure);
                        break;
                    }
                    if (mMultiPointer) {
                        mMultiPointer = false;
                        points.add(mPointTmp);
                        addPoint(points, pressures, PDFPath.e_pointTypeLineToCloseFigure);
                        break;
                    }
                    points.add(point);
                    addPoint(points, pressures, PDFPath.e_pointTypeLineToCloseFigure);
                    break;
                case MotionEvent.ACTION_CANCEL:
                default:
                    break;
            }
            return true;
        }

        public Bitmap getBmp() {
            Bitmap bitmap = null;
            if (mBitmap != null) {
                bitmap = Bitmap.createBitmap(mBitmap);
            }
            return bitmap;
        }
    }


}
