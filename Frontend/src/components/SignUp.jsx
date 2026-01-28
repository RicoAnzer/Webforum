import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useError } from '../global-variables/ErrorManager.jsx';
import { FormattedMessage, useIntl } from 'react-intl';
import '../Styles/SignUp.css';

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
  const { errorMessage } = useError();
  return errorMessage !== '' ? <div className='headline' >{errorMessage}</div> : null;
}

function SignUp() {
  const { setErrorMessage } = useError();
  const intl = useIntl();

  //useNavigate() allows navigation to other pages
  const navigate = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }

  //Internationalization of placeholders
  const namePlaceholder = intl.formatMessage({
    id: 'forum.form.username.placeholder',
  });
  const passwordPlaceholder = intl.formatMessage({
    id: 'forum.form.password.placeholder',
  });
  const confirmPasswordPlaceholder = intl.formatMessage({
    id: 'forum.form.confirmPassword.placeholder',
  });

  function signUp(formData) {
    const name = formData.get("name");
    const userPassword = formData.get("password");
    const confirmUserPassword = formData.get("confirmPassword");

    const response = axios
      .post("https://localhost:8080/auth/register", { username: name, password: userPassword, confirmedPassword: confirmUserPassword }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        navigate('/login');
      })
      .catch(error => {
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
        <div className="form-group">
          <label className='secondary-text' name="name"><FormattedMessage id="forum.form.username" /></label>
          <input type="text"
            name="name"
            placeholder={namePlaceholder} />
        </div>

        <div className="form-group">
          <label className='secondary-text' name="password"><FormattedMessage id="forum.form.password" /></label>
          <input type="password"
            name="password"
            placeholder={passwordPlaceholder} />
        </div>

        <div className="form-group">
          <label className='secondary-text' name="confirmPassword"><FormattedMessage id="forum.form.confirmPassword" /></label>
          <input type="password"
            name="confirmPassword"
            placeholder={confirmPasswordPlaceholder} />
        </div>

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
