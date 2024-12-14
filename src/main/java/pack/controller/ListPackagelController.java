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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class ListPackagelController
 */
@WebServlet("/index")
public class ListPackagelController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ListPackagelController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Package> packages = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load the SQL Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to the Azure SQL database
            con = DriverManager.getConnection(
                    "jdbc:sqlserver://nikkospace.database.windows.net:1433;" +
                            "database=haiya;user=nikko@nikkospace;password=Muhammadyazid01!;" + 
                            "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                            "loginTimeout=30;"
            );

            stmt = con.createStatement();
            String sql = "SELECT * FROM package ORDER BY packageId"; 
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Package p = new Package(
                        rs.getInt("packageId"),
                        rs.getString("packageName"),
                        rs.getDouble("packagePrice")
                );
                packages.add(p);
            }

        } catch (SQLException e) {
            // Log the exception
            System.err.println("Error retrieving packages: " + e.getMessage());

            // Optionally, display a user-friendly error page
            request.setAttribute("errorMessage", "An error occurred while fetching packages.");
            RequestDispatcher req = request.getRequestDispatcher("error.jsp"); // Assuming you have an error.jsp
            req.forward(request, response);
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException (e.g., log it or display an error)
            System.err.println("Error loading SQL Server driver: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* Ignored */}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { /* Ignored */}
            try { if (con != null) con.close(); } catch (SQLException e) { /* Ignored */}
        }

        request.setAttribute("packages", packages);
        RequestDispatcher req = request.getRequestDispatcher("index.jsp"); 
        req.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
