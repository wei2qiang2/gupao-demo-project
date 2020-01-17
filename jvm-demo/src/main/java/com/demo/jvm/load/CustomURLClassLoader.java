package com.demo.jvm.load;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.*;
import java.util.PropertyPermission;

/**
 * 破坏内置的沙箱安全机制
 *
 * 反射的时候设置值setAccessiible（true）也是破坏沙箱安全机制
 */
public class CustomURLClassLoader extends URLClassLoader {
    public CustomURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);

        //特权代码：改变内置的沙箱安全机制
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                //设置权限验证器
                System.setSecurityManager(CustomSecurityManager.INSTANCE);
                return null;
            }
        });
    }

    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions p = new Permissions();
        p.add(new AllPermission());
        return super.getPermissions(codesource);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    static class CustomSecurityManager extends SecurityManager{
        public static CustomSecurityManager INSTANCE = new CustomSecurityManager();

        private CustomSecurityManager(){}

        /**
         * 策略权限查看，当执行操作的时候第哦啊用，如果允许则返回，不允许测抛出SecurityException
         * @param perm
         */
        private void sandboxCheck(Permission perm){
            //设置只读属性
            //以下操作不做权限验证 Property属性读取，文件读写，运行时访问，反射权限
            if (perm instanceof SecurityPermission && perm.getName().startsWith("getProperty")){
                return;
            }else if (perm instanceof PropertyPermission) {
                return;
            }else if(perm instanceof FilePermission){
                return;
            }else if (perm instanceof RuntimePermission || perm instanceof ReflectPermission){
                return;
            }
            super.checkPermission(perm);
        }
    }
}
