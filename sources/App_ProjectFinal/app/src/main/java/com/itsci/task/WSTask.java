package com.itsci.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.itsci.it411_asynctask.R;
import com.itsci.model.ResponseModel;


public class WSTask extends AsyncTask {
    public interface WSTaskListener {
        void onComplete(String response);
        void onError(String err);
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private WSTaskListener listener;
    private Context context;

    public WSTask(Context context, WSTaskListener listener) {
        super();
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        RequestBody body = RequestBody.create(JSON, objects[1].toString());
        Request request = new Request.Builder()
                .url(context.getString(R.string.root_url).concat(objects[0].toString()))
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object s) {
        super.onPostExecute(s);
        if (listener == null)
            return;
        if (s == null) {
            listener.onError(context.getString(R.string.global));
            return;
        }
        Log.d("response : ", s.toString());
        Gson gson = new GsonBuilder().create();
        ResponseModel model = gson.fromJson(s.toString(), ResponseModel.class);
        if (!model.isSuccess()) {
            if(model.getResult() == null){
                listener.onError(context.getString(R.string.global));
                return;
            }
            listener.onError(model.getResult().toString());
            return;
        }
        listener.onComplete(s.toString());
    }

}
