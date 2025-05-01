import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../components/styles/Header.css";
import myImage from "../assets/transifyLogo.png";

function Header() {
  const navigate = useNavigate();
  const location = useLocation(); 
  const [auth, setAuth] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        setAuth(true);
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
  }, []);

  // Hide header on login page
  if (location.pathname === "/login") {
    return null;
  }

  return (
    <header className="header">
      <div className="logo">
        <img src={myImage} alt="TransiFy Logo" height={80} />
      </div>
      <nav className="nav">
        <ul>
          <li onClick={() => navigate("/")}>Home</li>
          <li onClick={() => navigate("/about")}>About Us</li>
          <li onClick={() => navigate("/features")}>Features</li>
          <li onClick={() => navigate("/contact")}>Contact</li>
        </ul>
      </nav>
      <div className="quote-button">
        {!auth ? (
          <button className="get-started" onClick={() => navigate("/login")}>
            Get Started
          </button>
        ) : (
          <button className="dashboard" onClick={() => navigate("/home")}>
            Dashboard
          </button>
        )}
      </div>
    </header>
  );
}

export default Header;
