package jp.study.chatapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.LineNumberReader;
import java.util.Calendar;
import java.util.concurrent.TransferQueue;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mInputMessage;
    private Button mSendMessage;
    private LinearLayout mMessageLog;
    private TextView mCpuMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setContentView(R.layout.activity_chat);
        WebView myWebView = (WebView)findViewById(R.id.webView1);
        // 標準ブラウザの削除
        myWebView.setWebViewClient(new WebViewClient());
        // アプリ起動時に読み込むURL
        myWebView.loadUrl("http://google.com");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mInputMessage = (EditText) findViewById(R.id.input_message);
        mSendMessage = (Button) findViewById(R.id.send_message);
        mMessageLog = (LinearLayout) findViewById(R.id.message_log);
        mCpuMessage = (TextView) findViewById(R.id.cpu_message);

        mSendMessage.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mSendMessage)) {
            String inputText = mInputMessage.getText().toString();
            String answer;

            TextView userMessage = new TextView(this);
            int messageColor = getResources().getColor(R.color.message_color);
            userMessage.setTextColor(messageColor);
            userMessage.setBackgroundResource(R.drawable.user_message);
            if (inputText.length() == 0) {
                inputText = "おみくじ";
            }
            userMessage.setText(inputText);
            final LinearLayout.LayoutParams userMessageLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            userMessageLayout.gravity = Gravity.END;
            final int marginSize = getResources().getDimensionPixelSize(R.dimen.message_margin);
            userMessageLayout.setMargins(0, marginSize, 0, marginSize);
            mMessageLog.addView(userMessage, 0, userMessageLayout);

            if (inputText.contains("お元気ですか")) {
                answer = "元気です";
            } else if (inputText.contains("疲れた")) {
                answer = "お疲れ様です";
            } else if (inputText.contains("時間")) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                answer = String.format("ただいまの時間は、%1$d時 %2$2d分 %3$d秒です。", hour, minute, second);
            } else if (inputText.contains("おみくじ")) {
                double random = Math.random() * 15d;
                if (random < 2d) {
                    answer = "大凶です！！";
                } else if (random < 4d) {
                    answer = "凶です・・・";
                } else if (random < 9d) {
                    answer = "吉です";
                } else if (random < 12d) {
                    answer = "中吉です！";
                } else if (random < 14d) {
                    answer = "大吉です！！！";
                } else {
                    answer = "やばいです・・・！";
                }
            } else {
                answer = "それはいいですね";
            }
            final TextView cpuMessage = new TextView(this);
            cpuMessage.setTextColor(messageColor);
            cpuMessage.setBackgroundResource(R.drawable.cpu_message);
            cpuMessage.setText(answer);
            cpuMessage.setGravity(Gravity.START);

            mInputMessage.setText("");
            TranslateAnimation userMessageAnimation = new TranslateAnimation(- mMessageLog.getWidth(), 0, 0, 0);
            userMessageAnimation.setDuration(400);
            userMessageAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams cpuMessageLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cpuMessageLayout.gravity = Gravity.START;
                    cpuMessageLayout.setMargins(marginSize, marginSize, marginSize, marginSize);
                    mMessageLog.addView(cpuMessage, 0, cpuMessageLayout);
                    TranslateAnimation cpuAnimation = new TranslateAnimation(mMessageLog.getWidth(), 0, 0, 0);
                    cpuAnimation.setDuration(400);
                    cpuMessage.setAnimation(cpuAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            userMessage.startAnimation(userMessageAnimation);
        }
    }
}
