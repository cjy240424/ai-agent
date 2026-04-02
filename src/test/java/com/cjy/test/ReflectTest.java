package com.cjy.test;

import com.cjy.reflect.Dog;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectTest {
    @Test
    public void getConstructorTest() throws Exception {
        //1. 获取Class对象
        Class c1 = Dog.class;

        //2. 获取构造方法
        Constructor[] cons = c1.getDeclaredConstructors();
        for (Constructor c : cons) {
            System.out.println(c);
        }

        System.out.println("-------------------------------------------");
        //获取某个构造器
        Constructor con1 = c1.getDeclaredConstructor(); // 无参构造器
        Constructor con2 = c1.getDeclaredConstructor(String.class, int.class); //2个参数的构造器
        System.out.println(con1.getName() + "(" + con1.getParameterCount() + ")");
        System.out.println(con2.getName() + "(" + con2.getParameterCount() + ")");

        System.out.println("-------------------------------------------");
        //使用构造器
        //暴力反射
        con1.setAccessible(true);
        con2.setAccessible(true);
        //通过con1创建对象
        Dog d1 = (Dog)con1.newInstance();
        Dog d2 = (Dog) con2.newInstance("李林炎", 20);

        System.out.println(d1);
        System.out.println(d2);
    }

    //获取成员变量
    @Test
    public void getFieldTest() throws Exception {
        Class c1 = Dog.class;
        System.out.println("-------------------------------------------");

        Field[] fields = c1.getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f);
        }

        Field name = c1.getDeclaredField("name");
        Field age = c1.getDeclaredField("age");
        Field hobby = c1.getDeclaredField("hobby");
//        System.out.println(name);

        System.out.println("-------------------------------------------");
        Dog d1 = new Dog("李达康", 20, "和曹景宇有缘");

        name.setAccessible(true);
        age.setAccessible(true);
        hobby.setAccessible(true);
        name.set(d1, "李林炎");
        age.set(d1, 19);
        hobby.set(d1, "和曹景宇的缘分开始了！");
        System.out.println(d1.getName() + "在" + d1.getAge() + "岁的时候" + d1.getHobby() );

    }

    //获取成员方法
    @Test
    public void getMethodTest() throws Exception {
        Class c1 = Dog.class;
        System.out.println("-------------------------------------------");

        Method[] methods = c1.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m);
        }

        Method eat = c1.getDeclaredMethod("eat");
        Method eat1 = c1.getDeclaredMethod("eat", String.class);
        System.out.println(eat);
        System.out.println(eat1);

        System.out.println("-------------------------------------------");
        Dog d1 = new Dog("李达康", 20, "昏天黑地");

        eat.setAccessible(true);
        eat1.setAccessible(true);
        Object object1 = eat.invoke(d1);

        Object object2 = eat1.invoke(d1, "骨头");
        System.out.println(object1);
        System.out.println(object2);
    }
}
