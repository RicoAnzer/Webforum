import { useParams } from 'react-router-dom';

/**Displaying threads of current topic*/
export const ThreadList = () => {
    const { topicId } = useParams();
    return (
        <div className="topic-detail">
            <h1>Test Topic ID: {topicId}</h1>
            {}
        </div>
    );
};