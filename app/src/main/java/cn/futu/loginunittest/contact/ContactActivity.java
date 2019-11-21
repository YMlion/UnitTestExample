package cn.futu.loginunittest.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.futu.loginunittest.R;
import cn.futu.loginunittest.contact.adapter.ContactAdapter;
import cn.futu.loginunittest.data.LocalDataSource;
import cn.futu.loginunittest.data.RemoteDataSource;
import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.search.SearchActivity;

/**
 * 联系人界面
 */
public class ContactActivity extends AppCompatActivity implements ContactContract.View
{

    private ProgressBar loadingProgressBar;
    private ContactContract.Presenter mPresenter;
    private ContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ContactActivity.this, SearchActivity.class));
            }
        });

        loadingProgressBar = findViewById(R.id.loading);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new ContactAdapter();
        recyclerView.setAdapter(mAdapter);

        String id = getIntent().getStringExtra("id");
        Repository repository = Repository.getInstance(new RemoteDataSource(), new LocalDataSource());
        mPresenter = new ContactPresenter(this, repository, id);
        mPresenter.loadContactList();
    }

    @Override
    public void onLoadStart()
    {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContactList(List<Contact> contacts)
    {
        loadingProgressBar.setVisibility(View.GONE);
        mAdapter.setData(contacts);
    }

    @Override
    public void showEmpty()
    {
        loadingProgressBar.setVisibility(View.GONE);
        mAdapter.setData(new ArrayList<Contact>());
        Toast.makeText(getApplicationContext(), getString(R.string.list_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadFailed(String message)
    {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPresenter.destroy();
    }

}
