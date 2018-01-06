package br.com.grupoccaa.projetolivroprofessor.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.PDFError;
import com.foxit.sdk.common.PDFException;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.uiextensions.DocumentManager;
import com.foxit.uiextensions.Module;
import com.foxit.uiextensions.UIExtensionsManager;
import com.foxit.uiextensions.annots.note.NoteModule;
import com.foxit.uiextensions.annots.textmarkup.highlight.HighlightModule;
import com.foxit.uiextensions.annots.textmarkup.squiggly.SquigglyModule;
import com.foxit.uiextensions.annots.textmarkup.strikeout.StrikeoutModule;
import com.foxit.uiextensions.annots.textmarkup.underline.UnderlineModule;
import com.foxit.uiextensions.controls.dialog.UITextEditDialog;
import com.foxit.uiextensions.modules.DocInfoModule;
import com.foxit.uiextensions.modules.DocInfoView;
import com.foxit.uiextensions.modules.OutlineModule;
import com.foxit.uiextensions.modules.SearchModule;
import com.foxit.uiextensions.modules.SearchView;
import com.foxit.uiextensions.modules.panel.annot.AnnotPanelModule;
import com.foxit.uiextensions.modules.signature.SignatureToolHandler;
import com.foxit.uiextensions.modules.thumbnail.ThumbnailModule;
import com.foxit.uiextensions.utils.UIToast;

import java.util.Timer;
import java.util.TimerTask;

import br.com.grupoccaa.projetolivroprofessor.R;

public class ReaderActivity extends FragmentActivity {

    private PDFViewCtrl pdfViewCtrl = null;
    private RelativeLayout parent = null;
    private UIExtensionsManager uiExtensionsManager = null;
    private int layoutMode = PDFViewCtrl.PAGELAYOUTMODE_SINGLE;
    private SearchModule searchModule = null;
    private DocInfoModule docInfoModule = null;
    private NoteModule noteModule = null;
    private HighlightModule highlightModule = null;
    private UnderlineModule underlineModule = null;
    private StrikeoutModule strikeoutModule = null;
    private SquigglyModule squigglyModule = null;

    private AnnotPanelModule annotPanelModule;
    private OutlineModule outlineModule;
    private ThumbnailModule thumbnailModule;

    private boolean isUnlock = false;
    private boolean mPasswordError = false;
    private Context mContext;
    private ActionMode mActionMode;
    String mPath = new String();

    private static String sn = "CXrDp5duyJyGMhQ9Vq0Z17wui+9mlu52OmDsSHRgV/y9abs0/yOd4Q==";
    private static String key = "ezKXjl0mrBh39LvoL2Xkb3lAZega+n7w/UemkNe5oNL7vhNzAdslxvRbmOI92+ykvpzWYCeaKOMumQAdBU4T7yx8vc11nO2GxLcgqTshHQ2TW6Se23qMCd7IPih1Jn/jeY2sec8kMYrC/Sj1eczaK8z4M8ajDRPv7P3zg3I/3khSRuAR7aTnc9JIg6lTP/WYIPCbQu4IYol1UwPRANpoMuKOgZVJRftd8CpX7UUTM8xQ+n6A7XD7ECThduDISLjQcLgdWApxLBtX9zlxXgQVaBXnorqVkQI8r3kzxhpwn2sTIjRTe+VAUeeFUrG0RfgjeX7e1Bv4DB0Abul4SP6+Btys9TfCz5QLsFl8Vndio8yejgvPO/5y15AlPn3r5xMkwwmTmZQY22DiUbUN4Usz/BojKbkYkL27ikkZRPRZeP+e8bvf90kc64j43LBd8hLQSwx9/i22tOnqS1zBxoFgLlTjh76I5tGujB2bhCKB6LmtD9ow+X7NvryowxRZPxeBS4e5Z8YV2R1Biel3dzL7nGahzyOst5Fld2lBdBi7xNTlo91S0nXaT2BYzaE0n88GKTQqdMTlLdjkdA4Rf0MrVDcKz+PQOhyTk3QzrsU6VijKJmQXlhCz/jZHdBswadSJJuPGubXKwyeYleFDivcEREoPYIy8gklFjr3EHQ1MX2ahUq/G0DsKJQcZL2crkpUZyl7NQWjcWRqr0czaiS97SStmtpUaUtLmsyhFdl/R8DsSrzEqqjN8Qbz0eRmj8jXwSzd8TPwDe9goRD+8JJdIEj9Ap2Llymsn96N7/TgaExgqrq8TpOac29HWQ3nCgqTBrGXcZrw8RuElLUcm4FNZd9qxKoOXoaDWw66Cxr40qkplVX8mehVV5+RHUYAhcbKWyVxW/+9h9z/x4MhEB+/grm2cdQ1HoENyFcFFeurGWHNz2m1ZPQCPMBOV1cZySFIOCk2zwsRZ6L4XMzE3R7RqkOwn32ieTcDMh07IVmFX8N4/tGZy5yAcDdIxyfkqH+KXFkPwJKBKenDEK7xRAAS23h2XsCQTWpxQgRbLdOLDfiiNCI+L/o3d6ZKDQUP4mqeKlKuVI9BUBoDj9tAp6iIxLNIg1jqn/0vfroDy5XUHYx/bjnjwy84+CoUhosoT2WC7ab5r3RvpNHIGeLdsPajmY/PRph0htDU5c3dfaPjhKMYisDeVss1IRRszig2Nj2QtAYZK2J46Rpwx9W8=";


    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    static {
        System.loadLibrary("rdk");
    }

