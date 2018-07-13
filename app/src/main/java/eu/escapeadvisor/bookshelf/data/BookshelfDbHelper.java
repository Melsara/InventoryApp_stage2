package eu.escapeadvisor.bookshelf.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

public class BookshelfDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Bookshelf.db";
    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_PROD_TABLE =
            "CREATE TABLE " + BookshelfEntry.TABLE_NAME + " (" +
                    BookshelfEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BookshelfEntry.COLUMN_PROD_PRODUCTNAME + " TEXT NOT NULL, " +
                    BookshelfEntry.COLUMN_PROD_ISBOOK + " INTEGER NOT NULL DEFAULT 0, " +
                    BookshelfEntry.COLUMN_PROD_TITLE + " TEXT, " +
                    BookshelfEntry.COLUMN_PROD_AUTHOR + " TEXT, " +
                    BookshelfEntry.COLUMN_PROD_PRICE + " REAL default 0, " +
                    BookshelfEntry.COLUMN_PROD_QUANTITY + " INTEGER, " +
                    BookshelfEntry.COLUMN_PROD_SUPPLIERNAME + " TEXT, " +
                    BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER + " TEXT);";

    private static final String SQL_DELETE_PROD_TABLE =
            "DROP TABLE IF EXISTS " + BookshelfEntry.TABLE_NAME;

    public BookshelfDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_PROD_TABLE);
        onCreate(db);
    }
}
