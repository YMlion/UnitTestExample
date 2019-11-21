package cn.futu.loginunittest.search;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.futu.loginunittest.R;
import cn.futu.loginunittest.data.LocalDataSource;
import cn.futu.loginunittest.data.RemoteDataSource;
import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.model.PhoneInfo;

/**
 * 电话号码搜索界面
 */
public class SearchActivity extends AppCompatActivity implements SearchContract.View
{

    private LinearLayout mPublicLayout;
    private LinearLayout mPersonalLayout;
    private ProgressBar mProgressBar;
    private TextView mPublicPhoneTv;
    private TextView mUsageTv;
    private TextView mPersonalPhoneTv;
    private TextView mAddressTv;
    private TextView mStateTv;
    private EditText mPhoneEt;

    private SearchContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPublicLayout = findViewById(R.id.public_result_layout);
        mPersonalLayout = findViewById(R.id.personal_result_layout);
        mProgressBar = findViewById(R.id.loading);
        mPublicPhoneTv = findViewById(R.id.phone_num_tv1);
        mUsageTv = findViewById(R.id.usage_tv);
        mPersonalPhoneTv = findViewById(R.id.phone_num_tv2);
        mAddressTv = findViewById(R.id.address_tv);
        mStateTv = findViewById(R.id.state_tv);

        mPhoneEt = findViewById(R.id.phone_et);
        Button search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.search(mPhoneEt.getText().toString());
            }
        });

        mPresenter = new SearchPresenter(this, Repository.getInstance(new RemoteDataSource(), new LocalDataSource()));
    }

    @Override
    public void onSearchStart()
    {
        mPublicLayout.setVisibility(View.GONE);
        mPersonalLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPublicInfo(PhoneInfo phoneInfo)
    {
        mProgressBar.setVisibility(View.GONE);
        mPublicLayout.setVisibility(View.VISIBLE);
        mPublicPhoneTv.setText(phoneInfo.getPhoneNum());
        mUsageTv.setText(phoneInfo.getUsage());
    }

    @Override
    public void showPersonalInfo(PhoneInfo phoneInfo)
    {
        mProgressBar.setVisibility(View.GONE);
        mPersonalLayout.setVisibility(View.VISIBLE);
        mPersonalPhoneTv.setText(phoneInfo.getPhoneNum());
        mAddressTv.setText(phoneInfo.getAddress());
        mStateTv.setText(phoneInfo.getState());
    }

    @Override
    public void showNoResult()
    {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, "未查询到该号码的信息", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchFailed(String message)
    {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mPresenter != null)
        {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
