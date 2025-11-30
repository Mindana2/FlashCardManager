package org.Flashcard.models.studySession;

public final class StudyAlgorithmFactory {


    private StudyAlgorithmFactory() {
    }

    public static StudyAlgorithm createAlgorithm(String algorithmType) {
        return switch (algorithmType.toLowerCase()) {
            case "all" -> new StudyAllAlgorithm();
            case "today" -> new StudyTodayAlgorithm();

            default -> throw new IllegalArgumentException("Unsupported study algorithm type: " + algorithmType);
        };
    }
}