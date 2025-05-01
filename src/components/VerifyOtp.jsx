import React, { useEffect, useState } from "react";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import "bootstrap/dist/css/bootstrap.min.css";

export default function VerifyOtp() {
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [otp, setOtp] = useState({});

  useEffect(() => {
    fetchShipments();
  }, []);

  const fetchShipments = async () => {
    setLoading(true);
    setError(null);
    const token = localStorage.getItem("token");

    try {
      const response = await fetch("http://localhost:8080/ship/shipments", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error("Failed to fetch shipments");

      const data = await response.json();
      console.log("Fetched Shipments:", data);

      const shipmentArray = Array.isArray(data) ? data : Object.values(data);

      // Filter shipments with "IN_TRANSIT" status
      const inTransitShipments = shipmentArray.filter(
        (shipment) => shipment?.status?.toUpperCase() === "IN_TRANSIT"
      );

      console.log("Filtered IN_TRANSIT shipments:", inTransitShipments);

      setShipments(inTransitShipments);
    } catch (err) {
      console.error("Fetch error:", err.message);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleOtpChange = (shipmentId, value) => {
    setOtp((prevOtp) => ({ ...prevOtp, [shipmentId]: value }));
  };

  const verifyOtp = async (shipmentId) => {
    const token = localStorage.getItem("token");
    const enteredOtp = otp[shipmentId];

    if (!enteredOtp) {
      alert("Please enter OTP");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/driver/verify-otp/${shipmentId}?otp=${enteredOtp}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      console.log(response);

      if (!response.ok) throw new Error("OTP verification failed");
      
      alert("Shipment verified successfully!");
      setShipments((prevShipments) =>
        prevShipments.filter((shipment) => shipment.shipmentId !== shipmentId)
      );

      setOtp((prevOtp) => {
        const updatedOtp = { ...prevOtp };
        delete updatedOtp[shipmentId];
        return updatedOtp;
      });
    } catch (err) {
      alert(`Error: ${err.message}`);
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Verify OTP</h2>

      {loading ? (
        <p>Loading shipments...</p>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : shipments.length === 0 ? (
        <p>No shipments requiring OTP verification.</p>
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
                    <strong>Receiver Email:</strong> {shipment?.orders?.recieverEmail || "N/A"}
                    <br />
                    <strong>Status:</strong>{" "}
                    <span className="badge bg-primary">{shipment?.status}</span>
                  </Card.Text>

                  <Form.Group className="mb-2">
                    <Form.Control
                      type="text"
                      placeholder="Enter OTP"
                      value={otp[shipment?.shipmentId] || ""}
                      onChange={(e) => handleOtpChange(shipment?.shipmentId, e.target.value)}
                    />
                  </Form.Group>

                  <Button
                    variant="success"
                    className="w-100"
                    onClick={() => verifyOtp(shipment?.shipmentId)}
                  >
                    Verify OTP
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
}
