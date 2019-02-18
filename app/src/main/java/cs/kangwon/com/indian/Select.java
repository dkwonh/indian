
package cs.kangwon.com.indian;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import cs.kangwon.com.teamsw.R;


/**
 * Created by Administrator on 2015-10-29.
 */

public class Select extends Activity {

    private static final String TAG = "Select";
    private static final boolean D = true;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private BluetoothAdapter bluetooth;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_CONNECT_DEVICE = 2;
    protected StringBuffer mOutBuffer;
    private String Device_name = null;
    protected Connect mConnect = null;
    Intent intent = new Intent(this, Game.class);
    private int cardCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");

        setContentView(R.layout.select);

        Button b2 = (Button) findViewById(R.id.enable_search);
        Button b3 = (Button) findViewById(R.id.search);

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (bluetooth == null) {
            Toast.makeText(this, "블루투스 지원안함", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ensureDiscoverable();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(Select.this, Search.class);
                startActivityForResult(search, REQUEST_CONNECT_DEVICE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!bluetooth.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

            // Otherwise, setup the chat session
        } else if (mConnect == null) {
            setConnect();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mConnect != null) mConnect.stop();
            close();
        } catch (Exception e) {
            // do nothing
        }
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    public void close() {
        finish();
        int nSDKVersion = Integer.parseInt(Build.VERSION.SDK);
        if (nSDKVersion < 8)    //2.1이하
        {
            ActivityManager actMng = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            actMng.restartPackage(getPackageName());
        } else {
            new Thread(new Runnable() {
                public void run() {
                    ActivityManager actMng = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    String strProcessName = getApplicationInfo().processName;
                    while (true) {
                        List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();
                        for (ActivityManager.RunningAppProcessInfo rap : list) {
                            if (rap.processName.equals(strProcessName)) {
                                if (rap.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
                                    actMng.restartPackage(getPackageName());
                                Thread.yield();
                                break;
                            }
                        }
                    }
                }
            }, "Process Killer").start();
        }
    }


    public void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ON RESUME");

        if (mConnect != null) {
            if (mConnect.getState() == Connect.STATE_NONE) {
                mConnect.start();
            }
        }
    }

    private void setConnect() {
        Log.d(TAG, "setupConnect()");

        mConnect = new Connect(this, mHandler);
        Game.mConnect = mConnect;
        mOutBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (bluetooth.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case Connect.STATE_CONNECTED:
                            break;
                        case Connect.STATE_CONNECTING:

                            break;
                        case Connect.STATE_LISTEN:
                        case Connect.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    if (writeMessage.substring(0, 4).equals("chip"))
                        Game.turn = 0;
                    else if (writeMessage.substring(0, 4).equals("dead")) {
                        ((Game) Game.mContext).die();
                        ((Game) Game.mContext).gstart();
                    } else if (writeMessage.substring(0, 4).equals("call")) {
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if (readMessage.length() >= 15) {//카드 인덱스
                        Game.yourCard = readMessage.split("x");
                        for (int i = 0; i < Game.r2.length; i++) {
                            if (Game.yourCard[i].equals("end"))
                                break;
                            Game.r2[i] = Integer.parseInt(Game.yourCard[i]);
                        }
                        Game.r2 = reverseArrayInt(Game.r2);
                        Game.ycard = Game.r2[1];
                    } else {//배팅수
                        String index = readMessage.substring(0, 4);
                        int val = 1;
                        if (index.equals("call")) {//콜했을때
                            int i = Game.bettingChip - Game.enemychip;
                            Game.ychip(Game.yourchip - i);
                            Game.tchip(Game.totalchip + i);
                            Game.WhatDo.setImageResource(R.drawable.icoll);
                            Game.WhatDo.setVisibility(View.VISIBLE);
                            ((Game) Game.mContext).call();
                        } else if (index.equals("chip")) {//레이즈 했을때
                            Game.turn = 1;
                            val = Integer.parseInt(readMessage.substring(4));
                            int i = val - Game.enemychip;
                            Game.ychip(Game.yourchip - i);
                            if (Game.yourchip == 0)
                                Toast.makeText(getApplicationContext(), "상대방 올 * 인.", Toast.LENGTH_LONG).show();
                            Game.tchip(Game.totalchip + i);
                            Game.WhatDo.setImageResource(R.drawable.imore);
                            Game.WhatDo.setVisibility(View.VISIBLE);
                            Game.textView.setText(Integer.toString(Game.totalchip));
                            Game.echip(val);
                        } else if (index.equals("dead")) {//상대방이 다이했을경우
                            Game.WhatDo.setImageResource(R.drawable.idie);
                            Game.WhatDo.setVisibility(View.VISIBLE);
                            ((Game)Game.mContext).result("win");
                        }
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    Device_name = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + Device_name, Toast.LENGTH_SHORT).show();
                    Intent game = new Intent(Select.this, Game.class);
                    startActivity(game);
                    break;
                /*case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;*/
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult" + resultCode);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setConnect();
                } else {
                    Log.d(TAG, "블루투스 활성화 안됨");
                    Toast.makeText(this, R.string.not_enable_bt, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(Search.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = bluetooth.getRemoteDevice(address);
                    mConnect.connect(device);
                    /*Intent game = new Intent(Select.this,Game.class);
                    startActivity(game);*/
                }
        }
    }
public static int[] reverseArrayInt(int[] n) {
        int left  = 0;             // 맨 좌측 요소의 첨자
        int right = n.length - 1;  // 맨 우측 요소의 첨자

        while (left < right) {
        int temp = n[left];
        n[left]  = n[right];     // 좌우 요소 교환
        n[right] = temp;

        left++; right--;         // 배열의 중간 부분으로 한칸씩 이동
        }

        return n;
        }
}

