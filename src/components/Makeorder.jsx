import React, { useState, useEffect } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { jwtDecode } from "jwt-decode";

export default function MakeOrder() {
  const [order, setOrder] = useState({
    pickupLoc: "",
    deliveryLoc: "",
    weight: "",
    recieverEmail: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    fetchUserId(); // Fetch user ID when component loads
  }, []);


  const fetchUserId = async () => {
    const token = localStorage.getItem("token");
    const decodedToken =jwtDecode(token);
    console.log(decodedToken.sub)
    const userName=decodedToken.sub
    if (!userName) {
      setMessage({ type: "error", text: "User name not found. Please log in again." });
      return;
    }
   // console.log(token);
    try {
      const response = await axios.get(`http://localhost:8080/user/getId/${userName}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUserId(response.data);
    } catch (error) {
      setMessage({ type: "error", text: "Failed to fetch user ID." });
    }
  };
  //console.log(userId);

  const handleChange = (e) => {
    setOrder({ ...order, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    const token = localStorage.getItem("token");

    if (!userId) {
      setMessage({ type: "error", text: "User ID not found. Please log in again." });
      setLoading(false);
      return;
    }

    const payload = {
      ...order,
      weight: parseFloat(order.weight),
      status: "PENDING",
      customer: { uid: userId }, 
    };

    try {
      await axios.post("http://localhost:8080/orders/placeOrder", payload, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      setMessage({ type: "success", text: "Order placed successfully!" });
      setOrder({ pickupLoc: "", deliveryLoc: "", weight: "", recieverEmail: "" });
    } catch (error) {
      setMessage({
        type: "error",
        text: error.response?.data?.message || "Failed to place order. Please try again.",
      });
    }

    setLoading(false);
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Place an Order</h2>

      {message && (
        <div className={`alert ${message.type === "success" ? "alert-success" : "alert-danger"}`} role="alert">
          {message.text}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Pickup Location</label>
          <input
            type="text"
            className="form-control"
            name="pickupLoc"
            value={order.pickupLoc}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Delivery Location</label>
          <input
            type="text"
            className="form-control"
            name="deliveryLoc"
            value={order.deliveryLoc}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Weight (kg)</label>
          <input
            type="number"
            className="form-control"
            name="weight"
            value={order.weight}
            onChange={handleChange}
            required
            min="1"
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Receiver Email</label>
          <input
            type="email"
            className="form-control"
            name="recieverEmail"
            value={order.recieverEmail}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary w-100" disabled={loading}>
          {loading ? "Placing Order..." : "Place Order"}
        </button>
      </form>
    </div>
  );
}
