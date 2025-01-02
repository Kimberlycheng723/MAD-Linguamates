package com.example.madasignment.community;

import java.util.ArrayList;
import java.util.List;

public class DiscussionRepository {

    // Simulates fetching discussion posts from a data source
    public List<DiscussionPost> getDiscussionPosts() {
        List<DiscussionPost> discussionPostList = new ArrayList<>();
        // Placeholder data
        discussionPostList.add(new DiscussionPost("Amad", "Hi there! How about the verb 'second guess'? Is it a synonym for 'question'?", 11));
        discussionPostList.add(new DiscussionPost("Martin", "Can you help me find grammatical mistakes in this text? We are not going to compare ourselves to others...", 5));
        discussionPostList.add(new DiscussionPost("Joe", "I need some advice on learning faster. Any tips for improving vocabulary retention?", 8));
        return discussionPostList;
    }
}
