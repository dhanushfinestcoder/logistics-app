import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import './App.css';
import Footer from './components/Footer';
import Header from './components/Header';
import HeroSection from './components/HeroSection';
import LoginPage from "./pages/LoginPage";
import SignUp from "./pages/SignUp";
import DashboardLayout from "./components/DashboardLayout";
import Driversts from "./components/Driversts";
import Shipments from "./components/Shipments"
import History from "./components/History";

import Home from "./pages/Home";
import Orders from "./pages/Orders"
import MakeOrder from "./components/Makeorder";
import AssignDriver from "./components/AssignDriver";
import AddDriver from "./components/AddDriver";
import VerifyOtp from "./components/VerifyOtp";
import Dashboard from "./pages/Dasboard";
import MakeIntransit from "./components/MakeIntransit";

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={
            <>
              <Header />
              <HeroSection />
              <Footer />
            </>
          } /> 

          <Route path="/login" element={
            <>
              <LoginPage />
            </>
          }/> 
          <Route path="/register" element={
            <>
              <SignUp />
            </>
          }/>
          <Route element={<DashboardLayout />}>
            <Route path="/dashboard" element={<Dashboard/>} />
            <Route path="/home" element={<Home/>}/>
            <Route path="/orders" element={<Orders/>} />
            <Route path="/driverSts" element={<Driversts/>}/>
            <Route path="/place" element={<MakeOrder/>}/>
            <Route path="/assign-driver/:orderId" element={<AssignDriver/>}/>
            <Route path="addDriver" element={<AddDriver/>}/>
            <Route path="/ship" element={<Shipments/>}/>
            <Route path="/otp" element={<VerifyOtp/>}/>
            <Route path="/intrans" element={<MakeIntransit/>}/>
            <Route path="/his" element={<History/>}/>
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
