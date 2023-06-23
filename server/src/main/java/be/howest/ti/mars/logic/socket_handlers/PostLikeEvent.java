package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.domain.Constants;

public class PostLikeEvent extends Event{
    private final int postId;

    protected PostLikeEvent(int gardenId, int postId) {
        super(gardenId, Constants.EventType.POST_LIKE);
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }
}
