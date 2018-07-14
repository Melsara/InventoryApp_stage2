package eu.escapeadvisor.bookshelf;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import eu.escapeadvisor.bookshelf.data.BookCursorAdapter;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private BookCursorAdapter bookCursorAdapter;
    private static final int BOOK_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRandomBook();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_view_books);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        bookCursorAdapter = new BookCursorAdapter(this, null);
        listView.setAdapter(bookCursorAdapter);

        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }



    public void insertRandomBook() {
        ContentValues values = new ContentValues();
        values.put(BookshelfEntry.COLUMN_PROD_PRODUCTNAME, getString(R.string.dummy_productname));
        values.put(BookshelfEntry.COLUMN_PROD_ISBOOK, BookshelfEntry.ISBOOK_YES);
        values.put(BookshelfEntry.COLUMN_PROD_TITLE, getString(R.string.dummy_title));
        values.put(BookshelfEntry.COLUMN_PROD_AUTHOR, getString(R.string.dummy_author));
        values.put(BookshelfEntry.COLUMN_PROD_PRICE, "9.99");
        values.put(BookshelfEntry.COLUMN_PROD_QUANTITY, "9999");
        values.put(BookshelfEntry.COLUMN_PROD_SUPPLIERNAME, getString(R.string.dummy_supplierName));
        values.put(BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER, getString(R.string.dummy_supplierPhoneNumber));

       Uri insertUri = getContentResolver().insert(BookshelfEntry.CONTENT_URI_PRODUCTS, values);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BaseColumns._ID,
                BookshelfEntry.COLUMN_PROD_PRODUCTNAME,
                BookshelfEntry.COLUMN_PROD_ISBOOK,
                BookshelfEntry.COLUMN_PROD_TITLE,
                BookshelfEntry.COLUMN_PROD_AUTHOR,
                BookshelfEntry.COLUMN_PROD_PRICE,
                BookshelfEntry.COLUMN_PROD_QUANTITY,
                BookshelfEntry.COLUMN_PROD_SUPPLIERNAME,
                BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER
        };
        String sortOrder = BaseColumns._ID + " ASC";

        return new CursorLoader(this,
                BookshelfEntry.CONTENT_URI_PRODUCTS,
                projection,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        bookCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookCursorAdapter.swapCursor(null);
    }
}
