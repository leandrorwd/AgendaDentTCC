package tcc.agendadent.servicos;

/**
 * Created by natha on 02/02/2017.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.StrictMode;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.objetos.Endereco;


public class BuscaEnderecoCep {


    private static BuscaEnderecoCep INSTANCE;
    private Endereco endereco;

    public static BuscaEnderecoCep getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BuscaEnderecoCep();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return INSTANCE;
    }

    public void BuscaEndereco(final String cep, final Activity activity) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://viacep.com.br/ws/" + cep + "/json/", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("cep").equals(cep)) {
                        try {
                            endereco = new Endereco(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(activity.getLocalClassName().equals("gui.CadastroGui") ){
                            CadastroController.getInstance().setCep(activity,endereco);
                        }

                    }
                } catch (JSONException e) {
                    endereco = null;
                    if(activity.getLocalClassName().equals("gui.CadastroGui") ){
                        //controllerContatos.SetCep(activity, endereco, loadingDialog);
                    }
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}

