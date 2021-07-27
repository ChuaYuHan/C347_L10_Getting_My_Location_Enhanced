package sg.edu.rp.c346.id19020844.gettingmylocationenhanced;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class AnotherActivity extends AppCompatActivity {

    Button btnRefresh, btnFavorites;
    TextView tvRecords;
    ListView lv;
    ArrayList alRecords;
    ArrayAdapter aaRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnFavorites = findViewById(R.id.btnFavorites);
        tvRecords = findViewById(R.id.tvRecords);
        lv = findViewById(R.id.listView);

        alRecords = new ArrayList();
        aaRecords = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alRecords);
        lv.setAdapter(aaRecords);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderLocation = getFilesDir().getAbsolutePath() + "/MyFolder";
                File targetFile = new File(folderLocation, "data.txt");

                if (targetFile.exists() == true) {
                    String data = "";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);

                        String line = br.readLine();
                        while (line != null) {
                            data += line + "\n";
                            line = br.readLine();
                        }
                        alRecords.add(data);
                        tvRecords.setText("Number of records: " + alRecords.size());
                        aaRecords.add(alRecords);
                        lv.setAdapter(aaRecords);

                        aaRecords.notifyDataSetChanged();

                        br.close();
                        reader.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}