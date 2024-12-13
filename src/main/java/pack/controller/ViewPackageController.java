package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.model.Package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Servlet implementation class ViewPackageController
 */
public class ViewPackageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewPackageController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id != null) {
            try {
                int packageId = Integer.parseInt(id);

                // Load the SQL Server driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Connect to the Azure SQL database
                Connection con = DriverManager.getConnection(
                    "jdbc:sqlserver://nikkospace.database.windows.net:1433;" +
                    "database=haiya;user=nikko@nikkospace;password={Muhammadyazid01!};" +
                    "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;"
                );

                String sql = "SELECT * FROM package WHERE packageId = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, packageId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Package pkg = new Package(
                        rs.getInt("packageId"),
                        rs.getString("packageName"),
                        rs.getDouble("packagePrice")
                    );
                    request.setAttribute("pkg", pkg); // Use "pkg" as the attribute name
                }

                con.close();

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid package ID format.");
            } catch (SQLException e) {
                System.out.println("Error retrieving package: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Error loading SQL Server driver: " + e.getMessage());
            }
        } else {
            System.out.println("Error: No package ID provided.");
        }

        RequestDispatcher req = request.getRequestDispatcher("viewPackage.jsp"); // Forward to the correct JSP
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
