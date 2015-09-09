package com.tutorial.deeplayer.app.deeplayer.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.tutorial.deeplayer.app.deeplayer.R;

import retrofit.RetrofitError;

/**
 * Created by ilya.savritsky on 21.07.2015.
 */
public class DialogFactory {
    public static void showProgressDialog(Context context, FragmentManager manager) {
        AlertDialogFragment errorDialog = (AlertDialogFragment) manager.findFragmentByTag(AlertDialogFragment.TAG);
        if (errorDialog == null) {
            errorDialog = new AlertDialogFragment();
        } else {
            errorDialog.dismiss();
        }
        errorDialog.clear();
        errorDialog.setContext(context);
        errorDialog.createProgressDialog(false);
        errorDialog.show(manager, AlertDialogFragment.TAG);
    }

    public static void closeAlertDialog(FragmentManager manager) {
        AlertDialogFragment errorDialog = (AlertDialogFragment) manager.findFragmentByTag(AlertDialogFragment.TAG);
        if (errorDialog != null) {
            errorDialog.dismiss();
        }
    }

//    public static void processFailedResponse(Context context, FragmentManager manager, RetrofitError error) {
//        if (error != null) {
//            if (error.getKind() != null) {
//                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                    if (error.getCause() != null) {
//                        //showNoServerConnectionDialog(context, manager, listener);
//                    }
//                } else if (error.getKind().equals(RetrofitError.Kind.HTTP)) {
//                    if (error.getResponse().getStatus() == 401) {
//                        DialogFactory.showSimpleErrorMessage(context, manager, context.getString(R.string.error_authentification_dialog_message_text));
//                    } else if (error.getResponse().getStatus() == 404) {
//                        //showNoServerConnectionDialog(context, manager, listener);
//                    } else if (error.getResponse().getStatus() == 500) {
//                        DialogFactory.showSimpleErrorMessage(context, manager, context.getString(R.string.error_server));
//                    } else {
//                        DialogFactory.showSimpleErrorMessage(context, manager, error.getLocalizedMessage());
//                    }
//                } else {
//                    DialogFactory.showSimpleErrorMessage(context, manager, error.getLocalizedMessage());
//                }
//            } else {
//                DialogFactory.showSimpleErrorMessage(context, manager, error.getLocalizedMessage());
//            }
//        } else {
//            DialogFactory.showSimpleErrorMessage(context, manager, context.getString(R.string.error_application));
//        }
//    }
//
//    public static void showSimpleErrorMessage(Context context, FragmentManager manager, String message) {
//        AlertDialogFragment errorDialog = (AlertDialogFragment) manager.findFragmentByTag(AlertDialogFragment.TAG);
//        if (errorDialog == null) {
//            errorDialog = new AlertDialogFragment();
//        } else {
//            errorDialog.dismiss();
//        }
//        errorDialog.clear();
//        errorDialog.setContext(context);
//        errorDialog.setTitle("Error");
//        errorDialog.setMessage(message);
//        errorDialog.create();
//        errorDialog.show(manager, AlertDialogFragment.TAG);
//    }
}
