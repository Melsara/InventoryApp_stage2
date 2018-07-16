package eu.escapeadvisor.bookshelf;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
}
