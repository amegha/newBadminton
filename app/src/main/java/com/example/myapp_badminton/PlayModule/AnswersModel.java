package com.example.myapp_badminton.PlayModule;

public class AnswersModel {
    int questionNumber;
    String shotType;
    String ShotLocation;
    String correctShotLocation;
    String correctShotType;
    long elapsedTime, maxTime;

    public AnswersModel(int questionNumber, String shotType, String shotLocation, String correctShotType, String correctShotLocation, long elapsedTime, int maxTime) {
        this.questionNumber = questionNumber;
        this.shotType = shotType;
        this.ShotLocation = shotLocation;
        this.correctShotType = correctShotType;
        this.correctShotLocation = correctShotLocation;
        this.elapsedTime = elapsedTime;
        this.maxTime = maxTime;
    }

    @Override
    public String toString() {
        return "AnswersModel{" +
                "questionNumber=" + questionNumber +
                ", shotType='" + shotType + '\'' +
                ", ShotLocation='" + ShotLocation + '\'' +
                ", correctShotLocation='" + correctShotLocation + '\'' +
                ", correctShotType='" + correctShotType + '\'' +
                ", elapsedTime=" + elapsedTime +
                ", maxTime=" + maxTime +
                '}';
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getShotType() {
        return shotType;
    }

    public void setShotType(String shotType) {
        this.shotType = shotType;
    }

    public String getShotLocation() {
        return ShotLocation;
    }

    public void setShotLocation(String shotLocation) {
        ShotLocation = shotLocation;
    }

    public String getCorrectShotLocation() {
        return correctShotLocation;
    }

    public void setCorrectShotLocation(String correctShotLocation) {
        this.correctShotLocation = correctShotLocation;
    }

}
