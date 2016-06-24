package com.notifytec.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.notifytec.MenuPrincipal;
import com.notifytec.R;
import com.notifytec.contratos.AlunoNotificacaoModel;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.NotificacaoOpcaoModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.taks.ConfirmarVisualizadoTask;
import com.notifytec.taks.ResponderNotificacaoTask;

import java.util.Collections;
import java.util.List;
import funcoes.Funcoes;

/**
 * Created by Bruno on 19/05/2016.
 */
public class NotificacoesAdapter extends RecyclerView.Adapter<NotificacoesAdapter.MyViewHolder> {
    private List<NotificacaoCompletaModel> mList;
    private LayoutInflater mLayoutInflater;
    //private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private MenuPrincipal activity;
    private UsuarioModel usuario;
    private NotificacoesAdapter thisAdapter;

    public NotificacoesAdapter(MenuPrincipal activity, List<NotificacaoCompletaModel> l, UsuarioModel usuario) {
        this.activity = activity;
        mList = l;
        this.usuario = usuario;
        mLayoutInflater = activity.getLayoutInflater();
        this.thisAdapter = this;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_list_notificacoes_card, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public void resultadoResposta(Resultado<NotificacaoCompletaModel> listResultado, int position, MyViewHolder myViewHolder){
        if(listResultado.isSucess()){
            mList.set(position, listResultado.getResult());
            this.onBindViewHolder(myViewHolder, position);
        }else{
            activity.mostrarErro(listResultado.getMessage());
        }
    }

    public void resultadoVisualizar(Resultado<NotificacaoCompletaModel> listResultado, int position, MyViewHolder myViewHolder){
        if(listResultado.isSucess()){
            mList.remove(position);
            notifyDataSetChanged();
            Snackbar.make(myViewHolder.itemView, "Notificação movida para Avisos", Snackbar.LENGTH_SHORT).show();
        }else{
            activity.mostrarErro(listResultado.getMessage());
        }
    }

    @Override
    //vincula os dados da nossa lista a view
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        Log.i("LOG", "onBindViewHolder()");
        myViewHolder.tvTitulo.setText(mList.get(position).getTitulo());
        myViewHolder.tvDescricao.setText(mList.get(position).getConteudo());
        myViewHolder.tvUsuario.setText("De: " + mList.get(position).getNomeUsuario());

        myViewHolder.tvExpiraEm.setVisibility(View.GONE);
        myViewHolder.tvDtHrRecebida.setVisibility(View.GONE);

        if(mList.get(position).getDataHoraEnvio() != null){
            myViewHolder.tvDtHrRecebida.setVisibility(View.VISIBLE);
            myViewHolder.tvDtHrRecebida.setText("Enviado em " +Funcoes.ajustaDataHoraDisplayResumo(mList.get(position).getDataHoraEnvio()));
        }

        if(mList.get(position).getExpiraEm() != null){
            myViewHolder.tvExpiraEm.setVisibility(View.VISIBLE);
            myViewHolder.tvExpiraEm.setText("Válido até " + Funcoes.ajustaDataHoraDisplayResumo(mList.get(position).getExpiraEm()));
        }

        myViewHolder.tvCursoPeriodo.setText("Para: " + mList.get(position).getNomePeriodo() + "º " + mList.get(position).getNomeCurso());

        AlunoNotificacaoModel respostaAluno = mList.get(position).getResposta();

        if(usuario.isPodeEnviar()) {
            myViewHolder.linearDetalhes.setVisibility(View.VISIBLE);
            myViewHolder.visualizados.setText(String.valueOf(mList.get(position).getTotalAlunosVisualizados()));
            myViewHolder.enviados.setText(String.valueOf(mList.get(position).getTotalAlunosEnviados()));

            myViewHolder.contentRespondidos.setVisibility(View.GONE);
            if(mList.get(position).getOpcoes() != null && mList.get(position).getOpcoes().size() >  0) {
                myViewHolder.contentRespondidos.setVisibility(View.VISIBLE);
                myViewHolder.respondido.setText(String.valueOf(mList.get(position).getTotalRespondidos()));
            }
        }else{
            myViewHolder.linearDetalhes.setVisibility(View.GONE);

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!usuario.isPodeEnviar()) {
                        new ConfirmarVisualizadoTask(thisAdapter, position,
                                mList.get(position).getId(), activity, myViewHolder).execute();
                    }
                    return true;
                }
            });
        }

        if(mList.get(position).getOpcoes() == null){
            myViewHolder.linearOpcoes.setVisibility(View.GONE);
        } else {
            myViewHolder.linearOpcoes.removeAllViews();
            for(NotificacaoOpcaoModel op : mList.get(position).getOpcoes()){
                final NotificacaoOpcaoModel opFinal = op;

                View view = activity.getLayoutInflater().inflate(R.layout.item_opcao, null);
                Button btn = ((Button)view.findViewById(R.id.item_opcao_botao));

                if(usuario.isPodeEnviar()) {
                    float porcentagem = 0;
                    if(mList.get(position).getTotalRespondidos() > 0)
                        porcentagem = (op.getTotalRespondidos() /
                                        mList.get(position).getTotalRespondidos()) * 100;

                    btn.setText(op.getNome() + " (" + op.getTotalRespondidos() + " - " + porcentagem + "%)");
                    btn.setEnabled(false);
                }
                else{
                    btn.setText(op.getNome());

                    // NOTE: Aluno já respondeu???
                    if(respostaAluno != null && respostaAluno.getNotificacaoOpcao() != null){
                        if(respostaAluno.getNotificacaoOpcao().equals(op.getId())){
                            btn.setBackgroundResource(R.color.colorAccent);
                            btn.setTextColor(Color.parseColor("#FFFFFF"));
                        }

                        btn.setEnabled(false);
                    }else{
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new ResponderNotificacaoTask(thisAdapter, position, activity, opFinal.getNotificacaoID(), opFinal.getId(), myViewHolder).execute();
                            }
                        });
                    }
                }

                myViewHolder.linearOpcoes.addView(view);
            };
        }
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private UsuarioModel usuario;

        public TextView tvDtHrRecebida;
        public TextView tvTitulo;
        public TextView tvDescricao;
        public TextView tvUsuario;
        public TextView tvCursoPeriodo;
        public TextView tvExpiraEm;
        public LinearLayout linearDetalhes;
        public LinearLayout linearOpcoes;
        public TextView visualizados;
        public TextView respondido;
        public TextView enviados;
        public LinearLayout contentRespondidos;

        //itemView é o item root. É cada "linha" do RecyclerView
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDtHrRecebida = (TextView) itemView.findViewById(R.id.tvDtHrRecebida);
            tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            tvDescricao = (TextView) itemView.findViewById(R.id.tvDescricao);
            tvUsuario = (TextView) itemView.findViewById(R.id.tvUsuario);
            tvCursoPeriodo = (TextView) itemView.findViewById(R.id.tvCursoPeriodo);
            tvExpiraEm = (TextView) itemView.findViewById(R.id.tvExpiraEm);
            linearOpcoes = (LinearLayout) itemView.findViewById(R.id.linear_opcoes);
            linearDetalhes =(LinearLayout) itemView.findViewById(R.id.item_opcao_detalhes);
            respondido = (TextView) itemView.findViewById(R.id.item_opcao_respondidos);
            visualizados = (TextView) itemView.findViewById(R.id.item_opcao_visualizados);
            enviados = (TextView) itemView.findViewById(R.id.item_opcao_enviados);
            contentRespondidos = (LinearLayout) itemView.findViewById(R.id.item_opcao_content_respondidos);
        }
    }
}