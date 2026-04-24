package com.priyanshu.quiz;

import java.util.List;

import com.priyanshu.quiz.model.LeaderboardEntry;

public class Main {
    private static final String REG_NO = "2024CS101"; // Priyanshu Gupta

    public static void main(String[] args) {
        try {
            QuizService service = new QuizService();

            List<LeaderboardEntry> leaderboard = service.generateLeaderboard(REG_NO);

            System.out.println("Final Leaderboard:");
            int total = 0;
            for (LeaderboardEntry entry : leaderboard) {
                System.out.println(entry.getParticipant() + " -> " + entry.getTotalScore());
                total += entry.getTotalScore();
            }

            System.out.println("Total Score: " + total);

            service.submit(REG_NO, leaderboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}