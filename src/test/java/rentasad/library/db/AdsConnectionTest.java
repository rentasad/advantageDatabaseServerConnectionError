package rentasad.library.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdsConnectionTest
{
	private Map<String, String> configMap;

	@BeforeEach public void beforeAll()
	{
		String host = "//192.168.111.30";
		//		String host = "//192.168.130.10";
		String socket = "6262";
		String databaseDictionaty = "/vs4/TMPF01/";
		String lockType = "proprietary";
		String charType = "OEM";
		String tableType = "cdx";
		this.configMap = new HashMap<>();
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_HOST, host);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_SOCKET, socket);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_DATABASE_DICTIONARY, databaseDictionaty);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_LOCK_TYPE, lockType);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_CHAR_TYPE, charType);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_TABLE_TYPE, tableType);

	}

	@Test public void testDbConnectionWithParameters() throws Exception
	{

		Connection con = AdsConnection.dbConnect(configMap);
		con.close();

	}

	@Test public void encodingCheckQuarterAddress() throws Exception
	{

		String query = "SELECT NAME FROM F99\\ADRESSEN\\V2AD1001 WHERE NUMMER = '1100001159'";
		String host = "//192.168.130.10";
		String socket = "6262";
		String databaseDictionaty = "/vs4/VS/DG/VC2/";
		String lockType = "proprietary";
		String charType = "OEM";
		String tableType = "cdx";
		HashMap<String, String> configMap = new HashMap<String, String>();
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_HOST, host);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_SOCKET, socket);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_DATABASE_DICTIONARY, databaseDictionaty);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_LOCK_TYPE, lockType);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_CHAR_TYPE, charType);
		configMap.put(AdsConnection.PARAMETER_NAME_ADS_TABLE_TYPE, tableType);
		Connection con = AdsConnection.dbConnect(configMap);

		PreparedStatement ps = con.prepareStatement(query);

		ResultSet rs = ps.executeQuery();
		if (rs.next())
		{
			final String expected = "Müller";
			final String fieldName = "NAME";
			String normalFieldValue = rs.getString(fieldName);
			byte[] textBytes = rs.getBytes(fieldName);
			//					rs.getString(fieldName).getBytes("Cp1252");
			String actualText = new String(textBytes, "Cp1252");
			assertEquals(expected, actualText);
			assertEquals(expected, normalFieldValue);
			System.out.println(actualText);
		}
	}
}
