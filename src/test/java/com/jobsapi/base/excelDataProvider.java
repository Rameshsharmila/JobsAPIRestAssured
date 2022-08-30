package com.jobsapi.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelDataProvider {

	public static FileInputStream fipstr;
	public static FileOutputStream fopstr;
	public static XSSFWorkbook workbook;
	public static XSSFSheet worksheet;
	public static XSSFRow row;
	public static XSSFCell cell;
	
	
	public static int getRowCount(String xlFile, String xlSheet) throws IOException {
		fipstr = new FileInputStream(xlFile);
		workbook = new XSSFWorkbook(fipstr);
	//	worksheet = workbook.getSheet(xlSheet);
		worksheet = workbook.getSheetAt(0);
		int rowCount = worksheet.getLastRowNum();
		workbook.close();
		fipstr.close();
		
		return rowCount;
		}
	
	public static int getCellCount(String xlFile, String xlSheet, int rowNum) throws IOException{
		fipstr = new FileInputStream(xlFile);
		workbook = new XSSFWorkbook(fipstr);
//		worksheet = workbook.getSheet(xlSheet);
		worksheet = workbook.getSheetAt(0);
		row = worksheet.getRow(rowNum);
		int columnCount = row.getLastCellNum();
		workbook.close();
		fipstr.close();
		
		return columnCount;
		}
	
	public static String getCellData(String xlFile, String xlSheet, int rowNum, int columnNum) throws IOException{
		fipstr = new FileInputStream(xlFile);
		workbook = new XSSFWorkbook(fipstr);
//		worksheet = workbook.getSheet(xlSheet);
		worksheet = workbook.getSheetAt(0);
		row = worksheet.getRow(rowNum);
		cell = row.getCell(columnNum);
		String data;
		
		
		try {
				DataFormatter formatter = new DataFormatter();
				data = formatter.formatCellValue(cell);
			} catch (Exception e) {
				data = "";
			}
		workbook.close();
		fipstr.close();
		
		return data;
			
	}
		
	public static void setCellData(String xlFile, String xlSheet, int rowNum, int columnNum, String data) throws IOException {
		fipstr = new FileInputStream(xlFile);
		workbook = new XSSFWorkbook(fipstr);
		worksheet = workbook.getSheet(xlSheet);
		row = worksheet.getRow(rowNum);
		cell = row.createCell(columnNum);
		cell.setCellValue(data);
		fopstr = new FileOutputStream(xlFile);
		workbook.write(fopstr);
		workbook.close();
		fopstr.close();
		fipstr.close();
		}
		
	
	
	
}
