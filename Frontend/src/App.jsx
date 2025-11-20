import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css'
import LoginPage from './components/Login.jsx';
import SignupPage from './components/SignUp.jsx';

function App() {

  return (
    <div className="main">
      <Router>
            <Routes>
              <Route path="/" element={<LoginPage/>} />
              <Route path="/signup" element={<SignupPage/>} />
            </Routes>
      </Router>
    </div>
  )
}

export default App
