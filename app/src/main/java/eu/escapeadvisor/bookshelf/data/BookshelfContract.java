package eu.escapeadvisor.bookshelf.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookshelfContract {

    public static final String CONTENT_AUTHORITY = "eu.escapeadvisor.bookshelf";
    public static final String PATH_PRODUCTS = "products";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private BookshelfContract() {

    }

    public static final class BookshelfEntry implements BaseColumns {
        public static final Uri CONTENT_URI_PRODUCTS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String TABLE_NAME = "products";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PROD_PRODUCTNAME = "product_name";
        public static final String COLUMN_PROD_ISBOOK = "is_book";
        public static final String COLUMN_PROD_TITLE = "title";
        public static final String COLUMN_PROD_AUTHOR = "author";
        public static final String COLUMN_PROD_PRICE = "price";
        public static final String COLUMN_PROD_QUANTITY = "quantity";
        public static final String COLUMN_PROD_SUPPLIERNAME = "supplier_name";
        public static final String COLUMN_PROD_SUPPLIERPHONENUMBER = "supplier_phonenumber";

        public static final int ISBOOK_YES = 1;
        public static final int ISBOOK_NO = 0;

    }
}
