import React, { useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

export default function AddDriver() {
  const [driver, setDriver] = useState({
    name: "",
    uemailId: "",
    upass: "",
    role: "DRIVER",
    status: "AVAILABLE",
    vehicleType: "",
    vehicleCap: "",
    vehicleStatus: "AVAILABLE",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);

  const handleChange = (e) => {
    setDriver({ ...driver, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    const token = localStorage.getItem("token");
    if (!token) {
      setMessage({ type: "error", text: "❌ Authentication failed. Please log in again." });
      setLoading(false);
      return;
    }

    // ✅ Corrected payload to match the backend structure
    const payload = {
      name: driver.name,
      uemailId: driver.uemailId,
      upass: driver.upass,
      role: driver.role,
      driver: {
        status: driver.status,
        vechicles: {  // Ensure this matches your backend model
          vechicleType: driver.vehicleType,
          vechicleCap: parseFloat(driver.vehicleCap),  // Convert to float
          vechicleStatus: driver.vehicleStatus,
        },
      },
    };

    console.log("Payload being sent:", payload);

    try {
      const response = await axios.post("http://localhost:8080/admin/addDV", payload, {
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      setMessage({ type: "success", text: "✅ Driver added successfully!" });

      // Reset form after successful submission
      setDriver({
        name: "",
        uemailId: "",
        upass: "",
        role: "DRIVER",
        status: "AVAILABLE",
        vehicleType: "",
        vehicleCap: "",
        vehicleStatus: "AVAILABLE",
      });
    } catch (error) {
      setMessage({
        type: "error",
        text: `❌ Failed to add driver. ${error.response?.data || "Please try again."}`,
      });
    }

    setLoading(false);
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Add New Driver</h2>

      {message && (
        <div className={`alert ${message.type === "success" ? "alert-success" : "alert-danger"}`} role="alert">
          {message.text}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        {/* Driver Name */}
        <div className="mb-3">
          <label className="form-label">Driver Name</label>
          <input
            type="text"
            className="form-control"
            name="name"
            value={driver.name}
            onChange={handleChange}
            required
          />
        </div>

        {/* Email */}
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input
            type="email"
            className="form-control"
            name="uemailId"
            value={driver.uemailId}
            onChange={handleChange}
            required
          />
        </div>

        {/* Password */}
        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            type="password"
            className="form-control"
            name="upass"
            value={driver.upass}
            onChange={handleChange}
            required
          />
        </div>

        {/* Vehicle Type */}
        <div className="mb-3">
          <label className="form-label">Vehicle Type</label>
          <select className="form-control" name="vehicleType" value={driver.vehicleType} onChange={handleChange} required>
            <option value="">Select Vehicle Type</option>
            <option value="Bike">Bike</option>
            <option value="Van">Van</option>
            <option value="Truck">Truck</option>
          </select>
        </div>

        {/* Vehicle Capacity */}
        <div className="mb-3">
          <label className="form-label">Vehicle Capacity (kg)</label>
          <input
            type="number"
            className="form-control"
            name="vehicleCap"
            value={driver.vehicleCap}
            onChange={handleChange}
            required
            min="1"
          />
        </div>

        {/* Submit Button */}
        <button type="submit" className="btn btn-primary w-100" disabled={loading}>
          {loading ? "Adding Driver..." : "Add Driver"}
        </button>
      </form>
    </div>
  );
}
