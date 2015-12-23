package com.example.snake;

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
import com.example.graphics.R;

/**
 * Created by ramyashenoy on 11/16/14.
 */
public class MainActivity extends Activity {
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        checkScore();
        Button start = (Button) findViewById(R.id.startSnake);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent snakeIntent = new Intent(MainActivity.this, SnakeAnimation.class);
                MainActivity.this.startActivity(snakeIntent);
            }
        });

        Button alternate = (Button) findViewById(R.id.startAlternate);


        alternate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alternateIntent = new Intent(MainActivity.this, SnakeAlternateActivity.class);
                MainActivity.this.startActivity(alternateIntent);
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
