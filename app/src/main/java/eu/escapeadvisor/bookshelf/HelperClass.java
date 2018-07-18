package eu.escapeadvisor.bookshelf;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    public static void validateData (){

    }
}
