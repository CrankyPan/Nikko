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

/**
 * Servlet implementation class AddPackageController
 */
@WebServlet("/AddPackageController") // Added URL pattern for this servlet
public class AddPackageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddPackageController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String packageName = request.getParameter("packageName");
            // Check if packageName is null or empty
            if (packageName == null || packageName.trim().isEmpty()) {
                System.out.println("Error: Package name cannot be empty.");
                // Consider redirecting to an error page or displaying an error message
                return;
            }

            String packagePriceStr = request.getParameter("packagePrice");
            if (packagePriceStr == null || packagePriceStr.trim().isEmpty()) {
                System.out.println("Error: Package price cannot be empty.");
                // Consider redirecting to an error page or displaying an error message
                return;
            }
            double packagePrice = Double.parseDouble(packagePriceStr);

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to the Azure SQL database (Remember to NOT hardcode your password!)
            Connection con = DriverManager.getConnection("jdbc:sqlserver://nikkospace.database.windows.net:1433;database=haiya;user=nikko@nikkospace;password=Muhammadyazid01!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            String sql = "INSERT INTO package (packageName, packagePrice) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) { // Use try-with-resources
                ps.setString(1, packageName);
                ps.setDouble(2, packagePrice);
                ps.executeUpdate();
            } // con will be closed automatically due to try-with-resources

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid package price format.");
            // Consider redirecting to an error page or displaying an error message
        } catch (Exception e) {
            System.out.println(e);
            // Consider redirecting to an error page or displaying an error message
        }

        // Redirect after successful insertion
        response.sendRedirect("ListPackageController"); // Redirect to the servlet that lists packages
    }
}
