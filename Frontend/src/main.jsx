import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import {
  useAddTopicInput, useConfirmPasswordInput, useNameInput, usePasswordInput,
  NameInputProvider, PasswordInputProvider, ConfirmPasswordInputProvider, AddTopicInputProvider,
  useSignUpVisible, useLoginVisible, useAddTopicVisible,
  LoginVisibilityProvider, SignUpVisiblityProvider, AddTopicVisiblityProvider
} from './global-variables/PopupData.jsx'
import { TopicProvider } from './global-variables/Topics.jsx'
import Compose from './components/compose.jsx';

import './Styles/index.css'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Compose components={[
      TopicProvider,
      NameInputProvider,
      PasswordInputProvider,
      ConfirmPasswordInputProvider,
      AddTopicInputProvider,
      LoginVisibilityProvider,
      SignUpVisiblityProvider,
      AddTopicVisiblityProvider]}>
      <App/>
    </Compose>
  </StrictMode>,
)
