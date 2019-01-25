package luyao.box.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by luyao
 * on 2018/11/1 14:23
 */
public class CloseUtils {

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable... params) {
        for (Closeable closeable : params)
            close(closeable);
    }
}
