package edu.cust.student.dao;

import edu.cust.student.domain.Student;
import edu.cust.util.DAOTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentDAO extends DAOTemplate<Student> {
    public StudentDAO(){
        //反射的知识点，用来动态装配实体类
        clazz = Student.class;
        //数据表的主键
        pkColumns = new String[]{"c_number"};
        //列出所需要的字段(若不赋值该变量则默认所有字段都可列出)
        listProjections = new String[]{"c_number", "c_name","c_grade"};
        //要操控的字段
        comColumns = new String[]{"c_name","c_grade"};
        //数据表
        tableName = "c_student";
        init();
    }
}