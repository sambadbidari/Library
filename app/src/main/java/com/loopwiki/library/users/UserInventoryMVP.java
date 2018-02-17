package com.loopwiki.library.users;

import android.database.Cursor;

/**
 * Created by sambad on 2/17/18
 */

public class UserInventoryMVP {
    interface Presenter {
        void initializeOnCreate();

        UserInventoryPresenter.MyAdapter returnMyAdapter();

        Cursor getAllBooks();

        void onBindHoldersViews(UserInventoryPresenter.MyAdapter.ViewHolder holder, int position);

        int getDataCount();

        void swapCursor(Cursor allBooks);

        Cursor getMainCursor();
    }

    interface View {

    }

    interface HolderView {

    }
}
