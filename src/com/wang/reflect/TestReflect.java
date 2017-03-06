package com.wang.reflect;


import com.wang.bean.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by hppc on 2017/3/2.
 */

//反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；
// 这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。
//接下来试试调用一下反射的一些api
public class TestReflect {
    public static void main(String[] args) {
        //以下是常规方法创建一个对象，并调用对象的方法为对象的属性赋值
        User user1=new User();
        user1.setName("小王"); user1.setPassword("123");
        System.out.println(user1);
        //以下是通过反射的方式创建一个对象，并调用方法
        try {
           //Class是一个类，每一个类对应一个Class类的实例，可以理解为用这个实例读取对应类的各种信息，如修饰符 类名 变量名 方法名等等 Class类是反射的源头
           //java程序是运行在虚拟机上的，用到类的时候需要先把类加载到虚拟机上 下面这一句就可以用来加载类
            Class userClass =Class.forName("com.wang.bean.User");//利用反射加载类并且打印出类的名字，通过全类名找到这个类
            System.out.println(userClass.getName());//可以得到这个Class类的实例所对应的类的名字 即User类
            Field[] fields=userClass.getDeclaredFields();//Field是一个用来表示类中字段(属性)的类 通过这一行可以得到User类中所有的字段(属性)
            for(Field field:fields){
                System.out.println(field.getName());//打印字段(属性)的名字
            }
            Method[] methods=userClass.getDeclaredMethods();//得到类中所有的方法 Method类的实例可以表示类中的某一个方法 并且可以调用相应的方法执行
            for(Method method:methods){
                System.out.println(method.getName());//打印出方法的名字
            }
            try {
                Method mset=userClass.getMethod("setName",String.class);//第二个参数为方法参数的类型

                    //Object obj=userClass.newInstance();//通过Class对象的实例new一个对应的类的实例 但是是object类型的 用的话需要强转

                try {
                    mset.invoke(user1,"小阳");//执行方法 第一个参数为需要执行方法的对象 第二个为方法的参数
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                System.out.println(user1);//由于执行了setName方法，这个对象中的name属性的值已经改变了


            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //为什么叫做反射？
        //通过下面这句可以得到user对应的Class类，得到了对应的Class类就可以执行上面的一系列方法
        Class user1Class=user1.getClass();
        System.out.println(user1Class.getName());

        //常规的是  导入“包类”名称--->通过new实例化--->取得实例化对象
        //反射是    取得实例化对象--->getClass方法--->得到完整的“包类”名称
        //所以叫做 反射



    }
}
