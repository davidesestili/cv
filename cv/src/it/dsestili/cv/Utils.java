package it.dsestili.cv;

/*
Download cv Utils class
Copyright (C) 2017 Davide Sestili

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class Utils 
{
	private static final Logger logger = Logger.getLogger(Utils.class);
	
	private static Connection connection = null;
	
	private static final String PROP_FILE_NAME = "config.properties";

	public static String getProperty(String key)
	{
		Properties prop = new Properties();
		InputStream input = null;
		String value = null;

		try
		{
			String propFileName = PROP_FILE_NAME;
			input = Utils.class.getClassLoader().getResourceAsStream(propFileName);
			
			if(input == null) 
			{
				logger.debug("File di properties non trovato " + propFileName);
				return null;
			}

			prop.load(input);
			value = prop.getProperty(key);
		}
		catch(IOException ex)
		{
			logger.debug("Errore di lettura dal file di properties", ex);
		}
		finally
		{
			if (input != null) 
			{
				try 
				{
					input.close();
				} 
				catch(IOException e) 
				{
					logger.debug("Errore di chiusura input stream", e);
				}
			}
		}
		
		return value;
	}

	public static String decodeBase64(String enc)
	{
		byte[] decodedBytes = Base64.getDecoder().decode(enc);
		return new String(decodedBytes);
	}

	public static void openConnection()
	{
		if(connection == null)
		{
			try 
			{
				String connectionString = getProperty("connectionString");
				String userName = decodeBase64(getProperty("userName"));
				String password = decodeBase64(getProperty("password"));
				
				connection = DriverManager.getConnection(connectionString, userName, password);
				logger.debug("Connessione riuscita");
			}
			catch(SQLException e) 
			{
				logger.debug("Errore di connessione", e);
			}
		}
	}

	public static void incrementDownloadCounter(HttpServletRequest request)
	{
		logger.debug("metodo incrementDownloadCounter()");
		
		openConnection();
		
		try
		{
			String incrementDownloadCounterQuery = getProperty("query.incrementDownloadCounter");
			
			PreparedStatement statement = connection.prepareStatement(incrementDownloadCounterQuery);
			statement.setString(1, request.getParameter(Constants.PARAM));
			statement.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
			statement.setString(3, request.getRemoteAddr());
			statement.setString(4, getRequestHeader(request));
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			logger.debug("Errore in incrementDownloadCounter()", e);
		}
		finally
		{
			if(connection != null)
			{
				try 
				{
					connection.close();
					connection = null;
					logger.debug("connessione chiusa");
				} 
				catch(SQLException e) 
				{
					logger.debug("Errore di chiusura connessione", e);
				}
			}
		}
	}
	
	public static int getDownloadCounter(String fileName)
	{
		logger.debug("metodo getDownloadCounter()");
		
		openConnection();
		
		int count = 0;
		try
		{
			String getDownloadCounerQuery = getProperty("query.getDownloadCounter");
			
			PreparedStatement statement = connection.prepareStatement(getDownloadCounerQuery);
			statement.setString(1, fileName);
			ResultSet rs = statement.executeQuery();
			rs.next();
			count = rs.getInt(1);
		}
		catch(Exception e)
		{
			logger.debug("Errore in getDownloadCounter()", e);
		}
		finally
		{
			if(connection != null)
			{
				try 
				{
					connection.close();
					connection = null;
					logger.debug("connessione chiusa");
				} 
				catch(SQLException e) 
				{
					logger.debug("Errore di chiusura connessione", e);
				}
			}
		}
		
		return count;
	}
	
	private static String getRequestHeader(HttpServletRequest request)
	{
		String request_header = "";
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements())
		{
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			request_header += key + ": " + value + "; ";
		}
		
		request_header = request_header.substring(0, request_header.length() -1);
		
		return request_header;
	}
}
