package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;


/**
 * Servlet implementation class AddPackageController
 */
public class AddPackageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public AddPackageController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            String packageName = request.getParameter("packageName");
            // Check if packageName is null or empty
            if (packageName == null || packageName.trim().isEmpty()) {
             
                System.out.println("Error: Package name cannot be empty.");
                return; 
            }

            String packagePriceStr = request.getParameter("packagePrice");
            if (packagePriceStr == null || packagePriceStr.trim().isEmpty()) {
     
                System.out.println("Error: Package price cannot be empty.");
                // For now, let's just return to prevent further errors
                return;
            }
            double packagePrice = Double.parseDouble(packagePriceStr);

             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to the Azure SQL database
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://nikkospace.database.windows.net:1433;" +
                "database=haiya;user=nikko@nikkospace;password={Muhammadyazid01!};" +
                "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;"
            );

            String sql = "INSERT INTO package (packageName, packagePrice) VALUES (?, ?)"; // Assuming packageId is auto-generated
            PreparedStatement ps = con.prepareStatement(sql);


            ps.setString(1, packageName);
            ps.setDouble(2, packagePrice);

            ps.executeUpdate();
            con.close();

        } catch (NumberFormatException e) {
            // Handle the case where packagePrice is not a valid number
            System.out.println("Error: Invalid package price format.");
        } catch (Exception e) {
            System.out.println(e);
        }

        RequestDispatcher req = request.getRequestDispatcher("index.jsp");
        req.forward(request, response);
    }

}
