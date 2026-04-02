package com.cjy.reflect;

public class Test {
    public static void main(String[] args) throws Exception {
        //1. 类.Class
        Class c1 = Student.class;

        //2. 对象.getClass()
        Student s = new Student();
        Class c2 = s.getClass();

        //3. Class.forName("全类名")
        Class c3 = Class.forName("com.cjy.reflect.Student");

        System.out.println(c1 == c2); //true
        System.out.println(c1 == c3); //true
    }
}
