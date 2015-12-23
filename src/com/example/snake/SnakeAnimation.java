package com.example.snake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.view.ViewGroup.*;

import android.database.sqlite.*;
import android.widget.TextView;
import com.example.graphics.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeAnimation extends Activity {

    protected static final int GUIUPDATEID = 0x1FF;
    AnimRunnable animRunnable;
    GraphicsTestView myGraphicsView;
    int life;
    int level;
    Thread thread;
    int score;
    TextView lifeTextView;


    Handler myGUIUpdateHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SnakeAnimation.GUIUPDATEID:
                    myGraphicsView.invalidate();
                    break;
            }
        }
    };
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        score = 0;
        life = 3;
        level = 1;
        setContentView(R.layout.snake_animation);
        LinearLayout ll = (LinearLayout) findViewById(R.id.middle);
        myGraphicsView = new GraphicsTestView(this);
        ll.addView(myGraphicsView);


        Button btn = (Button) findViewById(R.id.startSnakeGame);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myGraphicsView.turnLeft();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Button rightBtn = (Button) findViewById(R.id.rightbtn);
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myGraphicsView.turnRight();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread = new Thread(new AnimRunnable(this));
        thread.start();

    }

    private static class AnimRunnable implements Runnable {
        SnakeAnimation myAnimation;

        public AnimRunnable(SnakeAnimation myAnim) {
            myAnimation = myAnim;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = SnakeAnimation.GUIUPDATEID;
                myAnimation.myGUIUpdateHandler.sendMessage(message);

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private class GraphicsTestView extends View {
        private ShapeDrawable myDrawable;

        Rect leftTop, top, rightTop, leftBottom,bottom, rightBottom;
        Rect o1, o2, o3, o4, o5, o6, o7, o8, o9, o10;
        Paint paint = new Paint();

        int ovalX;
        int ovalY;
        int directionX;
        int directionY;
        int speed = 5;

        ArrayList<Integer> snakeXCoordinates;
        ArrayList<Integer> snakeYCoordinates;

        ArrayList<Rect> obstacles;
        ArrayList<Rect> boundary;
        ArrayList<Rect> tail;
        public GraphicsTestView(Context context) {
            super(context);
            setFocusable(true);
            

            leftTop = new Rect(0, 0, 30, 290);
            leftBottom = new Rect(0, 330, 30, 640);
            top = new Rect(0, 0, 600, 20);
            rightTop = new Rect(600, 0, 630, 290);
            bottom = new Rect(0, 600, 630, 640);
            rightBottom = new Rect(600, 330, 630, 640);

            o1 = new Rect(400, 110, 400, 490);
            o2 = new Rect(300, 110, 400, 110);
            o3 = new Rect(300, 490, 400, 490);
            o4 = new Rect(400, 300, 460, 300);
            o5 = new Rect(100, 110, 100, 490);
            o6 = new Rect(100, 300, 460, 300);
            o7 = new Rect(460, 150, 460, 380);
            o8 = new Rect(100, 110, 300, 110);
            o9 = new Rect(300, 110, 300, 490);
            o10 = new Rect(300, 490, 469, 490);

            RectShape rectShape = new RectShape();
            myDrawable = new ShapeDrawable(rectShape);
            myDrawable.getPaint().setColor(0xFFFF0000);

            ovalX = 0;
            ovalY = 300;

            directionX = 5;
            directionY = 0;

            snakeXCoordinates = new ArrayList<Integer>();
            snakeYCoordinates = new ArrayList<Integer>();

            obstacles = new ArrayList<Rect>();
            boundary = new ArrayList<Rect>();
            tail = new ArrayList<Rect>();
            boundary.add(leftBottom);
            boundary.add(top);
            boundary.add(leftTop);
            boundary.add(rightBottom);
            boundary.add(rightTop);
            boundary.add(bottom);
        }


        public void setOvalX(int ovalX) {
            this.ovalX = ovalX;
        }

        public void setOvalY(int ovalY) {
            this.ovalY = ovalY;
        }

        public void setDirectionX(int directionX) {
            this.directionX = directionX;
        }

        public void setDirectionY(int directionY) {
            this.directionY = directionY;
        }

        public void clearCanvas() {
            snakeXCoordinates.clear();
            snakeYCoordinates.clear();
            this.setOvalX(0);
            this.setOvalY(300);
            this.setDirectionX(5);
            this.setDirectionY(0);
        }

        public void increaseSpeed() {
            this.speed = this.speed + 10;
        }

        @Override
        protected void onDraw(Canvas canvas) {

            if (ovalX >= 620 && ovalX <= 640 && ovalY >= 300 && ovalY <= 320) {
                Log.d("Reached the other side", "Yay");
                this.clearCanvas();
                SnakeAnimation.this.incrementScore();
            }

            if(ifIntersectedWithObstacles() || ifIntersectedWithBoundary() ){

                SnakeAnimation.this.updateGame();
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(20);

            if(SnakeAnimation.this.level == 1){
                canvas.drawLine(400, 110, 400, 490, paint);
                canvas.drawLine(300, 110, 400, 110, paint);
                canvas.drawLine(300, 490, 400, 490, paint);
                canvas.drawLine(400, 300, 460, 300, paint);

                obstacles.clear();
                obstacles.add(o1);
                obstacles.add(o2);
                obstacles.add(o3);
                obstacles.add(o4);
            }
            if(SnakeAnimation.this.level == 2){
                    canvas.drawLine(100, 110, 100, 490, paint);
                    canvas.drawLine(100, 300, 460, 300, paint);
                    canvas.drawLine(460, 150, 460, 380, paint);

                obstacles.clear();
                obstacles.add(o5);
                obstacles.add(o6);
                obstacles.add(o7);
            }
            if(SnakeAnimation.this.level == 3){
                canvas.drawLine(100, 110, 300, 110, paint);
                canvas.drawLine(300, 110, 300, 490, paint);
                canvas.drawLine(300, 490, 469, 490, paint);

                obstacles.clear();
                obstacles.add(o8);
                obstacles.add(o9);
                obstacles.add(o10);
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawRect(leftTop, paint);
            canvas.drawRect(leftBottom, paint);
            canvas.drawRect(rightTop, paint);
            canvas.drawRect(top, paint);
            canvas.drawRect(bottom, paint);
            canvas.drawRect(rightBottom, paint);


            int width = 20;
            int height = 20;

            snakeXCoordinates.add(ovalX);
            snakeYCoordinates.add(ovalY);
            tail.add(new Rect(ovalX, ovalY, ovalX+width, ovalY+height));

            for (int i = 0; i < snakeXCoordinates.size(); i++) {
                int x = snakeXCoordinates.get(i);
                int y = snakeYCoordinates.get(i);
                myDrawable.setBounds(x, y, x + width, y + height);
                myDrawable.draw(canvas);
            }

            ovalX += directionX;
            ovalY += directionY;
        }

        private boolean checkIfIntersectedWithTail(){
            for(Rect r: tail){
                if(intersects(r, new Rect(myDrawable.getBounds()))){
                    return true;
                }
            }
            return false;
        }

        private boolean ifIntersectedWithObstacles() {
            for(Rect r: obstacles){
                if(myDrawable.getBounds().intersect(r)){
                    return true;
                }
            }
            return false;
        }

        private boolean ifIntersectedWithBoundary() {
            for(Rect r: boundary){
                if(myDrawable.getBounds().intersect(r)){
                    return true;
                }
            }
            return false;
        }

        public boolean intersects(Rect rect1, Rect rect2){

            if((Math.abs(rect1.left - rect2.left)) >20 && (Math.abs(rect1.top - rect2.top)) > 20 && (Math.abs(rect1.right - rect2.right)) > 20 && (Math.abs(rect1.bottom - rect2.bottom)) > 20){
                return true;
            }
            return false;
        }
        public void turnRight() throws InterruptedException {

            if (directionX == speed && directionY == 0) {
                directionX = 0;
                directionY = speed;
            } else if (directionX == 0 && directionY == speed) {
                directionX = -speed;
                directionY = 0;
            } else if (directionX == -speed && directionY == 0) {
                directionX = 0;
                directionY = -speed;
            } else if (directionX == 0 && directionY == -speed) {
                directionX = speed;
                directionY = 0;
            }

        }

        public void turnLeft() throws InterruptedException {
            if (directionX == speed && directionY == 0) {
                directionX = 0;
                directionY = -speed;
            } else if (directionX == 0 && directionY == -speed) {
                directionX = -speed;
                directionY = 0;
            } else if (directionX == -speed && directionY == 0) {
                directionX = 0;
                directionY = speed;
            } else if (directionX == 0 && directionY == speed) {
                directionX = speed;
                directionY = 0;
            }
        }
    }

    private void updateGame() {

        lifeTextView = (TextView) findViewById(R.id.life);

        life--;
        if (this.life <= 0) {

            this.thread.interrupt();
            Log.d("End game", "End Game");
            lifeTextView.setText("Lives: " + life);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Game Over!");
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    alertDialog.dismiss();
                    t.cancel();
                }
            }, 3000);
            Intent myIntent = new Intent(SnakeAnimation.this, MainActivity.class);
            SnakeAnimation.this.startActivity(myIntent);
            db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
            db.execSQL("INSERT INTO HIGH_SCORE VALUES ("+ score+ ")");

        } else {
            myGraphicsView.clearCanvas();
            lifeTextView.setText("Lives: " + life);
        }

    }

    private void saveGameScore() {
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS HIGH_SCORE(SCORE INTEGER);");

        db.execSQL("INSERT INTO HIGH_SCORE VALUES(5)");
    }

    private void incrementScore() {
        score++;
        level++;
        myGraphicsView.increaseSpeed();
        TextView scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText("Score: " + score);
    }
}


