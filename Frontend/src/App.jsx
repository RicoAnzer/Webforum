import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css'
import LoginPage from './components/Login.jsx';
import SignupPage from './components/SignUp.jsx';
import Landing from "./components/Landing Page.jsx";

function App() {

  return (
    <div className="main">
      <Router>
            <Routes>
              <Route path="/" element={<LoginPage/>} />
              <Route path="/signup" element={<SignupPage/>} />
              <Route path="/landing" element={<Landing/>} />
            </Routes>
      </Router>
    </div>
  )
}

export default App
