package rosita.linkage.tests;

import java.util.ArrayList;

import rosita.linkage.filtering.DatabaseEncryptor;
import rosita.linkage.io.*;
import rosita.linkage.util.MappingConfig;
import rosita.linkage.*;

public class XMLTest 
{
	public static void main(String[] args)
	{
		XML_Reader xmlr = new XML_Reader("cfg/encryption_config.xml");

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
		de.setDoWrite(false);
		de.setOneBlock(true);
		de.setVerbose(true);
		de.encryptDB();


	}
}
