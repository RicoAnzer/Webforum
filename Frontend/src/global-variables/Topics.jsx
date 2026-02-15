import { useState, createContext, useContext } from 'react';

const TopicContext = createContext({ topics: [], setTopics: () => { } });
export const useTopics = () => useContext(TopicContext);

export const TopicProvider = ({ children }) => {
  const [topics, setTopics] = useState([]);
  return (
    <TopicContext.Provider value={{ topics, setTopics }}>
      {children}
    </TopicContext.Provider>
  )
}