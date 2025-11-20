import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate, BrowserRouter} from 'react-router-dom';
import SignupPage from './SignUp.jsx';
import '../Styles/SignUp.css';

function Login () {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [passwordConfirm, setPasswordConfirm] = useState("");
    return ( 
      <div className="register-container">
        <h1 className='headline'>Anmelden</h1>
        <form className="register-form" onSubmit={e => e.preventDefault()}>
          <div className="form-group">
            <label className='secondary-text' htmlFor="name">Name</label>
            <input type="text" 
                   id="name" 
                   name="name" 
                   placeholder="Dein Name" 
                   onChange={(e) => setName(e.target.value)} />
          </div>

          <div className="form-group">
            <label className='secondary-text' htmlFor="password">Passwort</label>
            <input type="password" 
                   id="password" 
                   name="password" 
                   placeholder="Passwort" 
                   onChange={(e) => setPassword(e.target.value)} 
                   />
          </div>

          <button type="submit" className="submit-btn">Anmelden</button>
        </form>
        
        <div className="hint">
            Noch keinen Account? <a href="/signup">Registrieren</a>
        </div>
      </div>
    );
}

export default Login;
