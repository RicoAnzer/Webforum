import axios from 'axios';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import { useTopics } from '../global-variables/Topics';
import { useAddTopicVisible, useLoginVisible, useSignUpVisible } from "../global-variables/FormData";

import '../Styles/TopicsHeader.css';

const header = {
    'Content-Type': 'application/json'
}

//Read topicsList and for each topics display topics-container
//Use react-router-dom Link to dynamically create pages
const DisplayTopics = ({ topicsList }) => {
    const { setAddTopicVisible } = useAddTopicVisible()
    return topicsList?.length > 0 ? (
        <div className="topics-list">
            {topicsList.map((topic) => (
                <Link to={`/${topic.id}`} key={topic.id} className='topics-container'>
                    <p>{topic.name}</p>
                </Link>
            ))}
            <div className='topics-container add-topic' onClick={() => setAddTopicVisible(prev => !prev)}>
                <p><FormattedMessage id="forum.form.addTopic" /></p>
            </div>
        </div>
    ) : null;
};

export const Header = () => {
    const { topics, setTopics } = useTopics()
    const { setSignUpVisible } = useSignUpVisible()
    const { setLoginVisible } = useLoginVisible()

    function getTopics() {
        return axios
            .get(`https://${import.meta.env.VITE_SPRING_URL}/topic/getAll`, {
                withCredentials: true,
                headers: header
            }).then(response => {
                setTopics(response?.data);
            })
            .catch(error => {
                console.log(error);
            });
    }

    //Load Topics at start
    useEffect(() => {
        getTopics()
    }, []);

    return (
        <header>
            <DisplayTopics topicsList={topics}></DisplayTopics>
            <div className='profile-container'>
                <button type="button" onClick={() => setLoginVisible(prev => !prev)} className="submit-btn"><FormattedMessage id="forum.form.login" /></button>
                <button type="button" onClick={() => setSignUpVisible(prev => !prev)} className="submit-btn"><FormattedMessage id="forum.form.register" /></button>
            </div>
        </header>
    )
}