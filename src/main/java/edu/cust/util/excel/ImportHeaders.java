package edu.cust.util.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ImportHeaders {

	private HashMap<String, Header> headerMap = new HashMap<String, Header>();
	private ArrayList<Header> headerArray = new ArrayList<Header>();
	private String table;
	
	public ImportHeaders(String table){
		this.table = table;
	}
	
	public Header getHeader(String name){
		return headerMap.get(name);
	}

	public Header addHeader(Header h) {
		if(h.name == null){
			headerArray.add(h);
			return h;
		}
		headerMap.put(h.name, h);
		headerArray.add(h);
		return h;
	}
	
	public String buildInsertSQL() {
		StringBuffer sql = new StringBuffer();
		StringBuffer params = new StringBuffer();
		sql.append("insert into ");
		sql.append(table);
		sql.append(" (");
		for (Iterator<?> i = headerArray.iterator(); i.hasNext();) {
			Header h = (Header) i.next();
			if (h.column != null) {
				sql.append(h.column);
				sql.append(',');
				params.append("?,");
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		params.deleteCharAt(params.length() - 1);
		sql.append(") values (");
		sql.append(params);
		sql.append(')');
		
	//	log.info(sql);
		return sql.toString();
	}

	/*public String buildUpdateSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		sql.append(table);
		sql.append(" set ");
		for (Iterator<?> i = headerArray.iterator(); i.hasNext();) {
			Header h = (Header) i.next();
			if (h.column != null) {
				sql.append(h.column);
				sql.append("=?");
				sql.append(',');
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" where " + pkHeader.column + " = ?");
		return sql.toString();
	}
	
	public String buildSelectSQL(int keysSize){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ");
		sql.append(table);
		sql.append(" where ");
		sql.append(pkHeader.column);
		sql.append(" in (");
		for(int i = 0; i < keysSize; i++){
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(')');
		return sql.toString();
	}*/
	
	

	public void readFirstRow(HSSFSheet sheet) {
		HSSFRow xssfRow = sheet.getRow(0);// ?????????????????????????????????
		// ????????????????????????
		int columns = xssfRow.getPhysicalNumberOfCells();

		//ArrayList<Header> headerArray = new ArrayList<Header>();// ??????Header????????????,???????????????excel??????????????????
		for (int i = 0; i < columns; i++) {
			// log.info("???"+i+"???????????????"+hssfRow.getCell(i)); //?????????????????????
			HSSFCell c = xssfRow.getCell(i);
			if(c == null){
				continue;
			}
			Header header = headerMap.get(c.toString());

			if (header == null) {// ???hash?????????????????????????????????????????????????????????????????????????????????????????????null
				continue;
			}
			header.setIndex(i);
			//headerArray.add(header);
		}
	}
	
	private ArrayList<Object> readRowAsList(HSSFRow row){
		ArrayList<Object> result = new ArrayList<Object>();
		for (Iterator<?> i = headerArray.iterator(); i.hasNext();) {
			Header h = (Header) i.next();
			if (h.column == null) {
				continue;
			}
			result.add(h.getCellValue(row));
		}
		return result;
	}

	public Object[] readRow(HSSFRow row) {
		return readRowAsList(row).toArray();
	}
	
	/*public Object[] readRowForUpdate(HSSFRow row) {
		ArrayList<Object> result = readRowAsList(row);
		result.add(pkHeader.getCellValue(row));
		return result.toArray();
	}
	
	public Object readPK(HSSFRow row){
		return pkHeader.getCellValue(row);
	}*/

}
