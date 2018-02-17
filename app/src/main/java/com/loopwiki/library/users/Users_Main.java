package com.loopwiki.library.users;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopwiki.library.R;
import com.loopwiki.library.users.UserInventoryMVP.Presenter;
import com.loopwiki.library.users.UserInventoryMVP.View;

/**
 * Created by sambad on 2/15/18
 */

public class Users_Main extends AppCompatActivity implements View{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user);
        presenter = new UserInventoryPresenter(this);
        presenter.initializeOnCreate();

        UserInventoryPresenter.MyAdapter mAdapter = presenter.returnMyAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.book_recyclerview1);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
