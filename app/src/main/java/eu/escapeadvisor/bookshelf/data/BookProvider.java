package eu.escapeadvisor.bookshelf.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

public class BookProvider extends ContentProvider {

    private BookshelfDbHelper mDbHelper;
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String LOG_TAG = BookProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(BookshelfContract.CONTENT_AUTHORITY, BookshelfContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(BookshelfContract.CONTENT_AUTHORITY, BookshelfContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new BookshelfDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case PRODUCTS:
                cursor = db.query(BookshelfEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            case PRODUCT_ID:
                selection = BookshelfEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(BookshelfEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            default:

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return BookshelfEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return BookshelfEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        //Data validation
        String insert_productName = values.getAsString(BookshelfEntry.COLUMN_PROD_PRODUCTNAME);
        if (insert_productName.equals(null) || insert_productName.equals("")) {
            throw new IllegalArgumentException("Products table requires a name");
        }

        String insert_productPrice = values.getAsString(BookshelfEntry.COLUMN_PROD_PRICE);
        if (insert_productPrice.equals(null) || insert_productName.equals("")) {
            throw new IllegalArgumentException("Products table requires a price");
        }

        Integer insert_isBook = values.getAsInteger(BookshelfEntry.COLUMN_PROD_ISBOOK);

        /*        Will add this validation later. For the moment there is no UI for isBook.
           if (insert_isBook == null || !isValidItem(insert_isBook)) {
            throw new IllegalArgumentException("Products table requires a gender");
        }*/

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(BookshelfEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "SQL Insert failed " + uri);
            return null;
        } else {
            Log.v("MainActivity", "New row ID is " + id);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BookshelfEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                // Delete a single row given by the ID in the URI
                selection = BookshelfEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BookshelfEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    public static boolean isValidItem(int isBook) {
        if (isBook == BookshelfEntry.ISBOOK_NO || isBook == BookshelfEntry.ISBOOK_YES) {
            return true;
        }
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = BookshelfEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Checking data before updating

        if (values.containsKey(BookshelfEntry.COLUMN_PROD_PRODUCTNAME)) {
            String insert_name = values.getAsString(BookshelfEntry.COLUMN_PROD_PRODUCTNAME);
            if (insert_name == null) {
                throw new IllegalArgumentException("Products table requires a name");
            }


            if (values.containsKey(BookshelfEntry.COLUMN_PROD_QUANTITY)) {
                Integer insert_quantity = values.getAsInteger(BookshelfEntry.COLUMN_PROD_QUANTITY);
                if (insert_quantity < 0 && insert_quantity != null) {
                    throw new IllegalArgumentException("Products table requires a quantity equal or higher than 0");
                }
            }
            if (values.containsKey(BookshelfEntry.COLUMN_PROD_PRICE)) {
                Float insert_price = values.getAsFloat(BookshelfEntry.COLUMN_PROD_PRICE);
                if (insert_price < 0 && insert_price != null) {
                    throw new IllegalArgumentException("Products table requires a price equal or higher than 0");
                }
            }

            if (values.containsKey(BookshelfEntry.COLUMN_PROD_SUPPLIERNAME)) {
                String insert_supplierName = values.getAsString(BookshelfEntry.COLUMN_PROD_SUPPLIERNAME);
                if (insert_supplierName == null) {
                    throw new IllegalArgumentException("Products table requires a supplier name");
                }
            }

            if (values.containsKey(BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER)) {
                String insert_supplierPhoneNumber = values.getAsString(BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER);
                if (insert_supplierPhoneNumber == null) {
                    throw new IllegalArgumentException("Products table requires a supplier phone number");
                }
            }

        }


        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(BookshelfEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
