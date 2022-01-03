package edu.cust.student.domain;

import lombok.Data;

/**
 * student的实体类，与DAO层相关的字段及主键自动赋值给实体类
 * 命名方式采用数据表字段去掉c_前缀，当遇见 _ 时第二个字母大写，如：c_name => name,c_abc_name => abcName
 * 严格采用上述方法命名，否则将导致程序异常
 * 变量尽量做好注释
 *
 * created by ybl at 2021/12/2
 */

@Data
//Data注解用于给实体类自动创建getter/setter等实体类常用方法
public class Student {
    //学号
    private String number;
    //姓名
    private String name;
    //年级
    private int grade;
}
