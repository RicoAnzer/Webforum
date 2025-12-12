import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Styles/SignUp.css';

function Landing() {
  const history = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }


  function logout() {
    const logoutResponse = axios
      .post("https://localhost:8080/auth/logout", {}, {
        withCredentials: true,
        headers: header
      }).then(response => {
        history('/');
      })
      .catch(error => {
        console.log(error);
      });
  }

  return (
    <div className="register-container">
      <h1 className='headline'>Landing Page</h1>

      <button type="submit" onClick={logout} className="submit-btn">Logout</button>

      <div className="hint">
        Noch keinen Account? <a href="/signup">Registrieren</a>
      </div>
    </div>
  );
}

export default Landing;
