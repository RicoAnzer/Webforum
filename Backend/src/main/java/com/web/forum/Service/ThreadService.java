package com.web.forum.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.web.forum.DAO.ThreadDAO;
import com.web.forum.Entity.Thread;
import com.web.forum.Security.Error.CustomErrors.ConflictError;
import com.web.forum.Security.Error.CustomErrors.NotFoundError;

//ThreadService to manage database traffic of Threads (create, delete, etc.)
@Service
public class ThreadService {

    private final ThreadDAO threadDAO;

    public ThreadService(ThreadDAO threadDAO) {
        this.threadDAO = threadDAO;
    }

    //Save a new Thread to database
    public Thread createNewThread(String topicSLug, String name) {
        if (threadDAO.readByName(name) != null) {
            throw new ConflictError("Thread with this name already exists");
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

        Thread createdThread = new Thread(null,topicSLug, name, slug);
        return threadDAO.create(topicSLug, createdThread);
    }

    //Search and return Threads based on ID
    public Thread getThreadByName(String name) {
        Thread foundThread = threadDAO.readByName(name);
        if (foundThread == null) {
            throw new NotFoundError("No Thread found");
        }
        return foundThread;
    }

    //Search and return Threads related to a sepcific Topic
    public List<Thread> getAllThreads(String topicSlug) {
        return threadDAO.readAll(topicSlug);
    }

    //Delete existing Thread based on name
    public String deleteThread(String name) {
        return threadDAO.delete(name);
    }
}
