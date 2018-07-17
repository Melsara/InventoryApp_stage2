package eu.escapeadvisor.bookshelf;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import eu.escapeadvisor.bookshelf.data.BookCursorAdapter;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_EDIT_CLICKED;
import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_FAB_CLICKED;
import static eu.escapeadvisor.bookshelf.HelperClass.disableButton;
import static eu.escapeadvisor.bookshelf.HelperClass.disableEditText;
import static eu.escapeadvisor.bookshelf.HelperClass.helperGetText;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    BookCursorAdapter bookCursorAdapter;
    private static final int BOOK_LOADER = 0;
    int mRowsUpdated;
    int mRowsDeleted;
    private Uri mCurrentProductUri;
    private Boolean editMode;
    private Boolean insertMode;

    private EditText mEtProductName;
    private EditText mEtPrice;
    private EditText mEtQuantity;
    private EditText mEtSupplierName;
    private EditText mEtSupplierPhoneNumber;

    private Button mSaveProduct;
    private Button mDeleteProduct;
    private Button mIncreaseQuantity;
    private Button mDecreaseQuantity;
    private Button mOrder;

    private boolean mProductHasChanged = false;

    private Toast editorToast;
    private int toastDuration = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        editMode = extras.getBoolean(KEY_EDIT_CLICKED);
        insertMode = extras.getBoolean(KEY_FAB_CLICKED);
        mCurrentProductUri = intent.getData();

        setActivityComponent();

        /*Manage 3 cases:
        1. (EditorActivity) insertMode is true and editMode is false, plus mCurrentProductUri is null - user clicked on fab button - insert new product (all fields enabled and empty, save button)
        2. (EditorActivity) insertMode is false and editMode is false - user clicked on the item - see details (all fields disabled and pre-filled, no buttons)
        3. (EditorActivity) insertMode is false and editMode is true - user clicked on edit button - edit product (all fields enabled and pre-filled, buttons increaseQuantity, decreaseQuantity, order, save, delete)
         */

            //1. insertMode is true and editMode is false, plus mCurrentProductUri is null - user clicked on fab button - insert new product (all fields enabled and empty, save button)
        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            invalidateOptionsMenu();
            disableButton(mIncreaseQuantity);
            disableButton(mDecreaseQuantity);
            disableButton(mDeleteProduct);
            disableButton(mOrder);
            mSaveProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveProduct();
                }
            });

            //2. insertMode is false and editMode is false - user clicked on the item - see details (all fields disabled and pre-filled, no buttons)
        } else if (!insertMode && !editMode) {
            setTitle(getString(R.string.editor_activity_title_edit_product));
            disableEditText(mEtProductName);
            disableEditText(mEtPrice);
            disableEditText(mEtQuantity);
            disableEditText(mEtSupplierName);
            disableEditText(mEtSupplierPhoneNumber);
            disableButton(mIncreaseQuantity);
            disableButton(mDecreaseQuantity);
            disableButton(mOrder);
            disableButton(mDeleteProduct);
            disableButton(mSaveProduct);

            getLoaderManager().initLoader(BOOK_LOADER, null, this);

            //3. (EditorActivity) insertMode is false and editMode is true - user clicked on edit button - edit product (all fields enabled and pre-filled, buttons increaseQuantity, decreaseQuantity, order, save, delete)
        } else if (!insertMode && editMode){
            getLoaderManager().initLoader(BOOK_LOADER, null, this);
            mSaveProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveProduct();
                }
            });
        }



    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BaseColumns._ID,
                BookshelfEntry.COLUMN_PROD_PRODUCTNAME,
                BookshelfEntry.COLUMN_PROD_PRICE,
                BookshelfEntry.COLUMN_PROD_QUANTITY,
                BookshelfEntry.COLUMN_PROD_SUPPLIERNAME,
                BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER,
        };
        String sortOrder = BaseColumns._ID + " ASC";

        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int productNameColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRODUCTNAME);
            int priceColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_SUPPLIERNAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER);

            String currentProductName = cursor.getString(productNameColumnIndex);
            Float currentPrice = cursor.getFloat(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            String currentSupplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);

            mEtProductName.setText(currentProductName);
            mEtPrice.setText(Float.toString(currentPrice));
            mEtQuantity.setText(Integer.toString(currentQuantity));
            mEtSupplierName.setText(currentSupplierName);
            mEtSupplierPhoneNumber.setText(currentSupplierPhoneNumber);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEtProductName.setText("");
        mEtPrice.setText("");
        mEtQuantity.setText("");
        mEtSupplierName.setText("");
        mEtSupplierPhoneNumber.setText("");

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    private void setActivityComponent() {
        mEtProductName = findViewById(R.id.et_productName);
        mEtPrice = findViewById(R.id.et_price);
        mEtQuantity = findViewById(R.id.et_quantity);
        mEtSupplierName = findViewById(R.id.et_supplierName);
        mEtSupplierPhoneNumber = findViewById(R.id.et_supplierPhoneNumber);
        mIncreaseQuantity = findViewById(R.id.button_increase_quantity);
        mDecreaseQuantity = findViewById(R.id.button_decrease_quantity);
        mDeleteProduct = findViewById(R.id.button_delete);
        mSaveProduct = findViewById(R.id.button_save);
        mOrder = findViewById(R.id.button_order);

        bookCursorAdapter = new BookCursorAdapter(this, null);
        mEtProductName.setOnTouchListener(mTouchListener);
        mEtPrice.setOnTouchListener(mTouchListener);
        mEtQuantity.setOnTouchListener(mTouchListener);
        mEtSupplierName.setOnTouchListener(mTouchListener);
        mEtSupplierPhoneNumber.setOnTouchListener(mTouchListener);

    }

    private void saveProduct() {
        String productNameString = helperGetText(mEtProductName);
        String priceString = helperGetText(mEtPrice);
        String quantityString = helperGetText(mEtQuantity);
        String supplierNameString = helperGetText(mEtSupplierName);
        String supplierPhoneNumberString = helperGetText(mEtSupplierPhoneNumber);
        float priceFloat = 0;
        int quantityInt = 0;
        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(productNameString) &&
                TextUtils.isEmpty(priceString) &&
                priceFloat == 0 &&
                quantityInt == 0 &&
                TextUtils.isEmpty(supplierNameString) &&
                TextUtils.isEmpty(supplierPhoneNumberString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BookshelfEntry.COLUMN_PROD_PRODUCTNAME, productNameString);
        values.put(BookshelfEntry.COLUMN_PROD_SUPPLIERNAME, supplierNameString);
        values.put(BookshelfEntry.COLUMN_PROD_SUPPLIERPHONENUMBER, supplierPhoneNumberString);

        if (!TextUtils.isEmpty(quantityString)) {
            quantityInt = Integer.parseInt(quantityString);
        }
        values.put(BookshelfEntry.COLUMN_PROD_QUANTITY, quantityInt);

        if (!TextUtils.isEmpty(priceString)) {
            priceFloat = Float.parseFloat(priceString);
        }

        values.put(BookshelfEntry.COLUMN_PROD_PRICE, priceFloat);

        if (mCurrentProductUri == null) {
            Uri insertUri = getContentResolver().insert(BookshelfEntry.CONTENT_URI_PRODUCTS, values);

            if (insertUri == null) {
                editorToast.makeText(this, getString(R.string.insert_failure), toastDuration).show();
            } else {
                editorToast.makeText(this, getString(R.string.insert_success_user), toastDuration).show();
                finish();
            }

        } else {

            mRowsUpdated = getContentResolver().update(
                    mCurrentProductUri,
                    values,
                    null,
                    null
            );

            if (mRowsUpdated == 0) {
                editorToast.makeText(this, getString(R.string.update_failure), toastDuration).show();
            } else {
                editorToast.makeText(this, getString(R.string.update_success), toastDuration).show();
                finish();
            }

        }

    }
}
