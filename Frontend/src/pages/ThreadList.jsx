import { useParams } from 'react-router-dom';

export const ThreadList = () => {
    // Holt die topicId aus der URL
    const { topicId } = useParams();

    // Hier würdest du normalerweise die Daten für dieses spezifische Topic 
    // aus einer API oder einem Global State laden.
    return (
        <div className="topic-detail">
            <h1>Test Topic ID: {topicId}</h1>
            {/* Inhalt der Seite */}
        </div>
    );
};