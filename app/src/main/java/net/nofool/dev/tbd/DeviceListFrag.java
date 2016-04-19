package net.nofool.dev.tbd;


import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceListFrag extends ListFragment{

    Context context = getActivity();
    final String PREFS="dev.nofool.net.tbd";
    private String TAG = DeviceListFrag.class.getSimpleName();
    ArrayList<Device> deviceArrayList = new ArrayList<Device>();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDevices(Devices.getId());
        for (Device w : deviceArrayList){
            Log.v(TAG, "getLocation called for:"+w.getID());
            getLocation(w.getID());
        }

    }

    private void getDevices(int id){
        final String url = "http://dev.nofool.net/app/getDevices.php";
        final String idS = ""+id;
        final RequestBody body = new FormBody.Builder().add("id", idS).build();
        Request request = new Request.Builder().url(url).get().post(body).build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray dArray = jsonObject.getJSONArray("devices");
                        ArrayList<Device> deviceList = new ArrayList<Device>();
                        for (int i = 0; i < dArray.length(); i++) {
                            JSONObject d = dArray.getJSONObject(i);
                            String name = d.getString("name");
                            String key = d.getString("key");
                            Log.v(TAG, i + " " + name + " " + key);
                            deviceList.add(new Device(name, key));
                        }
                        SetFrag(deviceList);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception", e);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception", e);
                }
            }
        });
    }

    private void SetFrag(ArrayList<Device> dList){
        deviceArrayList = dList;
        String[] names = new String[dList.size()];
        for (int i = 0; i<dList.size(); i++){
            Device d = dList.get(i);
            names[i]=d.getName();
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, names);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListAdapter(adapter);
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Device device = deviceArrayList.get(position);
        Intent i = new Intent(getActivity(),MoInfo.class);
        i.putExtra("name",device.getName());
        i.putExtra("google",device.getGcmID());
        startActivity(i);
    }

    public void getLocation(String id){
        SharedPreferences s = getActivity().getSharedPreferences(PREFS, 0);
        String perp=s.getString("thisGCM",null);
        String url = "http://dev.nofool.net/app/gcm.php";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("mess", "loc")
                .add("targetKey",id)
                .add("senderKey", perp)
                .build();
        Request request = new Request.Builder().url(url).get().post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String jsonData = response.body().string();
                    Log.v(TAG, "getLoc" + jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String o = jsonObject.getString("message");
                        if (o.equalsIgnoreCase("TRUE")) {
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
    }
}
