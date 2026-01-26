package com.terracetech.tims.webmail.common.manager;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;

public class GeoIpManager {

	private static final String dbname = "GeoLite2-Country.mmdb";
	
	public boolean isInCountry(String ip, String countryCode) {
        DatabaseReader reader = null;
        try {
            reader = getDBReader();
            Country country = getCountry(ip);
            if (country == null) {
            	return false;
            }
            return countryCode.equals(country.getIsoCode());
        } catch (Exception e) {
            LogManager.writeErr("fail to read GeoIP DB..{}", e.getMessage());
            return false;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            	LogManager.writeDebug("fail to close GeoIP DB..{}", e.getMessage());
            }
        }
    }
	
	public Country getCountry(String ip) {
        DatabaseReader reader = null;
        try {
            reader = getDBReader();
            CountryResponse country = reader.country(InetAddress.getByName(ip));
            return country.getCountry();
        } catch (Exception e) {
            LogManager.writeErr("fail to read GeoIP DB..{}", e.getMessage());
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            	LogManager.writeDebug("fail to close GeoIP DB..{}", e.getMessage());
            }
        }
    }
	
	private DatabaseReader getDBReader() throws IOException, URISyntaxException {
		String geoIpDbPath = EnvConstants.getUtilSetting("geoip.db.path");
        File ipFile = null;
        if (geoIpDbPath == null) {
            ipFile = new File(DatabaseReader.class.getResource("/" + dbname).toURI());
        } else {
            ipFile = new File(geoIpDbPath);
        }
        return new DatabaseReader.Builder(ipFile).build();
    }
}
