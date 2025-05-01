import React, { useEffect, useState } from "react";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";

export default function ShipmentsList() {
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedShipmentId, setSelectedShipmentId] = useState(null);

  useEffect(() => {
    fetchShipments();
  }, []);

  const fetchShipments = () => {
    setLoading(true);
    setError(null);
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/ship/shipments", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to fetch shipments");
        return response.json();
      })
      .then((data) => {
        console.log(data);
        setShipments(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  };

  const toggleDetails = (shipmentId) => {
    setSelectedShipmentId(selectedShipmentId === shipmentId ? null : shipmentId);
  };

  
  return (
    <div className="container mt-4">
      <h2 className="mb-4">Shipments</h2>

      {loading ? (
        <p>Loading shipments...</p>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : shipments.length === 0 ? (
        <p>No shipments available.</p>
      ) : (
        <Row>
          {shipments.map((shipment) => (
            <Col md={6} lg={4} key={shipment?.shipmentId} className="mb-3">
              <Card className="shadow-sm">
                <Card.Body>
                  <Card.Title>Shipment ID: {shipment?.shipmentId}</Card.Title>
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
                    <strong>Status:</strong>
                    <span
                      className={`badge bg-${
                        shipment?.status === "DISPATCHED"
                          ? "warning"
                          : shipment?.status === "IN-TRANSIT"
                          ? "primary"
                          : shipment?.status === "DELIVERED"
                          ? "success"
                          : "secondary"
                      } ml-2`}
                    >
                      {shipment?.status || "N/A"}
                    </span>
                  </Card.Text>
                  <Button variant="primary" className="w-100" onClick={() => toggleDetails(shipment?.shipmentId)}>
                    {selectedShipmentId === shipment?.shipmentId ? "Hide Details" : "View Details"}
                  </Button>
                  {selectedShipmentId === shipment?.shipmentId && (
                    <div className="mt-3">
                      <hr />
                      <h6>🚚 Vehicle Details</h6>
                      <strong>Type:</strong> {shipment?.vechicles?.vechicleType || "N/A"}
                      <br />
                      <strong>Capacity:</strong> {shipment?.vechicles?.vechicleCap || "N/A"} kg
                      <br />
                      <strong>Vehicle Status:</strong> {shipment?.vechicles?.vechicleStatus || "N/A"}
                      <br />
                      <hr />
                      <h6>👨‍✈️ Driver Details</h6>
                      <strong>Name:</strong> {shipment?.driver?.user?.name || "Unassigned"}
                      <br />
                      <strong>Email:</strong> {shipment?.driver?.user?.uemailId || "N/A"}
                      <br />
                      <strong>Driver Status:</strong> {shipment?.driver?.status || "N/A"}
                    </div>
                  )}
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
}
