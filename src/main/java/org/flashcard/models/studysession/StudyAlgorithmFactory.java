package org.flashcard.models.studysession;

public final class StudyAlgorithmFactory {

    /**
     * This package is responsible for dividing flashcards into sub-decks:
     * Flashcards that are due today or sooner get sorted into one 'pile'.
     * While the option to study all cards remains with StudyAllAlgorithm.
     */
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