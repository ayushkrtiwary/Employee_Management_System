<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Dashboard</title>
    <link rel="stylesheet" href="HRDashboard.css">
</head>
<body>
    <div class="container">
        <div class="left-section">
            <h2>Hi, <span id="hrName"></span></h2>
            <p>HR ID: <span id="hrID"></span></p>
            <p>Department: <span id="department"></span></p>
            <p>Date of Joining: <span id="doj"></span></p>
            <p>Phone No.: <span id="phone"></span></p>
        </div>
        <div class="right-section">
            <h3>Medbook Requests</h3>
            <div id="medbook-requests"></div>
            
            <h3>Quarter Allotment Requests</h3>
            <div id="quarter-requests"></div>
            
            <h3>Holiday Requests</h3>
            <div id="holiday-requests"></div>
        </div>
    </div>
    <script>
        // Fetch HR details on page load
        document.addEventListener("DOMContentLoaded", function() {
            fetch('HRDashboardServlet')
                .then(response => response.json())
                .then(data => {
                    document.getElementById('hrName').textContent = data.hrName;
                    document.getElementById('hrID').textContent = data.hrID;
                    document.getElementById('department').textContent = data.department;
                    document.getElementById('doj').textContent = data.doj;
                    document.getElementById('phone').textContent = data.phone;

                    populateRequests('medbook-requests', data.medbookRequests, 'medbook');
                    populateRequests('quarter-requests', data.quarterRequests, 'quarter');
                    populateRequests('holiday-requests', data.holidayRequests, 'holiday');
                })
                .catch(error => console.error('Error fetching HR details:', error));
        });

        function populateRequests(containerId, requests, type) {
            const container = document.getElementById(containerId);
            requests.forEach(request => {
                const div = document.createElement('div');
                div.innerHTML = `
                    <p>EID: ${request.eid}, Remarks: ${request.remarks}</p>
                    <button onclick="handleRequest('${type}', ${request.id}, 'accept')">Accept</button>
                    <button onclick="handleRequest('${type}', ${request.id}, 'reject')">Reject</button>
                `;
                container.appendChild(div);
            });
        }

        function handleRequest(type, id, action) {
            fetch(`HRDashboardServlet?action=${action}&type=${type}&id=${id}`, {
                method: 'POST'
            }).then(() => location.reload())
              .catch(error => console.error('Error handling request:', error));
        }
    </script>
</body>
</html>
