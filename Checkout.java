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




@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
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
	
	private String generateOrderNumber(){
		String orderNumber="";
		int randomInteger=0;
		for (int i=0;i<10;i++){
			randomInteger=0 + (int)(Math.random() * 9); 
			orderNumber=orderNumber+ orderNumber.valueOf(randomInteger);
		}
		return orderNumber;
		
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
	public Checkout() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		request.setAttribute("count", count);
		request.getRequestDispatcher( "/WEB-INF/Checkout.jsp" ).forward(
				request, response );	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String fullName = request.getParameter("name");
		String email = request.getParameter("email");
		String address = request.getParameter("address");

		String orderNumber="";
		
		List<itemClass> cart = (List<itemClass>)request.getSession().getAttribute("cart");
		String firstName = null;
		String lastName = null;
		Boolean hasError=false;

		if (fullName == null && fullName.trim().length() == 0){

			hasError = true;
			request.setAttribute("nameError", "Must be a valid full name2");

		} 
		else{

			// Tokenize the fullName
			String[] tokens = fullName.trim().split(" ");

			// Did the User submit at least two names?
			if (tokens.length < 2){
				hasError = true;
				request.setAttribute("nameError", "Must be a valid full name1");				
			}		
			else{
				firstName = tokens[0];
				lastName = tokens[1];
			}
		}


		if(email == null || email.trim().length() == 0||!(email.contains("@"))){

			hasError = true;
			request.setAttribute("emailError", "Must be a valid email");
		}

		if(address == null || address.trim().length() == 0){

			hasError = true;
			request.setAttribute("addressError", "Must be a valid address");
		}

		else{
			if(!hasError){
				for(itemClass item:cart){
					itemClass entry=getEntry(item.getId());
					int newQuantity=entry.getQuantity()-item.getQuantity();
					


					Connection c = null;
					try
					{
						String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu24";
						String username = "cs3220stu24";
						String password = "#H5PEhVC";

						String sql = "update itemList set  quantity = ? where id = ?";
						c = DriverManager.getConnection( url, username, password );
						PreparedStatement pstmt = c.prepareStatement( sql );

						pstmt.setInt( 1, newQuantity );
						pstmt.setInt( 2, entry.getId() );
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
				}
			}
			orderNumber=generateOrderNumber();
			request.setAttribute("orderSuccess", "Congrats your order was a success. Order: "+ orderNumber);
			request.getSession().removeAttribute("cart");	
		}
		
		
		doGet(request, response);
	}

}
