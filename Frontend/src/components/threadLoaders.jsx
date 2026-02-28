import axios from 'axios';

const header = {
    'Content-Type': 'application/json'
}

//Load Array of Threads of current topic
export const threadLoader = async ({ params }) => {
    const response = await axios
        .get(`https://${import.meta.env.VITE_SPRING_URL}/thread/getAll/${params.topicSlug}`, {
            withCredentials: true,
            headers: header
        });
    //Return object containing topicSlug and array of Threads
    return {
        topicSlug: params.topicSlug,
        threads: response?.data
    };
};