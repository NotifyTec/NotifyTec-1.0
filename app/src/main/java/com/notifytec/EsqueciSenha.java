package com.notifytec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import funcoes.ValidarCPF;

public class EsqueciSenha extends AppCompatActivity {
    private boolean cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        //Setando componentes
        Button BtnNext = (Button) findViewById(R.id.btnNextStep);
        Button BtnVoltar = (Button) findViewById(R.id.btnVoltar);

        final EditText edRA_CPF = (EditText) findViewById(R.id.edRA_CPF);

        //Setando o clique no botão Proximo Passo
        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel = false;
                String ra_cpf = edRA_CPF.getText().toString();

                //Checando se RA ou CPF foram preenchidos
                if (TextUtils.isEmpty(ra_cpf)){
                    edRA_CPF.setError(getString(R.string.error_field_required));
                    edRA_CPF.requestFocus();
                    cancel = true;
                }
                else if (ra_cpf.length() < 9){
                    edRA_CPF.setError(getString(R.string.error_RA_CPF_invalido));
                    edRA_CPF.requestFocus();
                    cancel = true;
                }
                //É CPF. --> Validando
                else if ( (isCPF(ra_cpf)) && (!ValidarCPF.isCPF(ra_cpf)) ){
                    //CPF inválido
                    edRA_CPF.setError(getString(R.string.error_cpf_invalid));
                    edRA_CPF.requestFocus();
                    cancel = true;
                }

                //TODO: validar RA ou CPF existente na base

                else if (!cancel){
                    Intent tela = new Intent(getBaseContext(), TrocarSenha.class);
                    startActivity(tela);
                    //finish();
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

    private boolean isCPF(String dado){
        return dado.length() > 9;
    }
}
