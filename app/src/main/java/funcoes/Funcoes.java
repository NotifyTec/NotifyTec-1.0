package funcoes;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.UUID;

/**
 * Created by Bruno on 19/05/2016.
 */
public class Funcoes {
    public static boolean conectadoAInternet(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isCPFValid(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;

            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String imprimeCPF(String CPF) {
        return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." +
                CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
    }

    public static String zeroEsquerda(int valor, int tam){
        return String.format("%0" + String.valueOf(tam) + "d", valor);
    }

    public static int booleanToInt (boolean valor){
        if (valor){
            return 1;
        } else {
            return 0;
        }
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static Date StringToDate(String data){
        //Converte uma String de data no formata dd/MM/yyyy hh:mm:ss, para um date
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
        Date dt = null;
        try {
            dt = df.parse (data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static Date StringToDate2(String data){
        //Converte uma String de data no formata dd/MM/yyyy hh:mm:ss, para um date
        DateFormat df = new SimpleDateFormat ("yyyy/MM/dd hh:mm:ss");
        Date dt = null;
        try {
            dt = df.parse (data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String dateTimeToSQL(Date date, boolean pHour){
        DateFormat dateFormat;
        if (pHour){
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        }
        return dateFormat.format(date);
    }

    public static String ajustaDataHoraDisplay(Date date){
        if(date == null)
            return "";

        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(date);
    }

    public static String ajustaDataHoraDisplayResumo(Date date){
        if(date == null)
            return "";

        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        return dateFormat.format(date);
    }

    public static String ajustaDataHoraDisplay(String data){
        if(data == null || data.isEmpty())
            return "";
        return ajustaDataDisplay(data) + " " + ajustaHoraDisplay(data);
    }

    public static String ajustaDataHoraDisplayResumo(String data){
        if(data == null || data.isEmpty())
            return "";
        return ajustaDataDisplay(data).substring(0,5) + " - " + ajustaHoraDisplay(data);
    }

    public static String ajustaDataDisplay(String data){
        if(data == null || data.isEmpty())
            return "";
        String retorno = "";
        retorno = data.substring(8, 10) + "/";
        retorno += data.substring(5, 7) + "/";
        retorno += data.substring(0, 4);
        return retorno;
    }

    public static String ajustaHoraDisplay(String hora){
        return hora.substring(11, 16);
    }

    public static String textToSQL(String dado){
        if (dado == null || dado.equals("")){
            return "NULL";
        } else {
            return "'" + dado + "'";
        }
    }

    public static void msgSimples(Context ctx, String title, String mensagem) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ctx);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(mensagem);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

}
