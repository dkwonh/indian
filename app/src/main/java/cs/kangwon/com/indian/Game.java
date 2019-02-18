package cs.kangwon.com.indian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cs.kangwon.com.teamsw.R;


public class Game extends Activity {
    public static Context mContext;
    protected static Connect mConnect = null;
    protected static TextView textView;
    protected EditText editText = null;
    protected static int turn = 1;
    protected static int mychip = 20;//내 칩수
    protected static int yourchip = 20;//상대방 칩수
    protected static int totalchip = 0;//베팅된 칩 수
    protected static int bettingChip = 1;//내가 낸 총 칩수
    protected static int enemychip = 1;//상대방이 낸 총 칩수
    boolean r = false;
    protected int chip;
    protected static int r1 = 1;
    protected static int[] r2 = new int[11];
    protected static int ycard = 0;
    protected static int round = 0;
    private String c = "chip";
    private String call = "call";
    private String dead = "dead";
    private int cardIndex[] = new int[10];
    protected static String yourCard[] = new String[10];
    protected boolean dr = false;
    protected int wait = 0;
    byte[] send = new byte[1024];
    protected String let = null;
    protected static int card = 0;
    String cardString = "end";
    protected int chip_1 = 0;
    protected int chip_10 = 0;
    protected int chip_1Y = 0;
    protected int chip_10Y = 0;
    protected ImageView c1;
    protected ImageView c10;
    protected ImageView lose;
    protected ImageView win;
    protected ImageView Mchip;
    protected ImageView x;
    protected ImageView draw;
    protected ImageView c1Y;
    protected ImageView c10Y;
    protected ImageView xy;
    protected ImageView Ychip;

    protected ImageView mbc1;
    protected ImageView mbc10;
    protected ImageView mbchip;
    protected ImageView mbx;
    protected ImageView ybc1;
    protected ImageView ybc10;
    protected ImageView ybchip;
    protected ImageView ybx;
    protected ImageView MyCardView;

    protected static ImageView WhatDo;
    protected ImageView round_V;
    int ybc_1=0;
    int ybc_10=0;
    int mbc_1=0;
    int mbc_10=0;
    long backKeyPressedTime = 0;

    protected static ImageView yourCardV;
    private static final int CARD = 0;
    private static final int RESULT = 1;
    private static final int CHIPSET = 2;
    private static final int CHIPSETY = 3;
    private static final int YBCHIPSET = 4;
    private static final int MBCHIPSET = 5;
    private static final int ROUNDSET = 6;

