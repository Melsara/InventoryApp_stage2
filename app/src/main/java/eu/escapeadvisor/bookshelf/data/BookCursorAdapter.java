package eu.escapeadvisor.bookshelf.data;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import eu.escapeadvisor.bookshelf.EditorActivity;
import eu.escapeadvisor.bookshelf.R;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_FAB_CLICKED;
import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_SWIPE_DIR;

public class BookCursorAdapter extends CursorAdapter {

    private boolean swipedLeftToRight;
    private Boolean fabClicked;
    Bundle extras;
    ImageButton deleteItem;

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView tvName = view.findViewById(R.id.name);
        int productNameColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRODUCTNAME);
        String productName = cursor.getString(productNameColumnIndex);
        tvName.setText(productName);

        TextView tvPrice = view.findViewById(R.id.price);
        int priceColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRICE);
        String productPrice = cursor.getString(priceColumnIndex);
        tvPrice.setText(productPrice);

        TextView tvQuantity = view.findViewById(R.id.quantity);
        int quantityColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_QUANTITY);
        String productQuantity = cursor.getString(quantityColumnIndex);
        tvQuantity.setText(productQuantity);
    }

}
