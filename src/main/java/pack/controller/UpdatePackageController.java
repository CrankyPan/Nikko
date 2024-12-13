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
import java.sql.SQLException;

/**
 * Servlet implementation class UpdatePackageController
 */
public class UpdatePackageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePackageController() {
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

                // Fetch the package details for the given ID
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
                    request.setAttribute("pkg", pkg);
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

        RequestDispatcher req = request.getRequestDispatcher("updatePackage.jsp");
        req.forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id != null) {
            try {
                int packageId = Integer.parseInt(id);
                String packageName = request.getParameter("packageName");
                double packagePrice = Double.parseDouble(request.getParameter("packagePrice"));

                // Load the SQL Server driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Connect to the Azure SQL database
                Connection con = DriverManager.getConnection(
                    "jdbc:sqlserver://nikkospace.database.windows.net:1433;" +
                    "database=haiya;user=nikko@nikkospace;password={Muhammadyazid01!};" +
                    "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;"
                );

                String sql = "UPDATE package SET packageName = ?, packagePrice = ? WHERE packageId = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, packageName);
                ps.setDouble(2, packagePrice);
                ps.setInt(3, packageId);
                ps.executeUpdate();
                con.close();

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid package ID or price format.");
            } catch (SQLException e) {
                System.out.println("Error updating package: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Error loading SQL Server driver: " + e.getMessage());
            }
        } else {
            System.out.println("Error: No package ID provided.");
        }

        RequestDispatcher req = request.getRequestDispatcher("listPackage.jsp");
        req.forward(request, response);
    }
	
}
