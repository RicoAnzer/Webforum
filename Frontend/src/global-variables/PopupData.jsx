import { useState, createContext, useContext } from 'react';

//Save form data in Sign Up and Login Forms and prevents reset of input field if errors occur
const NameInputContext = createContext({ nameInput: '', setNameInput: () => { } });
const PasswordInputContext = createContext({ passwordInput: '', setPasswordInput: () => { } });
const ConfirmPasswordInputContext = createContext({ confirmPasswordInput: '', setConfirmPasswordInput: () => { } });
const AddTopicInputContext = createContext({ addTopicInput: '', setAddTopicInput: () => { } });
//Toggle visiblity of popup windows
const LoginVisibilityContext = createContext({ loginVisible: false, setLoginVisible: () => { } });
const SignUpVisibilityContext = createContext({ signUpVisible: false, setSignUpVisible: () => { } });
const AddTopicVisibilityContext = createContext({ addTopicVisible: false, setAddTopicVisible: () => { } });

//useContext to display and change variables globally
export const useNameInput = () => useContext(NameInputContext);
export const usePasswordInput = () => useContext(PasswordInputContext);
export const useConfirmPasswordInput = () => useContext(ConfirmPasswordInputContext);
export const useAddTopicInput = () => useContext(AddTopicInputContext);

export const useLoginVisible = () => useContext(LoginVisibilityContext);
export const useSignUpVisible= () => useContext(SignUpVisibilityContext);
export const useAddTopicVisible= () => useContext(AddTopicVisibilityContext);

//Provider need to be set at App.jsx for global variables to work
export const NameInputProvider = ({ children }) => {
    const [nameInput, setNameInput] = useState("");
    return (
        <NameInputContext.Provider value={{ nameInput, setNameInput }}>
            {children}
        </NameInputContext.Provider>
    )
}

export const PasswordInputProvider = ({ children }) => {
    const [passwordInput, setPasswordInput] = useState("");
    return (
        <PasswordInputContext.Provider value={{ passwordInput, setPasswordInput }}>
            {children}
        </PasswordInputContext.Provider>
    )
}

export const ConfirmPasswordInputProvider = ({ children }) => {
    const [confirmPasswordInput, setConfirmPasswordInput] = useState("");
    return (
        <ConfirmPasswordInputContext.Provider value={{ confirmPasswordInput, setConfirmPasswordInput }}>
            {children}
        </ConfirmPasswordInputContext.Provider>
    )
}

export const AddTopicInputProvider = ({ children }) => {
    const [addTopicInput, setAddTopicInput] = useState("");
    return (
        <AddTopicInputContext.Provider value={{ addTopicInput, setAddTopicInput }}>
            {children}
        </AddTopicInputContext.Provider>
    )
}

export const LoginVisibilityProvider = ({ children }) => {
    const [loginVisible, setLoginVisible] = useState(false)
    return (
        <LoginVisibilityContext value={{ loginVisible, setLoginVisible }}>
            {children}
        </LoginVisibilityContext>
    )
}

export const SignUpVisiblityProvider = ({ children }) => {
    const [signUpVisible, setSignUpVisible] = useState(false)
    return (
        <SignUpVisibilityContext value={{ signUpVisible, setSignUpVisible }}>
            {children}
        </SignUpVisibilityContext>
    )
}

export const AddTopicVisiblityProvider = ({ children }) => {
    const [addTopicVisible, setAddTopicVisible] = useState(false)
    return (
        <AddTopicVisibilityContext value={{ addTopicVisible, setAddTopicVisible }}>
            {children}
        </AddTopicVisibilityContext>
    )
}