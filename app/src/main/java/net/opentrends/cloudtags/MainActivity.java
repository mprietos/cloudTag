package net.opentrends.cloudtags;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.opentrends.cloudtaglibrary.CloudTag;
import net.opentrends.cloudtaglibrary.Tag;

public class MainActivity extends AppCompatActivity {

    private CloudTag mCloudTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCloudTags = (CloudTag)findViewById(R.id.cloudTags);

        mCloudTags.setOnTagDeleteListener(new CloudTag.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int position) {
                // Action to do if you want delete a TAG
            }
        });
        initTags();

    }

    private void initTags() {

        for (int i=0;i<20;i++){

            Tag tag = new Tag(i, "Number tag " + i);
            mCloudTags.add(tag);

        }
        mCloudTags.drawTags();

    }

}
