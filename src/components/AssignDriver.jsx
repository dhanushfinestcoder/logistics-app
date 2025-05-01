import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Card from "react-bootstrap/Card";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";

export default function AssignDriver() {
  const { orderId } = useParams();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [drivers, setDrivers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAvailableDrivers = async () => {
      setLoading(true);
      setError(null);

      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
          `http://localhost:8080/admin/availableDrivers/${orderId}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(response.data);
        setDrivers(response.data);
      } catch (err) {
        setError(err.response?.data || "Failed to fetch available drivers.");
      } finally {
        setLoading(false);
      }
    };

    fetchAvailableDrivers();
  }, [orderId]);

  const handleAssignDriver = async (driverId) => {
    setLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem("token");

      const response = await axios.put(
        `http://localhost:8080/admin/assignDriver/${orderId}`,
        { driverId },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert(response.data); 
      navigate("/orders"); 
    } catch (err) {
      setError(err.response?.data || "Failed to assign driver and vehicle.");
    } finally {
      setLoading(false);
    }
  };

  console.log(drivers)
  return (
    <Modal show={true} onHide={() => navigate("/orders")} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Assign Driver for Order {orderId}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <p className="text-danger">{error}</p>}
        {loading ? (
          <p>Loading...</p>
        ) : (
          <div className="row">
            {drivers.map((driver) => (
              <div className="col-md-4 mb-3" key={driver.driverId}>
                <Card>
                  <Card.Body>
                    <Card.Title>{driver?.user?.name}</Card.Title>
                    <Card.Text>
                      <strong>Vehicle:</strong> {driver.vechicles.vechicleType} <br />
                      <strong>Capacity:</strong> {driver.vechicles.vechicleCap} kg <br />
                      <strong>Status:</strong> {driver.status}
                    </Card.Text>
                    <Button
                      variant="primary"
                      onClick={() => handleAssignDriver(driver.driverId)}
                      disabled={loading}
                    >
                      {loading ? "Assigning..." : "Assign Driver"}
                    </Button>
                  </Card.Body>
                </Card>
              </div>
            ))}
          </div>
        )}
      </Modal.Body>
    </Modal>
  );
}