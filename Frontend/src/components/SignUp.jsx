import React from 'react';
import '../Styles/SignUp.css';

function Registrieren () {
    return (
      <div className="register-container">
        <h1 className='headline'>Registrierung</h1>
        <form className="register-form" onSubmit={e => e.preventDefault()}>
          <div className="form-group">
            <label className='secondary-text' htmlFor="name">Name</label>
            <input type="text" id="name" name="name" placeholder="Dein Name" />
          </div>

          <div className="form-group">
            <label className='secondary-text' htmlFor="password">Passwort</label>
            <input type="password" id="password" name="password" placeholder="Passwort" />
          </div>

          <div className="form-group">
            <label className='secondary-text' htmlFor="confirmPassword">Passwort wiederholen</label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Passwort bestätigen" />
          </div>

          <button type="submit" className="submit-btn" disabled>Registrieren</button>
        </form>

        <p className="hint">
          Bereits registriert? <a href="/login">Einloggen</a>
        </p>
      </div>
    );
}

export default Registrieren;
