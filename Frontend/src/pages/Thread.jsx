import { useLocation, useParams } from 'react-router-dom';

/**Displaying Posts of current Thread*/
export const Thread = () => {
    //Id saved as <Link state={{ threadId: thread.id }}></Link>
    const location = useLocation();
    const { threadSlug } = useParams()
    //const threadId = location.state?.threadId;
    return (
        <div className="post-list">
            <h1>Test Thread Slug: {threadSlug}</h1>
        </div>
    );
};