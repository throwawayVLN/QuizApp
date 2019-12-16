package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiz {
    public String name;
    public ArrayList<Question> questions;

    public Quiz() { this.questions = new ArrayList<Question>(); }

    public Quiz(String name) {
        this.name = name;
        this.questions = new ArrayList<Question>();
    }

    public Quiz(String name, ArrayList<Question> question) {
        this.name = name;
        this.questions = question;
    }

    /**
     * parses a json-object to Quiz
     *
     * @param jsonQuiz the jsonObject to parse
     * @return
     */
    public static Quiz jsonToQuiz(Map<String, Object> jsonQuiz) {
        Quiz result = new Quiz();
        result.setName((String) jsonQuiz.get("name"));
        ArrayList<Map<String, Object>> questionsJson = (ArrayList<Map<String, Object>>) jsonQuiz.get("questions");
        result.questions.clear();
        for (Map<String, Object> q: questionsJson) {
            result.questions.add(Question.jsonToQuestion(q));
        }
        return result;
    }

    /**
     * parses the current object to a json-object
     *
     * @return returns a json-objet in the form of Map<String, Object>
     */
    public Map<String, Object> toJsonObject() {
        Map<String, Object> result = new HashMap<String, Object>();
        ArrayList<Map<String, Object>> jsonQuestions = new ArrayList<Map<String, Object>>();
        for (Question q: questions) {
            jsonQuestions.add(q.toJsonObject());
        }
        result.put("questions", jsonQuestions);
        result.put("name", name);
        return result;
    }

    /**
     * calculates the number of questions that are answered correctly.
     *
     * @return the number of questions that are answered correctly.
     */
    public int getScore() {
        int result = 0;
        for (Question q: questions) {
            if (q.isAnsweredCorrectly())
                result++;
        }
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }
}

