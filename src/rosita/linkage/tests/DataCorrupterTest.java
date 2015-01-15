package rosita.linkage.tests;

import java.sql.ResultSet;
import java.util.Random;

import rosita.linkage.main;
import rosita.linkage.filtering.E_Record;
import rosita.linkage.io.DatabaseConnection;
import rosita.linkage.tools.DataCorrupter;

public class DataCorrupterTest {
	
	public static class CorruptedResult{
		String strCorruptedString;
		int intType;
		
		public CorruptedResult(String parStr, int parType){
			strCorruptedString = parStr;
			intType = parType;
		}
	}

	private static int FIRSTNAME_PROP = 40;
	private static int LASTNAME_PROP = 40;
	private static int ZIP_PROP = 40;
	private static int SSN_PROP = 40;

	private static DataCorrupter  corrupter = new DataCorrupter();

	public static String toProperCase(String parStr){
		String result = parStr;
		
		if(parStr.length()>0){
			result = parStr.substring(0,1).toUpperCase()+parStr.substring(1,parStr.length()).toLowerCase();
		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String tblSourceTable ="tz.sourcebi_100K";
		
		//Connect to the database
		
//		DatabaseConnection dbcRead = new DatabaseConnection("jdbc:mysql://140.226.182.109:3306/", main.mysqlDriver, "pprl", "ongt", "toor3");
//		DatabaseConnection dbcWrite = new DatabaseConnection("jdbc:mysql://140.226.182.109:3306/", main.mysqlDriver, "pprl", "ongt", "toor3");

		DatabaseConnection dbcRead = new DatabaseConnection("jdbc:postgresql://localhost:5432/", main.mysqlDriver, "rosita", "rosita", "rosita@2012");
		DatabaseConnection dbcWrite = new DatabaseConnection("jdbc:postgresql://localhost:5432/", main.mysqlDriver, "rosita", "rosita", "rosita@2012");
		ResultSet sqlResults = dbcRead.getTableQuery("SELECT * FROM "+tblSourceTable);
		
		int intCount= 0;
		String strCache ="";
		
		String[][] dataRow; int count = 0;
		while ((dataRow = dbcRead.getNextResultWithColName(sqlResults)) != null) {
			
			String strUpdateCmd = "UPDATE "+tblSourceTable+" SET ";
			String strSnn_pk="";
			
			for(int i=0;i<dataRow[0].length;i++){
				if(dataRow[0][i]!=null){
					if(dataRow[0][i].equals("x_demographic_id")){
						strSnn_pk = dataRow[1][i];
						if(strSnn_pk.equals("7162")){
							int jj = 0 ;
							jj++;
						}
					}
				}
			}
			for(int i=1;i<dataRow[0].length-8;i++){

				String strValue = "";
				int intType = 0;
				int intProp = 5;
				Boolean shouldbeCorrupted = false;
				
				if(dataRow[0][i].equals("first") || dataRow[0][i].equals("last") || dataRow[0][i].equals("city") || dataRow[0][i].equals("county") || dataRow[0][i].equals("address_1")){
					shouldbeCorrupted = true;
				}else if(dataRow[0][i].equals("zip") || dataRow[0][i].equals("phone") || dataRow[0][i].equals("bdate") || dataRow[0][i].equals("acct") || dataRow[0][i].equals("sec")  || dataRow[0][i].equals("expr_date")){
					intType = 1;
					shouldbeCorrupted = true;
				}else if(dataRow[0][i].equals("mi") || dataRow[0][i].equals("country") || dataRow[0][i].equals("card")){
					intType = 3;
					intProp = 5;
					shouldbeCorrupted = true;
				}else if(dataRow[0][i].equals("sex")){
					intType = 2;
					intProp = 5;
					shouldbeCorrupted = true;
				}
				
				if(shouldbeCorrupted==true){
					strValue = corrupt(dataRow[1][i], intType, intProp).strCorruptedString;
					
					String strWasCorrupted = "FALSE";
					
					if(!dataRow[1][i].toUpperCase().equals(strValue.toUpperCase())){
						strWasCorrupted = "TRUE";
					}
					
					strUpdateCmd += dataRow[0][i]+"='"+validateString(strValue)+"',  "+dataRow[0][i]+"_corrupt="+strWasCorrupted+",";
				}
			}
			
			strUpdateCmd = strUpdateCmd.substring(0, strUpdateCmd.length()-1);
			
			strUpdateCmd += " WHERE x_demographic_id='"+strSnn_pk+"';";
			
			if(intCount<1000){
				intCount++;
				strCache += strUpdateCmd;
			}else{
				dbcWrite.executeQuery(strCache);
				intCount = 0;
				strCache ="";
			}
		}
		
		if(intCount>0){
			dbcWrite.executeQuery(strCache);
		}
	}
	
	static String validateString(String parStr){
		String result = parStr;
		
		result = result.replace("'", "''");
		
		return result;
	}
	
	/**
	 * 
	 * @param parStr - The string to corrupt
	 * @param intType - There are two types 0: normal, 1: ssn
	 * @param parProp - the probability that the string is corrupted
	 * @return
	 */
	private static CorruptedResult corrupt(String parStr, int intType, int parProp){
		CorruptedResult result = new CorruptedResult(parStr, 0);
		
		Random rnd = new Random();
		int x = rnd.nextInt(100);
		if( x < parProp){
			//Type = 0: Text
			//Type = 1: Number 
			//Type = 2: Gender
			//Type = 3: Substitute only 
			if(intType == 0){
				int y = rnd.nextInt(4);
				if(y==0){ result.strCorruptedString = corrupter.createTypoInsertError(parStr); result.intType = y + 1; }
				else if (y==1){ result.strCorruptedString = corrupter.createTypoDeleteError(parStr); result.intType = y + 1;}
				else if (y==2){ result.strCorruptedString = corrupter.createTypoSubstituteError(parStr); result.intType = y + 1;}
				else if (y==3){ result.strCorruptedString = corrupter.createTypoTransposeError(parStr); result.intType = y + 1;}
			}else if(intType == 1){
				result.strCorruptedString = corrupter.createNumberError(parStr);
				result.intType = 1;
			}else if(intType == 2){
				result.strCorruptedString = corrupter.createGenderError(parStr);
			}else if(intType ==3){
				result.strCorruptedString = corrupter.createTypoSubstituteError(parStr);
			}
		}
		
		return result;
	}
	
	private void runSingleTests(){
		String testString= "love";
		String testSSNString = "111-11-1111";
		System.out.println("Insert     : " + corrupter.createTypoInsertError(testString));
		System.out.println("Delete     : " + corrupter.createTypoDeleteError(testString));
		System.out.println("Substitute : " + corrupter.createTypoSubstituteError(testString));
		System.out.println("Transpose  : " + corrupter.createTypoTransposeError(testString));
		System.out.println("SSN        : " + corrupter.createNumberError(testSSNString));
	}

}
