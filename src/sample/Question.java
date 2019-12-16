package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Question {
    private String text;
    private int correctOption = 0;
    private int chosenOption = 0;
    private ArrayList<String> options;

    public Question() {
        this.options = new ArrayList<String>();
        for (int i = 0; i < 4; i++) options.add("");
        text = new String("");
    }

    /**
     * parses the given json-object to Question
     *
     * @param jsonQuestion the json-object to be parsed.
     * @return
     */
    public static Question jsonToQuestion(Map<String, Object> jsonQuestion) {
        Question result = new Question();
        result.setOptions((ArrayList<String>) jsonQuestion.get("options"));
        result.setText((String) jsonQuestion.get("text"));
        result.setChosenOption((Integer) jsonQuestion.get("chosen-option"));
        result.setCorrectOption((Integer) jsonQuestion.get("correct-option"));
        return result;
    }

    /**
     * parses the object to a json-object
     *
     * @return the json-object in the form of Map<String, Object>
     */
    public Map<String, Object> toJsonObject() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("text", text);
        result.put("correct-option", correctOption);
        result.put("chosen-option", chosenOption);
        result.put("options", options);

        return result;
    }

    /*
    returns true of the question is answered correctly, otherwise false.
     */
    public boolean isAnsweredCorrectly() {
        return correctOption == chosenOption;
    }

    public void setOptionByNumber(String option, int index) {
        options.set(index, option);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public int getChosenOption() {
        return chosenOption;
    }

    public void setChosenOption(int chosenOption) {
        this.chosenOption = chosenOption;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int compareTo(Question other) {
        if (this.text.equals(other.text) &&
                this.options.equals(other.options) &&
                this.chosenOption == other.chosenOption &&
                this.correctOption == other.correctOption) {
            return 0;
        } else {
            return -1;
        }
    }

    public void setOption1(String s) { options.set(0, s); }
    public void setOption2(String s) { options.set(1, s); }
    public void setOption3(String s) { options.set(2, s); }
    public void setOption4(String s) { options.set(3, s); }
}
