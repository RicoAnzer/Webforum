import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Styles/SignUp.css';

function Landing() {
  
  //useNavigate() allows navigation to other pages
  const navigate = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }

  async function useLogout() {
    return await axios
      .post("https://localhost:8080/auth/logout", {}, {
        withCredentials: true,
        headers: header
      }).then(response => {
        navigate('/login');
      })
      .catch(error => {
        console.log(error);
      });
  }

  return (
    <div className="register-container">
      <h1 className='headline'>Landing Page</h1>

      <button type="button" onClick={useLogout} className="submit-btn">Logout</button>

      <div className="hint">
        Noch keinen Account? <a href="/signup">Registrieren</a>
      </div>
    </div>
  );
}

export default Landing;
