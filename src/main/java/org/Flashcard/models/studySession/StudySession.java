//package org.Flashcard.models.studySession;
//
//import org.Flashcard.models.dataClasses.Deck;
//import org.Flashcard.models.dataClasses.FlashCard;
//import org.Flashcard.models.dataClasses.User;
//
//import java.util.List;
//
//public class StudySession {
//    private List<FlashCard> cards;
//    private FlashCard currentCard;
//
//    public StudySession(Deck deck, User user, StudyAlgorithm studyAlgorithm) {
//        cards = studyAlgorithm.prepareCards(deck, user);
//    }
//
//    public FlashCard getNextCardAndRemove() {
//        if (this.cards.isEmpty()) {
//            return null;
//        }
//        currentCard = cards.getFirst();
//        //return cards.removeFirst();
//    }
//
//    public void ApplyRating(){
//        //TODO Lägg till RatingStrategy metod
//    }
//}
