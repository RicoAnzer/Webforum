import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../Styles/SignUp.css';

function Login() {
  const history = useNavigate();
  function login(formData) {
    const varname = formData.get("name");
    const varpassword = formData.get("password");

    const header = {
      'Content-Type': 'application/json'
    }
    const response = axios
      .post("https://localhost:8080/auth/login", { name: varname, password: varpassword }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        history('/landing');
      })
      .catch(error => {
        console.log(error);
      });
  }

  return (
    <div className="register-container">
      <h1 className='headline'>Anmelden</h1>

      <form className="register-form" action={login}>
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
            placeholder="Passwort"
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
