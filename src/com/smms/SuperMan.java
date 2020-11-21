package com.smms;

/**
 * @author setusb
 * @version 1.0
 * @date 2020/11/5 18:33
 */
public class SuperMan {
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private int age;
    /**
     * 会员编码
     */
    private int cardId;
    /**
     * 会员密码
     */
    private String password;
    /**
     * 会员生日
     */
    private String birthday;
    /**
     * 入会时间
     */
    private String joiningTime;
    /**
     * 积分
     */
    private long score;

    public SuperMan() {
    }

    public SuperMan(String name, String sex, int age, int cardId, String password, String birthday, String joiningTime, long score) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.cardId = cardId;
        this.password = password;
        this.birthday = birthday;
        this.joiningTime = joiningTime;
        this.score = score;
    }

    @Override
    public String toString() {
        return "SuperMan{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", cardId=" + cardId +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                ", joiningTime='" + joiningTime + '\'' +
                ", score=" + score +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJoiningTime() {
        return joiningTime;
    }

    public void setJoiningTime(String joiningTime) {
        this.joiningTime = joiningTime;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
