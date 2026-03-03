package com.web.forum.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.forum.DAO.TopicDAO;
import com.web.forum.Entity.Topic;
import com.web.forum.Security.Error.CustomErrors.ConflictError;
import com.web.forum.Security.Error.CustomErrors.NotFoundError;

@Service
public class TopicService {

    @Autowired
    private TopicDAO topicDAO;

    //Add a new Topic
    public Topic createNewTopic (String name){
        if (topicDAO.readbyName(name) != null) {
            throw new ConflictError("Topic with this name already exists");
        }
        //Generate slug
        //Remove all non ASCII Symbols like "@"
        Pattern NON_ASCII = Pattern.compile("[^\\p{ASCII}]");
        //Remove all characters but letters (a-z) and numbers
        Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");
        //Create slug out of name
        //=> "This is a test name" to "this-is-a-test-name"
        //1. Remove all accents (ä to a), 2. Remove all non ASCII Symbols like "@"
        //3. All letters to lowerCase, 4. All characters but a-z and 0-9 to "-"
        //5. Delete leading and ending "-"
        String slug = Normalizer.normalize(name, Normalizer.Form.NFKD);
        slug = NON_ASCII.matcher(slug).replaceAll("");
        slug = NON_ALPHANUMERIC.matcher(slug.toLowerCase())
                                      .replaceAll("-")
                                      .replaceAll("^-|-$", "");
        Topic generatedTopic = new Topic(null, name, slug);

        return topicDAO.create(generatedTopic);
    }

    //Return Topic based on ID
    public Topic getTopicById (Long Id){
        if (topicDAO.readbyID(Id) == null) {
            throw new NotFoundError("No Topic found");
        }
        return topicDAO.readbyID(Id);
    }

    //Return Topic based on name
    public Topic getTopicByName (String name){
        if (topicDAO.readbyName(name) == null) {
            throw new NotFoundError("No Topic found");
        }
        return topicDAO.readbyName(name);
    }

    //Return all Topics
    public List<Topic> getAllTopics(){
        return topicDAO.readAll();
    }

    //Deletes Topic by name
    public String deleteTopic(String name){
        return topicDAO.delete(name);
    }
}
