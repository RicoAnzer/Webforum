import { useState, createContext, useContext } from 'react';

const UserContext = createContext({ user: null, setUser: () => { } });
export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  )
}
