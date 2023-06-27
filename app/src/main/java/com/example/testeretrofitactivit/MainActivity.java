package com.example.testeretrofitactivit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testeretrofitactivit.Service.BancoService;
import com.example.testeretrofitactivit.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextSenha;
    private Button entrar;
    private Retrofit retrofit;
    private String token = "123";
    private String URL = "http://10.0.0.242/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Aumente o tempo limite para 60 segundos
                .build();

        Gson gson = new GsonBuilder()
                .setLenient() // Habilitar modo leniente para aceitar JSON malformado (se necessário)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        inicializarComponetes();

        entrar.setOnClickListener(view -> {
            if (editTextNome.getText().toString().equals("") || editTextSenha.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Digite em todos os campos", Toast.LENGTH_SHORT).show();
            }else{

                cadastrarCliente();
            }
        });
    }

    public void cadastrarCliente(){
        BancoService service = retrofit.create(BancoService.class);
        String nome = editTextNome.getText().toString();
        String senha = editTextSenha.getText().toString();

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setSenha(senha);

        // Criar um objeto JSON para enviar os dados
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Email", u.getNome());
            jsonBody.put("Senha", u.getSenha());
            jsonBody.put("Authorization", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Converter o objeto JSON em uma string
        String requestBody = jsonBody.toString();

        // Criar a requisição POST com o corpo da requisição
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody);

        /*// Adicionar o cabeçalho de autenticação
        String token = "123";
        String authorizationHeader = "Bearer " + token; // Adicione "Bearer " antes do token
        okhttp3.Headers headers = okhttp3.Headers.of("Authorization", authorizationHeader);*/

        Call<ResponseBody> call = service.inserirUsuario(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Vencemos porraaaaaa!!!!!!!!!!" + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("algumaCoisa", "onResponse:");
                    int statusCode = response.code();

                    if (statusCode == 200){
                        ResponseBody responseBody = response.body();
                        if (responseBody != null){
                            try {
                                // Fazer o parse dos dados JSON utilizando Gson
                                Gson gson = new Gson();

                                Usuario usuario = gson.fromJson(responseBody.charStream(), Usuario.class);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        // ...

                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorJson = errorBody.string();
                            Log.d("Error JSON", errorJson);
                        }

                        // ...
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API request", "Falha na requisição", t);
                Toast.makeText(getApplicationContext(), "Falha na requisição", Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Imprime a pilha de exceções no console

                // Imprimir o JSON de erro
                if (call != null && call.isExecuted() && call.isCanceled()) {
                    Response<ResponseBody> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (response != null && response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            Log.d("Error JSON", errorJson);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    public void inicializarComponetes(){
        entrar = findViewById(R.id.buttonCadastrar);
        editTextNome = findViewById(R.id.editTextNome);
        editTextSenha = findViewById(R.id.editTextSenha);
    }
}


