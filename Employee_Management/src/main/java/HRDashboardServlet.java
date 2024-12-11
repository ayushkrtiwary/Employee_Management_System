import java.io.IOException;
import java.io.PrintWriter;
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
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/HRDashboardServlet")
public class HRDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String hrID = (String) session.getAttribute("userId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

            String hrQuery = "SELECT HRName, HRID, dept, doj, phone FROM HR WHERE HRID = ?";
            PreparedStatement hrPstmt = conn.prepareStatement(hrQuery);
            hrPstmt.setString(1, hrID);
            ResultSet hrRs = hrPstmt.executeQuery();

            JSONObject json = new JSONObject();
            if (hrRs.next()) {
                json.put("hrName", hrRs.getString("HRName"));
                json.put("hrID", hrRs.getString("HRID"));
                json.put("department", hrRs.getString("dept"));
                json.put("doj", hrRs.getString("doj"));
                json.put("phone", hrRs.getString("phone"));
            }

            json.put("medbookRequests", getRequests(conn, "Medbook"));
            json.put("quarterRequests", getRequests(conn, "Quarter"));
            json.put("holidayRequests", getRequests(conn, "Holiday"));

            out.print(json);
            hrRs.close();
            hrPstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private JSONArray getRequests(Connection conn, String table) throws Exception {
//        String query = "SELECT id, eid, remarks FROM " + table;
//        PreparedStatement pstmt = conn.prepareStatement(query);
//        ResultSet rs = pstmt.executeQuery();
//
//        JSONArray requests = new JSONArray();
//        while (rs.next()) {
//            JSONObject request = new JSONObject();
//            request.put("id", rs.getInt("id"));
//            request.put("eid", rs.getString("eid"));
//            request.put("remarks", rs.getString("remarks"));
//            requests.put(request);
//        }

        rs.close();
        pstmt.close();
        return requests;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String type = request.getParameter("type");
        int id = Integer.parseInt(request.getParameter("id"));

        if ("accept".equals(action) || "reject".equals(action)) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "rohit");

                String table = "";
                if ("medbook".equals(type)) {
                    table = "Medbook";
                } else if ("quarter".equals(type)) {
                    table = "Quarter";
                } else if ("holiday".equals(type)) {
                    table = "Holiday";
                }

                String query = "DELETE FROM " + table + " WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                pstmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // After processing, reload the page
        response.sendRedirect("HRDashboardServlet");
    }
}
