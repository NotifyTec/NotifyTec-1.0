package com.notifytec.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.UsuarioService;

/**
 * Cria e gerencia os tokens.
 */
public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(String.valueOf(Log.DEBUG), refreshedToken);

        Resultado<Boolean> r = new UsuarioService(getApplicationContext()).setGcmToken(refreshedToken);
        Log.d(String.valueOf(Log.ERROR), "InstanceIdService.onTokenRefresh() : " + r.getMessage());
    }
}
