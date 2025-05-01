import React, { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaMotorcycle, FaCar, FaTruck, FaQuestionCircle } from "react-icons/fa";
import "../components/styles/Driver.css"

export default function Drivers() {
  const [drivers, setDrivers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [expanded, setExpanded] = useState({});

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/driver/allDrivers", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch drivers");
        }
        return response.json();
      })
      .then((data) => {
        setDrivers(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const toggleDetails = (uid) => {
    setExpanded((prevState) => ({
      ...prevState,
      [uid]: !prevState[uid],
    }));
  };

  const getVehicleIcon = (type) => {
    switch (type?.toLowerCase()) {
      case "bike":
        return <FaMotorcycle size={20} color="blue" />;
      case "car":
        return <FaCar size={20} color="green" />;
      case "truck":
        return <FaTruck size={20} color="red" />;
      default:
        return <FaQuestionCircle size={20} color="gray" />;
    }
  };

  if (loading) return <h2 className="text-center mt-4">Loading drivers...</h2>;
  if (error) return <h2 className="text-center mt-4">Error: {error}</h2>;

  return (
    <div className="container-fluid mt-3">
      <h1 className="mb-4 text-center">Drivers List</h1>
      <div className="row gx-3">
        {drivers.map((driver) => (
          <div className="col-lg-3 col-md-4 col-sm-6 col-12 mb-3" key={driver.uid}>
            <Card className="shadow h-100 d-flex flex-column">
              <Card.Body className="d-flex flex-column justify-content-between">
                <div className="text-center">
                  <Card.Title>Driver ID: {driver.id}</Card.Title>
                </div>

                <div className="text-left">
                  <Card.Text>
                    <strong>Name:</strong> {driver?.user.name} <br />
                    <strong>Email:</strong> {driver?.user.uemailId} <br />
                    <strong>Driver Status:</strong>
                    <span className={`driver-status ${driver?.status === "AVAILABLE" ? "available" : "unavailable"}`}>
                      {driver?.status || "Unknown"}
                    </span>
                  </Card.Text>
                </div>

                {expanded[driver.uid] && driver.driver?.vechicles ? (
                  <div className="vehicle-details">
                    <h6 className="text-center">Vehicle Details</h6>
                    <p>
                      {getVehicleIcon(driver.driver.vechicles.vechicleType)}
                      <strong> Type:</strong> {driver.driver.vechicles.vechicleType || "N/A"} <br />
                      <strong>Vehicle ID:</strong> {driver.driver.vechicles.vechicleId} <br />
                      <strong>Capacity:</strong> {driver.driver.vechicles.vechicleCap} kg <br />
                      <strong>Status:</strong> {driver.driver.vechicles.vechicleStatus || "N/A"}
                    </p>
                  </div>
                ) : null}

                <Button 
                  variant={expanded[driver.uid] ? "danger" : "primary"} 
                  className="w-100 view-details-btn mt-auto" 
                  onClick={() => toggleDetails(driver.uid)}
                >
                  {expanded[driver.uid] ? "Hide Details" : "View Details"}
                </Button>
              </Card.Body>
            </Card>
          </div>
        ))}
      </div>
    </div>
  );
}
