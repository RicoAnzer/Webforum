import { BrowserRouter as Router, Route, Routes, BrowserRouter } from 'react-router-dom';
import { useState } from 'react';
import { FormattedMessage, IntlProvider } from 'react-intl';
import { ErrorProvider } from './global-variables/ErrorMessage.jsx'
import { TopicProvider } from './global-variables/Topics.jsx'
import { ThreadList } from './pages/ThreadList.jsx';
import Compose from './components/compose.jsx';
import {
  NameInputProvider,
  PasswordInputProvider,
  ConfirmPasswordInputProvider,
  AddTopicInputProvider,
  LoginVisibilityProvider,
  SignUpVisiblityProvider,
  AddTopicVisiblityProvider
} from './global-variables/FormData.jsx'
import { Header } from './components/topics.jsx'
import './Styles/App.css'

//Import routing pages
import Forum from "./pages/Forum.jsx";

//Import locale files containing translations
import messages_en from './locales/en.json';
//List locale files to iterate through to switch language
const messages = {
  en: messages_en,
};

function App() {
  //Starting language
  const [state] = useState({ locale: 'en' })

  return (
    <ErrorProvider>
      <Compose components={[
        TopicProvider,
        NameInputProvider,
        PasswordInputProvider,
        ConfirmPasswordInputProvider,
        AddTopicInputProvider,
        LoginVisibilityProvider,
        SignUpVisiblityProvider,
        AddTopicVisiblityProvider]}>
        <IntlProvider locale={state.locale}
          messages={messages[state.locale]}>
          <div className="main">
            <BrowserRouter >
              <Header></Header>
              <Routes >
                <Route path="/" element={<Forum />} />
                <Route path="/:topicId" element={<ThreadList />} />
              </Routes>
            </BrowserRouter>
          </div>
        </IntlProvider>
      </Compose>
    </ErrorProvider>
  )
}
export default App
