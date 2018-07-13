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



@WebServlet("/Details")
public class Details extends HttpServlet {
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

	private itemClass getEntry( Integer id ) throws ServletException
	{
		itemClass entry = null;
		Connection c = null;
		try
		{
			String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu24";
			String username = "cs3220stu24";
			String password = "#H5PEhVC";

			String sql = "select * from itemList where id = ?";

			c = DriverManager.getConnection( url, username, password );

			PreparedStatement pstmt = c.prepareStatement( sql );

			pstmt.setInt( 1, id );
			ResultSet rs = pstmt.executeQuery();

			if( rs.next() )
				entry = new itemClass( rs.getInt( "id" ),
						rs.getString( "name" ), rs.getString( "description" ), rs.getInt( "quantity" ), rs.getDouble( "price" ) );
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

		return entry;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		// this will store details
		itemClass detail ;
		// checks the session 
		List<itemClass> cart = (List<itemClass>) request.getSession().getAttribute("cart");
		//if its not there it will intsatiate one
		if(cart==null){
			 cart = new ArrayList<itemClass>();
		}
		int count = cart.size();
		if(count>0){
			count=0;
			for(itemClass item:cart){
				count=count+item.getQuantity();
			}
		}
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		detail=getEntry(id);
		
		request.setAttribute("count", count);
		request.setAttribute("detail", detail);
		request.getRequestDispatcher("/WEB-INF/Details.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
