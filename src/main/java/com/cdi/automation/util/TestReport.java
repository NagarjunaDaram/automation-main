package com.cdi.automation.util;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestReport {
	public static String GenerateReport(String filename) {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Test Report");
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 4000);

		Row header = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillForegroundColor((short) 1);
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		//font.setColor(cyan);
		font.setFontHeightInPoints((short) 11);
		font.setBold(false);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("TestScenarioId");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Data Element");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(2);
		headerCell.setCellValue("Provider");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(3);
		headerCell.setCellValue("Provider Integration");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(4);
		headerCell.setCellValue("Consumer");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(5);
		headerCell.setCellValue("Consumer Integration");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(6);
		headerCell.setCellValue("Owner");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(7);
		headerCell.setCellValue("Flow");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(8);
		headerCell.setCellValue("Test Scenario");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(9);
		headerCell.setCellValue("Test Case #");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(10);
		headerCell.setCellValue("Test Case");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(11);
		headerCell.setCellValue("Result");
		headerCell.setCellStyle(headerStyle);
		
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		/*
		 * Row row = sheet.createRow(2); Cell cell = row.createCell(0);
		 * cell.setCellValue("test"); cell.setCellStyle(style);
		 * 
		 * cell = row.createCell(1); cell.setCellValue(20); cell.setCellStyle(style);
		 */
		
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			workbook.write(outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Yes";
		
		
		

		
		
	}
	

}
