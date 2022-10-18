package mx.com.caminandog;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.facebook.GraphRequest.TAG;

public class Tracking extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String order_id_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);



        Button cancel20 = (Button)findViewById(R.id.btn_20_charge);
        cancel20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences paymnt_json = getSharedPreferences("PREFS",0);
                final SharedPreferences.Editor editor = paymnt_json.edit();
                String json = paymnt_json.getString("jsonPago", "");
                JSONObject jsonPago = new JSONObject();
                try {
                    jsonPago = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (json.isEmpty()) {
                    Intent intent = new Intent(Tracking.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                System.out.println("json envia"+jsonPago);

                try {
                    order_id_str = jsonPago.getString("order_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                OkHttpClient httpClient = new OkHttpClient();
                HttpUrl.Builder httpBuider =
                        HttpUrl.parse("https://us-central1-caminandog-218818.cloudfunctions.net/cancel_with_charge").newBuilder();
                httpBuider.addQueryParameter("text",""+jsonPago);
                final Request req = new Request.Builder().
                        url(httpBuider.build()).build();
                httpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(TAG, "error in getting response from firebase cloud function");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Cound’t get response from cloud function",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        String resp = "";
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "fail response from firebase cloud function");
                            Toast.makeText(getApplicationContext(),
                                    "Cound’t get response from cloud function",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                resp = responseBody.string();
                                System.out.println(resp);


                            } catch (IOException e) {
                                resp = "Problem in getting discount info";
                                Log.e(TAG, "Problem in reading response " + e);
                            }
                        }
                        runOnUiThread(responseRunnable(resp));





                    }

                    private void runOnUiThread(Runnable runnable) {

                        //texttok.setText(""+req);

                    }

                });



                final DatabaseReference paseos_ref1 = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.PASEO_USR_REFERERENCE).child(user.getUid());
                final Query query1 = paseos_ref1;

                query1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String key = dataSnapshot.getKey();

                        if (Integer.parseInt(key)==Integer.parseInt(order_id_str)){
                            Paseo paseo = dataSnapshot.getValue(Paseo.class);
                            String status = paseo.getStatus();
                            if (status.contentEquals("completed")){


                                paseos_ref1.child(key).child("status").setValue("cancelled" );
                                System.out.println((getResources().getString(R.string.Cancelar)));
                                editor.clear().commit();




                            }
                        }

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(Tracking.this, MainActivity.class));

            }
        });






        Button trig = (Button)findViewById(R.id.trigger_paseo);
        trig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences paymnt_json = getSharedPreferences("PREFS",0);
                final SharedPreferences.Editor editor = paymnt_json.edit();
                String json = paymnt_json.getString("jsonPago", "");
                JSONObject jsonPago = new JSONObject();
                try {
                    jsonPago = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (json.isEmpty()) {
                    Intent intent = new Intent(Tracking.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                System.out.println("json envia"+jsonPago);

                try {
                    order_id_str = jsonPago.getString("order_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                OkHttpClient httpClient = new OkHttpClient();
                HttpUrl.Builder httpBuider =
                        HttpUrl.parse("https://us-central1-caminandog-218818.cloudfunctions.net/confirm").newBuilder();
                httpBuider.addQueryParameter("text",""+jsonPago);
                final Request req = new Request.Builder().
                        url(httpBuider.build()).build();
                httpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(TAG, "error in getting response from firebase cloud function");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Cound’t get response from cloud function",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        String resp = "";
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "fail response from firebase cloud function");
                            Toast.makeText(getApplicationContext(),
                                    "Cound’t get response from cloud function",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                resp = responseBody.string();
                                System.out.println(resp);


                            } catch (IOException e) {
                                resp = "Problem in getting discount info";
                                Log.e(TAG, "Problem in reading response " + e);
                            }
                        }
                        runOnUiThread(responseRunnable(resp));





                    }

                    private void runOnUiThread(Runnable runnable) {

                        //texttok.setText(""+req);

                    }

                });



                final DatabaseReference paseos_ref1 = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.PASEO_USR_REFERERENCE).child(user.getUid());
                final Query query1 = paseos_ref1;

                query1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String key = dataSnapshot.getKey();

                        if (Integer.parseInt(key)==Integer.parseInt(order_id_str)){
                            Paseo paseo = dataSnapshot.getValue(Paseo.class);
                            String status = paseo.getStatus();
                            if (status.contentEquals("completed")){



                                System.out.println(getResources().getString(R.string.Pagado));
                                editor.clear().commit();




                            }
                        }

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(Tracking.this, MainActivity.class));
            }
        });
    }

    private Runnable responseRunnable(final String responseStr){
        Runnable resRunnable = new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext()
                        ,responseStr,
                        Toast.LENGTH_SHORT).show();
                System.out.println(responseStr);
            }
        };
        return resRunnable;
    }
}
