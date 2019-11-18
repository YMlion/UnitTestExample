package cn.futu.loginunittest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.futu.loginunittest.data.LocalDataSource;
import cn.futu.loginunittest.data.RemoteDataSource;
import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.User;

/**
 * main view
 */
public class MainActivity extends AppCompatActivity implements MainContract.View
{

    private View loginContent;
    private View listContent;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private Button loadListButton;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this, Repository.getInstance(new RemoteDataSource(), new LocalDataSource()));

        loginContent = findViewById(R.id.login_content);
        listContent = findViewById(R.id.list_content);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        loadListButton = findViewById(R.id.load_list_btn);

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                mPresenter.check(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    mPresenter.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
        loadListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.loadContactList();
            }
        });
    }

    @Override
    public void onLoginStart()
    {
        loadingProgressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
    }

    @Override
    public void onLoginSuccess(User user)
    {
        loadingProgressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(), getString(R.string.welcome), Toast.LENGTH_LONG).show();
        loginContent.setVisibility(View.GONE);
        listContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void verifyPhoneCode(User user)
    {
        loadingProgressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(), getString(R.string.verify), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed(String tips)
    {
        loadingProgressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeInputState(boolean phoneError, boolean pwdError)
    {
        loginButton.setEnabled(!phoneError && !pwdError);
        if (phoneError)
        {
            usernameEditText.setError(getString(R.string.invalid_username));
        }
        if (pwdError)
        {
            passwordEditText.setError(getString(R.string.invalid_password));
        }
    }

    @Override
    public void onLoadStart()
    {
        loadingProgressBar.setVisibility(View.VISIBLE);
        loadListButton.setEnabled(false);
    }

    @Override
    public void showContactList(List<Contact> contacts)
    {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty()
    {
        loadingProgressBar.setVisibility(View.GONE);
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
        mPresenter.onDestroy();
    }
}
