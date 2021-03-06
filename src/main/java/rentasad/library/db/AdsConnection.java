package rentasad.library.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Gustini GmbH
 * Creation: 15.12.2015 Library gustini.library.db
 * Last Update: 10.03.2020
 * <p>
 * Changed from inline Connection String to Config File based Connection-String
 *
 * @author Matthias Staud
 * <p>
 * <p>
 * Description: Stellt eine Verbindung zur ADS-Datenbank her.
 */

public class AdsConnection
{
	public static final String DEFAULT_CONFIG_FILE_PATH = "resources/config/adsConnection.ini";
	public static final String DEFAULT_SECTION_NAME = "ADS_CONNECTION";
	/**
	 *
	 */
	public static final String PARAMETER_NAME_ADS_HOST = "ADS_HOST";
	/**
	 * Parameter fuer Verbindungsport zur Datenbank. Default-Value: 6262
	 */
	public static final String PARAMETER_NAME_ADS_SOCKET = "ADS_SOCKET";

	/**
	 * Type of locking to use. <br>
	 * The valid values for this property are "proprietary" or "compatible". <br>
	 * The default is "proprietary". <br>
	 * <p>
	 * If the application is to be used with non-Advantage applications, then
	 * "compatible" <br>
	 * locking should be used. If the table will be used only by Advantage
	 * applications, then <br>
	 * "proprietary" locking should be used. When the TableType property is "adt",
	 * this property <br>
	 * is ignored and "proprietary" locking is always used. When "compatible"
	 * locking is chosen, <br>
	 * Advantage uses the appropriate style based on the table type. See Advantage
	 * Locking Modes <br>
	 * for more information.<br>
	 */
	public static final String PARAMETER_NAME_ADS_LOCK_TYPE = "LOCK_TYPE";
	/**
	 * Type of character data in the table. <br>
	 * The valid values for this property are <br>
	 * "ansi", <br>
	 * "oem" or a known collation name, such as <br>
	 * GERMAN_VFP_CI_AS_1252. <br>
	 * See AdsSetCollation for additional information. <br>
	 * The default is "ansi". <br>
	 * <br>
	 * This property indicates the type of character data to be stored in the table
	 * and how comparisons of character strings are performed. <br>
	 * If the property is set to "ansi" or "oem", the default ansi or oem collation
	 * of the server will be used. <br>
	 * For compatibility with DOS-based CA-Clipper applications, "oem" should be
	 * specified. <br>
	 * When TableType property is "adt", oem collation is never used When opening a
	 * database table, i.e., <br>
	 * table that is part of the Advantage Data Dictionary specified in the Catalog
	 * property, this parameter is ignored.<br>
	 * <br>
	 * The Advantage Server will use the information stored in the data dictionary
	 * to resolve the character data type.<br>
	 */
	public static final String PARAMETER_NAME_ADS_CHAR_TYPE = "CHAR_TYPE";
	/**
	 * Type of table. The valid values for this property are "adt", "vfp", "cdx" or
	 * "ntx".<br>
	 * If the catalog property specifies the path of a Advantage Data
	 * Dictionary,<br>
	 * this property is ignored except for executing the SQL statement "CREATE TABLE
	 * ... ".<br>
	 * If the catalog property specifies a directory where free tables are
	 * located,<br>
	 * this property applies to tables used in all SQL Statements.<br>
	 */
	public static final String PARAMETER_NAME_ADS_TABLE_TYPE = "TABLE_TYPE";

