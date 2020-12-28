package com.example.lookarounddemo.data;

import java.io.Serializable;
/**
 *
 * @ClassName: CommentItem
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yiw
 * @date 2015-12-28 下午3:44:38
 *
 */
public class CommentItem implements Serializable {

    private String commentId;
    private String publisher;
    private String text;
    private String time;
    public CommentItem(String commentId, String publisher, String time, String text){
        this.commentId = commentId;
        this.publisher = publisher;
        this.time = time;
        this.text = text;

    }
    public String getCommentId() {
        return this.commentId;
    }

    public String getText() {
        return this.text;
    }
    public String getPublisher(){
        return this.publisher;
    }
    public String getTime() {
        return this.time;
    }

//    public void setId(String id) {
//        this.id = id;
//    }
//    public void setContent(String content) {
//        this.content = content;
//    }
//    public User getUser() {
//        return user;
//    }
//    public void setUser(User user) {
//        this.user = user;
//    }
//    public User getToReplyUser() {
//        return toReplyUser;
//    }
//    public void setToReplyUser(User toReplyUser) {
//        this.toReplyUser = toReplyUser;
//    }

}

