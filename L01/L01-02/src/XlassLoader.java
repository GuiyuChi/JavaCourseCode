import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;

public class XlassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        // 相关参数
        final String className = "Hello";
        // 通过方法查看得知Hello类中定义了方法hello
        final String methodName = "hello";

        // 加载相应的类
        Class<?> clazz = new XlassLoader().findClass(className);
        // 查看类中定义的方法
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println(method.getName());
        }

        // 创建对象
        Object instance = clazz.getDeclaredConstructor().newInstance();
        // 调用实例方法
        Method method = clazz.getMethod(methodName);
        method.invoke(instance);

    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        // 原始文件为xlass结尾
        String suffix = ".xlass";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(className + suffix);

        try {
            // 读取数据
            byte[] rawData = new byte[inputStream.available()];
            inputStream.read(rawData);

            // 转换
            byte[] classBytes = decode(rawData);
            // 通知底层定义这个类
            return defineClass(className, classBytes, 0, classBytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(className, e);
        } finally {
            closeStream(inputStream);
        }
    }

    // 解码
    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }

    private void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
