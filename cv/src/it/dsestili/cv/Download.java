/*
Download cv servlet
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

package it.dsestili.cv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class Download
 */
@WebServlet("/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger logger = Logger.getLogger(Download.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Download() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String param = (String)request.getParameter(Constants.PARAM);

		if(param != null && param.trim().equals(Constants.CV_PARAM))
		{
			String pathName = Utils.getProperty(Constants.CV_PARAM);
			byte[] data = getData(pathName);
			
			String fileName = Utils.getProperty(Constants.CV_FILENAME);
			response.setContentType("application/msword");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
		    response.setHeader("Cache-Control", "no-cache");
		    response.setHeader("Expires", "-1");
		    
		    response.getOutputStream().write(data);

			Utils.incrementDownloadCounter(request);
			logger.debug("file scaricato e contatore incrementato");
		}
		else
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter fileName missing");
		}
	}
	
	protected byte[] getData(String fileName) throws IOException
	{
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		byte[] data = new byte[(int) file.length()];
		bis.read(data);
		
		bis.close();
		
		return data;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
