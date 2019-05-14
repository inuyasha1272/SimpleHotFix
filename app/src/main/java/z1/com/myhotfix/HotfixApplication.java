package z1.com.myhotfix;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;

public class HotfixApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        File apk = new File(getCacheDir() + "/hotfix.dex");
        if (apk.exists()) {
            try {
                ClassLoader classLoader = getClassLoader();
                Class loaderClass = BaseDexClassLoader.class;
                Field pathListField = loaderClass.getDeclaredField("pathList");
                pathListField.setAccessible(true);
                Object pathListObject = pathListField.get(classLoader);
                Class pathListClass = pathListObject.getClass();
                Field dexElementsField = pathListClass.getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                //获取当前类加载器中的dexElements对象
                Object dexElementsObject = dexElementsField.get(pathListObject);

                // 加载 补丁apk中的 classLoader.pathList.dexElements 对象;
                PathClassLoader newClassLoader = new PathClassLoader(apk.getPath(), null);
                Object newPathListObject = pathListField.get(newClassLoader);
                Object newDexElementsObject = dexElementsField.get(newPathListObject);
                //原apk中的dexElements数组的长度 即class个数
                int oldLength = Array.getLength(dexElementsObject);
                //补丁中的dexElements数组的长度 即class个数
                int newLength = Array.getLength(newDexElementsObject);
                //创建一个数组，数组长度=补丁的Class个数+原apk的Class个数
                Object concatDexElementsObject = Array.newInstance(dexElementsObject.getClass().getComponentType(), oldLength + newLength);
                //【关键点：将补丁的中的Class【先】放进dexElements数组中】，因为findClass时是从数组角标0开始的】
                for (int i = 0; i < newLength; i++) {
                    Array.set(concatDexElementsObject, i, Array.get(newDexElementsObject, i));
                }
                for (int i = 0; i < oldLength; i++) {
                    Array.set(concatDexElementsObject, newLength + i, Array.get(dexElementsObject, i));
                }
                dexElementsField.set(pathListObject, concatDexElementsObject);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
