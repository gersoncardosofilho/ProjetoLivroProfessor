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
package com.foxit.uiextensions.security.standard;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.sdk.Task;
import com.foxit.sdk.common.CommonDefines;
import com.foxit.sdk.common.PDFException;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.security.StandardSecurityHandler;
import com.foxit.uiextensions.DocumentManager;
import com.foxit.uiextensions.R;
import com.foxit.uiextensions.UIExtensionsManager;
import com.foxit.uiextensions.controls.dialog.UITextEditDialog;
import com.foxit.uiextensions.utils.AppUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PasswordStandardSupport {

	private PasswordDialog mDialog;
	private boolean 				mIsOwner = false;
	private boolean					mIsDocOpenAuthEvent = true;

	public UITextEditDialog mCheckOwnerPWD;
	public EditText mEditText;

	private PasswordSettingFragment mSettingDialog;
	private PDFViewCtrl mPdfViewCtrl;
	private Context mContext;
	private String mFilePath = null;
	private boolean bSuccess = false;

	public PasswordStandardSupport(Context context, PDFViewCtrl pdfViewCtrl){
		mContext = context;
		mPdfViewCtrl = pdfViewCtrl;
	}

	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}

	public String getFilePath() {
		return this.mFilePath;
	}

	public boolean checkOwnerPassword(String password) {
		if (password == null) return false;
		try {
			return mPdfViewCtrl.getDoc().checkPassword(password.getBytes()) == PDFDoc.e_pwdOwner;
		} catch (PDFException e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean isOwner() {
		return mIsOwner = DocumentManager.getInstance(mPdfViewCtrl).isOwner();
	}

	public void showCheckOwnerPasswordDialog(final int operatorType) {
		if (mPdfViewCtrl.getUIExtensionsManager() == null) return;
		Context context = ((UIExtensionsManager)mPdfViewCtrl.getUIExtensionsManager()).getAttachedActivity();
		if (context == null) return;
		mCheckOwnerPWD = new UITextEditDialog(context);
		mEditText = mCheckOwnerPWD.getInputEditText();
		TextView tv = mCheckOwnerPWD.getPromptTextView();
		mCheckOwnerPWD.setTitle(mContext.getString(R.string.rv_doc_encrpty_standard_ownerpassword_title));
		tv.setText(mContext.getString(R.string.rv_doc_encrypt_standard_ownerpassword_content));

		final Button button_ok = mCheckOwnerPWD.getOKButton();
		final Button button_cancel = mCheckOwnerPWD.getCancelButton();

		mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mEditText.getText().length() != 0 && mEditText.getText().length() <= 32) {
					button_ok.setEnabled(true);
				} else {
					button_ok.setEnabled(false);
				}
			}
		});

		mEditText.setKeyListener(new NumberKeyListener() {

			@Override
			public int getInputType() {
				return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
			}

			@Override
			protected char[] getAcceptedChars() {
				return PasswordConstants.mAcceptChars;
			}
		});

		button_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIsDocOpenAuthEvent) {
					mIsOwner = checkOwnerPassword(mEditText.getText().toString());
					if (mIsOwner) {
						mCheckOwnerPWD.dismiss();
						if (operatorType == PasswordConstants.OPERATOR_TYPE_REMOVE)
							removePassword();
					} else {
						mEditText.setText("");
						Toast.makeText(mContext, R.string.rv_doc_encrpty_standard_ownerpassword_failed, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCheckOwnerPWD.dismiss();
			}
		});

		mCheckOwnerPWD.show();
		AppUtil.showSoftInput(mEditText);

	}

	public void passwordManager(final int operatorType) {
		int type = 0;
		try {
			type = mPdfViewCtrl.getDoc().getEncryptionType();
		} catch (PDFException e) {
			e.printStackTrace();
		}
		if ((type == PDFDoc.e_encryptPassword && !mIsOwner) || !mIsDocOpenAuthEvent) {
			showCheckOwnerPasswordDialog(operatorType);

		} else {
			switch (operatorType) {
				case PasswordConstants.OPERATOR_TYPE_CREATE:
					showSettingDialog();
					break;
				case PasswordConstants.OPERATOR_TYPE_REMOVE:
					removePassword();
					break;
				default:
					break;
			}
		}
	}

	public void showSettingDialog() {
		mSettingDialog = new PasswordSettingFragment(((UIExtensionsManager)mPdfViewCtrl.getUIExtensionsManager()).getAttachedActivity());
		mSettingDialog.init(this, mPdfViewCtrl);
		mSettingDialog.showDialog();
	}

	public void addPassword(final String userPassword, final String ownerPassword, boolean isAddAnnot, boolean isCopy, boolean isManagePage, boolean isPrint, boolean isFillForm, boolean isModifyDoc, boolean isTextAccess, final String newFilePath) {
		showDialog();
		long userPermission = 0xFFFFFFFC;
		if (isAddAnnot) {
			userPermission = userPermission | PDFDoc.e_permAnnotForm;
		} else {
			userPermission = userPermission & (~PDFDoc.e_permAnnotForm);
		}

		if (isCopy) {
			userPermission = userPermission | PDFDoc.e_permExtract;
		} else {
			userPermission = userPermission & (~PDFDoc.e_permExtract);
		}

		if (isManagePage) {
			userPermission = userPermission | PDFDoc.e_permAssemble;
		} else {
			userPermission = userPermission & (~PDFDoc.e_permAssemble);
		}

		if (isPrint) {
			userPermission = userPermission | PDFDoc.e_permPrint | PDFDoc.e_permPrintHigh;
		} else {
			userPermission = userPermission & (~(PDFDoc.e_permPrint | PDFDoc.e_permPrintHigh));
		}

		if (isFillForm) {
			userPermission = userPermission | PDFDoc.e_permFillForm;
		} else {
			userPermission = userPermission & (~PDFDoc.e_permFillForm);
		}

		if (isModifyDoc) {
			userPermission = userPermission | PDFDoc.e_permModify;
		} else {
			userPermission = userPermission & (~PDFDoc.e_permModify);
		}

		if (isTextAccess) {
			userPermission = userPermission | PDFDoc.e_permExtractAccess;
		} else {
			userPermission = userPermission & (~PDFDoc.e_permExtractAccess);
		}


		try {
			StandardSecurityHandler securityHandler = new StandardSecurityHandler();
			byte[] up = userPassword == null ? null : userPassword.getBytes();
			byte[] op = ownerPassword == null ? null : ownerPassword.getBytes();
			securityHandler.initialize(userPermission, up, op, CommonDefines.e_cipherAES, 16, true);
			if (mPdfViewCtrl.getDoc().isEncrypted()) {
				mPdfViewCtrl.getDoc().removeSecurity();
			}
			mPdfViewCtrl.getDoc().setSecurityHandler(securityHandler);
			String path = mFilePath + "fsencrypt.pdf";
			reopenDoc(path, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removePassword() {
		if (mPdfViewCtrl.getUIExtensionsManager() == null) return;
		Context context = ((UIExtensionsManager)mPdfViewCtrl.getUIExtensionsManager()).getAttachedActivity();
		if (context == null) return;
		final UITextEditDialog removePassworDialog = new UITextEditDialog(context);
		removePassworDialog.setTitle(mContext.getString(R.string.rv_doc_encrpty_standard_remove));
		removePassworDialog.getPromptTextView().setText(mContext.getString(R.string.rv_doc_encrpty_standard_removepassword_confirm));
		removePassworDialog.getInputEditText().setVisibility(View.GONE);
		removePassworDialog.getOKButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
				removePassworDialog.dismiss();
				try {
					mPdfViewCtrl.getDoc().removeSecurity();
					mIsOwner = true;
				} catch (PDFException e) {
					e.printStackTrace();
				}
				String path = mFilePath + "fsencrypt.pdf";
				reopenDoc(path, null);
			}
		});

		removePassworDialog.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removePassworDialog.dismiss();
			}
		});

		removePassworDialog.show();

	}

	private static int getDialogTheme() {
		int theme;
		if (Build.VERSION.SDK_INT >= 21) {
			theme = android.R.style.Theme_Holo_Light_Dialog_NoActionBar;
		} else if (Build.VERSION.SDK_INT >= 14) {
			theme = android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar;
		} else if (Build.VERSION.SDK_INT >= 11) {
			theme = android.R.style.Theme_Holo_Light_Dialog_NoActionBar;
		} else {
			theme = R.style.rv_dialog_style;
		}
		return theme;
	}

	public void showDialog() {
		if (mPdfViewCtrl.getUIExtensionsManager() == null) return;
		final Context context = ((UIExtensionsManager)mPdfViewCtrl.getUIExtensionsManager()).getAttachedActivity();
		if (context == null) return;
		((Activity)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (null == mDialog) {
					mDialog = new PasswordDialog(context, getDialogTheme());
					mDialog.getWindow().setBackgroundDrawableResource(R.color.ux_color_translucent);
				}
				mDialog.show();
			}
		});
	}

	public void hideDialog() {
		if (null != mDialog && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
		if (null != mSettingDialog && mSettingDialog.isShowing()) {
			mSettingDialog.dismiss();
			mSettingDialog = null;
		}
	}

	public boolean getIsOwner() {
		return mIsOwner;
	}

	public void setIsOwner(boolean isOwner) {
		mIsOwner = isOwner;
	}

	public boolean isDocOpenAuthEvent() {
		return mIsDocOpenAuthEvent;
	}

	public void setDocOpenAuthEvent(boolean mIsDocOpenAuthEvent) {
		this.mIsDocOpenAuthEvent = mIsDocOpenAuthEvent;
	}

	private static boolean copyFile(String oriPath, String desPath) {
		if (oriPath == null || desPath == null) return false;
		OutputStream os = null;
		try {
			os = new FileOutputStream(desPath);
			byte[] buffer = new byte[1 << 13];
			InputStream is = new FileInputStream(oriPath);
			int len = is.read(buffer);
			while (len != -1) {
				os.write(buffer, 0, len);
				len = is.read(buffer);
			}
			is.close();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	private void reopenDoc(final String path, final String password) {
		Task.CallBack callBack = new Task.CallBack() {
			@Override
			public void result(Task task) {
				if (!bSuccess) return;
				byte[] up = password == null ? null : password.getBytes();
				mPdfViewCtrl.openDoc(mFilePath, up);
				if (password == null) {
					mIsDocOpenAuthEvent = true;
				} else {
					DocumentManager.getInstance(mPdfViewCtrl).clearUndoRedo();
				}
				mIsOwner = true;
				hideDialog();
			}
		};

		Task task = new Task(callBack) {
			@Override
			protected void execute() {
				try {
					bSuccess = mPdfViewCtrl.getDoc().saveAs(path, PDFDoc.e_saveFlagNormal);
					if (!bSuccess) return;
					File oriFile = new File(mFilePath);
					if (oriFile.exists()) {
						oriFile.delete();
					}

					File newFile = new File(path);
					if (!newFile.exists()) return;
					bSuccess = copyFile(path, mFilePath);
					if (!bSuccess) return;
					newFile.delete();
					bSuccess = true;
				} catch (Exception e) {
					bSuccess = false;
				}
			}
		};
		mPdfViewCtrl.addTask(task);
	}

}