    private String getStorageDirectory(){
        String path = null;
        boolean sdExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdExist) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        }
        return path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reader);

        try {
            Library.init(sn, key);
            isUnlock = true;
        } catch (PDFException e) {
            if (e.getLastError() == PDFError.LICENSE_INVALID.getCode()) {
                UIToast.getInstance(getApplicationContext()).show("The license is invalid!");
            } else {
                UIToast.getInstance(getApplicationContext()).show("Failed to initialize the library!");
            }
            isUnlock = false;
            return;
        }

        pdfViewCtrl = (PDFViewCtrl) findViewById(R.id.pdfViewer);
        parent = (RelativeLayout) findViewById(R.id.rd_main_id);
        uiExtensionsManager = new UIExtensionsManager(this.getApplicationContext(), parent, pdfViewCtrl);
        uiExtensionsManager.setAttachedActivity(this);
        pdfViewCtrl.setUIExtensionsManager(uiExtensionsManager);

        // Note: Here, filePath will be set with the total path of file.
        String sdcardPath = getStorageDirectory();
        String filePath = sdcardPath + "FoxitSDK/Sample.pdf";

        mPath = filePath;
        parent = (RelativeLayout) findViewById(R.id.rd_main_id);

        outlineModule = (OutlineModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_OUTLINE);
        if (outlineModule == null){
            outlineModule = new OutlineModule(this, parent, pdfViewCtrl, uiExtensionsManager);
            outlineModule.loadModule();
        }
        annotPanelModule = (AnnotPanelModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_ANNOTPANEL);
        if (annotPanelModule == null){
            annotPanelModule = new AnnotPanelModule(mContext, pdfViewCtrl, uiExtensionsManager);
            annotPanelModule.loadModule();
        }
        thumbnailModule = (ThumbnailModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_THUMBNAIL);
        if (thumbnailModule == null){
            thumbnailModule = new ThumbnailModule(mContext, pdfViewCtrl, uiExtensionsManager);
            thumbnailModule.loadModule();
        }

        pdfViewCtrl.registerDocEventListener(docListener);
        pdfViewCtrl.openDoc(filePath, null);

        pdfViewCtrl.registerDoubleTapEventListener(new PDFViewCtrl.IDoubleTapEventListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mActionMode == null) {
                    mActionMode = ((Activity)mContext).startActionMode(mActionModeCallback);
                }
                else {
                    mActionMode.finish();
                    mActionMode = null;
                }
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int permission = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }

        if (Build.VERSION.SDK_INT >= 24) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    PDFViewCtrl.IDocEventListener docListener = new PDFViewCtrl.IDocEventListener() {
        @Override
        public void onDocWillOpen() {
        }

        @Override
        public void onDocOpened(PDFDoc pdfDoc, int errCode) {
            //switch case require constant value
            if (errCode == PDFError.NO_ERROR.getCode()) {
                mPasswordError = false;
            }else if (errCode == PDFError.PASSWORD_INVALID.getCode()){
                String tips = null;
                if (mPasswordError) {
                    tips = "The password is incorrect, please try again";
                } else {
                    tips = "This file is password protected, please enter password below";
                }
                final UITextEditDialog uiTextEditDialog = new UITextEditDialog(ReaderActivity.this);
                uiTextEditDialog.getDialog().setCanceledOnTouchOutside(false);
                uiTextEditDialog.getInputEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                uiTextEditDialog.setTitle("Please Input password");
                uiTextEditDialog.getPromptTextView().setText(tips);
                uiTextEditDialog.show();
                uiTextEditDialog.getOKButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uiTextEditDialog.dismiss();
                        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        String pw = uiTextEditDialog.getInputEditText().getText().toString();
                        pdfViewCtrl.openDoc(mPath, pw.getBytes());
                        mPasswordError = true;
                    }
                });

                uiTextEditDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uiTextEditDialog.dismiss();
                        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        onExit();
                    }
                });

                uiTextEditDialog.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            uiTextEditDialog.getDialog().cancel();
                            onExit();
                            return true;
                        }
                        return false;
                    }
                });
                uiTextEditDialog.show();
            }else {
                showDialog(PDFException.getErrorMessage(errCode));
            }

        }

        @Override
        public void onDocWillClose(PDFDoc pdfDoc) {
        }

        @Override
        public void onDocClosed(PDFDoc pdfDoc, int i) {
            try {
                Library.release();
            } catch (PDFException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDocWillSave(PDFDoc pdfDoc) {
        }

        @Override
        public void onDocSaved(PDFDoc pdfDoc, int i) {
        }
    };

    private void showDialog(String msg) {
        final UITextEditDialog uiTextEditDialog = new UITextEditDialog(this);
        uiTextEditDialog.getDialog().setCanceledOnTouchOutside(false);
        uiTextEditDialog.getInputEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        uiTextEditDialog.setTitle("Warning");
        uiTextEditDialog.getInputEditText().setVisibility(View.GONE);
        uiTextEditDialog.getCancelButton().setVisibility(View.GONE);
        uiTextEditDialog.getPromptTextView().setText("Faile to open " + mPath + ".\n" + msg);
        uiTextEditDialog.getOKButton().setEnabled(true);
        uiTextEditDialog.getOKButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExit();
            }
        });

        uiTextEditDialog.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    uiTextEditDialog.getDialog().cancel();
                    onExit();
                    return true;
                }
                return false;
            }
        });

        uiTextEditDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(uiExtensionsManager != null) {
            uiExtensionsManager.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pdfViewCtrl != null)
            pdfViewCtrl.requestLayout();
    }

    private void onExit() {
        if (isUnlock) {
            pdfViewCtrl.closeDoc();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private static Boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (((UIExtensionsManager)pdfViewCtrl.getUIExtensionsManager()).getCurrentToolHandler() instanceof SignatureToolHandler) {
                ((UIExtensionsManager)pdfViewCtrl.getUIExtensionsManager()).setCurrentToolHandler(null);
                pdfViewCtrl.invalidate();
                return true;
            }
            Timer timer = null;
            if (isExit == false) {
                isExit = true;
                UIToast.getInstance(this).show("Press again to exit.");
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);

            } else {
                onExit();
            }
        }
        return false;
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (!isUnlock) {
                UIToast.getInstance(getApplicationContext()).show("Unlock Library failed,the menu item is unavailable!");
                return false;
            }

            int itemId = item.getItemId();
            if (itemId == R.id.Note || itemId == R.id.Highlight
                    || itemId == R.id.Squiggly || itemId == R.id.Underline
                    || itemId == R.id.StrikeOut) {
                DocumentManager dm = DocumentManager.getInstance(pdfViewCtrl);
                if (false == dm.canAddAnnot()) {
                    UIToast.getInstance(getApplicationContext()).show("The current document is protected,You can't modify it");
                    return false;
                }
            }

            if (itemId == R.id.Outline) {
                if (outlineModule != null)
                    outlineModule.show();
            } else if (itemId == R.id.ChangeLayout) {
                if (layoutMode == PDFViewCtrl.PAGELAYOUTMODE_SINGLE) {
                    pdfViewCtrl.setPageLayoutMode(PDFViewCtrl.PAGELAYOUTMODE_CONTINUOUS);
                    layoutMode = PDFViewCtrl.PAGELAYOUTMODE_CONTINUOUS;
                } else {
                    pdfViewCtrl.setPageLayoutMode(PDFViewCtrl.PAGELAYOUTMODE_SINGLE);
                    layoutMode = PDFViewCtrl.PAGELAYOUTMODE_SINGLE;
                }
            } else if (itemId == R.id.Search) {
                if (searchModule == null) {

                    searchModule = (SearchModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_SEARCH);
                    if (searchModule == null){
                        searchModule = new SearchModule(mContext, parent, pdfViewCtrl, uiExtensionsManager);
                        searchModule.loadModule();
                    }
                }
                SearchView searchView = searchModule.getSearchView();
                searchView.show();
            } else if (itemId == R.id.Note) {
                if (noteModule == null) {
                    noteModule = (NoteModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_NOTE);
                }
                uiExtensionsManager.setCurrentToolHandler(noteModule.getToolHandler());
            } else if (itemId == R.id.DocInfo) {
                if (docInfoModule == null) {
                    docInfoModule = (DocInfoModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_DOCINFO);
                }

                DocInfoView docInfoView = docInfoModule.getView();
                if (docInfoView != null)
                    docInfoView.show();
            } else if (itemId == R.id.Highlight) {
                if (highlightModule == null)
                    highlightModule = (HighlightModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_HIGHLIGHT);
                uiExtensionsManager.setCurrentToolHandler(highlightModule.getToolHandler());
            } else if (itemId == R.id.Underline) {
                if (underlineModule == null) {
                    underlineModule = (UnderlineModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_UNDERLINE);
                }
                uiExtensionsManager.setCurrentToolHandler(underlineModule.getToolHandler());
            } else if (itemId == R.id.StrikeOut) {
                if (strikeoutModule == null) {
                    strikeoutModule = (StrikeoutModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_STRIKEOUT);
                }
                uiExtensionsManager.setCurrentToolHandler(strikeoutModule.getToolHandler());
            } else if (itemId == R.id.Squiggly) {
                if (squigglyModule == null) {
                    squigglyModule = (SquigglyModule) uiExtensionsManager.getModuleByName(Module.MODULE_NAME_SQUIGGLY);
                }
                uiExtensionsManager.setCurrentToolHandler(squigglyModule.getToolHandler());
            }else if (itemId == R.id.Annotations) {
                if (annotPanelModule != null) {
                    annotPanelModule.show();
                }
            } else if (itemId == R.id.Thumbnail) {
                if (thumbnailModule != null) {
                    thumbnailModule.show();
                }
            }

            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

}
