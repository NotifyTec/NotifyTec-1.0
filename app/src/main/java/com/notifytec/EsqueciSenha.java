package com.notifytec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.taks.EsqueceuSenhaTask;

import funcoes.Funcoes;

public class EsqueciSenha extends BaseActivity {
    private boolean cancel;
    private EsqueciSenha mActivity;

    private Button btnNext;
    private Button btnVoltar;
    private EditText edRA_CPF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        //Setando componentes
        btnNext = (Button) findViewById(R.id.btnNextStep);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        edRA_CPF = (EditText) findViewById(R.id.edRA_CPF);

        //Setando o clique no botão Proximo Passo
        btnNext.setOnClickListener(new View.OnClickListener() {
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
                else if ( (isCPF(ra_cpf)) && (!Funcoes.isCPFValid(ra_cpf)) ){
                    //CPF inválido
                    edRA_CPF.setError(getString(R.string.error_cpf_invalid));
                    edRA_CPF.requestFocus();
                    cancel = true;
                }

                //TODO: validar RA ou CPF existente na base

                else if (!cancel){
                    new EsqueceuSenhaTask(mActivity, ra_cpf).execute();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mActivity = this;
    }

    public void habilitarCampos(boolean habilitar){
        edRA_CPF.setEnabled(habilitar);
        btnNext.setEnabled(habilitar);
        btnVoltar.setEnabled(habilitar);
    }

    public void antesEsqueceuSenha(){
        setCarregando(true, "Gerando token de redefinição...");
    }

    public void resultadoEsqueceuSenha(Resultado<UsuarioModel> resultado){
        habilitarCampos(true);
        setCarregando(false, null);

        if(resultado.isSucess()){
            Intent tela = new Intent(getBaseContext(), TrocarSenha.class);
            startActivity(tela);
        }else{
            mostrarErro(resultado.getMessage());
        }
    }

    private boolean isCPF(String dado){
        return dado.length() > 9;
    }
}
