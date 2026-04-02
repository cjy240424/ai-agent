package com.cjy.reflect;

public class Dog {
    private String name;
    private int age;
    private String hobby;

    private Dog() {
        System.out.println("这是私有的构造方法，无参数构造器执行了！！！");
    }

    private Dog(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("这是私有的构造方法，两个参数构造器执行了！！！");
    }

    public Dog(String name, int age, String hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        System.out.println("这是公有的构造方法，三个参数构造器执行了！！！");
    }

    private void eat() {
        System.out.println("狗在吃骨头！！！！");
    }

    private String eat(String name) {
        System.out.println("狗吃：" + name + "$$$$$");
        return "曹景宇，你记住，你进步的路上不缺李林炎！";
    }
    // getter/setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
