package com.lglab.ivan.lgxeducontroller.games.millionaire.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lglab.ivan.lgxeducontroller.R;
import com.lglab.ivan.lgxeducontroller.activities.manager.CreatePOIActivity;
import com.lglab.ivan.lgxeducontroller.activities.navigate.POIController;
import com.lglab.ivan.lgxeducontroller.games.GameManager;
import com.lglab.ivan.lgxeducontroller.games.ISaveData;
import com.lglab.ivan.lgxeducontroller.games.millionaire.Millionaire;
import com.lglab.ivan.lgxeducontroller.games.millionaire.MillionaireQuestion;
import com.lglab.ivan.lgxeducontroller.legacy.beans.POI;
import com.lglab.ivan.lgxeducontroller.legacy.data.POIsProvider;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.multiplemodel.ItemEntity;
import github.chenupt.multiplemodel.ItemEntityUtil;

import static android.app.Activity.RESULT_OK;

public class MillionaireQuestionEditFragment extends Fragment implements ISaveData {

    private int questionNumber;
    private Millionaire millionaire;
    private View view;
    private MillionaireQuestion question;

    private List<POI> poiList;
    private ArrayAdapter<POI> poiStringList;
    private EditText questionEditText;
    private RadioGroup correctAnswerRadioButton;
    private EditText[] textAnswers;

    private AutoCompleteTextView textQuestionPOI;
    private AutoCompleteTextView answersPOITextEdit;

    private EditText additionalInformation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemEntity<Integer> itemEntity = ItemEntityUtil.getModelData(this);
        questionNumber = itemEntity.getContent();
        millionaire = (Millionaire) GameManager.getInstance().getGame();
        question = (MillionaireQuestion) millionaire.getQuestions().get(questionNumber);

