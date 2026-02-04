import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { useState } from 'react';
import { FormattedMessage, IntlProvider } from 'react-intl';
import { ErrorProvider } from './global-variables/ErrorMessage.jsx'
import { FormDataProvider } from './global-variables/FormData.jsx'
import './Styles/App.css'

//Import routing pages
import LoginPage from './pages/Login.jsx';
import SignupPage from './pages/SignUp.jsx';
import Landing from "./pages/Landing Page.jsx";

//Import locale files containing translations
import messages_en from './locales/en.json';
import messages_de from './locales/de.json';
//List locale files to iterate through to switch language
const messages = {
  en: messages_en,
  de: messages_de,
};

function App() {
  //Starting language
  const [state, setState] = useState({ locale: 'en' })
  //Switch language
  function handleLanguageChange() {
    (e) => { this.setState({ locale: e.target.value }) }
  }

  return (
    <FormDataProvider>
      <ErrorProvider>
        <IntlProvider locale={state.locale}
          messages={messages[state.locale]}>
          <div className="main">
            <Router>
              <Routes>
                <Route path="/" element={<Landing />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/login" element={<LoginPage />} />
              </Routes>
            </Router>
          </div>
        </IntlProvider>
      </ErrorProvider>
      </FormDataProvider>
  )
}
export default App
