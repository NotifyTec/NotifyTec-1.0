package com.notifytec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PrimeiroAcesso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeiro_acesso);

        final EditText edSenha1 = (EditText) findViewById(R.id.edNova_Senha);
        final EditText edSenha2 = (EditText) findViewById(R.id.edNova_Senha2);
        Button btnConfirmar = (Button) findViewById(R.id.btnConfimar);

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
                    Toast.makeText(getBaseContext(), "Senha alterada com sucesso", Toast.LENGTH_LONG).show();
                    Intent tela = new Intent(getBaseContext(), MenuPrincipal.class);
                    startActivity(tela);
                    finish();
                }
            }
        });

    }
}
