package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.connection.ConnectionManager;
import pack.model.Package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class ListPackagelController
 */
public class ListPackagelController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListPackagelController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	List<Package> packages = new ArrayList<>();

        try {
            // Load the SQL Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to the Azure SQL database
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://nikkospace.database.windows.net:1433;" +
                "database=haiya;user=nikko@nikkospace;password={Muhammadyazid01!};" +
                "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;"
            );

            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM package ORDER BY packageId"; // Assuming "package" is the table name
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Package p = new Package(
                    rs.getInt("packageId"),
                    rs.getString("packageName"),
                    rs.getDouble("packagePrice")
                );
                packages.add(p);
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Error retrieving packages: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading SQL Server driver: " + e.getMessage());
        }

        request.setAttribute("packages", packages);
        RequestDispatcher req = request.getRequestDispatcher("listPackage.jsp");
        req.forward(request, response);
    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
