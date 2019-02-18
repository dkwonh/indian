package cs.kangwon.com.indian;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import cs.kangwon.com.teamsw.R;

/**
 * Created by Administrator on 2015-11-26.
 */
public class Card extends Activity {
    ImageView imgFront;
    ImageView imgBack;
    private int[] cardImg = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6,
            R.drawable.card7, R.drawable.card8, R.drawable.card9, R.drawable.card10,};
    private static final int CHANGE = 0;
    private static final int TURN = 1;
    boolean isBackVisible = true;
    AnimatorSet setRightOut;
    AnimatorSet setLeftIn;
    CardChange cardChange = new CardChange();
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgFront = (ImageView) findViewById(R.id.imgFront);
        imgFront.setImageResource(cardImg[Game.r1-1]);
        Game.yourCardV.setImageResource(cardImg[Game.ycard - 1]);
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flight_right_out);
        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flight_left_in);
        cardChange.start();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        }

    class CardChange extends Thread {
        public void run() {
            try {
                sleep(3000);
                handler.sendEmptyMessage(CHANGE);
                try {
                    sleep(3000);
                    handler.sendEmptyMessage(CHANGE);
                    vibrator.vibrate(1750);
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    //recycleBitmap(imgFront);
                    handler.sendEmptyMessage(TURN);
                    finish();
                } catch (InterruptedException e) {
                    finish();
                }
            } catch (InterruptedException e) {
                finish();
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE:
                    if (!isBackVisible) {
                        setRightOut.setTarget(imgFront);
                        setLeftIn.setTarget(imgBack);
                        setRightOut.start();
                        setLeftIn.start();
                        isBackVisible = true;
                    } else {
                        setRightOut.setTarget(imgBack);
                        setLeftIn.setTarget(imgFront);
                        setRightOut.start();
                        setLeftIn.start();
                        isBackVisible = false;
                    }
                    break;
                case TURN:
                    if (Game.turn == 1)
                        Toast.makeText(getApplicationContext(), "니 차례", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "쟤 차례", Toast.LENGTH_SHORT).show();

            }
        }
    };
}