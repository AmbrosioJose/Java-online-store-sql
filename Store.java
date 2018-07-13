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

/**
 * Servlet implementation class Store
 */
@WebServlet("/Store")
public class Store extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<itemClass> inventory = new ArrayList<itemClass>();
		// modified: checks the session 
		List<itemClass> cart = (List<itemClass>) request.getSession().getAttribute("cart");
		//if its not there it will intsatiate one
		if(cart==null){
			 cart = new ArrayList<itemClass>();
		}
		
		//this gets the number of items in cart
		int count = cart.size();
		if(count>0){
			count=0;
			for(itemClass item:cart){
				count=count+item.getQuantity();
			}
		}
		//
		Connection c = null;
		try {

			String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu24";
			String username = "cs3220stu24";
			String password = "#H5PEhVC";

			c = DriverManager.getConnection(url, username, password);

			Statement stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * from itemList");

			while (rs.next()) {
				inventory.add(new itemClass(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
						rs.getInt("quantity"), rs.getDouble("price")));
			}

		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				throw new ServletException(e);
			}
		}
		//here I added it to the session
		request.getSession().setAttribute("cart", cart);
		
		request.setAttribute("inventory", inventory);
		request.setAttribute("count", count);

		request.getRequestDispatcher("/WEB-INF/Store.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	
	}

}

