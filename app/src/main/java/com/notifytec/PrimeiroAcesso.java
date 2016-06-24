package com.notifytec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.service.LoginService;
import com.notifytec.service.RestService;
import com.notifytec.service.UsuarioService;
import com.notifytec.taks.RedefinirSenhaTask;

import org.w3c.dom.Text;

public class PrimeiroAcesso extends BaseActivity {

    private PrimeiroAcesso mActivity;
    private EditText edSenha1;
    private EditText edSenha2;
    private Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeiro_acesso);

        edSenha1 = (EditText) findViewById(R.id.edNova_Senha);
        edSenha2 = (EditText) findViewById(R.id.edNova_Senha2);
        btnConfirmar = (Button) findViewById(R.id.btnConfimar);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String senha1 = edSenha1.getText().toString();
                String senha2 = edSenha2.getText().toString();

                //Validar preenchimento
                if (TextUtils.isEmpty(senha1)){
                    edSenha1.setError(getString(R.string.error_field_required));
                    edSenha1.requestFocus();
                }
                else if (TextUtils.isEmpty(senha2)){
                    edSenha2.setError(getString(R.string.error_field_required));
                    edSenha2.requestFocus();
                }
                //Campos preenchidos
                else if (!senha1.equals(senha2)) {
                    edSenha1.setError(getString(R.string.error_senhas_diferentes));
                    edSenha1.requestFocus();
                }
                //tudo OK
                else {
                    new RedefinirSenhaTask(mActivity, senha1).execute();
                }
            }
        });

        mActivity = this;
    }

    public void habilitarCampos(boolean habilitar){
        edSenha1.setEnabled(habilitar);
        edSenha2.setEnabled(habilitar);
        btnConfirmar.setEnabled(habilitar);
    }

    public void anteRedefinirSenha(){
        habilitarCampos(false);
        setCarregando(true, "Redefinindo senha...");
    }

    public void resultadoRedefinirSenha(Resultado<UsuarioModel> resultado){
        habilitarCampos(true);
        setCarregando(false, null);

        if(resultado.isSucess()){
            new UsuarioDao(getApplicationContext()).update(resultado.getResult());
            Intent tela = new Intent(getBaseContext(), MenuPrincipal.class);
            startActivity(tela);
            finish();
        }else{
            mostrarErro(resultado.getMessage());
        }
    }
}
