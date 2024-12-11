<%@ page import ="java.util.List"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Employee Account</title>
    <link rel="stylesheet" href="createEmployeeAccount.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Create Employee Account</h1>
        </header>
        <main>
            <form action="createEmployeeAccountServlet" method="POST">
                <label for="ename">Employee Name:</label>
                <input type="text" id="ename" name="ename" required><br>
                
                <label for="eid">Employee ID:</label>
                <input type="text" id="eid" name="eid" max="9999" required><br>
                
                <label for="department">Department:</label>
                <input type="text" id="department" name="department" required><br>
                
                <label for="doj">Date of Joining:</label>
                <input type="date" id="doj" name="doj" required><br>
                
                <label for="phone">Phone No:</label>
                <input type="text" id="phone" name="phone" max="9999999999" required><br>
                
                <label for="hr">HR:</label>
                <select id="hr" name="hr" required>
                    <% 
                        List<String> hrNames = (List<String>) request.getAttribute("hrNames");
                        if (hrNames != null) {
                            for (String hrName : hrNames) {
                                out.println("<option value=\"" + hrName + "\">" + hrName + "</option>");
                            }
                        }
                    %>
                </select><br>
                
                <button type="submit">Create Account</button>
            </form>
        </main>
    </div>
</body>
</html>
