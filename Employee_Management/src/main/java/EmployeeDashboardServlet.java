import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EmployeeDashboardServlet")
public class EmployeeDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestType = request.getParameter("requestType");
        String remarks = request.getParameter("remarks");
        String eid = (String) request.getSession().getAttribute("userId");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

            String table = "";
            if ("quarter".equals(requestType)) {
                table = "Quarter";
            } else if ("medbook".equals(requestType)) {
                table = "Medbook";
            } else if ("holiday".equals(requestType)) {
                table = "Holiday";
            }

            // Fetch hrid based on eid
            String hridQuery = "SELECT hr FROM Employee WHERE EID = ?";
            PreparedStatement hridPstmt = conn.prepareStatement(hridQuery);
            hridPstmt.setInt(1, Integer.parseInt(eid));
            ResultSet hridRs = hridPstmt.executeQuery();

            String hr=null;
            if (hridRs.next()) {
                hr = hridRs.getString("HR");
            }
            hridPstmt.close();

            // Insert data into the chosen table
            String query = "INSERT INTO " + table + " (hrname, EID, REMARKS) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, hr);
            pstmt.setInt(2, Integer.parseInt(eid));
            pstmt.setString(3, remarks);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            // Redirect to GET method after processing POST
            response.sendRedirect(request.getContextPath() + "/EmployeeDashboardServlet");
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logging framework for better error management
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("EmployeeDashboard.jsp");
            rd.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String eid = (String) session.getAttribute("userId");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

            // Fetch employee details for display
            String empQuery = "SELECT * FROM Employee WHERE EID = ?";
            PreparedStatement empPstmt = conn.prepareStatement(empQuery);
            empPstmt.setInt(1, Integer.parseInt(eid));
            ResultSet empRs = empPstmt.executeQuery();

            if (empRs.next()) {
                request.setAttribute("ename", empRs.getString("ename"));
                request.setAttribute("department", empRs.getString("department"));
                request.setAttribute("doj", empRs.getString("doj"));
                request.setAttribute("phone", empRs.getLong("phone"));
                // Add other attributes as needed
            }

            empPstmt.close();
            conn.close();

            RequestDispatcher rd = request.getRequestDispatcher("EmployeeDashboard.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logging framework for better error management
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("EmployeeDashboard.jsp");
            rd.forward(request, response);
        }
    }
}
