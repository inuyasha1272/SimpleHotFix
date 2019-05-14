package z1.com.myhotfix.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PublicUtils {
    private static final String TAG = PublicUtils.class.getSimpleName();

    public void say(){
        Log.e(TAG, "said from mainProject PublicUtils");
    }
    //文件复制
    public static void copyFileUsingFileStreams(InputStream input, File dest) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(input != null){
                input.close();
            }
            if(output != null){
                output.close();
            }
        }
    }
}
