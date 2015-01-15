package rosita.linkage.tools;

import java.sql.ResultSet;
import java.util.Random;

import rosita.linkage.main;
import rosita.linkage.io.DatabaseConnection;
import rosita.linkage.tests.DataCorrupterTest.CorruptedResult;



public class MissingDataCreator {
	
	public MissingDataCreator(){
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Random rand = new Random();
		String tblSourceTable ="TblSourceB_3000i_missing";
		
		//Connect to the database
		
		DatabaseConnection dbcRead = new DatabaseConnection("jdbc:mysql://140.226.132.40:3306/", main.mysqlDriver, "pprl", "ongt", "toor3");
		DatabaseConnection dbcWrite = new DatabaseConnection("jdbc:mysql://140.226.132.40:3306/", main.mysqlDriver, "pprl", "ongt", "toor3");

		ResultSet sqlResults = dbcRead.getTableQuery("SELECT * FROM "+tblSourceTable);

		String[][] dataRow; int count = 0;
		while ((dataRow = dbcRead.getNextResultWithColName(sqlResults)) != null){
			
			String strUpdateCmd = "UPDATE "+tblSourceTable+" SET ";
			String strSnn_pk="";
			
			for(int i=0;i<dataRow[0].length;i++){
				if(dataRow[0][i].equals("ssn_pk")){
					strSnn_pk = dataRow[1][i];
				}
			}
			for(int i=1;i<dataRow[0].length-1;i++){
				
				int rndNum = rand.nextInt(100);
				
				String strValue = "";
				if(rndNum<10){
					strValue = "NULL ";
				}else{
					strValue = "'"+dataRow[1][i]+"'";
				}
				
				strUpdateCmd += dataRow[0][i]+"="+strValue+",";
			}
			
			strUpdateCmd = strUpdateCmd.substring(0, strUpdateCmd.length()-1);
			
			strUpdateCmd += " WHERE ssn_pk='"+strSnn_pk+"'";
			dbcWrite.executeQuery(strUpdateCmd);
		}
	}
}
