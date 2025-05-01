import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { FaBox, FaMapMarkerAlt, FaWeightHanging, FaUser, FaTruck } from "react-icons/fa";
import { MdOutlineAssignmentInd } from "react-icons/md";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import "bootstrap/dist/css/bootstrap.min.css";
import "../components/styles/Orders.css";

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const getUserDetails = () => {
    const token = localStorage.getItem("token");
    if (!token) return { role: null, name: null };

    try {
      const decodedToken = jwtDecode(token);
      const currentTime = Date.now() / 1000;

      if (decodedToken.exp < currentTime) {
        console.error("Token expired. Logging out...");
        localStorage.removeItem("token");
        return { role: null, name: null };
      }

      return { role: decodedToken.roles, name: decodedToken.sub };
    } catch (error) {
      console.error("Invalid token:", error);
      localStorage.removeItem("token");
      return { role: null, name: null };
    }
  };

  const { role, name } = getUserDetails();

  useEffect(() => {
    if (!role) {
      setError("User not authenticated");
      setLoading(false);
      return;
    }

    const apiUrl =
      role === "ROLE_CUSTOMER"
        ? `http://localhost:8080/orders/getThierOrders?name=${encodeURIComponent(name)}`
        : "http://localhost:8080/orders/getOrders";

    axios
      .get(apiUrl, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        withCredentials: true,
      })
      .then((response) => {
        setOrders(response.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("API Error:", err);
        setError(err.response?.data?.message || "Failed to fetch orders");
        setLoading(false);
      });
  }, [role, name]);

  if (loading) return <h2 className="text-center mt-4">Loading orders...</h2>;
  if (error) return <h2 className="text-center mt-4">Error: {error}</h2>;

  return (
    <div className="container-fluid mt-3">
      <h1 className="mb-4">Orders List</h1>
      <div className="row gx-2">
        {orders.map((order) => (
          <div className="col-lg-3 col-md-4 col-sm-6 col-12 mb-3" key={order.orderId}>
            <Card className={`shadow h-100 ${order.status === "DELIVERED" ? "border-success" : ""}`}>
              <Card.Body>
                <Card.Title>
                  <FaBox size={20} color="#007bff" /> Order ID: {order.orderId}
                </Card.Title>
                <div className="order-details">
                  <div className="order-detail-item">
                    <FaMapMarkerAlt color="green" />
                    <strong>Pickup:</strong> {order.pickupLoc}
                  </div>
                  <div className="order-detail-item">
                    <FaMapMarkerAlt color="red" />
                    <strong>Delivery:</strong> {order.deliveryLoc}
                  </div>
                  <div className="order-detail-item">
                    <FaWeightHanging color="gray" />
                    <strong>Weight:</strong> {order.weight} kg
                  </div>
                  <div className="order-detail-item">
                    <FaUser color="#17a2b8" />
                    <strong>Receiver:</strong> {order.recieverEmail}
                  </div>
                  <div className="order-detail-item">
                    <FaTruck color={order.status === "DELIVERED" ? "green" : "orange"} />
                    <strong>Status:</strong> {order.status}
                  </div>
                </div>

                {role === "ROLE_ADMIN" && order.status === "PENDING" && (
                  <Button
                    variant="warning"
                    className="w-100 mt-2"
                    onClick={() => navigate(`/assign-driver/${order.orderId}`)}
                  >
                    <MdOutlineAssignmentInd size={18} /> Assign Driver
                  </Button>
                )}

                {order.status === "IN_TRANSIT" && (
                  <Button variant="primary" className="w-100 mt-2" disabled>
                    <FaTruck /> In Transit
                  </Button>
                )}

                {order.status === "DELIVERED" && (
                  <Button className="delivered-btn w-100 mt-2" disabled>
                    <FaTruck /> Delivered
                  </Button>
                )}
              </Card.Body>
            </Card>
          </div>
        ))}
      </div>
    </div>
  );
}
