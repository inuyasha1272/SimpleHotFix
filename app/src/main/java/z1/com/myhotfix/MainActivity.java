package z1.com.myhotfix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import z1.com.myhotfix.util.PublicUtils;
import z1.com.myhotfix.util.Utils;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main)
    TextView mTextView;

    @OnClick({R.id.btn_go_about})
    public void goAbout(){
        mTextView.setText(Utils.getTitle());
    }
    @OnClick({R.id.btn_do_hotfix})
    public void doHotFix(){
        Toast.makeText(this, "do hotfix", Toast.LENGTH_SHORT).show();
        try {
            InputStream inputStream = getResources().getAssets().open("hotfix.dex");
            PublicUtils.copyFileUsingFileStreams(inputStream, new File(getCacheDir()+"/hotfix.dex"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.btn_remove_hotfix_dex})
    public void removeHotfixDex(){
        File file = new File(getCacheDir() + "/hotfix.dex");
        if(file.exists()){
            Toast.makeText(this, "remove Hotfix Dex success", Toast.LENGTH_SHORT).show();
            file.delete();
        }else{
            Toast.makeText(this, "remove Hotfix Dex failed", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick({R.id.btn_kill_self_process})
    public void killSelf(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
