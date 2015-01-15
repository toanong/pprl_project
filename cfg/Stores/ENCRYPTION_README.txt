===========================================================
== Setting up encryption_config.xml README file
== Tutorial written by Brandon Abbott
== Last update: 16 Dec 2011
===========================================================

	This is a sample configuration file for the encryption
	procedure. Proper setup of this file will allow 
	rosita.linakge.filtering.DatabaseEncryptor to function
	properly.

	Requirements:
	1. A database reader must be properly configured.

	<DatabaseReaderConnection>
		<database> DatabaseName </database>
		<table>TableName</table>
		<url> jdbc:mysql://localhost/ </url>
		<user> root </user>
		<password> mypassword </password>
	</DatabaseReaderConnection>


	2. A database writer must be properly configured.

	<DatabaseWriterConnection>
		<database> DatabaseName </database>
		<table>TableName</table>
		<url> jdbc:mysql://localhost/ </url>
		<user> root </user>
		<password> mypassword </password>
	</DatabaseWriterConnection>

	3. The attributes of one reading database must be
	mapped to the attributes of the writing database.
	That is, the program needs to know if "Person_SSN" 
	in the read table should be written to "SocialSecurtiyNumber"
	in another table. 

	Currently, there is normalization support for...
	SSN, SEX, PHONE

	Simply set attr="TYPE" in the mapped pair object to
	have those records normalized before the encryption
	procedure takes place.

	A MappedPair object should be created for each value 
	that you would like to be encrypted & written.

	<DatabaseMap>
		<MappedPair attr="SSN">
			<read_col>Person_SSN</read_col>
			<write_col>SocialSecurityNumber</write_col>
		</MappedPair>
	</DatabaseMap>


	4. There must be a date of birth column in the reader set.
	Let <encryption_config> know what it is by specifying it in
	<Blocking>
		<read_col>Date_of_Birth</read_col>	
		...
	</Blocking>

	5. There must be a place to put the resulting date of birth
	calculated hash value.  Let <encryption_config> know what it is
	by specifying it in 
	<Blocking>
		...
		<write_col>Date_of_Birth</write_col>	
	</Blocking>

