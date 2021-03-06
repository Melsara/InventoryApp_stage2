package eu.escapeadvisor.bookshelf;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import eu.escapeadvisor.bookshelf.data.BookCursorAdapter;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

import static eu.escapeadvisor.bookshelf.GlobalConstant.QUANTITY_SALE;
import static eu.escapeadvisor.bookshelf.HelperClass.createIntent;
import static eu.escapeadvisor.bookshelf.HelperClass.createIntentWIthId;
import static eu.escapeadvisor.bookshelf.HelperClass.showDeleteConfirmationDialog;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private BookCursorAdapter bookCursorAdapter;
    private static final int BOOK_LOADER = 0;
    private Boolean editClicked;
    private Boolean fabClicked;
    private FloatingActionButton fab;
    private ListView listView;
    private View emptyView;
    private Button saleButton;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivityComponent();

        /*setting OnClickListeners on the items in the UI*/
        bookCursorAdapter = new BookCursorAdapter(this, null);
        listView.setAdapter(bookCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                editClicked = false;
                fabClicked = false;
                createIntentWIthId(MainActivity.this, editClicked, fabClicked, id);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClicked = true;
                editClicked = false;
                createIntent(MainActivity.this, editClicked, fabClicked);
            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null, this);

    }



    public void editItem (int id){
        editClicked = true;
        fabClicked = false;
        createIntentWIthId(MainActivity.this, editClicked, fabClicked, id);
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

    private void setActivityComponent() {
        fab = findViewById(R.id.floatingActionButton);
        saleButton = findViewById(R.id.button_sale);
        editButton = findViewById(R.id.button_edit);
        listView = findViewById(R.id.list_view_books);
        emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.menu_action_delete_all_entries:
                showDeleteConfirmationDialog(BookshelfEntry.CONTENT_URI_PRODUCTS, MainActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

