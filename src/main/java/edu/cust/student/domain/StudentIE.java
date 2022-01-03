package edu.cust.student.domain;

import javax.servlet.http.HttpServletRequest;

import edu.cust.util.excel.ColumnTypeConverter;
import edu.cust.util.excel.ExportHeaders;
import edu.cust.util.excel.Header;
import edu.cust.util.excel.ImportExport;
import edu.cust.util.excel.ImportHeaders;

public class StudentIE implements ImportExport {
    @Override
    public ImportHeaders getImportHeaders(HttpServletRequest request) {
        // TODO Auto-generated method stub
        ImportHeaders h = new ImportHeaders("c_student");
        h.addHeader(new Header("学号","C_NUMBER", null, ColumnTypeConverter.STRING));
        h.addHeader(new Header("姓名", "C_NAME", null, ColumnTypeConverter.STRING));
        h.addHeader(new Header("年级", "C_GRADE", null, ColumnTypeConverter.STRING));
        return h;
    }
    @Override
    public ExportHeaders getExportHeaders(HttpServletRequest request) {
        // TODO Auto-generated method stub
        ExportHeaders h = new ExportHeaders("c_student");
        h.addHeader(new Header("学号","C_NUMBER", null, ColumnTypeConverter.STRING));
        h.addHeader(new Header("姓名", "C_NAME", null, ColumnTypeConverter.STRING));
        h.addHeader(new Header("年级", "C_GRADE", null, ColumnTypeConverter.STRING));
        return h;
    }
}
