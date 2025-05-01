import React from "react";
import "../components/styles/HeroSection.css";

function HeroSection() {
  return (
    <section className="hero">
      <div className="hero-overlay">
        <div className="hero-content">
          <h1>
            The Future of <span className="highlight">Logistics</span> is Here
          </h1>
          <p>Seamless, Reliable & Real-Time Freight Solutions for Global Logistics.</p>
          <div className="hero-buttons">
            <button className="primary-btn">Get a Quote</button>
            <button className="secondary-btn">Explore Services</button>
          </div>
          <div className="stats">
            <div className="stat-item">
              <h3>99%</h3>
              <p>On-time Delivery Rate</p>
            </div>
            <div className="stat-item">
              <h3>500K+</h3>
              <p>Shipments Completed</p>
            </div>
            <div className="stat-item">
              <h3>$10B+</h3>
              <p>Freight Value Handled</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

export default HeroSection;
