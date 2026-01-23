import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { useState } from 'react';
import './App.css'
import LoginPage from './components/Login.jsx';
import SignupPage from './components/SignUp.jsx';
import Landing from "./components/Landing Page.jsx";

function App() {
  const [user, setUser] = useState(null)
  const [errorMessage, setErrorMessage] = useState('');
  return (
    <div className="main">
     {user != null && <h1>Hello user.name</h1>}
      <Router>
            <Routes>
              <Route path="/" element={<Landing/>} />
              <Route path="/signup" element={<SignupPage/>} />
              <Route path="/login" element={<LoginPage/>} />
            </Routes>
      </Router>
    </div>
  )
}
export default App
