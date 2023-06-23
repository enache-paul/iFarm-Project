package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.domain.Constants;

public class CommentLikeEvent extends Event{
    int commentId;

    protected CommentLikeEvent(int gardenId, int commentId) {
        super(gardenId, Constants.EventType.COMMENT_LIKE);
        this.commentId = commentId;
    }

    public int getCommentId() {
        return commentId;
    }
}
