import axios from 'axios';
import { Link, useLoaderData, useParams, useRevalidator } from 'react-router-dom';
import { FormattedMessage, useIntl } from 'react-intl';
import { useError, DisplayErrorMessage } from '../global-variables/ErrorMessage.jsx';
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
            threadList.map((thread) => (
                <Link to={`${thread.slug}`}
                    key={thread.id}
                    className='thread-container'>
                    <p className='secondary-text'>{thread.name}</p>
                </Link>
            ))
        ) : null}
        <div className='thread-container add-thread' onClick={() => setAddThreadVisible(prev => !prev)}>
            <p><FormattedMessage id="forum.form.addThread" /></p>
        </div>
    </>
};

/**Displaying Threads of current Topic*/
export const ThreadList = () => {
    //Load data of threadLoader => List of all Threads
    const { topicSlug, threads } = useLoaderData();

    const revalidator = useRevalidator();
    const intl = useIntl();

    const defaultError = intl.formatMessage({ id: "error.unexpected" });
    const { setErrorMessage } = useError()

    const { addThreadInput, setAddThreadInput } = useAddThreadInput();
    const { addThreadVisible, setAddThreadVisible } = useAddThreadVisible()

    async function addThread(event) {
        try {
            if (event) event.preventDefault();
            const response = await axios
                .post(`https://${import.meta.env.VITE_SPRING_URL}/thread/${topicSlug}/${addThreadInput}`, {}, {
                    withCredentials: true,
                    headers: header
                })
            //Exit add Thread Popup
            setAddThreadVisible(prev => !prev)
            //Reset data if success
            setAddThreadInput("")
            //Rerender Thread list
            revalidator.revalidate();
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

    return (
        <div className="thread-list">
            <DisplayThreads threadList={threads}></DisplayThreads>
            {/**Add thread popup */}
            { addThreadVisible &&
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