    private int[] chip_1img = {R.drawable.num1_0, R.drawable.num1_1, R.drawable.num1_2, R.drawable.num1_3,
            R.drawable.num1_4, R.drawable.num1_5, R.drawable.num1_6, R.drawable.num1_7,
            R.drawable.num1_8, R.drawable.num1_9,};
    private int[] chip_10img = {R.drawable.num1_0, R.drawable.num1_1, R.drawable.num1_2, R.drawable.num1_3,
            R.drawable.num1_4};
    private int[] chip_1imgY = {R.drawable.num2_0, R.drawable.num2_1, R.drawable.num2_2, R.drawable.num2_3, R.drawable.num2_4,
            R.drawable.num2_5, R.drawable.num2_6, R.drawable.num2_7, R.drawable.num2_8, R.drawable.num2_9};
    private int[] chip_10imgY = {R.drawable.num2_0, R.drawable.num2_1, R.drawable.num2_2, R.drawable.num2_3, R.drawable.num2_4};
    private int[] cardImg = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6,
            R.drawable.card7, R.drawable.card8, R.drawable.card9, R.drawable.card10,};
    private int[] round_view = {R.drawable.r1,R.drawable.r2,R.drawable.r3,R.drawable.r4,R.drawable.r5,R.drawable.r6,R.drawable.r7,R.drawable.r8,
            R.drawable.r9,R.drawable.r10};
    private ImgThread Img = new ImgThread();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameroom);
        mContext = this;
        c1 = (ImageView) findViewById(R.id.c1);
        c10 = (ImageView) findViewById(R.id.c10);
        lose = (ImageView) findViewById(R.id.loseView);
        win = (ImageView) findViewById(R.id.winView);
        Mchip = (ImageView) findViewById(R.id.myChip);
        x = (ImageView) findViewById(R.id.x);
        c1Y = (ImageView) findViewById(R.id.c1y);
        c10Y = (ImageView) findViewById(R.id.c10y);
        Ychip = (ImageView) findViewById(R.id.yourchip);
        xy = (ImageView) findViewById(R.id.xy);
        draw = (ImageView) findViewById(R.id.drawView);
        yourCardV = (ImageView)findViewById(R.id.yourCard);
        mbc1 = (ImageView)findViewById(R.id.mbc1);
        mbc10 = (ImageView)findViewById(R.id.mbc10);
        mbchip = (ImageView)findViewById(R.id.MyBetting);
        mbx = (ImageView)findViewById(R.id.mbx);
        ybc1 = (ImageView)findViewById(R.id.ybc1);
        ybc10 = (ImageView)findViewById(R.id.ybc10);
        ybchip = (ImageView)findViewById(R.id.YouBetting);
        ybx = (ImageView)findViewById(R.id.ybx);
        MyCardView = (ImageView)findViewById(R.id.Mycard);
        WhatDo = (ImageView)findViewById(R.id.turn);
        round_V = (ImageView)findViewById(R.id.Round_View);
        ResultView.items = new ArrayList<Result>();

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        Button morebutton = (Button) findViewById(R.id.moreB);
        Button callbutton = (Button) findViewById(R.id.callB);
        Button diebutton = (Button) findViewById(R.id.dieB);
        cardMake();
        gstart();
        Img.start();
        morebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (turn == 1) {
                    String moreChip = editText.getText().toString();
                    if (moreChip.equals("") || moreChip.equals("0")) {
                        Toast.makeText(getApplicationContext(), "값을입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        if (yourchip == 0) {
                            Toast.makeText(getApplicationContext(), "자동 콜.", Toast.LENGTH_LONG).show();
                            chip = enemychip - bettingChip;
                            bchip(bettingChip + chip);
                            mchip(mychip - chip);
                            tchip(totalchip + chip);
                            textView.setText(Integer.toString(totalchip));
                            WhatDo.setVisibility(View.INVISIBLE);
                            new Thread() {
                                public void run() {
                                    sendMessage(call);
                                }
                            }.start();
                            call();
                        } else {
                            chip = Integer.parseInt(moreChip) + (enemychip - bettingChip);
                            if (mychip - chip < 0)
                                Toast.makeText(getApplicationContext(), "칩이부족", Toast.LENGTH_SHORT).show();
                            else if (yourchip < Integer.parseInt(moreChip)) {
                                Toast.makeText(getApplicationContext(), "상대칩 부족 재배팅", Toast.LENGTH_LONG).show();
                            } else {
                                bettingChip = bettingChip + chip;
                                mychip = mychip - chip;
                                totalchip = totalchip + chip;
                                let = Integer.toString(bettingChip);
                                textView.setText(Integer.toString(totalchip));
                                new Thread() {
                                    public void run() {
                                        sendMessage(c + let);
                                    }
                                }.start();
                                WhatDo.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                } else
                    Toast.makeText(

                            getApplicationContext(),

                            "상대방 차례", Toast.LENGTH_SHORT).

                            show();

                if (mychip == 0)
                    Toast.makeText(

                            getApplicationContext(),

                            "올인", Toast.LENGTH_SHORT).

                            show();
                editText.setText("");

            }

        });

        callbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (turn == 1) {
                        if (bettingChip == 1 && enemychip == 1)
                            Toast.makeText(getApplicationContext(), "첫턴 콜 불가", Toast.LENGTH_SHORT).show();
                        else {
                            chip = (enemychip - bettingChip);
                            bchip(chip + bettingChip);
                        mchip(mychip - chip);
                        tchip(totalchip + chip);
                        textView.setText(Integer.toString(totalchip));
                        new Thread() {
                            public void run() {
                                sendMessage(call);
                            }
                        }.start();
                        call();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "상대방 차례", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        });

        diebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (turn == 1) {
                    new Thread() {
                        public void run() {
                            sendMessage(dead);
                        }
                    }.start();
                } else
                    Toast.makeText(getApplicationContext(), "상대방 차례", Toast.LENGTH_SHORT).show();
                editText.setText("");


            }
        });

    }

    protected void cardMake() {
        for (int i = 0; i < cardIndex.length; i++) {
            cardIndex[i] = (int) (Math.random() * 10) + 1;
            for (int j = 0; j < i; j++) {
                if (cardIndex[i] == cardIndex[j]) {
                    i = i - 1;
                    break;
                }
            }

        }
        for (int i = 0; i < cardIndex.length; i++)
            cardString = cardIndex[i] + "x" + cardString;
        new Thread() {
            public void run() {
                sendMessage(cardString);
            }
        }.start();
    }

    protected void gstart() {
        round++;
        //r1 = 0;
        if (round <= 10 && mychip > 0 && yourchip > 0) {
            r1 = cardIndex[card];
            if (round > 1)
                ycard = r2[card + 1];
            round_V.setImageResource(round_view[round-1]);
            r = false;
            new Thread() {
                public void run() {
                    try {
                        sleep(2300);
                        roundSet();
                        sleep(1500);
                        roundSet();
                        cardSet();
                    } catch (InterruptedException e) {
                        finish();
                    }
                }
            }.start();
            card++;
            mchip(mychip - 1);
            ychip(yourchip - 1);
            if (wait > 0) {
                tchip(2 + wait);
                Toast.makeText(getApplicationContext(), "전판 배팅금 : " + wait, Toast.LENGTH_SHORT);
                wait = 0;
            } else {
                tchip(2);
            }
            echip(1);
            bchip(1);
            if (round == 1)
                new Thread() {
                    public void run() {
                        while (true)
                            if (ycard != 0) {
                                handler.sendEmptyMessage(CARD);
                                break;
                            }
                    }
                }.start();
            textView.setText(Integer.toString(totalchip));
        }
        else if(dr) {
            result("call");
            dr=false;
        }
        else
        {
            if(mychip>yourchip)
                ResultView.items.add(new Result("", "FINAL", "", "", "YOU WIN"));
            else if(mychip<yourchip)
                ResultView.items.add(new Result("", "FINAL", "", "", "YOU LOSE"));
            else
                ResultView.items.add(new Result("", "FINAL", "", "", "DRAW"));

            Intent intent = new Intent(Game.this,ResultView.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "게임종료", Toast.LENGTH_SHORT).show();
            mchip(20);//내 칩수
            ychip(20);//상대방 칩수
            tchip(0);//베팅된 칩 수
            bchip(1);//내가 낸 총 칩수
            echip(1);//상대방이 낸 총 칩수
            round = 0;
            turn = 1;
            wait = 0;
            card = 0;
            ycard = 0;
            r1 = 0;
            finish();
        }

    }

    protected void call() {
        result("call");
        gstart();
    }

    protected void die() {
        result("die");
    }

    protected void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mConnect.getState() != Connect.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothfeatureService to write
            send = message.getBytes();
            mConnect.write(send);
        }
    }

    public static synchronized void mchip(int chip) {
        mychip = chip;
    }

    public static synchronized void ychip(int chip) {
        yourchip = chip;
    }

    public static synchronized void tchip(int chip) {
        totalchip = chip;
    }

    public static synchronized void bchip(int chip) {
        bettingChip = chip;
    }

    public static synchronized void echip(int chip) {
        enemychip = chip;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CARD:
                    break;
                case RESULT:
                    win.setVisibility(View.INVISIBLE);
                    lose.setVisibility(View.INVISIBLE);
                    draw.setVisibility(View.INVISIBLE);
                    WhatDo.setVisibility(View.INVISIBLE);
                    MyCardView.setImageResource(R.drawable.card_back);
                    break;
                case CHIPSET:
                    c1.setImageResource(chip_1img[chip_1]);
                    c10.setImageResource(chip_10img[chip_10]);
                    break;
                case CHIPSETY:
                    c1Y.setImageResource(chip_1imgY[chip_1Y]);
                    c10Y.setImageResource(chip_10imgY[chip_10Y]);
                    break;
                case YBCHIPSET:
                    ybc1.setImageResource(chip_1imgY[ybc_1]);
                    ybc10.setImageResource(chip_10imgY[ybc_10]);
                    break;
                case MBCHIPSET:
                    mbc1.setImageResource(chip_1img[mbc_1]);
                    mbc10.setImageResource(chip_10img[mbc_10]);
                    break;
                case ROUNDSET:
                    if(r==false) {
                        round_V.setVisibility(View.VISIBLE);
                        r = true;
                    }
                    else
                        round_V.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void result(String msg) {
        if (msg.equals("call")) {
            if (r1 > ycard) {
                win.bringToFront();
                win.setVisibility(View.VISIBLE);
                MyCardView.setImageResource(cardImg[r1 - 1]);
                mchip(mychip + totalchip);
                ResultView.items.add(new Result(Integer.toString(round), "WIN", Integer.toString(r1), Integer.toString(ycard), Integer.toString(totalchip)));
                turn = 1;
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            handler.sendEmptyMessage(RESULT);
                        } catch (InterruptedException e) {
                        } finally {
                        }
                    }
                }.start();
            } else if (r1 < ycard) {
                lose.bringToFront();
                lose.setVisibility(View.VISIBLE);
                MyCardView.setImageResource(cardImg[r1 - 1]);
                turn = 0;
                ychip(yourchip + totalchip);
                ResultView.items.add(new Result(Integer.toString(round), "LOSE", Integer.toString(r1), Integer.toString(ycard), Integer.toString(totalchip)));
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            handler.sendEmptyMessage(RESULT);
                        } catch (InterruptedException e) {
                        } finally {
                        }
                    }
                }.start();
            } else {
                draw.bringToFront();
                draw.setVisibility(View.VISIBLE);
                MyCardView.setImageResource(cardImg[r1 - 1]);
                wait = totalchip;
                ResultView.items.add(new Result(Integer.toString(round),"DRAW",Integer.toString(r1),Integer.toString(ycard),Integer.toString(totalchip)));
                dr = true;
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            handler.sendEmptyMessage(RESULT);
                        } catch (InterruptedException e) {
                        } finally {
                        }
                    }
                }.start();
            }
        } else if (msg.equals("die")) {
            if (r1 == 10) {
                ychip(yourchip + 10);
                mchip(mychip - 10);
            }
            ychip(yourchip + totalchip);
            lose.bringToFront();
            lose.setVisibility(View.VISIBLE);
            MyCardView.setImageResource(cardImg[r1 - 1]);
            ResultView.items.add(new Result(Integer.toString(round), "LOSE", Integer.toString(r1), Integer.toString(ycard), Integer.toString(totalchip)));
            turn = 0;
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(RESULT);
                    } catch (InterruptedException e) {
                    } finally {
                    }
                }
            }.start();

        } else {
            mchip(mychip + totalchip);
            if (ycard == 10) {
                ychip(yourchip - 10);
                mchip(mychip + 10);
            }
            win.bringToFront();
            win.setVisibility(View.VISIBLE);
            ResultView.items.add(new Result(Integer.toString(round), "WIN", Integer.toString(r1), Integer.toString(ycard), Integer.toString(totalchip)));
            MyCardView.setImageResource(cardImg[r1-1]);
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(RESULT);
                    } catch (InterruptedException e) {
                    } finally {
                    }
                }
            }.start();
            turn = 1;
            gstart();
        }
    }

    public class ImgThread extends Thread {
        public void run() {
            while (true) {
                if(mychip<0||yourchip<0||round>10)
                    break;
                if (chip_1 != mychip % 10 || chip_10 != mychip / 10) {
                    chip_1 = mychip % 10;
                    chip_10 = mychip / 10;
                    handler.sendEmptyMessage(CHIPSET);
                }
                if (chip_1Y != yourchip % 10 || chip_10Y != yourchip / 10) {
                    chip_1Y = yourchip % 10;
                    chip_10Y = yourchip / 10;
                    handler.sendEmptyMessage(CHIPSETY);
                }
                if(ybc_1!=enemychip%10||ybc_10!=enemychip/10){
                    ybc_1=enemychip%10;
                    ybc_10=enemychip/10;
                    handler.sendEmptyMessage(YBCHIPSET);
                }
                if(mbc_1!=bettingChip%10||mbc_10!=bettingChip/10){
                    mbc_1=bettingChip%10;
                    mbc_10=bettingChip/10;
                    handler.sendEmptyMessage(MBCHIPSET);
                }
            }
        }
    }
    public void roundSet(){
        handler.sendEmptyMessage(ROUNDSET);
    }

    public void cardSet() {
        Intent intent = new Intent(Game.this, Card.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        if(System.currentTimeMillis()>backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            finish();
        }
    }
}