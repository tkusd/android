package tw.tkusd.appstudio.app;

/**
 * Created by user on 2015/7/31.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tw.tkusd.appstudio.R;

public class LogDemo extends Activity {

    private static final String ACTIVITY_TAG="LogDemo";
    private Button bt;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logdemo);
        //通过findViewById找到Button资源
        bt = (Button)findViewById(R.id.bt);
        //增加事件响应
        bt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v(LogDemo.ACTIVITY_TAG, "This is Verbose.");
                Log.d(LogDemo.ACTIVITY_TAG, "This is Debug.");
                Log.i(LogDemo.ACTIVITY_TAG, "This is Information");
                Log.w(LogDemo.ACTIVITY_TAG, "This is Warnning.");
                Log.e(LogDemo.ACTIVITY_TAG, "This is Error.");
            }

        });
    }

}