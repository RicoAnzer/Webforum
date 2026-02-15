import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import {
  NameInputProvider, PasswordInputProvider, ConfirmPasswordInputProvider, AddTopicInputProvider,
  LoginVisibilityProvider, SignUpVisiblityProvider, AddTopicVisiblityProvider
} from './global-variables/PopupData.jsx'
import { ErrorProvider } from './global-variables/ErrorMessage.jsx'
import { TopicProvider } from './global-variables/Topics.jsx'
import { UserProvider } from './global-variables/SignedInUser.jsx'
import Compose from './components/compose.jsx';

import './Styles/index.css'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Compose components={[
      ErrorProvider,
      TopicProvider,
      UserProvider,
      NameInputProvider,
      PasswordInputProvider,
      ConfirmPasswordInputProvider,
      AddTopicInputProvider,
      LoginVisibilityProvider,
      SignUpVisiblityProvider,
      AddTopicVisiblityProvider]}>
      <App />
    </Compose>
  </StrictMode>,
)
