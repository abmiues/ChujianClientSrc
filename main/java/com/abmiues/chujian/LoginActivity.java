package com.abmiues.chujian;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.seller.SellerMainActivity;
import com.abmiues.chujian.user.MainActivity;
import com.abmiues.chujian.user.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.abmiues.chujian.R.id.btn_setip;
import static com.abmiues.chujian.R.id.login;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;
    private Boolean inLogin = false;//登陆中的判断，防止用户频繁点击登录

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btnChange;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        populateAutoComplete();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //定义一个全局的点击事件监听方法
        OnClickListener myClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {int btnId = view.getId();OnBtnClick(btnId);}
        };

        TextView text_register = (TextView) findViewById(R.id.text_register);
        Button btn_setip = (Button) findViewById(R.id.btn_setip);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        btnChange=(Button)findViewById(R.id.btn_change);

        btnChange.setOnClickListener(myClickListener);
        text_register.setOnClickListener(myClickListener);
        btn_setip.setOnClickListener(myClickListener);
        mEmailSignInButton.setOnClickListener(myClickListener);
        btnChange.setText(GlobleValue.get_isUser()?"切换商家":"切换用户");

    }

    //统一管理所有按钮点击事件
    private void OnBtnClick(int btnId)
    {
        if(btnId==R.id.text_register)
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        else if(btnId==R.id.email_sign_in_button)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
            attemptLogin();
        }
        else if(btnId==R.id.btn_change)
        {
            Button btn=(Button)findViewById(btnId);
            GlobleValue.set_isUser(!GlobleValue.get_isUser());
            btn.setText(GlobleValue.get_isUser()?"切换商家":"切换用户");
        }
        else if(btnId== btn_setip)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("请输入用户名和密码");
            //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
            View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.setip, null);
            //    设置我们自己定义的布局文件作为弹出框的Content
            builder.setView(view);

            final EditText edittextip = (EditText) view.findViewById(R.id.edittextip);
            final EditText edittexthost = (EditText) view.findViewById(R.id.edittexthost);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String ip = edittextip.getText().toString().trim();
                    String b = edittexthost.getText().toString().trim();
                    int m_host = Integer.valueOf(b);
                    GlobleValue.get_globleData().edit().putString("ip", ip).commit();
                    GlobleValue.get_globleData().edit().putInt("host", m_host).commit();
                    GlobleValue.set_ip(ip);
                    GlobleValue.set_host(m_host);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (inLogin) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String params=(GlobleValue.get_isUser()?"userid=":"sellerid=")+email+"&pwd="+password;
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            inLogin=true;
            btnChange.setVisibility(View.GONE);
            HttpRequestUtil.Send("login",params,new HttpSendCallback() {
                @Override
                public void getdata(String data) {
                    showProgress(false);
                    if (data.equals("111")) {
                        startActivity(new Intent(LoginActivity.this,GlobleValue.get_isUser()?MainActivity.class:SellerMainActivity.class));
                        LoginActivity.this.finish();
                        GlobleValue.get_globleData().edit().putString("userid",email).commit();
                        GlobleValue.set_userid(email);
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();

                    }
                    btnChange.setVisibility(View.VISIBLE);
                    inLogin=false;
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
        //return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

