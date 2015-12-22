package com.example.graphics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ramyashenoy on 11/16/14.
 */
public class DatabaseHelper extends Activity {
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        checkScore();
        Button start = (Button) findViewById(R.id.start);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DatabaseHelper.this, SnakeAnimation.class);
                //myIntent.putExtra("key", value); //Optional parameters
                DatabaseHelper.this.startActivity(myIntent);
            }
        });

    }

    private void checkScore() {
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS HIGH_SCORE(SCORE INTEGER);");
        Cursor c=db.rawQuery("SELECT * FROM HIGH_SCORE", null);
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            TextView highScore = (TextView) findViewById(R.id.highScore);
            highScore.setText("High Score: " + c.getString(0));
            Log.d("KL", "score: "+c.getString(0)+"\n");
        }


    }
}
