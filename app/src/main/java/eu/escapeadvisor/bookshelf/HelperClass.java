package eu.escapeadvisor.bookshelf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.CopyOnWriteArrayList;

import eu.escapeadvisor.bookshelf.data.BookshelfContract;

import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_EDIT_CLICKED;
import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_FAB_CLICKED;
import static eu.escapeadvisor.bookshelf.GlobalConstant.QUANTITY_SALE;

public class HelperClass {

    public static void createIntentWIthId(Context context, boolean editClicked, boolean fabClicked, long id) {
        Intent intent = new Intent(context, EditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_EDIT_CLICKED, editClicked);
        bundle.putBoolean(KEY_FAB_CLICKED, fabClicked);
        intent.putExtras(bundle);
        Uri currentProductUri = ContentUris.withAppendedId(BookshelfContract.BookshelfEntry.CONTENT_URI_PRODUCTS, id);
        intent.setData(currentProductUri);
        context.startActivity(intent);

    }

    public static void createIntent(Context context, boolean editClicked, boolean fabClicked) {
        Intent intent = new Intent(context, EditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_EDIT_CLICKED, editClicked);
        bundle.putBoolean(KEY_FAB_CLICKED, fabClicked);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public static void disableEditText(EditText editText) {
        editText.setEnabled(false);
        editText.setClickable(false);
    }

    public static void disableButton(Button button) {
        button.setVisibility(View.GONE);
    }

    public static String helperGetText(EditText editText) {
        String text = editText.getText().toString().trim();
        return text;
    }

    public static void dialPhoneNumber(String phoneNumber, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void decreaseQuantity(int id, int quantity, CharSequence stringResource, Context context) {

        if (quantity == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_quantity_zero), Toast.LENGTH_LONG).show();

        } else if (quantity > 0) {

            quantity = quantity + QUANTITY_SALE;
            ContentValues values = new ContentValues();
            values.put(BookshelfContract.BookshelfEntry.COLUMN_PROD_QUANTITY, quantity);

            Uri uri = ContentUris.withAppendedId(BookshelfContract.BookshelfEntry.CONTENT_URI_PRODUCTS, id);
            int rowsUpdated = context.getContentResolver().update(uri, values, null, null);

            if (rowsUpdated == 0) {

                Toast.makeText(context, context.getResources().getString(R.string.toast_update_failed), Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show();

            }

        }

    }

    public static void increaseQuantity(int id, int quantity, CharSequence stringResource, Context context) {

        quantity = quantity - QUANTITY_SALE;
        ContentValues values = new ContentValues();
        values.put(BookshelfContract.BookshelfEntry.COLUMN_PROD_QUANTITY, quantity);

        Uri uri = ContentUris.withAppendedId(BookshelfContract.BookshelfEntry.CONTENT_URI_PRODUCTS, id);
        int rowsUpdated = context.getContentResolver().update(uri, values, null, null);

        if (rowsUpdated == 0) {

            Toast.makeText(context, context.getResources().getString(R.string.toast_update_failed), Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show();

        }
    }

    public static boolean checkIfEmpty (EditText editText, CharSequence stringResource, Context context){
        boolean isEmpty = false;
        String contentToCheck = helperGetText(editText);
        if (contentToCheck.isEmpty() || contentToCheck.equals(null) || contentToCheck.equals("")){
            Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show();
            isEmpty = true;
        }
        return isEmpty;
    }

    public static void validateData (ContentValues values, String column, String error){
        String string = values.getAsString(column);
        if (string.equals(null) || string.equals("")) {
            throw new IllegalArgumentException(error);
        }
    }

    public static void deletePet(Uri mCurrentProductUri, CharSequence stringResourceSuccess, CharSequence stringResourceFail, Context context) {
        int mRowsDeleted = 0;
        if (mCurrentProductUri != null) {
            mRowsDeleted = context.getContentResolver().delete(mCurrentProductUri,
                    null,
                    null);
            if (!(context instanceof MainActivity)) {
                //This means we are in an Activity which is not MainActivity, so we can safely close it.
                ((Activity) context).finish();
            }
        }

        if (mRowsDeleted == 0) {
            Toast.makeText(context, stringResourceFail, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, stringResourceSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener, Context context) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showDeleteConfirmationDialog(final Uri mCurrentProductUri, final Context context) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet(mCurrentProductUri, context.getResources().getString(R.string.delete_product_success), context.getResources().getString(R.string.delete_product_failed), context);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
