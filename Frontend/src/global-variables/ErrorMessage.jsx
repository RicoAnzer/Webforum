import { useState, createContext, useContext } from 'react';

const ErrorContext = createContext({ errorMessage: '', setErrorMessage: () => { } });
export const useError = () => useContext(ErrorContext);

export const ErrorProvider = ({ children }) => {
  const [errorMessage, setErrorMessage] = useState('');
  return (
    <ErrorContext.Provider value={{ errorMessage, setErrorMessage }}>
      {children}
    </ErrorContext.Provider>
  )
}

export const DisplayErrorMessage = () => {
  const { errorMessage } = useError();
  return <div className='form-group' >
    <div className='form-error'>
      <div className='error-message' >{errorMessage}</div>
    </div>
  </div>
}