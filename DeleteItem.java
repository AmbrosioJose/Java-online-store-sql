package OnlineStore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteItem")
public class DeleteItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );

        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
        }
        catch( ClassNotFoundException e )
        {
            throw new ServletException( e );
        }
    }	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//        request.getRequestDispatcher( "Inventory" ).forward(request, response );
		//int idTemp = request.getParameter("id") == null? -1 : Integer.parseInt(request.getParameter("id"));
		 int idTemp = Integer.parseInt(request.getParameter("id"));
		    
		 Connection c = null;
		 try
	        {
	            String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu24";
	            String username = "cs3220stu24";
	            String password = "#H5PEhVC";

	            c = DriverManager.getConnection( url, username, password );
	            Statement stmt = c.createStatement();
	            

              String deleteSt = "DELETE FROM `itemList` WHERE id = ?";
                          
              PreparedStatement pstmt = c.prepareStatement(deleteSt);
              
              pstmt.setInt(1, idTemp);
              pstmt.executeUpdate();    
	            
	        }
	        catch( SQLException e )
	        {
	            throw new ServletException( e );
	        }
	        finally
	        {
	            try
	            {
	                if( c != null ) c.close();
	            }
	            catch( SQLException e )
	            {
	                throw new ServletException( e );
	            }
	        }
	        

		 response.sendRedirect( "Inventory" ); 

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 
		doGet(request, response);
	}

}
