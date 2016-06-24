package com.notifytec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.taks.ConfirmarEsqueceuSenhaTask;

public class TrocarSenha extends BaseActivity {
    private TrocarSenha mActivity;
    private Button btnVoltar;
    private Button btnAlterarSenha;
    private EditText edToken;
    private EditText edNovaSenha;
    private EditText edNovaSenha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocar_senha);

        //Setando Botões
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnAlterarSenha = (Button) findViewById(R.id.btnAlterarSenha);

        edToken = (EditText) findViewById(R.id.edToken);
        edNovaSenha = (EditText) findViewById(R.id.edNova_Senha);
        edNovaSenha2 = (EditText) findViewById(R.id.edNova_Senha2);

        //Setando Eventos Click
        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                //Senhas são iguais?
                else if (!senha1.equals(senha2)){
                    edNovaSenha.setError(getString(R.string.error_senhas_diferentes));
                    edNovaSenha.requestFocus();
                }
                else {
                    new ConfirmarEsqueceuSenhaTask(mActivity, token, senha1).execute();
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

    public void habilitarCampos(boolean bloquear){
        btnAlterarSenha.setEnabled(bloquear);
        btnVoltar.setEnabled(bloquear);
        edNovaSenha.setEnabled(bloquear);
        edNovaSenha2.setEnabled(bloquear);
        edToken.setEnabled(bloquear);
    }

    public void antesTrocarSenha(){
        habilitarCampos(false);
        setCarregando(true, "Alterando a senha...");
    }

    public void resultadoConfirmarEsqueceuSenha(Resultado<UsuarioModel> resultado){
        habilitarCampos(true);
        setCarregando(false, null);

        if(resultado.isSucess()){
            Intent tela = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(tela);
            finish();
        }else{
            mostrarErro(resultado.getMessage());
        }
    }
}
