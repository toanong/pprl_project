package rosita.linkage.tools;

import java.sql.ResultSet;
import java.util.Random;

import rosita.linkage.main;
import rosita.linkage.io.DatabaseConnection;
import rosita.linkage.tests.DataCorrupterTest.CorruptedResult;



public class MissingDataCreator {
	
	public MissingDataCreator(){
		
	}
	
	static String validateString(String parStr){
		String result = parStr;
		
		result = result.replace("'", "''").trim();
		
		return result;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Random rand = new Random();
		String tblSourceTable ="tz.sourceb5K_15_missing";
		
		//Connect to the database
		
		DatabaseConnection dbcRead = new DatabaseConnection("jdbc:postgresql://localhost:5431/", main.mysqlDriver, "postgres", "postgres", "postgres");
		DatabaseConnection dbcWrite = new DatabaseConnection("jdbc:postgresql://localhost:5431/", main.mysqlDriver, "postgres", "postgres", "postgres");

		ResultSet sqlResults = dbcRead.getTableQuery("SELECT * FROM "+tblSourceTable);

		String[][] dataRow; int count = 0;
		while ((dataRow = dbcRead.getNextResultWithColName(sqlResults)) != null){
			
			String strUpdateCmd = "UPDATE "+tblSourceTable+" SET ";
			String strSnn_pk="";
			
			for(int i=0;i<9;i++){
				if(dataRow[0][i].equals("id")){
					strSnn_pk = dataRow[1][i];
				}
			}
			for(int i=0;i<9;i++){
				if(!dataRow[0][i].equals("id")){
					int rndNum = rand.nextInt(100);
					
					String strValue = "";
					if(rndNum<15){
						strValue = "NULL ";
					}else{
						strValue = "'"+validateString(dataRow[1][i])+"'";
					}
					
					strUpdateCmd += dataRow[0][i]+"="+strValue+",";
				}
			}
			
			strUpdateCmd = strUpdateCmd.substring(0, strUpdateCmd.length()-1);
			
			strUpdateCmd += " WHERE id='"+strSnn_pk+"'";
			dbcWrite.executeQuery(strUpdateCmd);
		}
	}
}
