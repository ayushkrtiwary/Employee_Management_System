

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getParameter("role");
        String id = request.getParameter(role.equals("HR") ? "hrid" : "empid");
        String phone = request.getParameter(role.equals("HR") ? "hrphone" : "empphone");

        try {
            // Establish database connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

            String table = role.equals("HR") ? "HR" : "Employee";
            String idColumn = role.equals("HR") ? "HRID" : "EID";

            // Prepare SQL query
            String query = "SELECT * FROM " + table + " WHERE " + idColumn + " = ? AND phone = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(id));
            pst.setLong(2, Long.parseLong(phone));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
            	
            	HttpSession session=request.getSession();
                session.setAttribute("userId", id);
                session.setAttribute("ename", rs.getString("ENAME"));
                session.setAttribute("department", rs.getString("DEPARTMENT"));
                session.setAttribute("phone", rs.getString("PHONE"));
                session.setAttribute("doj", rs.getString("DOJ").split(" ")[0]);
            	
            	
//            	session.setAttribute("userId", id);
            	
            	
                if (role.equals("HR")) {
                	RequestDispatcher rd = request.getRequestDispatcher("HRDashboardServlet");
    				rd.include(request, response);
                } else {
                	RequestDispatcher rd = request.getRequestDispatcher("EmployeeDashboardServlet");
    				rd.include(request, response);

                }
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><p>Invalid credentials. Please try again.</p></body></html>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><p>Error: " + e.getMessage() + "</p></body></html>");
        }
    }
}

