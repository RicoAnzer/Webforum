import { useState, createContext, useContext } from 'react';

const ErrorContext = createContext({ errorMessage: '', setErrorMessage: () => { } });
export const useError = () => useContext(ErrorContext);

export const ErrorProvider = ({ children }) => {
  const [errorMessage, setErrorMessage] = useState('');
  return (
  <ErrorContext.Provider value={{ errorMessage, setErrorMessage }}>
    {children}
  </ErrorContext.Provider>
  );
};