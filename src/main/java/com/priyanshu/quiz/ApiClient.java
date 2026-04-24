package com.priyanshu.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.priyanshu.quiz.model.LeaderboardEntry;
import com.priyanshu.quiz.model.QuizResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    private final ObjectMapper mapper = new ObjectMapper();

    public QuizResponse fetchMessages(String regNo, int poll) throws IOException {
        URL url = URI.create(BASE_URL + "/quiz/messages?regNo=" + regNo + "&poll=" + poll).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        return mapper.readValue(connection.getInputStream(), QuizResponse.class);
    }

    public String submitLeaderboard(String regNo, List<LeaderboardEntry> leaderboard) throws IOException {
        URL url = URI.create(BASE_URL + "/quiz/submit").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String payload = mapper.writeValueAsString(Map.of(
                "regNo", regNo,
                "leaderboard", leaderboard
        ));

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }

        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}