        poiStringList = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item);
        getPOIStringsFromDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_create_millionaire_question, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionEditText = view.findViewById(R.id.questionTextEdit);
        correctAnswerRadioButton = view.findViewById(R.id.radio_group_correct_answer);
        textQuestionPOI = view.findViewById(R.id.questionPOITextEdit);
        questionPOIText();
        POIButton(R.id.addQuestionPOIButton, 0); //0

        textAnswers = new EditText[MillionaireQuestion.MAX_ANSWERS];
        answersPOITextEdit= view.findViewById(R.id.answerPOITextEdit);
        answerPOIText(answersPOITextEdit);
        POIButton(R.id.addAnswerPOIButton, 1);

        for (int i = 0; i < MillionaireQuestion.MAX_ANSWERS; i++) {
            textAnswers[i] = view.findViewById(R.id.millionaire_answer1TextEdit + i);
        }

        additionalInformation = view.findViewById(R.id.informationTextEdit);

        questionEditText.setText(question.getQuestion());

        if (question.correctAnswer > 0)
            ((RadioButton) correctAnswerRadioButton.getChildAt(question.correctAnswer - 1)).setChecked(true);

        for (int i = 0; i < MillionaireQuestion.MAX_ANSWERS; i++) {
            if (question.answers[i] != null) {
                textAnswers[i].setText(question.answers[i]);
            }
        }

        if (question.initialPOI != null)
            textQuestionPOI.setText(question.initialPOI.getName());


        if (question.poi != null) {
            answersPOITextEdit.setText(question.poi.getName());
        }


        if (question.information != null)
            additionalInformation.setText(question.information);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveData();
    }

    private void getPOIStringsFromDatabase() {
        poiList = new ArrayList<>();
        Cursor poiCursor = POIsProvider.getAllPOIs();

        while (poiCursor.moveToNext()) {
            long poiID = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("_id"));
            String name = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Name"));
            String visitedPlace = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Visited_Place"));
            double longitude = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Longitude"));
            double altitude = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Altitude"));
            double latitude = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Latitude"));
            double heading = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Longitude"));
            double tilt = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Tilt"));
            double range = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Range"));
            String altitudeMode = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Altitude_Mode"));
            boolean hidden = poiCursor.getInt(poiCursor.getColumnIndexOrThrow("Hide")) == 1;
            int categoryID = poiCursor.getInt(poiCursor.getColumnIndexOrThrow("Category"));

            try {
                POI newPOI = new POI(poiID, name, visitedPlace, longitude, latitude, altitude, heading, tilt, range, altitudeMode, hidden, categoryID);
                poiList.add(newPOI);
            } catch (Exception e) {
                Log.e("BRUH", e.toString());
            }

        }
        poiCursor.close();
        for (POI poi : poiList) {
            poiStringList.add(poi);
        }
    }

    private void POIButton(int id, int resultCode) {
        view.findViewById(id).setOnClickListener(view -> {
            Intent createPoiIntent = new Intent(getContext(), CreatePOIActivity.class);
            createPoiIntent.putExtra("POI_BUTTON", resultCode);
            if(resultCode == 0 && question != null && question.initialPOI != null)
                createPoiIntent.putExtra("POI", question.initialPOI);
            else if(resultCode == 1 && question != null && question.poi != null)
                createPoiIntent.putExtra("POI", question.poi);
            startActivityForResult(createPoiIntent, 0);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            POI returnedPOI = data.getParcelableExtra("POI");
            int button = data.getIntExtra("button", -1);
            String namePOI = returnedPOI.getName();
            poiList.add(returnedPOI);
            poiStringList.add(returnedPOI);

            if (button == 0) {
                question.initialPOI = returnedPOI;
                textQuestionPOI.setText(namePOI);
            } else if (button == 1) {
                question.poi = returnedPOI;
                answersPOITextEdit.setText(namePOI);
            } else if (button == 6) {
                //Code for intent from Manager (get the quiz Trivia and if editing only one question or the whole quiz Boolean)

            }
        }
    }

    private void answerPOIText(AutoCompleteTextView textPOI) {
        textPOI.setAdapter(poiStringList);

        textPOI.setOnItemClickListener((parent, view, position, id) -> {
            POI poi = poiStringList.getItem(position);
            question.poi = poi;
        });
    }

    private String getTextFromEditText(EditText editText) {
        String toReturn = editText.getText().toString();
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn;
    }

    private void questionPOIText() {
        AutoCompleteTextView textPOI = view.findViewById(R.id.questionPOITextEdit);
        textPOI.setAdapter(poiStringList);

        textPOI.setOnItemClickListener((parent, view, position, id) -> question.initialPOI = poiStringList.getItem(position));
    }

    @Override
    public void saveData() {
        String questionS = getTextFromEditText(questionEditText);
        if (questionS == null || questionS.isEmpty()) {
            //Toast.makeText(getContext(), "A text must be filled in the Question textbox", Toast.LENGTH_SHORT).show();
            return;
        }

        int idSelectedRadioButton = correctAnswerRadioButton.getCheckedRadioButtonId();
        if (idSelectedRadioButton == -1) {
            //Toast.makeText(getContext(), getString(R.string.correct_answer), Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton pressedRadioButton = view.findViewById(idSelectedRadioButton);
        int correctAnswer = Integer.parseInt(pressedRadioButton.getText().toString());

        String[] answers = new String[MillionaireQuestion.MAX_ANSWERS];

        for (int i = 0; i < MillionaireQuestion.MAX_ANSWERS; i++) {
            String text = getTextFromEditText(textAnswers[i]);
            if (text == null || text.isEmpty()) {
                //Toast.makeText(getContext(), "Answer " + (i + 1) + " POI", Toast.LENGTH_SHORT).show();
                return;
            }
            answers[i] = text;
        }

        if (this.question.poi == null) {
            //Toast.makeText(getContext(), "POI of the answer " + (i + 1) + " is not selected, please select one", Toast.LENGTH_SHORT).show();
            return;
        }

        if (question.initialPOI == null) {
            question.initialPOI = POIController.EARTH_POI;
        }


        question.setQuestion(questionS);
        question.answers = answers;
        question.correctAnswer = correctAnswer;
        question.information = additionalInformation.getText().toString();
    }
}
