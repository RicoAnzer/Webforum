import { useLocation } from 'react-router-dom';

/**Displaying Posts of current Thread*/
export const Thread = () => {
    //Id saved as <Link state={{ threadId: thread.id }}></Link>
    const location = useLocation();
    const threadId = location.state?.threadId;
    return (
        <div className="post-list">
            <h1>Test Thread ID: {threadId}</h1>
        </div>
    );
};