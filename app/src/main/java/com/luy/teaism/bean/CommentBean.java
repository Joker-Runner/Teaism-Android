package com.luy.teaism.bean;

/**
 * 评论Bean
 * Created by joker on 7/11 0011.
 */
public class CommentBean {
    private int id;
    private int teaFoodId;
    private int commentatorId;
    private String comment;
    private long createTime;
    private int goodCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeaFoodId() {
        return teaFoodId;
    }

    public void setTeaFoodId(int teaFoodId) {
        this.teaFoodId = teaFoodId;
    }

    public int getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(int commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }
}
