# Quiz Leaderboard System

Java-based solution for the SRM Internship Assignment.

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 17 or higher |
| Maven | 3.6 or higher |

Verify your setup:
```bash
java -version
mvn -version
```

---

## How It Works

- Polls the validator API **10 times** (once every 5 seconds)
- Deduplicates events using `roundId + participant` as a unique key
- Aggregates total scores per participant
- Generates a leaderboard sorted by score (descending)
- Submits the final leaderboard to the API

---

## Quick Start

### 1. Clone the repository
```bash
git clone <your-repo-url>
cd quiz-leaderboard-system
```

### 2. Compile the project
```bash
mvn compile
```

### 3. Run the application
```bash
mvn exec:java -Dexec.mainClass="com.nirmit.quiz.Main"
```

> **Note:** The run takes ~50 seconds because it polls the API 10 times with a 5-second delay between each poll.

**Expected output:**
```
Final Leaderboard:
Bob -> 295
Alice -> 280
Charlie -> 260
Total Score: 835
Submission Response: {"regNo":"RA2311003030305","totalPollsMade":...,"submittedTotal":835,"attemptCount":...}}
```

---

## Running Tests

### Run all tests (unit + integration)
```bash
mvn test
```

### Run only unit tests (no API call, fast)
```bash
mvn test -Dtest="QuizServiceTest#testLeaderboardEntryStoresValues+testEventStoresValues+testQuizResponseStoresEvents+testLeaderboardSortedByScoreDescending+testTotalScoreCalculation+testDuplicateEventDeduplication"
```

### Run only the API integration test
```bash
mvn test -Dtest="QuizServiceTest#testApiReturnsValidResponse"
```

### Run a specific test by name
```bash
mvn test -Dtest="QuizServiceTest#testTotalScoreCalculation"
```

---

## Build a Standalone JAR

```bash
mvn package
```

Run the JAR directly:
```bash
java -cp target/quiz-leaderboard-system-1.0-SNAPSHOT.jar;target/dependency/* com.nirmit.quiz.Main
```

---

## Project Structure

```
src/
├── main/java/com/nirmit/quiz/
│   ├── Main.java               # Entry point
│   ├── ApiClient.java          # HTTP calls to the validator API
│   ├── QuizService.java        # Core leaderboard logic
│   └── model/
│       ├── Event.java          # Represents a single quiz event
│       ├── LeaderboardEntry.java
│       └── QuizResponse.java   # API response model
└── test/java/com/nirmit/quiz/
    └── QuizServiceTest.java    # Unit + integration tests
```

---

## API Reference

**Base URL:** `https://devapigw.vidalhealthtpa.com/srm-quiz-task`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/quiz/messages?regNo={regNo}&poll={0-9}` | Fetch quiz events for a specific poll |
| POST | `/quiz/submit` | Submit the final leaderboard |

---

## Update Registration Number

Edit `REG_NO` in `Main.java` before running:
```java
private static final String REG_NO = "RA2311003030305";
```
