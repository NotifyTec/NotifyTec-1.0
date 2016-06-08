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

import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.service.LoginService;
import com.notifytec.service.NotificacaoService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import outros.VarConst;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.edLogin);
        populateAutoComplete();

        mSenhaView = (EditText) findViewById(R.id.edSenha);

        mLoginView.setText("a");
        mSenhaView.setText("s");

        Button BtnLogin = (Button) findViewById(R.id.btnLogin);
        Button BtnEsqueciSenha = (Button) findViewById(R.id.btnEsqSenha);

        //Setando o clique no botão Login
        BtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTeste();
                //attemptLogin();
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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void loginTeste(){
        String login = mLoginView.getText().toString();
        String senha = mSenhaView.getText().toString();

        if (login.equals("")){
            VarConst.cod_usu_logado = 1;
            VarConst.usuario_logado = "1";
//            VarConst.nome_usu_logado = "Luis Fernando dos Santos silva sauro de oliveira";
            VarConst.nome_usu_logado = "Luis Fernando dos Santos";
            VarConst.podeEnviar = false;
            VarConst.novasnotificacoes = false;
        }
        else {
            VarConst.cod_usu_logado = 2;
            VarConst.usuario_logado = "2";
            VarConst.nome_usu_logado = "Jederson Donizete Zuchi";
            VarConst.podeEnviar = true;
            VarConst.novasnotificacoes = true ;
        }

        new UserLoginTask(login, senha).execute();
        /*
        Intent tela = new Intent(getBaseContext(), MenuPrincipal.class);
        startActivity(tela);
        finish();*/
    };

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
            //TODO: AUTENTICAR USUARIO
            if (autenticar(login, senha)){
                //Usuario válido
                if (isFirstAcess(login)){
                    firstAcess();
                }
                else {
                    Intent tela = new Intent(getBaseContext(), MenuPrincipal.class);
                    startActivity(tela);
                    finish();
                }
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
//                showProgress(true);
//                mAuthTask = new UserLoginTask(login, senha);
//                mAuthTask.execute((Void) null);
            } else {
                mLoginView.setError(getString(R.string.error_invalid_autenticacao));
                mLoginView.requestFocus();
            }
        }
    }

    private void firstAcess(){
        Intent tela = new Intent(this, PrimeiroAcesso.class);
        startActivity(tela);
        finish();
    }

    private void esqueciSenha(){
        Intent tela = new Intent(this, EsqueciSenha.class);
        startActivity(tela);
    }

    private boolean autenticar(String login, String senha){
        //TODO: ajustar
        if (login.equals("1") && senha.equals("1")){
            return true;
        } else {
            return false;
        }
    }

    private boolean isLoginValid(String login) {
//        return email.contains("@");
        //TODO: ajustar
        return login.length() > 2;
    }

    private boolean isFirstAcess(String login){
        return true;
    }

    private boolean isSenhaValid(String senha) {
        //TODO: ajustar
        return senha.length() > 2;
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
    public class UserLoginTask extends AsyncTask<Void, Void, Resultado<Token>> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Resultado<Token> doInBackground(Void... params) {
            new NotificacaoService().getEnviadas(UUID.randomUUID());
            new NotificacaoService().getRecebidas(UUID.randomUUID());

            Resultado<Token> resultado = new LoginService().login(mEmail, mPassword);

            return resultado;
        }

        @Override
        protected void onPostExecute(final Resultado<Token> resultado) {
            mAuthTask = null;
            showProgress(false);

            if (resultado.isSucess() && resultado.getResult().getToken() != null &&
                    !resultado.getResult().getToken().isEmpty()) {
                finish();
            } else {
                // Mensagens de erro. Mostrar na tela???
                resultado.getMessages();

                mSenhaView.setError(getString(R.string.error_invalid_autenticacao));
                mSenhaView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

