import { useState } from "react";
import { FcGoogle } from "react-icons/fc";
import { useNavigate, Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

import back from "../assets/bck.jpg";
import "../components/styles/LoginPage.css";

export default function LoginPage() {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);

    const loginData = {
      name, 
      upass: password, 
    };

    try {
      const response = await fetch("http://localhost:8080/user/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginData),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Login failed. Please check your credentials.");
      }

      const token = await response.text();
      console.log(token)
      console.log(loginData)
      localStorage.setItem("token", token); 

      const decodedToken = jwtDecode(token);
      const userRole = decodedToken.roles;
     // console.log(userRole)
      localStorage.setItem("role", decodedToken.roles); 

      alert("Login successful!");
      navigate("/home"); 
    } catch (error) {
      console.error("Error logging in:", error);
      alert(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-illustration">
        <img src={back} alt="Illustration" />
      </div>

      <div className="login-form-container">
        <h2 className="login-title">Welcome Back</h2>
        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label>Username</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="input-group">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        <div className="or-divider">or</div>

        <button className="google-btn">
          <FcGoogle className="google-icon" /> Sign in with Google
        </button>

        <p className="signup-link">
          Don't have an account? <Link to="/register">Sign Up</Link>
        </p>
      </div>
    </div>
  );
}
