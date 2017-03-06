package com.wang.test;

import com.wang.bean.Student;
import com.wang.bean.User;
import com.wang.xml.Factory;
import org.dom4j.DocumentException;


/**
 * Created by hppc on 2017/2/4.
 */
public class Test {
    public static void main(String[] args) {
        Factory factory=new Factory();
        try {
            factory.init("G:/IOC/src/ioc.xml");
            User user= (User) factory.getBean("user1");//因为放入map中的是object类型的 所以要强制转化
            System.out.println(user);
            Student student= (Student) factory.getBean("student1");
            System.out.println(student);
        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("加载xml文件时候抛出异常");
        }
    }
}
