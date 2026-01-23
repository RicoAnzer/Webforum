import { useState } from 'react'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Styles/SignUp.css';

function Login() {

  //useNavigate() allows navigation to other pages
  const navigate = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }

  const [errorMessage, setErrorMessage] = useState(null);

  async function useLogin(formData) {
    const varname = formData.get('name');
    const varpassword = formData.get('password');
    return await axios
      .post("https://localhost:8081/auth/login", { username: varname, password: varpassword }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        console.log(response.data);
        navigate('/');
      })
      .catch(error => {
        setErrorMessage(error?.response?.data);
        console.log(errorMessage);
      });
  }

  return (
    <div className="register-container">
      <h1 className='headline'>Anmelden</h1>
      {errorMessage != null && <h1>{errorMessage}</h1>}
      <form className="register-form" action={useLogin}>
        <div className="form-group">
          <label className='secondary-text' name="name">Name</label>
          <input type="text"
            name="name"
            placeholder="Dein Name" />
        </div>

        <div className="form-group">
          <label className='secondary-text' name="password">Passwort</label>
          <input type="password"
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
