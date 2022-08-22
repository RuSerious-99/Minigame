package com.ruserious99.minigame.utils;

public class TimeUtils {

    public static String getFormattedTime(int time) {
        int seconds;
        int minutes;
        minutes = time / 60;
        seconds = time - (minutes * 60);

        String minutesString, secondsString;
        minutesString = minutes < 10 ? "0" + minutes : minutes + "";
        secondsString = seconds < 10 ? "0" + seconds : seconds + "";

        return minutesString + " : " + secondsString;
    }
    public static double getProgress(int timeLeft, int totalTime) {
        return (double) timeLeft / (double) totalTime;
    }
}
