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

function Login() {
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


  async function useLogin(formData) {

    const varname = formData.get('name');
    const varpassword = formData.get('password');
    return await axios
      .post("https://localhost:8080/auth/login", { username: varname, password: varpassword }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        console.log(response.data);
        navigate('/');
      })
      .catch(error => {
        switch (error.response.data) {
          case "User not found":
            setErrorMessage(<FormattedMessage id="error.formLogin.userNotFound" />)
            break;
          case "Password not found":
            setErrorMessage(<FormattedMessage id="error.formLogin.passwordNotFound" />)
            break;
        }
      });
  }

  return (
    <div className="register-container">
      <h1 className='headline'><FormattedMessage id="forum.form.login" /></h1>
      <form className="register-form" action={useLogin}>
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
            placeholder={passwordPlaceholder}
          />
        </div>
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
