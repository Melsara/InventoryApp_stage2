package eu.escapeadvisor.bookshelf.data;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.CursorSwipeAdapter;

import eu.escapeadvisor.bookshelf.EditorActivity;
import eu.escapeadvisor.bookshelf.R;
import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_FAB_CLICKED;
import static eu.escapeadvisor.bookshelf.GlobalConstant.KEY_SWIPE_DIR;

public class BookCursorAdapter extends CursorSwipeAdapter {

    private SwipeLayout swipeLayout;
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

        //setting swipe actions on the list items
        swipeLayout = view.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        addSwipeLeftToRight(view);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //everything here is for checking which direction user swiped
                fabClicked = false;
                if (leftOffset > 200) {
                    deleteItem = view.findViewById(R.id.trash);
                    swipedLeftToRight = true;
                } else if (leftOffset == 0)
                    swipedLeftToRight = false;
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                if (swipedLeftToRight) {

                    deleteItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            long position = cursor.getPosition();
                            Toast.makeText(context, "swiped left to right", Toast.LENGTH_SHORT).show();
                            Intent deleteIntent = new Intent(context, EditorActivity.class);
                            extras = new Bundle();
                            extras.putBoolean(KEY_SWIPE_DIR, swipedLeftToRight);
                            extras.putBoolean(KEY_FAB_CLICKED, fabClicked);
                            deleteIntent.putExtras(extras);
                            Uri currentProductUri = ContentUris.withAppendedId(BookshelfEntry.CONTENT_URI_PRODUCTS, position);
                            deleteIntent.setData(currentProductUri);
                            context.startActivity(deleteIntent);
                            swipedLeftToRight = false;
                            position = 0;
                            return;
                        }
                    });
                    //trigger delete action on the swiped item

                } else if (!swipedLeftToRight) {
                    //trigger edit action on the swiped item
                    Toast.makeText(context, "swiped right to left", Toast.LENGTH_SHORT).show();
                    Intent editIntent = new Intent(context, EditorActivity.class);
                    editIntent.putExtra("Edit mode: expecting swipedLeftToRight to be false", swipedLeftToRight);
                    context.startActivity(editIntent);
                }
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void closeAllItems() {

    }

    private void addSwipeLeftToRight(View view) {
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_rl));
    }

}
