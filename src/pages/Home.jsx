import React from "react";
import { jwtDecode } from "jwt-decode";

export default function Home() {
  const token=localStorage.getItem("token");
  let username="Guest";
  if(token)
  {
    const decodedToken=jwtDecode(token)
     username=decodedToken.sub||"Guest";
  }
  return (
    <div className="dashboard-content">
      <h1>Dashboard Home</h1>
      <p>Welcome to the admin panel {username}.</p>
    </div>
  );
}
