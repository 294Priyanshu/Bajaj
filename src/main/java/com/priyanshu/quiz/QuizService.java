package com.priyanshu.quiz;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.priyanshu.quiz.model.Event;
import com.priyanshu.quiz.model.LeaderboardEntry;
import com.priyanshu.quiz.model.QuizResponse;

public class QuizService {
    private final ApiClient apiClient = new ApiClient();

    public List<LeaderboardEntry> generateLeaderboard(String regNo) throws IOException, InterruptedException {
        Set<String> processed = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int poll = 0; poll < 10; poll++) {
            QuizResponse response = apiClient.fetchMessages(regNo, poll);

            if (response.getEvents() != null) {
                for (Event event : response.getEvents()) {
                    String key = event.getRoundId() + "-" + event.getParticipant();
                    if (processed.add(key)) {
                        scores.merge(event.getParticipant(), event.getScore(), Integer::sum);
                    }
                }
            }

            if (poll < 9) {
                Thread.sleep(5000);
            }
        }

        return scores.entrySet().stream()
                .map(e -> new LeaderboardEntry(e.getKey(), e.getValue()))
                .sorted((a, b) -> Integer.compare(b.getTotalScore(), a.getTotalScore()))
                .collect(Collectors.toList());
    }

    public void submit(String regNo, List<LeaderboardEntry> leaderboard) throws IOException {
        String response = apiClient.submitLeaderboard(regNo, leaderboard);
        System.out.println("Submission Response: " + response);
    }
}