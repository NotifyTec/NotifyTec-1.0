package com.notifytec.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.notifytec.contratos.AlunoNotificacaoModel;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.NotificacaoOpcaoModel;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificacaoDao {
    private DatabaseManager manager;
    private SQLiteDatabase db;
    private UsuarioDao usuarioDao;

    public NotificacaoDao(Context context){
        manager = new DatabaseManager(context);
        usuarioDao = new UsuarioDao(context);
    }

    public List<NotificacaoCompletaModel> get(Boolean aviso){
        UsuarioModel t = usuarioDao.get();
        if(t == null) return null;

        db = manager.getReadableDatabase();

        String sql = "";

        if(aviso == null)
            sql = "SELECT * FROM NOTIFICACAO WHERE VISUALIZOU_EM IS NULL ORDER BY DATAHORAENVIO DESC";
        else
            sql = "SELECT * FROM NOTIFICACAO WHERE ENQUETE = " + (aviso ? "0" : "1") + " ORDER BY DATAHORAENVIO DESC";

        Cursor c = db.rawQuery(sql, new String[]{ });

        List<NotificacaoCompletaModel> list = new ArrayList<>();

        while(c.moveToNext()){
            NotificacaoCompletaModel n = new NotificacaoCompletaModel();
            n.setId(UUID.fromString(c.getString(c.getColumnIndex("ID"))));
            n.setTitulo(c.getString(c.getColumnIndex("TITULO")));
            n.setConteudo(c.getString(c.getColumnIndex("CONTEUDO")));
            n.setExpiraEm(manager.toDate(c.getString(c.getColumnIndex("EXPIRAEM"))));
            n.setUsuarioID(UUID.fromString(c.getString(c.getColumnIndex("USUARIOID"))));
            n.setDataHoraEnvio(manager.toDate(c.getString(c.getColumnIndex("DATAHORAENVIO"))));
            n.setNomeUsuario(c.getString(c.getColumnIndex("NOME_USUARIO")));
            n.setNomeCurso(c.getString(c.getColumnIndex("NOME_CURSO")));
            n.setNomePeriodo(c.getString(c.getColumnIndex("NOME_PERIODO")));
            n.setTotalAlunosEnviados(c.getInt(c.getColumnIndex("TOTAL_ALUNOS_ENVIADOS")));
            n.setTotalAlunosVisualizados(c.getInt(c.getColumnIndex("TOTAL_ALUNOS_VISUZALIZADOS")));
            n.setTotalRespondidos(c.getInt(c.getColumnIndex("TOTAL_RESPONDIDOS")));
            if(c.getString(c.getColumnIndex("VISUALIZOU_EM")) != null)
                n.setVisualizouEm(manager.toDate(c.getString(c.getColumnIndex("VISUALIZOU_EM"))));

            Cursor co = db.rawQuery("SELECT * FROM NOTIFICACAOOPCAO WHERE NOTIFICAOID = ?", new String[] { n.getId().toString() });
            List<NotificacaoOpcaoModel> ops = new ArrayList<>();
            while(co.moveToNext()){
                NotificacaoOpcaoModel o = new NotificacaoOpcaoModel();
                o.setId(UUID.fromString(co.getString(co.getColumnIndex("ID"))));
                o.setNotificacaoID(UUID.fromString(co.getString(co.getColumnIndex("NOTIFICAOID"))));
                o.setNome(co.getString(co.getColumnIndex("NOME")));
                o.setTotalRespondidos(co.getInt(co.getColumnIndex("TOTAL_RESPONDIDO")));

                boolean escolhida = Boolean.valueOf(co.getString(co.getColumnIndex("RESPOSTA_ESCOLHIDA")));

                if(escolhida){
                    AlunoNotificacaoModel an = new AlunoNotificacaoModel();
                    an.setVisualizouEm(manager.toDate(c.getString(c.getColumnIndex("VISUALIZOU_EM"))));
                    an.setNotificacaoID(n.getId());
                    an.setNotificacaoOpcao(o.getId());

                    n.setResposta(an);
                }

                ops.add(o);
            }
            n.setOpcoes(ops);
            list.add(n);
        }

        db.close();

        return list;
    }

    public boolean update(NotificacaoCompletaModel n){
        ContentValues v = getContentValuesForNotificacao(n);

        db = manager.getWritableDatabase();
        long resultado = db.update(DatabaseManager.TABELA_NOTIFICACAO, v, "ID='" + n.getId().toString() + "'", null);

        if(n.getOpcoes() != null)
            for(NotificacaoOpcaoModel op : n.getOpcoes()){
                ContentValues o = getContentValuesForNotificacaoOpcao(n, op);
                resultado = db.update(DatabaseManager.TABELA_NOTIFICACAOOPCAO, o, "ID='" + op.getId().toString() + "'", null);
            }
        db.close();
        return manager.sucesso(resultado);
    }

    public ContentValues getContentValuesForNotificacao(NotificacaoCompletaModel n){
        ContentValues v = new ContentValues();
        v.put("ID", n.getId().toString());
        v.put("TITULO", n.getTitulo());
        v.put("CONTEUDO", n.getConteudo());
        if(n.getExpiraEm() != null)
            v.put("EXPIRAEM", manager.toStringDate(n.getExpiraEm()));
        v.put("USUARIOID", n.getUsuarioID().toString());
        if(n.getDataHoraEnvio() != null)
            v.put("DATAHORAENVIO", manager.toStringDate(n.getDataHoraEnvio()));
        v.put("NOME_USUARIO", n.getNomeUsuario());
        v.put("NOME_CURSO", n.getNomeCurso());
        v.put("NOME_PERIODO", n.getNomePeriodo());
        v.put("TOTAL_ALUNOS_ENVIADOS", n.getTotalAlunosEnviados());
        v.put("TOTAL_ALUNOS_VISUZALIZADOS", n.getTotalAlunosVisualizados());
        v.put("TOTAL_RESPONDIDOS", n.getTotalRespondidos());
        v.put("ENQUETE", (n.getOpcoes() != null && n.getOpcoes().size() > 0) ? 1 : 0);

        if(n.getResposta() != null && n.getResposta().getVisualizouEm() != null)
            v.put("VISUALIZOU_EM", manager.toStringDate(n.getResposta().getVisualizouEm()));

        return v;
    }

    public ContentValues getContentValuesForNotificacaoOpcao(NotificacaoCompletaModel n, NotificacaoOpcaoModel o) {
        ContentValues ov = new ContentValues();
        ov.put("ID", o.getId().toString());
        ov.put("NOTIFICAOID", o.getNotificacaoID().toString());
        ov.put("NOME", o.getNome());
        ov.put("TOTAL_RESPONDIDO", o.getTotalRespondidos());
        if(n.getResposta() != null && n.getResposta().getNotificacaoOpcao() != null)
            ov.put("RESPOSTA_ESCOLHIDA", "true");
        else
            ov.put("RESPOSTA_ESCOLHIDA", "false");

            return ov;
        }

    public boolean add(List<NotificacaoCompletaModel> list){
        if(list == null || list.size() == 0) return true;

        for(NotificacaoCompletaModel n : list){
            db = manager.getReadableDatabase();
            Cursor cExiste = db.rawQuery("SELECT COUNT(*) FROM NOTIFICACAO WHERE ID = ?", new String[]{ n.getId().toString() });

            try {
                if (cExiste.moveToFirst()) {
                    if (cExiste.getInt(0) != 0) {
                        db.close();
                        update(n);
                        continue;
                    }
                }
            }catch(Exception ex){
                return false;
            }

            ContentValues v = getContentValuesForNotificacao(n);

            db = manager.getWritableDatabase();
            long resultado = db.insert(DatabaseManager.TABELA_NOTIFICACAO, null, v);
            db.close();
            if(!manager.sucesso(resultado))
                return false;

            if(n.getOpcoes() != null)
                for(NotificacaoOpcaoModel o : n.getOpcoes()){
                    ContentValues ov = getContentValuesForNotificacaoOpcao(n, o);
                    db = manager.getWritableDatabase();
                    db.insert(DatabaseManager.TABELA_NOTIFICACAOOPCAO, null, ov);
                    db.close();
                }
        }

        return true;
    }


}
