<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="pack.connection.ConnectionManager" %>
<%@ page import="java.sql.*" %>
<%@ page import="pack.model.Package" %> <%-- Import the Package class --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Package View</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
<header>
    <div id="menu-bar" class="fa fa-bars"></div>
    <a href="#" class="logo"><img class="capal_logo" src="img/CAPAL LOGO.png" alt="Logo"></a>
    <nav class="navbar">
        <a href="index.jsp">Home</a>
        <a href="index.jsp">Appointment</a>
        <a href="indexPet.jsp">Pet</a>
        <a href="indexProfile.jsp">Profile</a>
    </nav>
    <div class="icons">
        <a href="indexProfile.jsp"><i></i></a>
    </div>
</header>

<main>
    <div class="container">
        <h1>View Package</h1>
        <table border="1">
            <tr>
                <th>Name</th>
                <th>Value</th>
            </tr>
            <%
                Package pkg = null; // Initialize Package object
                String packageIdParam = request.getParameter("packageid");
                if (packageIdParam != null && !packageIdParam.isEmpty()) {
                    int id = Integer.parseInt(packageIdParam);

                    try {
                        Connection con = ConnectionManager.getConnection();
                        String sql = "SELECT * FROM packages WHERE packageId = ?";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            pkg = new Package(
                                    rs.getInt("packageId"),
                                    rs.getString("packageName"),
                                    rs.getDouble("packagePrice")
                            );
                        }
                        con.close();
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }

                } else {
                    // Handle the case where "packageid" is null
                    out.println("<p>Error: Invalid package ID.</p>");
                    // Or you can redirect to another page:
                    // response.sendRedirect("listPackages.jsp");
                }

                // Display package details if pkg is not null
                if (pkg != null) {
            %>
            <tr>
                <td>Package Id</td>
                <td><%= pkg.getpackageId() %></td>
            </tr>
            <tr>
                <td>Package Name</td>
                <td><%= pkg.getpackageName() %></td>
            </tr>
            <tr>
                <td>Package Price</td>
                <td><%= pkg.getpackagePrice() %></td>
            </tr>
            <%
                } 
            %>
        </table>
    </div>
</main>

</body>
</html>