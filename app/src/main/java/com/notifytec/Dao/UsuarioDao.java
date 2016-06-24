package com.notifytec.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;

import java.util.UUID;

public class UsuarioDao {
    private DatabaseManager manager;
    private SQLiteDatabase db;

    public UsuarioDao(Context context){
        this.manager = new DatabaseManager(context);
    }

    /*"CREATE TABLE USUARIO ( ID int primary key, TOKEN varchar(500),  GCM_TOKEN varchar(500), NOME VARCHAR(200))"*/

    public boolean logout(){
        try {
            db = manager.getWritableDatabase();
            manager.resetDatabase(db);
            db.close();
            return true;
        }catch(Exception ex) {
            return false;
        }
    }

    public boolean update(UsuarioModel u){
        try {
            UsuarioModel t = get();
            if (t == null)
                return false;

            ContentValues v = getContentValues(u);

            db = manager.getWritableDatabase();
            long resultado = db.update(DatabaseManager.Usuario.TABELA, v,
                    DatabaseManager.Usuario.FAKEID + "=1", null);
            db.close();

            return manager.sucesso(resultado);
        }catch(Exception ex){
                return false;
            }
    }

    private ContentValues getContentValues(UsuarioModel u){
        ContentValues c = new ContentValues();
        c.put(DatabaseManager.Usuario.FAKEID, 1);

        if(u.isAlterouSenha() != null)
            c.put(DatabaseManager.Usuario.ALTEROUSENHA, u.isAlterouSenha() ? 1 : 0);

        c.put(DatabaseManager.Usuario.DATAVALIDADETOKEN,
                    u.getDataValidadeToken() != null ? manager.toStringDate(u.getDataValidadeToken()) : null);
        if(u.getEmail() != null && !u.getEmail().isEmpty())
            c.put(DatabaseManager.Usuario.EMAIL, u.getEmail());
        if(u.getGcmToken() != null && !u.getGcmToken().isEmpty())
            c.put(DatabaseManager.Usuario.GCMTOKEN, u.getGcmToken());
        if(u.getId() != null)
            c.put(DatabaseManager.Usuario.ID, u.getId().toString());
        if(u.getLogin() != null)
            c.put(DatabaseManager.Usuario.LOGIN, u.getLogin());
        if(u.isPodeEnviar() != null)
            c.put(DatabaseManager.Usuario.PODEENVIAR, u.isPodeEnviar() == true ? 1 : 0);
        if(u.getTokenRecuperarSenha() != null)
            c.put(DatabaseManager.Usuario.TOKENRECUPERARSENHA, u.getTokenRecuperarSenha());
        if(u.getToken() != null)
            c.put(DatabaseManager.Usuario.TOKEN, u.getToken());

        return c;
    }

    public boolean inserir(UsuarioModel u){
        try {
            UsuarioModel t = get();
            if (t != null)
                return update(u);

            ContentValues v = getContentValues(u);

            db = manager.getWritableDatabase();
            long resultado = db.insert(DatabaseManager.Usuario.TABELA, null, v);
            db.close();

            return manager.sucesso(resultado);
        }catch(Exception ex){
            return false;
        }
    }

    public UsuarioModel get(){
        db = manager.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM USUARIO", null);

        if(c.moveToNext()){
            UsuarioModel t = new UsuarioModel();
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.GCMTOKEN)))
                t.setGcmToken(c.getString(c.getColumnIndex(DatabaseManager.Usuario.GCMTOKEN)));
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.ALTEROUSENHA)))
                t.setAlterouSenha(c.getInt(c.getColumnIndex(DatabaseManager.Usuario.ALTEROUSENHA)) == 1 ? true : false);
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.DATAVALIDADETOKEN)))
                t.setDataValidadeToken(manager.toDate(c.getString(c.getColumnIndex(DatabaseManager.Usuario.DATAVALIDADETOKEN))));
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.EMAIL)))
                t.setEmail(c.getString(c.getColumnIndex(DatabaseManager.Usuario.EMAIL)));
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.ID)))
                t.setId(UUID.fromString(c.getString(c.getColumnIndex(DatabaseManager.Usuario.ID))));
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.LOGIN)))
                t.setLogin(c.getString(c.getColumnIndex(DatabaseManager.Usuario.LOGIN)));
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.PODEENVIAR)))
                t.setPodeEnviar(c.getInt(c.getColumnIndex(DatabaseManager.Usuario.PODEENVIAR)) == 1 ? true : false);
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.TOKENRECUPERARSENHA)))
                t.setTokenRecuperarSenha(c.getString(c.getColumnIndex(DatabaseManager.Usuario.TOKENRECUPERARSENHA)));
            if(!c.isNull(c.getColumnIndex(DatabaseManager.Usuario.TOKEN)))
                t.setToken(c.getString(c.getColumnIndex(DatabaseManager.Usuario.TOKEN)));

            db.close();
            return t;
        }else{
            db.close();
            return null;
        }
    }
}
