package eu.escapeadvisor.bookshelf.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import eu.escapeadvisor.bookshelf.MainActivity;
import eu.escapeadvisor.bookshelf.R;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

import static eu.escapeadvisor.bookshelf.GlobalConstant.QUANTITY_SALE;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    private TextView tvQuantity;
    private static final String TAG = BookCursorAdapter.class.getName();

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        int idColumnIndex = cursor.getColumnIndex(BookshelfEntry._ID);
        final String productId= cursor.getString(idColumnIndex);

        TextView tvName = view.findViewById(R.id.name);
        int productNameColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRODUCTNAME);
        String productName = cursor.getString(productNameColumnIndex);
        tvName.setText(productName);

        TextView tvPrice = view.findViewById(R.id.price);
        int priceColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRICE);
        String productPrice = cursor.getString(priceColumnIndex);
        tvPrice.setText(productPrice);

        tvQuantity = view.findViewById(R.id.quantity);
        int quantityColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_QUANTITY);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        tvQuantity.setText(productQuantity);

        Button saleButton = view.findViewById(R.id.button_sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.decreaseQuantity(Integer.valueOf(productId), Integer.valueOf(productQuantity));
            }
        });

        Button editButton = view.findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.editItem(Integer.valueOf(productId));
            }
        });
    }

}

