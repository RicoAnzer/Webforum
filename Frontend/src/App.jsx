import { Route, Routes, BrowserRouter } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { FormattedMessage, IntlProvider } from 'react-intl';
import { ErrorProvider, useError, DisplayErrorMessage } from './global-variables/ErrorMessage.jsx'
import { ThreadList } from './pages/ThreadList.jsx';
import {
  useAddTopicInput, useConfirmPasswordInput, useNameInput, usePasswordInput,
  useSignUpVisible, useLoginVisible, useAddTopicVisible,
} from './global-variables/PopupData.jsx'
import {
  NameInput, PasswordInput, ConfirmPasswordInput, AddTopicInput
} from "./components/popup.jsx";
//Import external components
import { Header } from './components/topicsList.jsx'
//Import CSS styles
import './Styles/App.css'
import './Styles/Forum.css';
import './Styles/Popup.css';

//Import routing pages
import Forum from "./pages/Forum.jsx";
//Import locale files containing translations
import messages_en from './locales/en.json';

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
  const { errorMessage } = useError();
  return errorMessage !== '' ? <DisplayErrorMessage /> : null;
}

//List locale files to iterate through to switch language
const messages = {
  en: messages_en,
};

function App() {
  //Starting language
  const [state] = useState({ locale: 'en' })

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

  const header = {
    'Content-Type': 'application/json'
  }

  function signUp() {
    //useNavigate() allows navigation to other pages
    const navigate = useNavigate();
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
    //useNavigate() allows navigation to other pages
    const navigate = useNavigate();
    return await axios
      .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/login`, { username: nameInput, password: passwordInput }, {
        withCredentials: true,
        headers: header
      }).then(response => {
        //Reset data if success
        setNameInput("")
        setPasswordInput("")
        setErrorMessage("")

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
    //useNavigate() allows navigation to other pages
    const navigate = useNavigate();
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
      })
      .catch(error => {
        console.log(error.response?.data);
      });
  }

  //Load Topics at start
      useEffect(() => {
          console.log("Ist: " + addTopicVisible);
      }, [addTopicVisible]);

  return (
    <ErrorProvider>
        <IntlProvider locale={state.locale}
          messages={messages[state.locale]}>
          <div className="main">
            <BrowserRouter >
              <Header></Header>
              <div className='main-container'>
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
                  <Route path="/" element={<Forum />} />
                  <Route path="/:topicId" element={<ThreadList />} />
                </Routes>
              </div>
            </BrowserRouter>
          </div>
        </IntlProvider>
    </ErrorProvider>
  )
}
export default App
