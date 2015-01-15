package rosita.linkage.tests;

import rosita.linkage.io.DatabaseConnection;
import rosita.linkage.io.XML_Reader;
import rosita.linkage.util.MappingConfig;
import rosita.linkage.filtering.*;

public class EncryptionTest 
{
	public static void main(String args[])
	{
		XML_Reader xmlr = new XML_Reader("cfg/encryption_config_create_encrypted_B_1000Mi.xml");

		DatabaseConnection readDBC = xmlr.getDatabaseConnection(XML_Reader.READER);
		String readTable = xmlr.getTableName(XML_Reader.READER);
		String[] readColumnNames = readDBC.getColumnNames(readTable);

		DatabaseConnection writeDBC = xmlr.getDatabaseConnection(XML_Reader.WRITER);
		String writeTable = xmlr.getTableName(XML_Reader.WRITER);
		String[] writeColumnNames = writeDBC.getColumnNames(writeTable);

		MappingConfig mapConfig = new MappingConfig(readTable, readColumnNames, writeTable, writeColumnNames, 
				xmlr.getMappedPairs(), xmlr.getBlockingPair());

		DatabaseEncryptor de = 
			new DatabaseEncryptor(readDBC, writeDBC, mapConfig);

		de.setMaxCount(10);
		de.setDoWrite(true);
		de.setOneBlock(false);
		de.setVerbose(true);
		
		de.encryptDB();
	}
}
