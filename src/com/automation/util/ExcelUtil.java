package com.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static void main(String[] args) throws IOException {
		ExcelUtil util=new ExcelUtil();
		List<String[]> excelData = util.getExcelData("C:\\Users\\Administrator\\Documents\\DemoTest.xlsx", "Pages", "5");
		System.out.println(excelData.size());
		
	}
	
	

	
	public  List<String[]>  getExcelData(String excelFilePath,String sheet,String colno) throws IOException{
		 FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		 List<String[]> inputData=new ArrayList<>();
		 Workbook workbook = new XSSFWorkbook(inputStream);
		 Sheet mysheet= workbook.getSheet(sheet);
	     Iterator<Row> iterator = mysheet.iterator();
	     boolean isHeader=true;
	     int columnstoRead=0;
	        while(iterator.hasNext()){
	        	if(isHeader){
	        	iterator.next();
	        		isHeader=false;
	        		columnstoRead=Integer.parseInt(colno);
	        		continue;
	        	}
	        	 Row nextRow = iterator.next();
	        	Iterator<Cell> cellIterator = nextRow.cellIterator();
	             int currentcellno=0;
	             String[] myrow=new String[columnstoRead];
	            while (cellIterator.hasNext()) {
	            	if(currentcellno==columnstoRead){	            		
	            		break;
	            	}
	            	Cell cell = cellIterator.next();
	                switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_STRING:
	                    	myrow[currentcellno]=cell.getStringCellValue();
	                        break;
	                    case Cell.CELL_TYPE_BOOLEAN:
	                    	myrow[currentcellno]=cell.getBooleanCellValue()+"";
	                        break;
	                    case Cell.CELL_TYPE_NUMERIC:
	                        myrow[currentcellno]=cell.getNumericCellValue()+"";
	                        break;
	                }
	                currentcellno++;
	            }
	            inputData.add(myrow);
	            
	        	
	        	
	        	
	        }
	        
		return inputData;
		
	}
	
	
	
	
	
	public  List<String[]>  getExcelDataBasedOnRunCol(String excelFilePath,String sheet,int columnstoRead) throws IOException{
		 FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		 List<String[]> inputData=new ArrayList<>();
		 Workbook workbook = new XSSFWorkbook(inputStream);
		 Sheet mysheet= workbook.getSheet(sheet);
	     Iterator<Row> iterator = mysheet.iterator();
	     boolean isHeader=true;
	        while(iterator.hasNext()){
	        	if(isHeader){
	        	iterator.next();
	        		isHeader=false;
	        		continue;
	        	}
	        	 Row nextRow = iterator.next();
	        	Iterator<Cell> cellIterator = nextRow.cellIterator();
	             int currentcellno=0;
	             String[] myrow=new String[columnstoRead];
	            while (cellIterator.hasNext()) {
	            	if(currentcellno==columnstoRead){
	            		break;
	            	}
	            	Cell cell = cellIterator.next();
	            	String cellValue = "";
	                switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_STRING:
	                    	cellValue=cell.getStringCellValue();
	                        break;
	                    case Cell.CELL_TYPE_BOOLEAN:
	                    	cellValue=cell.getBooleanCellValue()+"";
	                        break;
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	cellValue=cell.getNumericCellValue()+"";
	                        break;
	                }
	                if((currentcellno+1==columnstoRead) && cellValue.equalsIgnoreCase("Yes")){
	                	  inputData.add(myrow);
	                	  break;
	                }
	                else{
	                	 myrow[currentcellno]=cellValue;
	                }
	                currentcellno++;
	            }
	           
	            
	        	
	        	
	        	
	        }
	        
		return inputData;
		
	}

}
