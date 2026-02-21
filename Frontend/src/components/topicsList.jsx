import axios from 'axios';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import { useTopics } from '../global-variables/Topics';
import { useUser } from '../global-variables/SignedInUser.jsx'
import { useAddTopicVisible, useLoginVisible, useSignUpVisible } from "../global-variables/PopupData";

import '../Styles/TopicsHeader.css';

const header = {
    'Content-Type': 'application/json'
}

//Read topicsList and for each topics display topics-container
//Use react-router-dom Link to dynamically create pages
const DisplayTopics = ({ topicsList }) => {
    const { setAddTopicVisible } = useAddTopicVisible()
    return <div className="topics-list">
        {topicsList?.length > 0 ? (
            <>
                {topicsList.map((topic) => (
                    <Link to={`${topic.slug}`}
                        key={topic.id}
                        state={{ topicId: topic.id }}
                        className='topics-container'>
                        <p className='secondary-text'>{topic.name}</p>
                    </Link>
                ))}
            </>
        ) : null}
        <div className='topics-container add-topic' onClick={() => setAddTopicVisible(prev => !prev)}>
            <p><FormattedMessage id="forum.form.addTopic" /></p>
        </div>
    </div>
};

export const Header = () => {
    const { topics, setTopics } = useTopics()
    const { user, setUser } = useUser()
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
                console.log(error?.response?.data);
            });
    }

    async function logout() {
        try {
            const response = await axios
                .post(`https://${import.meta.env.VITE_SPRING_URL}/auth/logout`, {}, {
                    withCredentials: true,
                    headers: header
                })
            //Delete logged in user information
            setUser(null)
        }
        catch (error) {
            console.log(error.response?.data);
        }
    }

    //Load Topics at start
    useEffect(() => {
        //Boolean to prevent race condition (One response takes longer and overwrites others using older data)
        let isCurrent = true;
        //Load Topics and fill list
        const getTopics = async () => {
            try {
                const response = await axios
                    .get(`https://${import.meta.env.VITE_SPRING_URL}/topic/getAll`, {
                        withCredentials: true,
                        headers: header
                    })
                //Update only, if useEffect hasn't been reset
                if (isCurrent) {
                    setTopics(response?.data);
                }
            } catch (error) {
                if (isCurrent) {
                    console.log(error?.response?.data);
                }
            }
        }
        getTopics()
        //Reset boolean if finished
        return () => {
            isCurrent = false;
        };
    }, []);

    return (
        <header>
            <DisplayTopics topicsList={topics}></DisplayTopics>
            <div className='profile-container'>
                {/**Display if logged in*/}
                {user != null &&
                    <div className='profile'>
                        <button type="button" onClick={logout} className="submit-btn"><FormattedMessage id="forum.form.logout" /></button>
                    </div>}

                {/**Display if logged out*/}
                {user == null &&
                    <div className='button-holder'>
                        <button type="button" onClick={() => setLoginVisible(prev => !prev)} className="submit-btn"><FormattedMessage id="forum.form.login" /></button>
                        <button type="button" onClick={() => setSignUpVisible(prev => !prev)} className="submit-btn"><FormattedMessage id="forum.form.register" /></button>
                    </div>
                }
            </div>
        </header>
    )
}