package com.nirmit.quiz;

import org.junit.jupiter.api.Test;

import com.priyanshu.quiz.ApiClient;
import com.priyanshu.quiz.model.Event;
import com.priyanshu.quiz.model.LeaderboardEntry;
import com.priyanshu.quiz.model.QuizResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizServiceTest {

    @Test
    void testLeaderboardEntryStoresValues() {
        LeaderboardEntry entry = new LeaderboardEntry("Alice", 280);
        assertEquals("Alice", entry.getParticipant());
        assertEquals(280, entry.getTotalScore());
    }

    @Test
    void testEventStoresValues() {
        Event event = new Event();
        event.setRoundId("R1");
        event.setParticipant("Bob");
        event.setScore(100);

        assertEquals("R1", event.getRoundId());
        assertEquals("Bob", event.getParticipant());
        assertEquals(100, event.getScore());
    }

    @Test
    void testQuizResponseStoresEvents() {
        Event e = new Event();
        e.setParticipant("Charlie");
        e.setScore(50);

        QuizResponse response = new QuizResponse();
        response.setEvents(List.of(e));

        assertNotNull(response.getEvents());
        assertEquals(1, response.getEvents().size());
        assertEquals("Charlie", response.getEvents().get(0).getParticipant());
    }

    @Test
    void testLeaderboardSortedByScoreDescending() {
        List<LeaderboardEntry> leaderboard = List.of(
                new LeaderboardEntry("Alice", 280),
                new LeaderboardEntry("Bob", 295),
                new LeaderboardEntry("Charlie", 260));

        List<LeaderboardEntry> sorted = leaderboard.stream()
                .sorted((a, b) -> Integer.compare(b.getTotalScore(), a.getTotalScore()))
                .toList();

        assertEquals("Bob", sorted.get(0).getParticipant());
        assertEquals("Alice", sorted.get(1).getParticipant());
        assertEquals("Charlie", sorted.get(2).getParticipant());
    }

    @Test
    void testTotalScoreCalculation() {
        List<LeaderboardEntry> leaderboard = List.of(
                new LeaderboardEntry("Alice", 280),
                new LeaderboardEntry("Bob", 295),
                new LeaderboardEntry("Charlie", 260));

        int total = leaderboard.stream().mapToInt(LeaderboardEntry::getTotalScore).sum();
        assertEquals(835, total);
    }

    @Test
    void testDuplicateEventDeduplication() {

        java.util.Set<String> processed = new java.util.HashSet<>();
        java.util.Map<String, Integer> scores = new java.util.HashMap<>();
        String key1 = "R1-Alice";
        if (processed.add(key1))
            scores.merge("Alice", 100, Integer::sum);

        if (processed.add(key1))
            scores.merge("Alice", 100, Integer::sum);

        String key2 = "R2-Alice";
        if (processed.add(key2))
            scores.merge("Alice", 50, Integer::sum);

        assertEquals(150, scores.get("Alice"));
    }

    @Test
    void testApiReturnsValidResponse() throws Exception {
        ApiClient client = new ApiClient();
        QuizResponse response = client.fetchMessages("2024CS101", 0);

        assertNotNull(response, "API response should not be null");
        System.out.println("[Integration] API poll-0 returned "
                + (response.getEvents() != null ? response.getEvents().size() : 0)
                + " event(s).");
    }
}
