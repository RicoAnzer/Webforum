import { useState, createContext, useContext } from 'react';

const ThreadContext = createContext({ threads: [], setThreads: () => { } });
export const useThreads = () => useContext(ThreadContext);

export const ThreadProvider = ({ children }) => {
  const [threads, setThreads] = useState([]);
  return (
    <ThreadContext.Provider value={{ threads, setThreads }}>
      {children}
    </ThreadContext.Provider>
  )
}