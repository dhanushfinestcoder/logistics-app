import React, { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode"; // Import JWT decode
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";

export default function MakeIntransit() {
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedShipmentId, setSelectedShipmentId] = useState(null);

  useEffect(() => {
    fetchUserId();
  }, []);

  const fetchUserId = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");

      if (!token) throw new Error("Token not found. Please log in.");
      const decodedToken = jwtDecode(token);
      const username = decodedToken.sub; // Assuming the username is stored in the token

      // Fetch user ID
      const response = await fetch(`http://localhost:8080/user/getId/${username}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Failed to fetch user ID");

      const userId = await response.json();
      fetchDriverId(userId);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const fetchDriverId = async (userId) => {
    try {
      const token = localStorage.getItem("token");

      // Fetch driver ID
      const response = await fetch(`http://localhost:8080/admin/getDid/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Failed to fetch driver ID");

      const driverId = await response.json();
      fetchDriverShipments(driverId); // Fetch shipments for this driver
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const fetchDriverShipments = async (driverId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/ship/shipments/${driverId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Failed to fetch shipments");

      const data = await response.json();
      setShipments(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const markInTransit = async (shipmentId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/driver/mark-in-transit/${shipmentId}`, {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Failed to update status");
      setShipments((prev) =>
        prev.map((s) =>
          s.shipmentId === shipmentId ? { ...s, status: "IN-TRANSIT" } : s
        )
      );
    } catch (err) {
      alert(`Error: ${err.message}`);
    }
  };

  const toggleDetails = (shipmentId) => {
    setSelectedShipmentId(selectedShipmentId === shipmentId ? null : shipmentId);
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">🚛 Assigned Shipments</h2>

      {loading ? (
        <p>Loading shipments...</p>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : shipments.length === 0 ? (
        <p>No shipments assigned.</p>
      ) : (
        <Row>
          {shipments.map((shipment) => {
            const statusBadgeColor = {
              DISPATCHED: "warning",
              "IN-TRANSIT": "primary",
              DELIVERED: "success",
            }[shipment?.status] || "secondary";

            return (
              <Col md={6} lg={4} key={shipment?.shipmentId} className="mb-3">
                <Card className="shadow-sm">
                  <Card.Body>
                    <div className="d-flex justify-content-between">
                      <Card.Title>Shipment ID: {shipment?.shipmentId}</Card.Title>
                    </div>
                    <Card.Text>
                      <strong>Order ID:</strong> {shipment?.orders?.orderId || "N/A"}
                      <br />
                      <strong>Pickup Location:</strong> {shipment?.orders?.pickupLoc || "N/A"}
                      <br />
                      <strong>Delivery Location:</strong> {shipment?.orders?.deliveryLoc || "N/A"}
                      <br />
                      <strong>Weight:</strong> {shipment?.orders?.weight || "N/A"} kg
                      <br />
                      <strong>Receiver Email:</strong> {shipment?.orders?.recieverEmail || "N/A"}
                      <br />
                      <strong>Status:</strong>{" "}
                      <span className={`badge bg-${statusBadgeColor}`}>
                        {shipment?.status || "N/A"}
                      </span>
                    </Card.Text>

                    {shipment?.status === "DISPATCHED" && (
                      <Button
                        variant="warning"
                        className="w-100"
                        onClick={() => markInTransit(shipment?.shipmentId)}
                      >
                        🚚 Make In-Transit
                      </Button>
                    )}

                    {shipment?.status === "IN-TRANSIT" && (
                      <Button variant="primary" className="w-100" disabled>
                        ✅ In-Transit
                      </Button>
                    )}

                    <Button
                      variant="primary"
                      className="w-100 mt-2"
                      onClick={() => toggleDetails(shipment?.shipmentId)}
                    >
                      {selectedShipmentId === shipment?.shipmentId ? "Hide Details" : "View Details"}
                    </Button>

                    {selectedShipmentId === shipment?.shipmentId && (
                      <div className="mt-3">
                        <hr />
                        <h6>🚚 Vehicle Details</h6>
                        <strong>Type:</strong> {shipment?.vehicles?.vechicleType || "N/A"}
                        <br />
                        <strong>Capacity:</strong> {shipment?.vehicles?.vechicleCap || "N/A"} kg
                        <br />
                        <strong>Vehicle Status:</strong> {shipment?.vehicles?.vechicleStatus || "N/A"}
                        <br />
                      </div>
                    )}
                  </Card.Body>
                </Card>
              </Col>
            );
          })}
        </Row>
      )}
    </div>
  );
}