package com.notifytec.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.NotificacaoOpcaoModel;
import java.util.List;

/**
 * Created by Bruno on 28/05/2016.
 */
public class OpcoesEnqueteBaseAdapter extends BaseAdapter {
    private Context ctx;
    private List<NotificacaoOpcaoModel> lista;
    private int positionNotify;
    private int[] lEscolhidos;
    private List<NotificacaoCompletaModel> mListNotify;
    private String idBinaryOpcaoEscolhida;
    private boolean colorirPrimeiro;

    public OpcoesEnqueteBaseAdapter(Context c, List<NotificacaoOpcaoModel> l, int posNotify, List<NotificacaoCompletaModel> lNotity) {
        ctx = c;
        lista = l;
        mListNotify = lNotity;
        positionNotify = posNotify;
        lEscolhidos = new int[lNotity.size()];
        /*lEscolhidos[positionNotify] = lNotity.get(positionNotify).getIndexOpcaoEscolhida();
        idBinaryOpcaoEscolhida = lNotity.get(positionNotify).getIdBinaryOpcaoEscolhida();*/
        colorirPrimeiro = false;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Button btn = new Button(ctx);

        /*if(colorirPrimeiro) {
            btn.setBackgroundResource(R.color.gray_principal);
            colorirPrimeiro = false;
        } else {
            btn.setBackgroundResource(R.color.white);
        }
        btn.setTag(R.string.testTagAux, "0");
        btn.setTextColor(ctx.getResources().getColor(R.color.red_principal));
        btn.setTypeface(null, Typeface.BOLD); //negrito
        btn.setText(lista.get(position).getNome());
        btn.setTag(R.string.testTagIdBinaryNotificacaoOpcao, lista.get(position).getId_binary());

        if (!VarConst.podeEnviar) {
            //Só Alunos. A final, somente eles respondem
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String auxS1 = String.valueOf(view.getTag(R.string.testTagAux));

                    if (auxS1.equals("0")) {
                        if (lEscolhidos[positionNotify] < 1) {
                            view.setBackgroundResource(R.color.gray_principal);
                            view.setTag(R.string.testTagAux, "1");
                            lEscolhidos[positionNotify] = 1;
                            mListNotify.get(positionNotify).setIndexOpcaoEscolhida(position);
                        }
                    } else {
                        view.setBackgroundResource(R.color.white);
                        view.setTag(R.string.testTagAux, "0");
                        lEscolhidos[positionNotify] = 0;
                        mListNotify.get(positionNotify).setIndexOpcaoEscolhida(-1);
                    }
                }
            });
        }
        if (lEscolhidos[positionNotify] == -9){
            String s = lista.get(position).getId_binary();
            if(s.equals(idBinaryOpcaoEscolhida)){
                btn.setBackgroundResource(R.color.gray_principal);
                colorirPrimeiro = true;
                lEscolhidos[positionNotify] = 1;
                mListNotify.get(positionNotify).setIndexOpcaoEscolhida(position);
            }
        }*/
        return btn;
    }
}
