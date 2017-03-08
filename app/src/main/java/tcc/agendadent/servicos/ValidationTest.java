package tcc.agendadent.servicos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

/**
 * Created by natha on 02/02/2017.
 */


public class ValidationTest {

    public static boolean validaString(String texto){
        return !(texto==null || texto.trim().isEmpty());
    }

    public static boolean validaEmail(String email){
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean validaSenhas(String senha, String senhaConfirma) {
        return senha.equals(senhaConfirma);
    }

    public static boolean validaTamanhoSenha(String senha) {
        return senha.length()>5;
    }
    public static boolean validaTelefone(String telefone){
        String aux= telefone.replace(" ","");
        aux = aux.replace("-","");

        if (!aux.matches("[0-9]+")){
            return false;
        }
        return aux.length()>9;
    }

    private static int calcularDigito(String cpf, int[] pesoCPF){
        int soma = 0;
        for (int indice=cpf.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(cpf.substring(indice,indice+1));
            soma += digito*pesoCPF[pesoCPF.length-cpf.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }
    public static boolean validaCpf(String cpfFormatado) {
        String cpf = cpfFormatado.replace("-","");
        cpf = cpf.replace(".","");
        if ((cpf==null) || (cpf.length()!=11)) return false;
        int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
    }

    public static boolean validaCep(String cep){
        if(!validaString(cep)) return false;
        if(cep.contains("-")) return cep.length()==9;
        return cep.length()==8;
    }

    public static boolean verifica3G(Context context) {
        try{
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return m3g.isConnected();
        }catch (Exception e){
            return false;
        }
    }

    public static boolean verificaWifi(Context context) {
        try{
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        }catch (Exception e){
            return false;
        }
    }

    public static boolean verificaInternet(Context context) {
        return verificaWifi(context) || verifica3G(context);
    }

    public static boolean validaNumeroPuro(String numero) {
        try{
            int controle = Integer.parseInt(numero);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean validaFotoTirada(ImageView perfil) {
        if(perfil.getTag()==null || perfil.getTag().equals("default"))
            return false;
        else
            return true;
    }
}