	/**
	 * The location of the database can be specified after the <hostname:port> <br>
	 * portion of the connection URL or it can be specified using the catalog
	 * property. <br>
	 * The following two URLs connect to the same database in the 'userdata' share
	 * on 'server1'. <br>
	 * <br>
	 * conn = DriverManager.getConnection (
	 * "jdbc:extendedsystems:advantage://server1:6262/userdata/db1/db.add" ); <br>
	 * conn = DriverManager.getConnection (
	 * "jdbc:extendedsystems:advantage://server1:6262;catalog =
	 * //server1/userdata/db1/db.add" ); <br>
	 * <br>
	 * <br>
	 * Although UNC path is the preferred method for specifying the location of the
	 * database, it is possible to use the drive <br>
	 * letter notation on the server to make the connection. For example, if the
	 * 'userdata' share on the 'server1' is actually <br>
	 * "c:\\userdata" on a Microsoft Windows 2003 server, the following URL can be
	 * use to obtain the connection. <br>
	 * <br>
	 * conn = DriverManager.getConnection (
	 * "jdbc:extendedsystems:advantage://server1:6262;catalog =
	 * c:\\userdata\\db1\\db.add" ); <br>
	 * <br>
	 * To connect to the Advantage Database Server to use free tables, specify the
	 * directory path as the catalog or specify the <br>
	 * directory path after the <hostname:port>. The following three statements will
	 * make the equivalent connection. <br>
	 * <br>
	 * conn = DriverManager.getConnection (
	 * "jdbc:extendedsystems:advantage://server1:6262/userdata/db2" ); <br>
	 * <br>
	 * conn = DriverManager.getConnection (
	 * "jdbc:extendedsystems:advantage://server1:6262;catalog =
	 * //server1/userdata/db2" ); <br>
	 * <br>
	 * <br>
	 * conn = DriverManager.getConnection (
	 * "jdbc:extendedsystems:advantage://server1:6262;catalog = c:\\userdata\\db2"
	 * ); <br>
	 * <br>
	 * <br>
	 * Note that when connecting to the Internet port of the Advantage Database
	 * Server, free table connection is not allowed. <br>
	 * An Advantage Data Dictionary must be used to authenticate the user. <br>
	 * <br>
	 * <br>
	 */
	public static final String PARAMETER_NAME_ADS_DATABASE_DICTIONARY = "DATABASE_DICTIONARY";
	private static Driver driver = null;
	private Map<String, String> configMap;

	/**
	 * @param configMap if should contain the following keys:
	 *                  <code>gustini.library.db.AdsConnection.PARAMETER_NAME_ADS_HOST</code>
	 *                  <code>gustini.library.db.AdsConnection.PARAMETER_NAME_ADS_SOCKET  (optional)</code>
	 *                  <code>gustini.library.db.AdsConnection.PARAMETER_NAME_ADS_DATABASE_DICTIONARY</code>
	 *                  <code>gustini.library.db.AdsConnection.PARAMETER_NAME_ADS_LOCK_TYPE</code>
	 *                  (optional)
	 *                  <code>gustini.library.db.AdsConnection.PARAMETER_NAME_ADS_CHAR_TYPE</code>
	 *                  (optional)
	 *                  <code>gustini.library.db.AdsConnection.PARAMETER_NAME_ADS_TABLE_TYPE</code>
	 *                  (optional)
	 *                  <p>
	 *                  of optional parameters not given, it was used default value.
	 * @throws SQLException
	 */
	public AdsConnection(final Map<String, String> configMap) throws SQLException
	{
		this.configMap = configMap;
	}

	public boolean isConfigMapValid(final Map<String, String> configMap)
	{
		boolean hostIsAvaible = configMap.containsKey(PARAMETER_NAME_ADS_HOST);
		boolean dictionaryIsAvaible = configMap.containsKey(PARAMETER_NAME_ADS_DATABASE_DICTIONARY);
		return hostIsAvaible && dictionaryIsAvaible;
	}

	/**
	 * Description: Stellt Verbindung unter Verwendung der uebergebenen
	 * Konfiguration her
	 *
	 * @return Creation: 15.12.2015 by mst
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException
	{
		return dbConnect(configMap);
	}

	/**
	 * Description:
	 *
	 * @param configMap Creation: 15.12.2015 by mst
	 * @throws SQLException
	 * @throws DbConnectionException
	 */
	public static Connection dbConnect(Map<String, String> configMap) throws SQLException
	{
		String host = configMap.get(PARAMETER_NAME_ADS_HOST);
		String socket = configMap.get(PARAMETER_NAME_ADS_SOCKET);
		String databaseDictionaty = configMap.get(PARAMETER_NAME_ADS_DATABASE_DICTIONARY);
		String lockType = configMap.get(PARAMETER_NAME_ADS_LOCK_TYPE);
		String charType = configMap.get(PARAMETER_NAME_ADS_CHAR_TYPE);
		String tableType = configMap.get(PARAMETER_NAME_ADS_TABLE_TYPE);
		String dsnProperties = String.format(";LockType=%s;CharType=%s;TableType=%s", lockType, charType, tableType);
		String connectionString = String.format("jdbc:extendedsystems:advantage:%s:%s%s%s", host, socket, databaseDictionaty, dsnProperties);

		try
		{
			initDriver();
			//            System.out.println(connectionString);
			return DriverManager.getConnection(connectionString);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e)
		{
			throw new SQLException(e);
		}
	}

	/**
	 * Description:
	 * <p>
	 * Creation: 15.12.2015 by mst
	 *
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static void initDriver() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		if (driver == null)
		{
			Class.forName("com.extendedsystems.jdbc.advantage.ADSDriver");
			//AdsConnection.driver = (Driver) Class.forName("com.extendedsystems.jdbc.advantage.ADSDriver").newInstance();
		}
	}

}
