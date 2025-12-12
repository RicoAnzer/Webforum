import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Styles/SignUp.css';

function SignUp() {
  const history = useNavigate();
  function signUp(formData) {
    const username = formData.get("name");
    const userPassword = formData.get("password");
    const confirmUserPassword = formData.get("confirmPassword");

    const header = {
      'Content-Type': 'application/json'
    }
    const response = axios
      .post("https://localhost:8080/auth/register", { name: username, password: userPassword }, {
        withCredentials: true,
        headers: header,
        params: { confirmPassword: confirmUserPassword }
      }).then(response => {
        history('/');
      })
      .catch(error => {
        console.log(error);
      });
  }
  return (
    <div className="register-container">
      <h1 className='headline'>Registrieren</h1>
      <form className="register-form" action={signUp}>
        <div className="form-group">
          <label className='secondary-text' name="name">Name</label>
          <input type="text"
            id="name"
            name="name"
            placeholder="Dein Name" />
        </div>

        <div className="form-group">
          <label className='secondary-text' name="password">Passwort</label>
          <input type="password"
            id="password"
            name="password"
            placeholder="Passwort" />
        </div>

        <div className="form-group">
          <label className='secondary-text' name="confirmPassword">Passwort wiederholen</label>
          <input type="password"
            id="confirmPassword"
            name="confirmPassword"
            placeholder="Passwort bestätigen" />
        </div>

        <button type="submit" className="submit-btn">Registrieren</button>
      </form>

      <div className="hint">
        Bereits registriert? <a href="/">Einloggen</a>
      </div>
    </div>
  );
}

export default SignUp;
