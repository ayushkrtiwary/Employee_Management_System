import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/createHRAccountServlet")
public class createHRAccountServlet extends HttpServlet { // Changed class name to match convention
    private static final long serialVersionUID = 1L; // Added serialVersionUID

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hrName = request.getParameter("hrname");
        String hrID = request.getParameter("hrid");
        String department = request.getParameter("department");
        String doj = request.getParameter("doj");
        String phone = request.getParameter("phone");

        try {
            // Establish database connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO HR (HRID, HRName, phone, department, doj) VALUES (?, ?, ?, ?, TO_DATE(?,'YYYY-MM-DD'))")) { 

                ps.setInt(1, Integer.parseInt(hrID));
                ps.setString(2, hrName);
                ps.setLong(3, Long.parseLong(phone)); 
                ps.setString(4, department);
                ps.setString(5, doj);
                

                // Execute the query
                ps.executeUpdate();
                
            }

            response.sendRedirect("loginPage.html");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); 
            response.getWriter().write("An error occurred: " + e.getMessage()); 
        }
    }
}
