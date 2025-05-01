import React, { useState, useEffect } from "react";
import { Outlet, Link } from "react-router-dom";
import * as FaIcons from "react-icons/fa";
import * as AiIcons from "react-icons/ai";
import { Avatar, IconButton, Menu, MenuItem, Typography } from "@mui/material";
import {jwtDecode} from "jwt-decode"; 
import "./styles/DashboardLayout.css";

export default function DashboardLayout() {
  const [sidebar, setSidebar] = useState(false);
  const [roles, setRoles] = useState(null);
  const [name, setName] = useState(null);
  const [anchorEl, setAnchorEl] = useState(null);
  const [auth,setAuth]=useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token"); 
    console.log(token)
    if (token) {
      try {
        const decodedToken = jwtDecode(token); 
        setName(decodedToken.sub); 
        setRoles(decodedToken.roles); 
        setAuth(true);
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
  }, []);

  

  const showSidebar = () => setSidebar(!sidebar);
  const handleMenuOpen = (event) => setAnchorEl(event.currentTarget);
  const handleMenuClose = () => setAnchorEl(null);
  const handleLogout = () => {
    localStorage.removeItem("jwt"); 
    setName(null);
    setRoles(null);
    handleMenuClose();
  };

  return (
    <>
     
      <div className="navbar">
        <Link to="#" className="menu-bars">
          <FaIcons.FaBars onClick={showSidebar} />
        </Link>
        <div className="navbar-right">
          {name ? (
            <>
              <IconButton onClick={handleMenuOpen} size="small">
                <Avatar>{name.charAt(0).toUpperCase()}</Avatar> {/* Show first letter of name */}
              </IconButton>
              <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleMenuClose}>
                <MenuItem>
                  <Typography variant="body1">
                    <strong>{name}</strong> ({roles})
                  </Typography>
                </MenuItem>
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
              </Menu>
            </>
          ) : (
            <Typography variant="body2">No user info</Typography>
          )}
        </div>
      </div>

      <nav className={sidebar ? "nav-menu active" : "nav-menu"}>
        <ul className="nav-menu-items" onClick={showSidebar}>
          <li className="navbar-toggle">
            <Link to="#" className="menu-bars">
              <AiIcons.AiOutlineClose />
            </Link>
          </li>

          <li className="nav-text">
            <Link to="/">
              <AiIcons.AiFillHome />
              <span>Home</span>
            </Link>
          </li>

          {roles === "ROLE_ADMIN" && (
            <>

              <li className="nav-text">
                <Link to="/dashboard">
                  <FaIcons.FaMoneyBill />
                  <span>DashBoard</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/orders">
                  <FaIcons.FaCartPlus />
                  <span>Orders</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/addDriver">
                  <FaIcons.FaCar />
                  <span>Add New Driver</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/driverSts">
                  <FaIcons.FaUser />
                  <span>Driver Status</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/ship">
                  <FaIcons.FaShippingFast />
                  <span>Shipments</span>
                </Link>
              </li>
              
            </>
          )}

          {roles === "ROLE_CUSTOMER" && (
            <>
              <li className="nav-text">
                <Link to="/orders">
                  <FaIcons.FaCartPlus />
                  <span>View Orders</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/place">
                  <FaIcons.FaShopify />
                  <span>Make Order</span>
                </Link>
              </li>
            </>
          )}


{roles === "ROLE_DRIVER" && (
            <>
              <li className="nav-text">
                <Link to="/intrans">
                  <FaIcons.FaCartPlus />
                  <span>Received</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/his">
                  <FaIcons.FaShopify />
                  <span>History</span>
                </Link>
              </li>
              <li className="nav-text">
                <Link to="/otp">
                  <FaIcons.FaUser/>
                  <span>Verify Otp</span>
                </Link>
              </li>
            </>
          )}

        </ul>
      </nav>

      <div className="dashboard-content">
        <Outlet />
      </div>
    </>
  );
}
