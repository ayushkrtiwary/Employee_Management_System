<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Dashboard</title>
    <link rel="stylesheet" href="EmployeeDashboard.css">
</head>
<body>
    <div class="container">
        <div class="left-section">
            <h2>Hi, ${ename}</h2>
            <p>Employee ID: ${userId}</p>
            <p>Department: ${department}</p>
            <p>Date of Joining: ${doj}</p>
            <p>Phone: ${phone}</p>
            <!-- Add other details as needed -->
        </div>
        <div class="right-section">
            <h3>Options</h3>
            <form action="EmployeeDashboardServlet" method="POST">
                <ul>
                    <li><input type="radio" name="requestType" value="quarter" required> Apply for Quarter Allotment</li>
                    <li><input type="radio" name="requestType" value="medbook" required> Apply for New Medical Book</li>
                    <li><input type="radio" name="requestType" value="holiday" required> Apply for Holiday</li>
                </ul>
                <textarea name="remarks" id="remarks" placeholder="Remarks/Request" required></textarea>
                <input type="hidden" name="eid" value="${userId}"> <!-- Hidden field for eid -->
                <button type="submit">Submit</button>
            </form>
        </div>
    </div>
</body>
</html>
