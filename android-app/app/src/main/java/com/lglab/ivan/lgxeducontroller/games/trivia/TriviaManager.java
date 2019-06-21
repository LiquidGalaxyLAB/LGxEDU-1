package com.lglab.ivan.lgxeducontroller.games.trivia;

import com.lglab.ivan.lgxeducontroller.games.trivia.activities.EditGameActivity;
import com.lglab.ivan.lgxeducontroller.games.Game;
import com.lglab.ivan.lgxeducontroller.games.GameManager;
import com.lglab.ivan.lgxeducontroller.games.trivia.activities.TriviaActivity;
import com.lglab.ivan.lgxeducontroller.games.trivia.fragments.TriviaQuestionEditFragment;

import java.util.ArrayList;
import java.util.List;

public class TriviaManager extends GameManager {

    private static final Class<?> GAME_ACTIVITY = TriviaActivity.class;
    private static final Class<?> TRIVIA_EDIT_FRAGMENT = TriviaQuestionEditFragment.class;

    private List<Integer> selectedAnswers;

    public TriviaManager(Game game) {
        super(game);
        selectedAnswers = new ArrayList<>();
        for(int i = 0; i < getGame().getQuestions().size(); i++)
            selectedAnswers.add(0);
    }

    @Override
    public Class<?> getGameActivity() {
        return GAME_ACTIVITY;
    }

    @Override
    public Class<?> getGameEditFragment() {
        return TRIVIA_EDIT_FRAGMENT;
    }

    public boolean hasAnsweredAllQuestions() {
        for (int answer : selectedAnswers) {
            if (answer == 0) return false;
        }
        return true;
    }

    public boolean hasAnsweredQuestion(int i) {
        return selectedAnswers.get(i) != 0;
    }

    public void answerQuestion(int i, int selectedAnswer) {
        selectedAnswers.set(i, selectedAnswer);
    }

    public int getAnswerIdOfQuestion(int i) {
        return selectedAnswers.get(i);
    }

    public int correctAnsweredQuestionsCount() {
        int total = 0;
        for (int i = 0; i < getGame().getQuestions().size(); i++) {
            total += isCorrectAnswer(i) ? 1 : 0;
        }
        return total;
    }

    public boolean isCorrectAnswer(Integer index) {
        return selectedAnswers.get(index) == ((TriviaQuestion)getGame().getQuestions().get(index)).correctAnswer;
    }
}
