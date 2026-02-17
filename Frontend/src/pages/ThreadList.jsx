import { useLocation } from 'react-router-dom';

/**Displaying threads of current topic*/
export const ThreadList = () => {
    //Id saved as <Link state={{ topicId: topic.id }}></Link>
    const location = useLocation();
    const topicId = location.state?.topicId;
    return (
        <div className="thread-list">
            <h1>Test Topic ID: {topicId}</h1>
            { }
        </div>
    );
};