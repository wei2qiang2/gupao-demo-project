package com.demo.jvm;

import java.io.IOException;
import java.io.InputStream;

/**
 * 同一个类，用不同的类加载器去加载，创建出来的对象是不一样的
 */
public class JiaZaiLei4 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader myload = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";

                System.err.println(fileName);
                InputStream inputStream = getClass().getResourceAsStream(fileName);
//                System.err.println(inputStream.toString());
                if (inputStream == null) {
                    return super.loadClass(name);
                }

                try {
                    byte[] buff = new byte[inputStream.available()];
                    inputStream.read(buff);
                    return defineClass(name, buff, 0, buff.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        System.out.println(myload.getClass().getClassLoader());
        System.out.println(JiaZaiLei4.class.getClassLoader());

        Object obj = myload.loadClass("com.demo.jvm.JiaZaiLei4").newInstance();
        System.out.println(obj.getClass().getClassLoader());
        System.out.println(obj instanceof JiaZaiLei4);
    }

    /**
     *   protected Class<?> loadClass(String var1, boolean var2) throws ClassNotFoundException {
     *         synchronized(this.getClassLoadingLock(var1)) {
     *             Class var4 = this.findLoadedClass(var1);
     *             if (var4 == null) {
     *                 long var5 = System.nanoTime();
     *
     *                 try {
     *                     if (this.parent != null) {
     *                          //用父加载器去加载
     *                         var4 = this.parent.loadClass(var1, false);
     *                     } else {//没有父加载器用根加载器去加载
     *                         var4 = this.findBootstrapClassOrNull(var1);
     *                     }
     *                 } catch (ClassNotFoundException var10) {
     *                 }
     *
     *                 if (var4 == null) {
     *                     long var7 = System.nanoTime();
     *                     var4 = this.findClass(var1);
     *                     PerfCounter.getParentDelegationTime().addTime(var7 - var5);
     *                     PerfCounter.getFindClassTime().addElapsedTimeFrom(var7);
     *                     PerfCounter.getFindClasses().increment();
     *                 }
     *             }
     *
     *             if (var2) {
     *                 this.resolveClass(var4);
     *             }
     *
     *             return var4;
     *         }
     */
}
