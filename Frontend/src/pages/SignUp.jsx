import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useError, DisplayErrorMessage } from '../global-variables/ErrorMessage.jsx';
import { useFormData } from '../global-variables/FormData.jsx';
import { FormattedMessage } from 'react-intl';
import { NameInput, PasswordInput, ConfirmPasswordInput } from "../components/forms.jsx";
import '../Styles/SignUp.css';

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
  const { errorMessage } = useError();
  return errorMessage !== '' ? <DisplayErrorMessage /> : null;
}

function SignUp() {
  const { setErrorMessage } = useError();
  //Save formData in global variable if Error
  const { setFormData, formData } = useFormData();
  //useNavigate() allows navigation to other pages
  const navigate = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }
  function signUp() {
    console.log(formData);
    const response = axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/register`, { username: formData.name, password: formData.password, confirmedPassword: formData.confirmPassword }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        //Reset data if success
        setFormData({
          ...formData,
          "name": "",
          "password": "",
          "confirmPassword": ""
        })
        setErrorMessage("")
        navigate('/login');
      })
      .catch(error => {
        const errorMsg = error.response?.data;
        if (!errorMsg) {
          setErrorMessage(<FormattedMessage id="error.unexpected" />)
          return;
        }

        switch (error.response.data) {
          case "Username is empty":
            setErrorMessage(<FormattedMessage id="error.formRegister.userEmpty" />)
            break;
          case "Please choose an username with less than 20 characters":
            setErrorMessage(<FormattedMessage id="error.formRegister.usernameTooLong" />)
            break;
          case "Username already exists":
            setErrorMessage(<FormattedMessage id="error.formRegister.usernameExists" />)
            break;
          case "Please choose a password":
            setErrorMessage(<FormattedMessage id="error.formRegister.passwordEmpty" />)
            break;
          case "Password and confirmedPassword don't match":
            setErrorMessage(<FormattedMessage id="error.formRegister.passwordNotMatching" />)
            break;
        }
      });
  }
  return (
    <div className="register-container">
      <h1 className='headline'><FormattedMessage id="forum.form.register" /></h1>
      <form className="register-form" action={signUp}>

        <NameInput onChange={(e) => setFormDataState({...formDataState, name: e.target.value})}/>
        <PasswordInput />
        <ConfirmPasswordInput />

        <ErrorDisplay />
        <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.register" /></button>
      </form>

      <div className="hint">
        <FormattedMessage id="forum.form.register.hint" /> <a href="/login"><FormattedMessage id="forum.form.login" /></a>
      </div>
    </div>
  );
}

export default SignUp;
