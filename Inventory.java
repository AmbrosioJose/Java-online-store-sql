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
import javax.servlet.http.HttpSession;


import OnlineStore.itemClass;


@WebServlet("/Inventory")
public class Inventory extends HttpServlet {
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
		
        
        List<itemClass> inventorys = new ArrayList<itemClass>();
        
        Connection c = null;
        try
        {
            String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu24";
            String username = "cs3220stu24";
            String password = "#H5PEhVC";

            c = DriverManager.getConnection( url, username, password );
            Statement stmt = c.createStatement();
        	ResultSet rs = stmt.executeQuery( "select * from itemList" );
        	 while( rs.next() )
             {
             	itemClass item = new itemClass( rs.getInt( "id" ), rs.getString( "name" ), rs.getString( "description" ), rs.getInt("quantity"),rs.getDouble("price"));
             	
             	inventorys.add( item );         	
             }
    
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
        
        
        request.setAttribute( "inventorys", inventorys );
   

        request.getRequestDispatcher( "/WEB-INF/Inventory.jsp" ).forward(request, response );
        

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		int quantity = Integer.parseInt(request.getParameter("quantity")); 
		double price = Double.parseDouble(request.getParameter("price")); 
      
		List<itemClass> inventorys = new ArrayList<itemClass>();
		
		String nameTemp ;
		int quanTemp=0;
		
		int idTemp=0;
		double priceTemp =0;
		Connection c = null;
        try
        {
            String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu24";
            String username = "cs3220stu24";
            String password = "#H5PEhVC";

            c = DriverManager.getConnection( url, username, password );
            
            Statement stmt = c.createStatement();
        	ResultSet rs = stmt.executeQuery( "select * from itemList" );
        	 while( rs.next() )
             {
        		if(rs.getString("name") == name){
        			idTemp = rs.getInt("id");
        			priceTemp = rs.getDouble("price");
        			quanTemp = rs.getInt("quantity");
        		}
        	
        		itemClass item = new itemClass( rs.getInt( "id" ), rs.getString( "name" ), rs.getString( "description" ), rs.getInt("quantity"),rs.getDouble("price"));
             	inventorys.add( item );
        	
             }

        	if(idTemp == 0){
	            String updateSt = "INSERT INTO `itemList` (`name`, `description`, `quantity`, `price`) VALUES ( ?, ?, ?, ?)";
	                        
	            		
	            PreparedStatement pstmt = c.prepareStatement(updateSt);
	            
	            pstmt.setString(1, name);
	            pstmt.setString(2, description);
	            pstmt.setInt(3, quantity);
	            pstmt.setDouble(4, price);
	            pstmt.executeUpdate();
        	}else{
        		
                String updateSt = "UPDATE itemList SET price = ?, quantity = ?  where id = ?";
                PreparedStatement pstmt = c.prepareStatement(updateSt);
                
                pstmt.setDouble(1, priceTemp);
                pstmt.setInt(2, quantity + quanTemp);
                pstmt.setInt(2, idTemp);
                pstmt.executeUpdate();

        		
        	}
            
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

}
