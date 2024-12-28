package ntutee.team3.JavaFinalProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{

    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        textView = findViewById(R.id.tx);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get_weather).setOnClickListener(v ->
        {
            String URL = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWA-5A6556A3-BD4C-48AC-8F7C-1CDF34213863&format=JSON&locationName=%E8%87%BA%E5%8C%97%E5%B8%82&elementName=&sort=time";
            Request request = new Request.Builder().url(URL).build();
            OkHttpClient okhttpClient = new OkHttpClient();
            okhttpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
                {
                    if (response.code() == 200)
                    {
                        if (response.body() == null) return;
                        Data data = new Gson().fromJson(response.body().string(), Data.class);
                        final StringBuilder displayText = new StringBuilder();

                        // 組裝顯示內容
                        for (int i = 0; i < data.records.location.length; i++) {
                            displayText.append("\n")
                                    .append(data.records.location[i].locationName)
                                    .append("的天氣\n")
                                    .append(data.records.location[i]); // 確保這部分的資料格式正確
                        }
                        // 更新 UI
                        runOnUiThread(() ->
                        {
                            textView.setText(displayText.toString()); // 將組裝好的字串顯示在 TextView
                        });
                    } else if (!response.isSuccessful())
                        Log.e("伺服器錯誤", response.code() + " " + response.message());
                    else
                        Log.e("其他錯誤", response.code() + " " + response.message());
                }
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e)
                {
                    if (e.getMessage() != null)
                        Log.e("查詢失敗", e.getMessage());
                }
            });
        });
    }
}