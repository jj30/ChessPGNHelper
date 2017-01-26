package bldg5.jj.pgnbase.common;

import android.util.Log;

import java.util.HashMap;

import bldg5.jj.pgnbase.adapters.ApiEndpoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Error {
    public static final String BASE_URL = "http://ec2-54-158-98-180.compute-1.amazonaws.com:8080/log_err/";
    public static final String tag = "Error.java";

    public static void sendError(String strError) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoint service = retrofit.create(ApiEndpoint.class);
        Call<Void> call = service.logError(strError);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                boolean bSuccess = response.isSuccessful();

                HashMap<String, String> hashMap = new HashMap<String, String>() {};
                if (bSuccess) {
                    Log.i(tag, "Logged the error to the web service successfully.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(tag, "Failed to log the error to the web service.");
            }
        });
    }
}
