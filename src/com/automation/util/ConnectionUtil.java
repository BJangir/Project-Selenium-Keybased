package com.automation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil {
	Connection connection=null;
	 static ConfigReader reader=new ConfigReader();
	 final static Logger logger = Logger.getLogger(ConnectionUtil.class);
	public Connection getConnection(){
		try {
			Class.forName("org.postgresql.Driver");
			Properties props = new Properties();
			props.setProperty("user",reader.getKeyValue("db.user"));
			props.setProperty("password",reader.getKeyValue("db.password"));
			props.setProperty("ssl",reader.getKeyValue("db.ssl"));
			props.setProperty("sslfactory",reader.getKeyValue("db.sslfactory"));
			connection =DriverManager.getConnection(reader.getKeyValue("db.url"), props);
			if(connection!=null){
				logger.info("Connection to DB is SUCCESS");
			}
			return connection;
		}catch (Exception e) {
			logger.error("Connection to DB is FAILED");
          e.printStackTrace();
		}
		return connection;
	}
	
	public List<String[]> executeSelectQuery(String query){
		List<String[]> result=new ArrayList<>();
		try{
			if(connection ==null){
				getConnection();
				}
			logger.info("Running Query "+query);
			Statement createStatement = connection.createStatement();
			ResultSet rs1 = createStatement.executeQuery(query);
			ResultSetMetaData metaData = rs1.getMetaData();
			int columnCount = metaData.getColumnCount();
			logger.info("Total Column Count"+columnCount);
			
			String[] column=new String[columnCount];
			for(int i=0;i<metaData.getColumnCount();i++){
				column[i] = metaData.getColumnName(i+1);
			}
			
			result.add(column);
			while (rs1.next()) {
				String[] row=new String[columnCount];
				for(int i=0;i<columnCount;i++){
					row[i]=rs1.getString(i+1);
				}
				result.add(row);
			}
	
		}catch (Exception e) {
			logger.error("Error while executing query"+query);
		}
	return result;
	}
	
	
	public  void  updateRecords( String updatequery, PreparedStatement preparedStmt)  {
		try{
			if(connection ==null){
				getConnection();
				}
		      preparedStmt.executeUpdate();
		}
	catch (Exception e) {
		logger.error("Error while updating query"+updatequery);
	}
	}
	
	public  void  closeConnection()  {
		try{
			if(connection !=null){
				connection.close();
				}
		}
	catch (Exception e) {
		logger.error("Error while closing Connection");
	}
		finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
}
