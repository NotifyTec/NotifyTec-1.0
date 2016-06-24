package com.notifytec.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseManager extends SQLiteOpenHelper{
    private static final String NOME_BANCO = "banco";
    private static final int VERSAO = 5;

    public static final String TABELA_NOTIFICACAO = "NOTIFICACAO";
    public static final String TABELA_NOTIFICACAOOPCAO = "NOTIFICACAOOPCAO";

    public DatabaseManager(Context context){
        super(context, NOME_BANCO, null, VERSAO);
    }


    public class Usuario{
        public static final String TABELA = "USUARIO";
        public static final String FAKEID = "FAKE_ID";
        public static final String ID = "ID";
        public static final String LOGIN = "LOGIN";
        public static final String SENHA = "SENHA";
        public static final String PODEENVIAR = "PODEENVIAR";
        public static final String ALTEROUSENHA = "ALTEROUSENHA";
        public static final String EMAIL = "EMAIL";
        public static final String TOKENRECUPERARSENHA = "TOKENRECUPERARSENHA";
        public static final String TOKEN = "TOKEN";
        public static final String DATAVALIDADETOKEN = "DATAVALIDADETOKEN";
        public static final String GCMTOKEN = "GCMTOKEN";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tabelaUsuario = "CREATE TABLE IF NOT EXISTS USUARIO(\n" +
                "  FAKE_ID PRIMARY KEY NOT NULL," +
                "  ID VARCHAR(36),\n" +
                "  LOGIN VARCHAR(50),\n" +
                "  SENHA VARCHAR(60),\n" +
                "  PODEENVIAR INT,\n" +
                "  ALTEROUSENHA INT,\n" +
                "  EMAIL VARCHAR(100),\n" +
                "  TOKENRECUPERARSENHA VARCHAR(5),\n" +
                "  DATAVALIDADETOKEN DATETIME,\n" +
                "  GCMTOKEN VARCHAR(500)," +
                "  TOKEN VRCHAR(500)," +
                "  VISUALIZOU_EM DATETIME \n" +
                ");";

        String tabelaNotificacao = "CREATE TABLE IF NOT EXISTS NOTIFICACAO(" +
                "  ID VARCHAR(36) PRIMARY KEY NOT NULL," +
                "  TITULO VARCHAR(100)," +
                "  CONTEUDO TEXT," +
                "  EXPIRAEM DATETIME," +
                "  USUARIOID VARCHAR(36)," +
                "  DATAHORAENVIO DATETIME," +
                "  NOME_USUARIO VARCHAR(200)," +
                "  NOME_PERIODO VARCHAR(200)," +
                "  NOME_CURSO VARCHAR(200)," +
                "  TOTAL_ALUNOS_ENVIADOS int," +
                "  TOTAL_ALUNOS_VISUZALIZADOS int," +
                "  TOTAL_RESPONDIDOS int," +
                "  VISUALIZOU_EM DATETIME," +
                "  ENQUETE int NOT NULL" +
                ");";

        String tabelaNotificacaoOpcao = "CREATE TABLE IF NOT EXISTS NOTIFICACAOOPCAO(" +
                "  ID VARCHAR(36) PRIMARY KEY NOT NULL," +
                "  NOTIFICAOID VARCHAR(36)," +
                "  NOME VARCHAR(100)," +
                "  TOTAL_RESPONDIDO int," +
                "  RESPOSTA_ESCOLHIDA bool," +
                "  FOREIGN KEY (NOTIFICAOID) REFERENCES NOTIFICACAO(ID)" +
                ");";


        db.execSQL(tabelaUsuario);
        db.execSQL(tabelaNotificacao);
        db.execSQL(tabelaNotificacaoOpcao);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    public void dropTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS USUARIO");
        db.execSQL("DROP TABLE IF EXISTS NOTIFICACAOOPCAO");
        db.execSQL("DROP TABLE IF EXISTS NOTIFICACAO");
    }

    public void resetDatabase(SQLiteDatabase db){
        db.execSQL("DELETE FROM USUARIO");
        db.execSQL("DELETE FROM NOTIFICACAOOPCAO");
        db.execSQL("DELETE FROM NOTIFICACAO");
    }

    public boolean sucesso(long r){
        return r != -1;
    }

    public String toStringDate(Date date){
        if(date == null)
            return null;

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("yyyy-MM-dd hh:mm:ss", date).toString();
    }

    public Date toDate(String string){
        if(string == null || string.isEmpty())
            return null;

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return sdf.parse(string);
        }catch(Exception ex){
            return null;
        }
    }
}
