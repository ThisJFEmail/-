package edu.cust.student;


import edu.cust.student.dao.StudentDAO;
import edu.cust.student.domain.Student;
import edu.cust.student.domain.StudentIE;
import edu.cust.util.AbstractController;
import edu.cust.util.BusinessException;
import edu.cust.util.excel.Export;
import edu.cust.util.page.Page;
import edu.cust.util.page.PageFactory;
import edu.cust.util.search.Search;
import edu.cust.util.excel.Import;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 学生管理的action，对前后端进行数据的交互
 * 其中@Controller注解声明了这是Controller层，也为@Autowired
 * 注解提供了注入条件
 *
 * 而@Scope注解解决跨域问题
 * created by ybl at 2021/12/3
 */
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StudentAction extends AbstractController {
    //使用@Autowired使数据库连接等配置从容器中自动依赖注入到以下全局变量
    @Autowired
    private JdbcTemplate jt;

    @Autowired
    private StudentDAO studentDAO;

    //    @RequiresPermissions({"yhgl"}) 定义接口访问权限，这里默认每个人都能访问，故不进行设置
    //RequestMapping注解接受后端请求并进行相应
    @RequestMapping("/adm/students/listAjax")
    public String list(int rows, int page, Search search, Model model) {
        //用search对象中的buildSQL方法构建前端所需要的sql查询语句
        String sql = search.buildSQL(studentDAO);
        sql += " order by c_number asc";
        //获取前端传递的分页数据配置 page: 当前所在页；rows:前端设定的一页多少行数据
        Page mlpage = PageFactory.getPage();
        mlpage.setPageNum(page);
        mlpage.setRecordNum(rows);
        List<Object> params = search.getParams();
        //params.add(cp);
        //根据sql语句与当前分页配置检索出该页数据
        List<?> result = mlpage.getOnePage(sql, params, studentDAO);
        //model返回给后端
        model.addAttribute("pages", mlpage);
        model.addAttribute("result", result);
        return "json";
    }

    //@Transactional注解声明改server会操作修改数据库中数据
    @Transactional
    @RequiresPermissions({"xsgl"})
    @RequestMapping("/adm/students/deleteAjax")
    public String delete(String number, Model model) {
        //用封装好的方法对数据库进行删除指定行的操作(主键为索引)
        studentDAO.delete(number);
        model.addAttribute("retMsg", "删除成功");
        return "json";
    }

    @Transactional
    @RequiresPermissions({"xsgl"})
    @RequestMapping("/adm/students/addAjax")
    public String add(Student student, Model model) {
        //用封装好的方法对数据库进行添加操作(实体类的值自动装配)
        studentDAO.insert(student);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }

    @Transactional
    @RequiresPermissions({"xsgl"})
    @RequestMapping("/adm/students/updateAjax")
    public String update(Model model, Student student) {
        ////用封装好的方法对数据库进行查询操作(主键为索引,实体类的值自动装配)
        Student a = studentDAO.loadOne(student.getNumber());
        a.setName(student.getName());
        a.setGrade(student.getGrade());
        studentDAO.update(a);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "更新成功");
        return "json";
    }

    @RequiresPermissions({"xsgl"})
    @RequestMapping("/adm/students/export")
    public String export(HttpServletResponse response, Model model) {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=students.xls");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet1");
        Export export = new Export();
        export.exportRecords(null, new StudentIE(), sheet, jt, "", null);
        try(OutputStream out = response.getOutputStream()) {
            wb.write(out);
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @RequiresPermissions({"xsgl"})
    @RequestMapping("/adm/students/importAjax")
    public String importStudent(HttpServletResponse response, MultipartFile file, Model model) {
        if(file == null || file.isEmpty()) {
            throw new BusinessException("请上传Excel文件");
        }
        if(file.getSize() > 10*1000*1000) {
            throw new BusinessException("文件不能超过10MB");
        }
        try(InputStream in = file.getInputStream()){
            log.info("------开始导入信息------");
            HSSFWorkbook wbs = new HSSFWorkbook(in);
            HSSFSheet childSheet = wbs.getSheetAt(0);
            Import imp = new Import();
            imp.importRecords(null, new StudentIE(), childSheet, jt, false);
            log.info("-------导入信息成功------");
        }catch(IOException e){
            throw new BusinessException("I/O错误",e);
        }
        model.addAttribute("retCode","OK");
        model.addAttribute("retMsg","导入成功");
        return "json";
    }

    @RequiresPermissions({"xsgl"})
    //注：该方法未被使用过
    @RequestMapping("/adm/students/existAjax")
    //该注解将返回值作为http正文返回给前端
    @ResponseBody
    public boolean existAjax(String number, Model model) {
        //判断是否存在该学生
        Student student = studentDAO.loadOne(number);
        return student == null;
    }
}
