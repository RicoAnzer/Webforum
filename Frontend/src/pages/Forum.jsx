import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import { useError, DisplayErrorMessage } from '../global-variables/ErrorMessage.jsx';
import {
  useAddTopicInput,
  useConfirmPasswordInput,
  useNameInput, usePasswordInput,
  useSignUpVisible,
  useLoginVisible,
  useAddTopicVisible
} from '../global-variables/PopupData.jsx';
import {
  NameInput,
  PasswordInput,
  ConfirmPasswordInput,
  AddTopicInput
} from "../components/popup.jsx";

import '../Styles/Popup.css';
import '../Styles/Forum.css';

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
  const { errorMessage } = useError();
  return errorMessage !== '' ? <DisplayErrorMessage /> : null;
}

function Forum() {
  const { setErrorMessage } = useError()
  //Save form data if Error
  const { addTopicInput, setAddTopicInput } = useAddTopicInput();
  const { nameInput, setNameInput } = useNameInput();
  const { passwordInput, setPasswordInput } = usePasswordInput();
  const { confirmPasswordInput, setConfirmPasswordInput } = useConfirmPasswordInput();

  //Toggle visiblity of popup windows
  const { addTopicVisible, setAddTopicVisible } = useAddTopicVisible()
  const { signUpVisible } = useSignUpVisible()
  const { loginVisible } = useLoginVisible()

  //useNavigate() allows navigation to other pages
  const navigate = useNavigate();
  const header = {
    'Content-Type': 'application/json'
  }

  function signUp() {
    const response = axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/register`, { username: nameInput, password: passwordInput, confirmedPassword: confirmPasswordInput }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        //Reset data if success
        setNameInput("")
        setPasswordInput("")
        setConfirmPasswordInput("")

        setErrorMessage("")
        navigate('/login')
      })
      .catch(error => {
        const errorMsg = error.response?.data;
        if (!errorMsg) {
          setErrorMessage(error.response?.data)
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

  async function login() {
    return await axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/login`, { username: nameInput, password: passwordInput }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        //Reset data if success
        setNameInput("")
        setPasswordInput("")

        setErrorMessage("")
        console.log(response.data)
        navigate('/');
      })
      .catch(error => {
        const errorMsg = error.response?.data;
        if (!errorMsg) {
          setErrorMessage(error.response?.data)
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

  async function logout() {
    return await axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/logout`, {}, {
        withCredentials: true,
        headers: header
      }).then(response => {
        navigate('/login');
      })
      .catch(error => {
        console.log(error.response?.data);
      });
  }

  async function addTopic() {
    return await axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/topic/add/${addTopicInput}`, {}, {
        withCredentials: true,
        headers: header
      }).then(response => {
        setAddTopicInput("")
        setAddTopicVisible(prev => !prev)
        console.log();
      })
      .catch(error => {
        console.log(error.response?.data);
      });
  }

  return (
    <div className="main-container">
      
      {signUpVisible &&
        <div className="register-container popup register">
          <h1 className='headline'><FormattedMessage id="forum.form.register" /></h1>
          <form className="register-form" action={signUp}>
            <NameInput/>
            <PasswordInput/>
            <ConfirmPasswordInput/>
            <ErrorDisplay/>
            <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.register" /></button>
          </form>

          <div className="hint">
            <FormattedMessage id="forum.form.register.hint" /> <a href="/login"><FormattedMessage id="forum.form.login" /></a>
          </div>
        </div>
      }

      {loginVisible &&
        <div className="register-container popup login">
          <h1 className='headline'><FormattedMessage id="forum.form.login" /></h1>
          <form className="register-form" action={login}>
            <NameInput/>
            <PasswordInput/>
            <ErrorDisplay/>
            <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.login" /></button>
          </form>
          <div className="hint">
            <FormattedMessage id="forum.form.login.hint" /> <a href="/signup"><FormattedMessage id="forum.form.register" /></a>
          </div>
        </div>
      }

      {addTopicVisible &&
        <div className="register-container popup addTopic">
          <h1 className='headline'><FormattedMessage id="forum.form.addTopic" /></h1>
          <form className="register-form" action={addTopic}>
            <AddTopicInput/>
            <ErrorDisplay/>
            <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.addTopic" /></button>
          </form>
        </div>
      }

      <div className="forum">
        <h1 className='headline'><FormattedMessage id="forum.main.title" /></h1>

        <button type="button" onClick={logout} className="submit-btn"><FormattedMessage id="forum.form.logout" /></button>
      </div>
    </div>
  );
}

export default Forum;
