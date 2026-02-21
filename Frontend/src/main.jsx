import { StrictMode, useState } from 'react'
import { createRoot } from 'react-dom/client'
import { IntlProvider } from 'react-intl';
import {
  NameInputProvider, PasswordInputProvider, ConfirmPasswordInputProvider, AddTopicInputProvider, AddThreadInputProvider,
  LoginVisibilityProvider, SignUpVisiblityProvider, AddTopicVisiblityProvider, AddThreadVisiblityProvider
} from './global-variables/PopupData.jsx'
import { ErrorProvider } from './global-variables/ErrorMessage.jsx'
import { TopicProvider } from './global-variables/Topics.jsx'
import { ThreadProvider } from './global-variables/Threads.jsx'
import { UserProvider } from './global-variables/SignedInUser.jsx'
import Compose from './components/compose.jsx';

//Import routing pages
//Import locale files containing translations
import messages_en from './locales/en.json';

import App from './App.jsx'

import './Styles/index.css'

//List locale files to iterate through to switch language
const messages = {
  en: messages_en,
};

function Main() {
   //Starting language
  const [state] = useState({ locale: 'en' })
  return (
    <>
      <StrictMode>
        <IntlProvider locale={state.locale}
          messages={messages[state.locale]}>
          <Compose components={[
            ErrorProvider,
            TopicProvider,
            ThreadProvider,
            UserProvider,
            NameInputProvider,
            PasswordInputProvider,
            ConfirmPasswordInputProvider,
            AddTopicInputProvider,
            AddThreadInputProvider,
            LoginVisibilityProvider,
            SignUpVisiblityProvider,
            AddTopicVisiblityProvider,
            AddThreadVisiblityProvider
          ]}>
            <App />
          </Compose>
        </IntlProvider>
      </StrictMode>,
    </>)
}

createRoot(document.getElementById('root')).render(
  <Main></Main>
)
