package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Comment {
    private final int id;
    private ForumPost post;
    private final Garden garden;
    private final String text;
    private final int likes;

    public Comment(int id, Garden garden, String text, int likes) {
        this.id = id;
        this.text = text;
        this.likes = likes;
        this.garden = garden;
    }
    public Comment(int id, ForumPost post, Garden garden, String text, int likes) {
        this(id, garden, text, likes);
        this.post = post;
    }

    public int getId() {
        return id;
    }
    public ForumPost getPost() {
        return post;
    }
    public Garden getGarden() {
        return garden;
    }
    public String getText() {
        return text;
    }
    public int getLikes() {
        return likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
