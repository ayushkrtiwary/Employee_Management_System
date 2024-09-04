import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/createEmployeeAccountServlet")
public class createEmployeeAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> hrNames = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

            String query = "SELECT HRName FROM HR";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                hrNames.add(rs.getString("HRName"));
            }
//            System.out.println(hrNames);

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("hrNames", hrNames);
        request.getRequestDispatcher("createEmployeeAccount.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empName = request.getParameter("ename");
        String empID = request.getParameter("eid");
        String department = request.getParameter("department");
        String doj = request.getParameter("doj");
        String phone = request.getParameter("phone");
        String hr = request.getParameter("hr");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

            String query = "INSERT INTO Employee (EID, EName, department, doj, phone, HR) VALUES (?, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(empID));
            ps.setString(2, empName);
            ps.setString(3, department);
            ps.setString(4, doj);
            ps.setLong(5, Long.parseLong(phone));
            ps.setString(6, hr);

            ps.executeUpdate();
           response.sendRedirect("loginPage.html");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.html");
        }
    }
}
