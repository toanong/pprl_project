package rosita.linkage.tests;

import rosita.linkage.io.DatabaseConnection;
import rosita.linkage.io.XML_Reader;
import rosita.linkage.util.MappingConfig;
import rosita.linkage.analysis.DBMS;
import rosita.linkage.filtering.*;

public class EncryptionTest 
{
	public static void main(String args[])
	{
		XML_Reader xmlr = new XML_Reader("cfg/enc_conf_source1b_set.xml");

		if(xmlr.getDBMS().equals(DBMS.MySQL)){
			DatabaseConnection readDBC = xmlr.getDatabaseConnection(XML_Reader.READER);
			String readTable = xmlr.getTableName(XML_Reader.READER);
			String[] readColumnNames = readDBC.getColumnNames(readTable);
	
			DatabaseConnection writeDBC = xmlr.getDatabaseConnection(XML_Reader.WRITER);
			String writeTable = xmlr.getTableName(XML_Reader.WRITER);
			String[] writeColumnNames = writeDBC.getColumnNames(writeTable);
			MappingConfig mapConfig = new MappingConfig(readTable, readColumnNames, writeTable, writeColumnNames, 
				xmlr.getMappedPairs(), xmlr.getBlockingPair());
			DatabaseEncryptor de = 
					new DatabaseEncryptor(readDBC, writeDBC, mapConfig,xmlr.getDBMS());

			de.setMaxCount(10);
			de.setDoWrite(true);
			de.setOneBlock(false);
			de.setVerbose(true);
			
			de.encryptDB();

		}else if(xmlr.getDBMS().equals(DBMS.PostgreSQL)){
			DatabaseConnection readDBC = xmlr.getDatabaseConnection(XML_Reader.READER);
			String readTable = xmlr.getTableName(XML_Reader.READER);
			String[] readColumnNames = readDBC.getColumnNames(readDBC.getSchema()+"."+readTable);
	
			DatabaseConnection writeDBC = xmlr.getDatabaseConnection(XML_Reader.WRITER);
			String writeTable = xmlr.getTableName(XML_Reader.WRITER);
			String[] writeColumnNames = writeDBC.getColumnNames(writeDBC.getSchema()+"."+writeTable);
			MappingConfig mapConfig = new MappingConfig(readTable, readColumnNames, writeTable, writeColumnNames, 
				xmlr.getMappedPairs(), xmlr.getBlockingPair());
			DatabaseEncryptor de = 
					new DatabaseEncryptor(readDBC, writeDBC, mapConfig, xmlr.getDBMS());

			de.setMaxCount(10);
			de.setDoWrite(true);
			de.setOneBlock(false);
			de.setVerbose(true);
			
			de.encryptDB();
		}
	}
}
