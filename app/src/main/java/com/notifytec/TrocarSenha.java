package com.notifytec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TrocarSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocar_senha);

        //Setando Botões
        Button BtnVoltar = (Button) findViewById(R.id.btnVoltar);
        Button BtnAlterarSenha = (Button) findViewById(R.id.btnAlterarSenha);

        //Setando Eventos Click
        BtnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edToken = (EditText) findViewById(R.id.edToken);
                EditText edNovaSenha = (EditText) findViewById(R.id.edNova_Senha);
                EditText edNovaSenha2 = (EditText) findViewById(R.id.edNova_Senha2);

                String token = edToken.getText().toString();
                String senha1 = edNovaSenha.getText().toString();
                String senha2 = edNovaSenha2.getText().toString();

                if (TextUtils.isEmpty(token)){
                    edToken.setError(getString(R.string.error_field_required));
                    edToken.requestFocus();
                }
                else if (TextUtils.isEmpty(senha1)){
                    edNovaSenha.setError(getString(R.string.error_field_required));
                    edNovaSenha.requestFocus();
                }
                else if (TextUtils.isEmpty(senha2)){
                    edNovaSenha2.setError(getString(R.string.error_field_required));
                    edNovaSenha2.requestFocus();
                }
                //Campos preenchidos
                //Validar Token
                else if(!validarToken(token)){
                    edToken.setError(getString(R.string.error_token_invalid));
                    edToken.requestFocus();
                }
                //Senhas são iguais?
                else if (!senha1.equals(senha2)){
                    edNovaSenha.setError(getString(R.string.error_senhas_diferentes));
                    edNovaSenha.requestFocus();
                }
                else {
                    //TODO: alterar senha usuario
                    Toast.makeText(getBaseContext(), "Senha Alterada", Toast.LENGTH_LONG ).show();
                    Intent tela = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(tela);
                    finish();
                }
            }
        });
        BtnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean validarToken(String token){
        //TODO: fazer validação token
        return true;
    }
}
