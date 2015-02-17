package com.aloknath.notetakingapp.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

/**
 * Created by ALOKNATH on 2/17/2015.
 */
public class TaskDisplayActivity extends Activity {

    private DateDataSource dataSource;
    private NotificationManager mNotificationManager;
    NoteItem item = new NoteItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_display);

        dataSource = new DateDataSource(this);
        dataSource.open();



        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        item.setTitle(bundle.getString("title"));
        item.setKey(bundle.getString("key"));

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(bundle.getInt("notificationId"));

        TextView textView = (TextView)findViewById(R.id.titleText);
        textView.setText(bundle.getString("title"));


        textView = (TextView)findViewById(R.id.titleDescription);
        textView.setText(bundle.getString("description"));


        textView = (TextView)findViewById(R.id.titleLocation);
        textView.setText(bundle.getString("location"));

        textView = (TextView)findViewById(R.id.titleTime);
        textView.setText(bundle.getString("time"));

        Button ok = (Button)findViewById(R.id.OK_button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDisplayActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Button taskDone = (Button)findViewById(R.id.task_done_button);
        taskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.removeFromList(item);
                dataSource.close();
                Intent intent = new Intent(TaskDisplayActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
