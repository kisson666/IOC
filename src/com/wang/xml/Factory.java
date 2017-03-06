package com.wang.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hppc on 2017/2/4.
 */
public class Factory {
    private Map<String,Object> beanMap=new HashMap<String,Object>();//保存xml文件中每一个id对应的bean
    public void init(String xml) throws DocumentException {  //加载xml文件
        SAXReader saxReader=new SAXReader();//创建一个saxreader对象
        File xmlfile =new File(xml);
        Document document=saxReader.read(xmlfile);//获取document对象,如果文档无节点，则会抛出Exception提前结束
        Element root=document.getRootElement();//获取根节点
        Element foo;
        for(Iterator i=root.elementIterator("bean");i.hasNext();){//获取所有<bean>结点
            foo=(Element)i.next();//获取当前循环中的<bean>结点及里面的子节点和属性等
            Attribute id=foo.attribute("id");
            Attribute cls=foo.attribute("class");//获取<bean id="" class="">中的id和class
            try {
                Class bean=Class.forName(cls.getText());//利用反射加载类
                Method mSet;  //创建一个设置值的方法的对象   Method 提供关于类或接口上单独某个方法（以及如何访问该方法）的信息
                try {
                    Object obj=bean.newInstance();
                    for(Iterator ite=foo.elementIterator("property");ite.hasNext();){//获取当前<bean>中的子节点<property>
                        Element foo2=(Element)ite.next();//获取当前循环到的<property>结点
                        Attribute name=foo2.attribute("name");//获取该property中的name属性
                        String value=null;//用来存放name属性的value值
                        for(Iterator ite2=foo2.elementIterator("value");ite2.hasNext();){
                            Element foo3=(Element)ite2.next();//获取<value>结点
                            value=foo3.getText();
                            break;
                        }
                        String str= "set"+name.getText().replace(name.getText().substring(0, 1),
                                name.getText().substring(0, 1).toUpperCase());//将属性name的值的首字母转换成大写,并加上”set“ str为方法的名字
                        try {
                            mSet=bean.getMethod(str,String.class);//这里把string.class写死了，因为我目前想不到这么去让识别set方法的参数类型
                            try {
                                mSet.invoke(obj,value);//执行方法
                            } catch (InvocationTargetException e) {
                                System.out.println("执行set方法的时候抛异常了");
                            }
                        } catch (NoSuchMethodException e) {
                            System.out.println("获得方法的时候抛出异常");
                        }
                    }
                    beanMap.put(id.getText(),obj);//放进去的是object类型的，用的时候要强制转化一下
                } catch (InstantiationException e) {
                    System.out.println("创建bean实例的时候抛出异常");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    System.out.println("创建bean实例的时候抛出异常");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("根据xml中class的值加载类的时候抛出异常");
            }

        }
    }
    public Object getBean(String id){   //根据xml中bean的id获取bean
        Object obj=beanMap.get(id);
        return obj;
    }

    public Factory() {
    }
}
