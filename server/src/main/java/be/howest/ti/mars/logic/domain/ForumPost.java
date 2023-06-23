package be.howest.ti.mars.logic.domain;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ForumPost {
    private final int id;
    private final Garden garden;
    private final String text;
    private final String imgLink;
    private final Date uploadTime;
    private static final int NO_ID_SET = -1;
    private final int likes;
    private final List<Comment> comments;


    public ForumPost(int id, Garden garden, String text, String imgLink, Date uploadTime, int likes, List<Comment> comments) {
        this.id = id;
        this.text = text;
        this.imgLink = imgLink;
        this.uploadTime = uploadTime;
        this.likes = likes;
        this.comments = comments;
        this.garden = garden;
    }
    public ForumPost(Garden garden, String text, String imgLink, Date uploadTime, int likes, List<Comment> comments) {
        this(NO_ID_SET, garden, text, imgLink, uploadTime, likes, comments);
    }

    public int getId() {
        return id;
    }
    public Garden getGarden() {
        return garden;
    }
    public String getText() {
        return text;
    }
    public String getImgLink() {
        return imgLink;
    }
    public Date getUploadTime() {
        return uploadTime;
    }
    public int getLikes() {return likes;}
    public List<Comment> getComments() {return comments;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForumPost)) return false;
        ForumPost forumPost = (ForumPost) o;
        return id == forumPost.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
