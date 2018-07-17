package eu.escapeadvisor.bookshelf;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import eu.escapeadvisor.bookshelf.data.BookshelfContract;

import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_EDIT_CLICKED;
import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_FAB_CLICKED;

public class HelperClass {

    public static void createIntentWIthId (Context context, boolean editClicked, boolean fabClicked, long id){
        Intent intent = new Intent(context, EditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_EDIT_CLICKED, editClicked);
        bundle.putBoolean(KEY_FAB_CLICKED, fabClicked);
        intent.putExtras(bundle);
        Uri currentProductUri = ContentUris.withAppendedId(BookshelfContract.BookshelfEntry.CONTENT_URI_PRODUCTS, id);
        intent.setData(currentProductUri);
        context.startActivity(intent);

    }

    public static void createIntent (Context context, boolean editClicked, boolean fabClicked){
        Intent intent = new Intent(context, EditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_EDIT_CLICKED, editClicked);
        bundle.putBoolean(KEY_FAB_CLICKED, fabClicked);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public static void disableEditText (EditText editText){
        editText.setEnabled(false);
        editText.setClickable(false);
    }

    public static void disableButton (Button button) {
        button.setVisibility(View.GONE);
    }

    public static String helperGetText (EditText editText) {
        String text = editText.getText().toString().trim();
        return text;
    }
}
