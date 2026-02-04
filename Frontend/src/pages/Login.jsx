import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useError, DisplayErrorMessage } from '../global-variables/ErrorMessage.jsx';
import { useFormData } from '../global-variables/FormData.jsx';
import { FormattedMessage } from 'react-intl';
import { NameInput, PasswordInput } from "../components/forms.jsx";
import '../Styles/SignUp.css';

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
  const { errorMessage } = useError();
  return errorMessage !== '' ? <DisplayErrorMessage /> : null;
}

function Login() {
  const { setErrorMessage } = useError();
  //Save formData in global variable if Error
  const { setFormData, formData } = useFormData();
  //useNavigate() allows navigation to other pages
  const navigate = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }

  async function useLogin() {
    return await axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/login`, { username: formData.name, password: formData.password }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        //Reset data if success
        setFormData({
          ...formData,
          "name": "",
          "password": ""
        })
        setErrorMessage("")
        console.log(response.data);
        navigate('/');
      })
      .catch(error => {
        const errorMsg = error.response?.data;
        if (!errorMsg) {
          setErrorMessage(<FormattedMessage id="error.unexpected" />)
          return;
        }

        switch (error.response.data) {
          case "User not found":
            setErrorMessage(<FormattedMessage id="error.formLogin.userNotFound" />)
            break;
          case "Password not found":
            setErrorMessage(<FormattedMessage id="error.formLogin.passwordNotFound" />)
            break;
          case "User is already logged in":
            setErrorMessage(<FormattedMessage id="error.formLogin.alreadyLoggedIn" />)
            break;
        }
      });
  }

  return (
    <div className="register-container">
      <h1 className='headline'><FormattedMessage id="forum.form.login" /></h1>
      <form className="register-form" action={useLogin}>

        <NameInput />
        <PasswordInput />

        <ErrorDisplay />
        <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.login" /></button>
      </form>
      <div className="hint">
        <FormattedMessage id="forum.form.login.hint" /> <a href="/signup"><FormattedMessage id="forum.form.register" /></a>
      </div>
    </div>
  );
}

export default Login;
