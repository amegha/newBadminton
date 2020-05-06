package com.example.myapp_badminton.megha;

public interface GetAnswers {
    void getCorrectAnswersFromServer(/*String serverAddress*/);
//    void getScore();
    void parseCorrectAnswers(String result);
}
