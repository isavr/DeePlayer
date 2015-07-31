package com.tutorial.deeplayer.app.deeplayer.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.R;

import java.lang.ref.WeakReference;

/**
 * Created by ilya.savritsky on 21.07.2015.
 */
public class AlertDialogFragment extends DialogFragment {
    public static final String TAG = AlertDialogFragment.class.getSimpleName();

    private Dialog dialog = null;
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private WeakReference<Context> mContextWeak;
    private AlertDialog.Builder builder;

    private DialogInterface.OnClickListener
            positiveButtonClickListener,
            negativeButtonClickListener;

    public void setContext(Context context) {
        mContextWeak = new WeakReference<>(context);
    }

    public AlertDialogFragment setMessage(String message) {
        this.message = message;
        return this;
    }

    public AlertDialogFragment setMessage(int message) {
        if (mContextWeak.get() != null) {
            this.message = (String) mContextWeak.get().getText(message);
        }
        return this;
    }

    public AlertDialogFragment setTitle(int title) {
        if (mContextWeak.get() != null) {
            this.title = (String) mContextWeak.get().getText(title);
        }
        return this;
    }

    public AlertDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertDialogFragment setPositiveButton(int positiveButtonText,
                                                 DialogInterface.OnClickListener listener) {
        if (mContextWeak.get() != null) {
            this.positiveButtonText = (String) mContextWeak.get().getText(positiveButtonText);
        }
        this.positiveButtonClickListener = listener;
        return this;
    }

    public AlertDialogFragment setPositiveButton(String positiveButtonText,
                                                 DialogInterface.OnClickListener listener) {
        this.positiveButtonText = positiveButtonText;
        this.positiveButtonClickListener = listener;
        return this;
    }

    public AlertDialogFragment setNegativeButton(int negativeButtonText,
                                                 DialogInterface.OnClickListener listener) {
        if (mContextWeak.get() != null) {
            this.negativeButtonText = (String) mContextWeak.get().getText(negativeButtonText);
        }
        this.negativeButtonClickListener = listener;
        return this;
    }

    public AlertDialogFragment setNegativeButton(String negativeButtonText,
                                                 DialogInterface.OnClickListener listener) {
        this.negativeButtonText = negativeButtonText;
        this.negativeButtonClickListener = listener;
        return this;
    }

    public void clear() {
        this.title = null;
        this.message = null;
        this.positiveButtonText = null;
        this.negativeButtonText = null;
    }

    @Override
    public void onCreate(Bundle savedStates) {
        super.onCreate(savedStates);
    }

    public void create() {
        if (dialog != null) {
            dismiss();
            dialog = null;
        }
        if (negativeButtonClickListener == null) {
            negativeButtonClickListener = (dialog1, which) -> dialog1.dismiss();
        }
        if (positiveButtonClickListener == null) {
            positiveButtonClickListener = (dialog1, which) -> dialog1.dismiss();
        }
        builder = new AlertDialog.Builder(mContextWeak.get(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle(title).setMessage(message)
                .setNegativeButton(negativeButtonText, negativeButtonClickListener)
                .setPositiveButton(positiveButtonText, positiveButtonClickListener);
    }

    public void createProgressDialog(boolean cancelable) {
        if (dialog != null) {
            dismiss();
            dialog = null;
        }
        dialog = new ProgressDialog(mContextWeak.get(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setCancelMessage(null);
        dialog.setTitle(null);
        dialog.setDismissMessage(null);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (builder != null) {
            dialog = builder.create();
            dialog.setOnShowListener(dialog1 -> {
                if (negativeButtonText == null) {
                    ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                }
                if (positiveButtonText == null) {
                    ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
                }
            });
        }
        if (dialog == null) {
            dialog = new Dialog(mContextWeak.get());
        }
        return dialog;
    }
}
