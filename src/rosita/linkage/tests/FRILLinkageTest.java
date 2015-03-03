package rosita.linkage.tests;

import java.util.ArrayList;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.collections.map.HashedMap;
import org.bouncycastle.util.test.Test;

import com.sun.corba.se.spi.orb.StringPair;

import cdc.components.AbstractDataSource;
import cdc.components.AbstractDistance;
import cdc.components.AbstractJoin;
import cdc.components.AbstractResultsSaver;
import cdc.configuration.ConfiguredSystem;
import cdc.datamodel.converters.ConverterColumnWrapper;
import cdc.datamodel.converters.ModelGenerator;
import cdc.datamodel.DataColumnDefinition;
import cdc.gui.LinkageProcessStarter;
import cdc.impl.conditions.WeightedJoinCondition;
import cdc.impl.datasource.jdbc.JDBCDataColumnDefinition;
import cdc.impl.datasource.jdbc.JDBCDataSource;
import cdc.impl.distance.EditDistance;
import cdc.impl.distance.EqualFieldsDistance;
import cdc.impl.join.nestedloop.NestedLoopJoin;
import cdc.impl.resultsavers.CSVFileSaver;
import cdc.utils.RJException;
import rosita.linkage.FRILLinker;
import rosita.linkage.MappedPair;
import rosita.linkage.analysis.Algorithm;
import rosita.linkage.io.DatabaseConnection;
import rosita.linkage.io.XML_Reader;
import rosita.linkage.util.MappingConfig;

public class FRILLinkageTest {

	public static void main(String[] args) 
	{
		FRILLinker myFRILLinker = new FRILLinker("cfg/Test1_DIST_IMPU_encrypted.xml");
		myFRILLinker.link();
	}
}
