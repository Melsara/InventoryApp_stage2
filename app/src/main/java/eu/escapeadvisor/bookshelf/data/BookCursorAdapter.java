package eu.escapeadvisor.bookshelf.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.CursorSwipeAdapter;

import eu.escapeadvisor.bookshelf.data.BookshelfContract.BookshelfEntry;

import eu.escapeadvisor.bookshelf.R;

public class BookCursorAdapter extends CursorSwipeAdapter {

    private SwipeLayout swipeLayout;
    private boolean swipedLeftToRight = false;

    public BookCursorAdapter (Context context, Cursor c) {
        super (context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView tvName = view.findViewById(R.id.name);
        int productNameColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRODUCTNAME);
        String productName = cursor.getString(productNameColumnIndex);
        tvName.setText(productName);

        TextView tvPrice =  view.findViewById(R.id.price);
        int priceColumnIndex = cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_PRICE);
        String productPrice = cursor.getString(priceColumnIndex);
        tvPrice.setText(productPrice);

        TextView tvQuantity = view.findViewById(R.id.quantity);
        int quantityColumnIndex =cursor.getColumnIndex(BookshelfEntry.COLUMN_PROD_QUANTITY);
        String productQuantity = cursor.getString(quantityColumnIndex);
        tvQuantity.setText(productQuantity);

        swipeLayout =  view.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        addSwipeLeftToRight(view);

        ImageView edit = view.findViewById(R.id.pencil);
        edit.setImageResource(R.drawable.baseline_edit_white_18);
        ImageView delete = view.findViewById(R.id.trash);
        delete.setImageResource(R.drawable.baseline_delete_white_18);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                if(leftOffset > 0) {
                    Toast.makeText(context, "swiped left to right", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

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
