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
	
	private static final String PARAM = "param";
	private static final String CV_PARAM = "cv";
	private static final String CV_FILENAME = "cv.fileName";
	
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
		String param = (String)request.getParameter(PARAM);

		if(param != null && param.trim().equals(CV_PARAM))
		{
			String pathName = Utils.getProperty(CV_PARAM);
			byte[] data = getData(pathName);
			
			String fileName = Utils.getProperty(CV_FILENAME);
			response.setContentType("application/msword");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
		    response.setHeader("Cache-Control", "no-cache");
		    response.setHeader("Expires", "-1");
		    
		    response.getOutputStream().write(data);

			Utils.incrementDownloadCounter(param);
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
