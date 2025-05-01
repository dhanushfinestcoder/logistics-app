import React, { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from "recharts";

export default function Dashboard() {
  const token = localStorage.getItem("token");
  let username = "Guest";

  if (token) {
    const decodedToken = jwtDecode(token);
    username = decodedToken.sub || "Guest";
  }

  const [orderStats, setOrderStats] = useState({
    PendingOrders: 0,
    DeliveredOrders: 0,
    DispatchedOrders: 0,
  });

  useEffect(() => {
    if (!token) {
      console.error("No token found!");
      return;
    }

    fetch("http://localhost:8080/admin/analytics", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch order stats");
        }
        return response.json();
      })
      .then((data) => {
        //console.log("Order Stats:", data);
        setOrderStats(data);
      })
      .catch((error) => console.error("Error fetching order stats:", error));
  }, [token]);

  const data = [
    { name: "Pending", value: orderStats.PendingOrders },
    { name: "Delivered", value: orderStats.DeliveredOrders },
    { name: "Dispatched", value: orderStats.DispatchedOrders },
  ];

  const COLORS = ["#FFD700", "#32CD32", "#FF4500"]; // Yellow, Green, Red

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>
      <p>Welcome, <strong>{username}</strong>.</p>

      <div className="chart-container">
        <h2>Order Status Analytics</h2>
        <ResponsiveContainer width="90%" height={300}>
          <PieChart>
            <Pie
              data={data}
              dataKey="value"
              nameKey="name"
              cx="50%"
              cy="50%"
              outerRadius={100}
              fill="#8884d8"
              label
            >
              {data.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index]} />
              ))}
            </Pie>
            <Tooltip />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
