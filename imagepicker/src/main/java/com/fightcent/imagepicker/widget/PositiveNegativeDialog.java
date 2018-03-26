package com.fightcent.imagepicker.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

/**
 * Created by andy.guo on 2016/10/10.
 * 两个按钮的dialog
 */
public class PositiveNegativeDialog {

    public static AlertDialog makeDialog(
            final Activity activity,
            boolean isCancelable,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener,
            int titleRes,
            int positiveRes,
            int negativeRes,
            int messageRes
    ) {
        Resources resources = activity.getResources();
        AlertDialog alertDialog = makeDialog(
                activity,
                isCancelable,
                positiveListener,
                negativeListener,
                titleRes,
                positiveRes,
                negativeRes,
                resources.getString(messageRes)
        );
        return alertDialog;
    }

    public static AlertDialog makeDialog(
            final Activity activity,
            boolean isCancelable,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener,
            String title,
            String positive,
            String negative,
            String message
    ) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(isCancelable);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton(negative, negativeListener);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        if (!activity.isFinishing()) {
            alertDialog = builder.show();
        }
        return alertDialog;
    }

    public static AlertDialog makeDialog(
            final Activity activity,
            boolean isCancelable,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener,
            int titleRes,
            int positiveRes,
            int negativeRes,
            String message
    ) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(isCancelable);
        if (titleRes >0){
            builder.setTitle(titleRes);
        }
        builder.setMessage(message);
        builder.setPositiveButton(positiveRes, positiveListener);
        builder.setNegativeButton(negativeRes, negativeListener);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        if (!activity.isFinishing()) {
            alertDialog = builder.show();
        }
        return alertDialog;
    }

    public static AlertDialog makeDialog(
            final Activity activity,
            boolean isCancelable,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener,
            DialogInterface.OnClickListener neutralListener,
            int titleRes,
            int positiveRes,
            int neutral,
            int negativeRes,
            String message
    ) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(isCancelable);
        if (titleRes >0){
            builder.setTitle(titleRes);
        }

        builder.setMessage(message);
        builder.setPositiveButton(positiveRes, positiveListener);
        builder.setNegativeButton(negativeRes, negativeListener);
        builder.setNeutralButton(neutral,neutralListener);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        if (!activity.isFinishing()) {
            alertDialog = builder.show();
        }
        return alertDialog;
    }

    public static AlertDialog makeDialog(
            final Activity activity,
            boolean isCancelable,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener,
            int positiveRes,
            int negativeRes,
            int messageRes
    ) {
        Resources resources = activity.getResources();
        AlertDialog alertDialog = makeDialog(
                activity,
                isCancelable,
                positiveListener,
                negativeListener,
                positiveRes,
                negativeRes,
                resources.getString(messageRes)
        );
        return alertDialog;
    }

    public static AlertDialog makeDialog(
            final Activity activity,
            boolean isCancelable,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener,
            int positiveRes,
            int negativeRes,
            String message
    ) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(isCancelable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveRes, positiveListener);
        builder.setNegativeButton(negativeRes, negativeListener);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        if (!activity.isFinishing()) {
            alertDialog = builder.show();
        }
        return alertDialog;
    }

}
