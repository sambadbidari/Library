package com.loopwiki.library.users;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopwiki.library.R;
import com.loopwiki.library.admin.BookListContract;
import com.loopwiki.library.admin.BookListDbHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by sambad on 2/17/18
 */

public class UserInventoryPresenter implements UserInventoryMVP.Presenter {
    public Cursor mainCursor;
    UserInventoryMVP.View view;
    MyAdapter mAdapter;
    /**
     * SQLiteDatabase Variables
     */
    static SQLiteDatabase mDatabase;

    public Cursor getMainCursor() {
        return mainCursor;
    }

    public UserInventoryPresenter(UserInventoryMVP.View view) {
        this.view = view;
    }

    public void initializeOnCreate() {
        BookListDbHelper dbHelper = new BookListDbHelper((Users_Main) view);
        mDatabase = dbHelper.getWritableDatabase();
        mainCursor = getAllBooks();
    }

    @Override
    public MyAdapter returnMyAdapter() {
        mAdapter = new MyAdapter(this);
        return mAdapter;
    }

    public Cursor getAllBooks() {
        return mDatabase.query(
                BookListContract.BookListEntry.TABLE_NAME,
                null, null, null, null, null, null);
    }

    @Override
    public void swapCursor(Cursor cursor) {
        mainCursor = cursor;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBindHoldersViews(MyAdapter.ViewHolder holder, int position) {
        if (!mainCursor.moveToPosition(position)) {
            return;
        }
        holder.authorTextView
                .setText(mainCursor.getString(mainCursor.getColumnIndex(BookListContract.BookListEntry.BOOK_AUTHOR)));
        holder.titleTextView
                .setText(mainCursor.getString(mainCursor.getColumnIndex(BookListContract.BookListEntry.BOOK_TITLE)));
        Picasso.with(holder.authorTextView.getContext())
                .load(mainCursor.getString(mainCursor.getColumnIndex(BookListContract.BookListEntry.BOOK_IMAGE_URL)))
                .into(holder.mImageView);
    }

    @Override
    public int getDataCount() {
        return mainCursor.getCount();
    }


    /**
     * Making inner class of the RecyclerView.Adapter class
     */
    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        UserInventoryMVP.Presenter presenter;

        public MyAdapter(UserInventoryPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_layout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            presenter.onBindHoldersViews(holder, position);

        }

        @Override
        public int getItemCount() {
                return presenter.getDataCount();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
                implements UserInventoryMVP.HolderView {
            TextView authorTextView;
            TextView titleTextView;
            ImageView mImageView;

            ViewHolder(View itemView) {
                super(itemView);
                authorTextView = itemView.findViewById(R.id.book_layout_author_textview1);
                titleTextView = itemView.findViewById(R.id.book_layout_title_textview1);
                mImageView = itemView.findViewById(R.id.book_image_imageview1);
            }
        }
    }
}
