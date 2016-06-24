package com.notifytec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.service.LoginService;
import com.notifytec.service.NotificacaoService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import outros.VarConst;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mSenhaView;
    private View mProgressView;
    private View mLoginFormView;
    private Snackbar mSnackLoading;

    public static final String LOG_OUT = "log-out-flag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsuarioModel usuario = new UsuarioDao(getApplicationContext()).get();
        proximoPasso(usuario);

        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.edLogin);
        populateAutoComplete();

        mSenhaView = (EditText) findViewById(R.id.edSenha);

        Button BtnLogin = (Button) findViewById(R.id.btnLogin);
        Button BtnEsqueciSenha = (Button) findViewById(R.id.btnEsqSenha);

        //Setando o clique no botão Login
        BtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //Setando o clique no botão Login
        BtnEsqueciSenha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                esqueciSenha();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
            Snackbar.make(mLoginView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mLoginView.setError(null);
        mSenhaView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String senha = mSenhaView.getText().toString();

        // Checando se login preenchido
        if (TextUtils.isEmpty(login)){
            mLoginView.setError(getString(R.string.error_field_required));
            mLoginView.requestFocus();
        }
        // Check for a valid login
        else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            mLoginView.requestFocus();
        }
        // Checando se senha preenchida
        else if (TextUtils.isEmpty(senha)){
            mSenhaView.setError(getString(R.string.error_field_required));
            mSenhaView.requestFocus();
        }
        // Check for a valid senha, if the user entered one.
        else if (!isSenhaValid(senha)) {
            mSenhaView.setError(getString(R.string.error_invalid_senha));
            mSenhaView.requestFocus();
        }
        //"ok"
        else {
            new UserLoginTask(login, senha).execute();
        }
    }

    private void proximoPasso(UsuarioModel usuario){
        if(usuario == null)
            return;

        if(!usuario.isAlterouSenha()){
            firstAcess();
            return;
        } else if(usuario != null && usuario.getToken() != null ){
            if(usuario.isPodeEnviar()){
                abrirAvisos();
            }else {
                menuPrincipal();
            }
            return;
        }
    }

    private void firstAcess(){
        Intent tela = new Intent(this, PrimeiroAcesso.class);
        startActivity(tela);
        finish();
    }

    private void menuPrincipal(){
        Intent tela = new Intent(getBaseContext(), MenuPrincipal.class);
        startActivity(tela);
        finish();
    }

    private void esqueciSenha(){
        Intent tela = new Intent(this, EsqueciSenha.class);
        startActivity(tela);
    }

    public void autenticar(Resultado<UsuarioModel> resultado){
        if(!resultado.isSucess()){
            mostrarErro(resultado.getMessage());
        }else {
            proximoPasso(resultado.getResult());
        }
    }

    private boolean isLoginValid(String login) {
//        return email.contains("@");
        //TODO: ajustar
        return login.length() != 0 ;
    }

    private boolean isFirstAcess(String login){
        return true;
    }

    private boolean isSenhaValid(String senha) {
        //TODO: ajustar
        return senha.length() != 0;
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

        addLoginToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addLoginToAutoComplete(List<String> loginAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, loginAddressCollection);

        mLoginView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Resultado<UsuarioModel>> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            setCarregando(true, "Realizando login...");
            super.onPreExecute();
        }

        @Override
        protected Resultado<UsuarioModel> doInBackground(Void... params) {
            Resultado<UsuarioModel> resultado = new LoginService(getApplicationContext()).login(mEmail, mPassword);

            return resultado;
        }

        @Override
        protected void onPostExecute(final Resultado<UsuarioModel> resultado) {
            setCarregando(false, null);

            mAuthTask = null;
            showProgress(false);

            autenticar(resultado);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

