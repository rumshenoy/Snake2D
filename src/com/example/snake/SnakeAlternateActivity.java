package com.example.snake;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.graphics.R;

/**
 * Created by ramyashenoy on 12/22/15.
 */
public class SnakeAlternateActivity extends Activity {

    private int mDisplayWidth, mDisplayHeight;
    private LinearLayout mFrame;

    private GameView gameView;

    private int level;
    private int score;
    private int lives;


    Rect left, right, top, bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alternate);

        mFrame = (LinearLayout) findViewById(R.id.background);
        gameView = new GameView(getApplicationContext());
        mFrame.addView(gameView);

        initGameData();


        Thread thread = new Thread(new AnimationRunnable(this));
        thread.start();



    }

    private static class AnimationRunnable implements Runnable{
        SnakeAlternateActivity snakeAlternateActivity;

        public AnimationRunnable(SnakeAlternateActivity snakeAlternateActivity) {
            this.snakeAlternateActivity = snakeAlternateActivity;
        }

        @Override
        public void run() {
            try {
                snakeAlternateActivity.gameView.invalidate();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initGameData() {
        score = 0;
        lives = 3;
        level = 0;
    }


    public class GameView extends View{

        public GameView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(20);
            canvas.drawLine(20, 55, 125, 200, paint);
            canvas.restore();
        }
    }
}
