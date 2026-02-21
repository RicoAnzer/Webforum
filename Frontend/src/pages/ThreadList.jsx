import axios from 'axios';
import { useEffect } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { FormattedMessage, useIntl } from 'react-intl';
import { useError, DisplayErrorMessage } from '../global-variables/ErrorMessage.jsx';
import { useThreads } from '../global-variables/Threads.jsx';
import { useAddThreadInput, useAddThreadVisible } from '../global-variables/PopupData.jsx';

import { AddThreadInput } from '../components/popup.jsx'
import { CancelButton } from '../components/cancelButton.jsx'

import '../Styles/ThreadList.css'
import '../Styles/Popup.css';

const header = {
    'Content-Type': 'application/json'
}

//Function to display global variable errorMessage => if errorMessage is null, make errorDisplay invisible
function ErrorDisplay() {
    const { errorMessage } = useError();
    return errorMessage !== '' ? <DisplayErrorMessage /> : null;
}

//Read threadList and display all Threads of current Topic
//Use react-router-dom Link to dynamically create pages for each Thread
const DisplayThreads = ({ threadList }) => {
    const { setAddThreadVisible } = useAddThreadVisible()
    return <>
        {threadList?.length > 0 ? (
            <>
                {
                    threadList.map((thread) => (
                        <div className='thread-container' key={thread.id}>
                            <Link to={`${thread.slug}`}
                                key={thread.id}
                                state={{ threadId: thread.id }}>
                                <p className='secondary-text'>{thread.name}</p>
                            </Link>
                        </div>
                    ))}
            </>
        ) : null}
        <div className='thread-container add-thread' onClick={() => setAddThreadVisible(prev => !prev)}>
            <p><FormattedMessage id="forum.form.addThread" /></p>
        </div>
    </>
};

/**Displaying Threads of current Topic*/
export const ThreadList = () => {
    //Id saved as <Link state={{ topicId: topic.id }}></Link>
    const location = useLocation();
    const topicId = location.state?.topicId;

    const intl = useIntl();
    const defaultError = intl.formatMessage({ id: "error.unexpected" });

    const { setErrorMessage } = useError()
    const { threads, setThreads } = useThreads()

    const { addThreadInput, setAddThreadInput } = useAddThreadInput();
    const { addThreadVisible, setAddThreadVisible } = useAddThreadVisible()

    async function addThread(event) {
        try {
            if (event) event.preventDefault();
            const response = await axios
                .post(`https://${import.meta.env.VITE_SPRING_URL}/thread/add/${topicId}/${addThreadInput}`, {}, {
                    withCredentials: true,
                    headers: header
                })
            //Add Thread to list
            setThreads(prevTopics => [...prevTopics, response.data]);
            //Exit add Thread Popup
            setAddThreadVisible(prev => !prev)
            //Reset data if success
            setAddThreadInput("")
        }
        catch (error) {
            switch (error.response?.data) {
                case "Thread name is empty":
                    setErrorMessage(<FormattedMessage id="error.addThread.empty" />)
                    break;
                case "Thread with this name already exists":
                    setErrorMessage(<FormattedMessage id="error.addThread.threadExists" />)
                    break;
                default:
                    const fullError = `${defaultError}: ${error?.response?.data || 'Unknown Error'}`
                    setErrorMessage(fullError)
                    break;
            }
        }
    }

    //Load Threads at start if Topic got clicked
    useEffect(() => {
        //Boolean to prevent race condition (One response takes longer and overwrites others using older data)
        let isCurrent = true;
        //Load Threads of current Topic and fill list
        const getThreads = async () => {
            try {
                const response = await axios.get(
                    `https://${import.meta.env.VITE_SPRING_URL}/thread/getAll/${topicId}`,
                    { withCredentials: true, headers: header }
                );
                //Update only, if useEffect hasn't been reset
                if (isCurrent) {
                    setThreads(response?.data);
                }
            } catch (error) {
                if (isCurrent) {
                    console.log(error?.response?.data);
                }
            }
        };
        getThreads();
        //Reset boolean if finished
        return () => {
            isCurrent = false;
        };
        //Load Threads if topicId got changed (when another Topic got clicked)
    }, [topicId]);

    return (
        <div className="thread-list">
            <DisplayThreads threadList={threads}></DisplayThreads>
            {/**Add thread popup */}
            {
                addThreadVisible &&
                <div className="register-container popup addThread">
                    <h1 className='headline'><FormattedMessage id="forum.form.addThread" /></h1>
                    <form className="register-form" onSubmit={addThread}>
                        <AddThreadInput />
                        <ErrorDisplay />
                        <div className='button-holder'>
                            <CancelButton setVisible={setAddThreadVisible}></CancelButton>
                            <button type="submit" className="submit-btn"><FormattedMessage id="forum.form.addThread" /></button>
                        </div>
                    </form>
                </div>
            }
        </div>
    );
};