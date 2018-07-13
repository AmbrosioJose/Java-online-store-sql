package OnlineStore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
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

	public static boolean isInteger(String value) {
		try { 
			Integer.parseInt(value); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		return true;
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

	public ShoppingCart() {
		super();

	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<itemClass> cart = (List<itemClass>) request.getSession().getAttribute("cart");
		if(cart==null){
			 cart = new ArrayList<itemClass>();
		}
		double total=0.0;
		for(itemClass item:cart){
			total=total + item.getTotalPrice();
		}
		request.getSession().setAttribute("cart",cart);
		request.getSession().setAttribute("total",total);
		request.getRequestDispatcher( "/WEB-INF/ShoppingCart.jsp" ).forward(
				request, response );	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idString= request.getParameter("id");
		String quantityString= request.getParameter("quantity");
		

		List<itemClass> cart = (List<itemClass>) request.getSession().getAttribute("cart");
		if(cart==null){
			cart = new ArrayList<itemClass>();
		}

		if(!isInteger(idString)||!isInteger(quantityString)){

			request.getRequestDispatcher( "Store" ).forward(
					request, response );
			return;
		}
		int id =Integer.parseInt(idString);
		int quantity =Integer.parseInt(quantityString);


		itemClass entry= getEntry(id);
		
		double totalPrice=quantity*entry.getPrice();
		entry.setQuantity(quantity);
		entry.setTotalPrice(totalPrice);
		cart.add(entry);
		request.getSession().setAttribute("cart",cart);
		request.getRequestDispatcher("Store").forward(request, response);
	}

}
