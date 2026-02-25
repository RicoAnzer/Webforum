import axios from 'axios';
import { Route, Routes, BrowserRouter } from 'react-router-dom';
import { FormattedMessage, useIntl } from 'react-intl';
import { useError, DisplayErrorMessage } from './global-variables/ErrorMessage.jsx'
import { useUser } from './global-variables/SignedInUser.jsx';
import { useTopics } from './global-variables/Topics.jsx'
import {
  useAddTopicInput, useConfirmPasswordInput, useNameInput, usePasswordInput,
  useSignUpVisible, useLoginVisible, useAddTopicVisible
} from './global-variables/PopupData.jsx'
import {
  NameInput, PasswordInput, ConfirmPasswordInput, AddTopicInput
} from "./components/popup.jsx";
//Import external components
import { Header } from './components/topicsList.jsx'
import { ThreadList } from './pages/ThreadList.jsx';
import { Thread } from './pages/Thread.jsx';
//Import CSS styles
import './Styles/App.css'
import './Styles/Popup.css';

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
  const { errorMessage } = useError();
  return errorMessage !== '' ? <DisplayErrorMessage /> : null;
}

function App() {
  const intl = useIntl();
  const defaultError = intl.formatMessage({ id: "error.unexpected" });

  const { setErrorMessage } = useError()
  const { setTopics } = useTopics()

  //Save form data if Error
  const { addTopicInput, setAddTopicInput } = useAddTopicInput();
  const { nameInput, setNameInput } = useNameInput();
  const { passwordInput, setPasswordInput } = usePasswordInput();
  const { confirmPasswordInput, setConfirmPasswordInput } = useConfirmPasswordInput();

  //Toggle visiblity of popup windows
  const { addTopicVisible, setAddTopicVisible } = useAddTopicVisible()
  const { signUpVisible, setSignUpVisible } = useSignUpVisible()
  const { loginVisible, setLoginVisible } = useLoginVisible()

  const { user, setUser } = useUser()

  const header = {
    'Content-Type': 'application/json'
  }

  async function signUp(event) {
    try {
      if (event) event.preventDefault();
      const response = await axios
        .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/register`, { username: nameInput, password: passwordInput, confirmedPassword: confirmPasswordInput }, {
          withCredentials: true,
          headers: header
        })
      setSignUpVisible(prev => !prev)
      //Reset data if success
      setNameInput("")
      setPasswordInput("")
      setConfirmPasswordInput("")

      setErrorMessage("")
    }
    catch (error) {
      switch (error?.response?.data) {
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
        default: setErrorMessage(<FormattedMessage id="error.unexpected" />)
          break;
      }
    }
  }

  async function login(event) {
    try {
      if (event) event.preventDefault();
      const response = await axios
        .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/login`, { username: nameInput, password: passwordInput }, {
          withCredentials: true,
          headers: header
        })
      setUser(response?.data)
      setLoginVisible(prev => !prev)
      //Reset data if success
      setNameInput("")
      setPasswordInput("")
      setErrorMessage("")
    }
    catch (error) {
      switch (error.response?.data) {
        case "User not found":
          setErrorMessage(<FormattedMessage id="error.formLogin.userNotFound" />)
          break;
        case "Password not found":
          setErrorMessage(<FormattedMessage id="error.formLogin.passwordNotFound" />)
          break;
        case "User is already logged in":
          setErrorMessage(<FormattedMessage id="error.formLogin.alreadyLoggedIn" />)
          break;
        default: setErrorMessage(<FormattedMessage id="error.unexpected" />)
          break;
      }
    }
  }

  async function addTopic(event) {
    try {
      if (event) event.preventDefault();
      const response = await axios
        .post(`https://${import.meta.env.VITE_SPRING_URL}/topic/add/${addTopicInput}`, {}, {
          withCredentials: true,
          headers: header
        })
      //Add Topic to list
      setTopics(prevTopics => [...prevTopics, response.data]);
      setAddTopicVisible(prev => !prev)
      //Reset data if success
      setAddTopicInput("")
    }
    catch (error) {
      switch (error.response?.data) {
        case "Topic is empty":
          setErrorMessage(<FormattedMessage id="error.addTopic.empty" />)
          break;
        case "Topic with this name already exists":
          setErrorMessage(<FormattedMessage id="error.addTopic.topicExists" />)
          break;
        default:
          const fullError = `${defaultError}: ${error?.response?.data || 'Unknown Error'}`
          setErrorMessage(fullError)
          break;
      }
    }
  }

  return (
    <div className="main">
      <BrowserRouter >
        <Header></Header>
        <div className='main-container'>
          {/**Sign up popup */}
          {signUpVisible &&
            <div className="register-container popup register">
              <h1 className='headline'><FormattedMessage id="forum.form.register" /></h1>
              <form className="register-form" onSubmit={signUp}>
                <NameInput />
                <PasswordInput />
                <ConfirmPasswordInput />
                <ErrorDisplay />
                <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.register" /></button>
              </form>

              <div className="hint">
                <FormattedMessage id="forum.form.register.hint" /> <a href="/login"><FormattedMessage id="forum.form.login" /></a>
              </div>
            </div>
          }

          {/**Log in popup */}
          {loginVisible &&
            <div className="register-container popup login">
              <h1 className='headline'><FormattedMessage id="forum.form.login" /></h1>
              <form className="register-form" onSubmit={login}>
                <NameInput />
                <PasswordInput />
                <ErrorDisplay />
                <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.login" /></button>
              </form>

              <div className="hint">
                <FormattedMessage id="forum.form.login.hint" /> <a href="/signup"><FormattedMessage id="forum.form.register" /></a>
              </div>
            </div>
          }

          {/**Add topic popup */}
          {addTopicVisible &&
            <div className="register-container popup addTopic">
              <h1 className='headline'><FormattedMessage id="forum.form.addTopic" /></h1>
              <form className="register-form" onSubmit={addTopic}>
                <AddTopicInput />
                <ErrorDisplay />
                <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.addTopic" /></button>
              </form>
            </div>
          }
          <Routes >
            <Route path=":topicId" element={<ThreadList />} />
            <Route path=":topicId/:threadId" element={<Thread />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  )
}
export default App
