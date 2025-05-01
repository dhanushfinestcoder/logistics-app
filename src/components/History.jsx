import React, { Component } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { jwtDecode } from "jwt-decode";

class History extends Component {
  constructor(props) {
    super(props);
    this.state = {
      history: [],
      loading: true,
      message: null,
      userId: null,
      driverId: null,  // Add driverId state
    };
  }

  componentDidMount() {
    this.fetchUserId();
  }

  fetchUserId = async () => {
    this.setState({ loading: true, message: null });

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("Token not found. Please log in.");
      }

      let userName;
      try {
        const decodedToken = jwtDecode(token);
        userName = decodedToken.sub;
      } catch (decodeError) {
        throw new Error("Invalid token. Please log in again.");
      }

      if (!userName) {
        throw new Error("User not found. Please log in again.");
      }

      // Step 1: Get userId from username
      const userResponse = await axios.get(`http://localhost:8080/user/getId/${userName}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const userId = userResponse.data;
      console.log("User ID:", userId);
      this.setState({ userId });

      // Step 2: Get driverId from userId
      const driverResponse = await axios.get(`http://localhost:8080/driver/Did/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const driverId = driverResponse.data;
      console.log("Driver ID:", driverId);
      this.setState({ driverId });

      // Step 3: Get shipment history by driverId
      const historyResponse = await axios.get(`http://localhost:8080/ship/getShipById/${driverId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!historyResponse.data || !Array.isArray(historyResponse.data)) {
        throw new Error("Invalid shipment history data.");
      }

      this.setState({ history: historyResponse.data });
    } catch (error) {
      this.setState({
        message: {
          type: "error",
          text: error.response?.data?.message || error.message || "Failed to fetch history.",
        },
      });
    } finally {
      this.setState({ loading: false });
    }
  };

  render() {
    const { history, loading, message } = this.state;

    return (
      <div className="container mt-4">
        <h2 className="mb-4">📜 Shipment History</h2>

        {message && (
          <div className={`alert ${message.type === "success" ? "alert-success" : "alert-danger"}`} role="alert">
            {message.text}
          </div>
        )}

        {loading ? (
          <p>Loading shipment history...</p>
        ) : history.length === 0 ? (
          <p>No past shipments found.</p>
        ) : (
          <div className="row">
            {history.map((shipment) => (
              <div className="col-md-6 col-lg-4 mb-3" key={shipment?.shipmentId || Math.random()}>
                <div className="card shadow-sm">
                  <div className="card-body">
                    <h5 className="card-title">Shipment ID: {shipment?.shipmentId || "N/A"}</h5>
                    <p>
                      <strong>Order ID:</strong> {shipment?.orders?.orderId || "N/A"} <br />
                      <strong>Pickup Location:</strong> {shipment?.orders?.pickupLoc || "N/A"} <br />
                      <strong>Delivery Location:</strong> {shipment?.orders?.deliveryLoc || "N/A"} <br />
                      <strong>Weight:</strong> {shipment?.orders?.weight || "N/A"} kg <br />
                      <strong>Receiver Email:</strong> {shipment?.orders?.recieverEmail || "N/A"} <br />
                      <strong>Status:</strong>{" "}
                      <span
                        className={`badge bg-${
                          shipment?.status === "DISPATCHED"
                            ? "warning"
                            : shipment?.status === "IN_TRANSIT"
                            ? "primary"
                            : shipment?.status === "DELIVERED"
                            ? "success"
                            : "secondary"
                        }`}
                      >
                        {shipment?.status || "N/A"}
                      </span>
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    );
  }
}

export default History;
