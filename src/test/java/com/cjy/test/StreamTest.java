package com.cjy.test;

import com.cjy.bean.Student;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamTest {
    List<Student> students = List.of(
            new Student("Alice", 19, 85),
            new Student("Bob", 22, 55),
            new Student("Charlie", 20, 92),
            new Student("David", 21, 59),
            new Student("Eve", 18, 100)
    );
    @Test
    public void streamTest1(){
        //利用 Stream，把所有及格（score >= 60）的学生筛选出来，
        // 并收集到一个新的 List<Student> 中。
        List<Student> list1 = students.stream()
                .filter(student -> student.getScore() >= 60)
                .collect(Collectors.toList());
        System.out.println("Test1:");
        list1.forEach(System.out::println);
    }

    @Test
    public void streamTest2(){
      //利用 Stream，提取出所有学生的姓名（name），并收集到一个全新的 List<String> 中。
        List<String> studentsName = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("Test2:");
        studentsName.forEach(System.out::println);
    }

    @Test
    public void streamTest3(){
        //利用 Stream，找出所有 年龄大于 20 岁 且 及格（>=60分） 的同学，
        // 并只提取出他们的姓名，放到一个 List<String> 中。
        List<String> list3 = students.stream()
                .filter(student -> student.getAge() > 20 &&
                        student.getScore() >= 60)
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("Test3:");
        list3.forEach(System.out::println);
    }

    @Test
    public void testOptional() {
        // --- 场景 1：模拟数据库正常查到了数据 ---
        Student student1 = new Student("Alice", 19, 85);

        // 把查询结果装进 Optional 这个“防爆盒”里
        Optional<Student> opt1 = Optional.ofNullable(student1);

        // 优雅处理：如果盒子里有学生，就把名字大写打印出来；如果没有，就打印"没找到"
        opt1.map(Student::getName)
                .map(String::toUpperCase)
                .ifPresentOrElse(
                        name -> System.out.println("场景1找到的学生：" + name),
                        () -> System.out.println("场景1：查无此人")
                );

        // --- 场景 2：模拟数据库查不到数据，返回了 null ---
        Student student2 = null;

        // 同样装进防爆盒
        Optional<Student> opt2 = Optional.ofNullable(student2);

        // 尝试获取分数，重点来了：如果 student2 是 null，直接使用 orElse 给个默认值 0，绝不报错！
        int score = opt2.map(Student::getScore).orElse(0);
        System.out.println("场景2的分数是：" + score);
    }
}
