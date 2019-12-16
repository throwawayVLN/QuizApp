package sample;

import com.oracle.javafx.jmx.json.JSONReader;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {


    @Test
    void setOption1() {
        Question q = new Question();
        q.setOption1("option1");

        assertEquals(q.getOptions().get(0), "option1");
    }

    @Test
    void setOption2() {
        Question q = new Question();
        q.setOption2("option2");

        assertEquals(q.getOptions().get(1), "option2");
    }

    @Test
    void setOption3() {
        Question q = new Question();
        q.setOption3("option3");

        assertEquals(q.getOptions().get(2), "option3");
    }

    @Test
    void setOption4() {
        Question q = new Question();
        q.setOption4("option4");

        assertEquals(q.getOptions().get(3), "option4");
    }

    @Test
    void jsonToQuestion() throws IOException {
        final String jsonString = "{\n" +
                "\"chosen-option\": 0, \n" +
                "\"options\": [\"option1\", \"option2\", \"option3\", \"option4\"], \n" +
                "\"text\": \"question text\", \n" +
                "\"correct-option\": 0\n" +
                "}\n";

        // actual
        Question actual = Question.jsonToQuestion(JsonReader.parse(jsonString));

        // expected
        Question expected = new Question();
        expected.setText("question text");
        expected.setOption1("option1");
        expected.setOption2("option2");
        expected.setOption3("option3");
        expected.setOption4("option4");

        assertEquals(expected.compareTo(actual), 0);
    }

    @Test
    void toJsonObject() throws IOException {
        final String jsonString = "{\n" +
                "\"chosen-option\": 0, \n" +
                "\"options\": [\"option1\", \"option2\", \"option3\", \"option4\"], \n" +
                "\"text\": \"question text\", \n" +
                "\"correct-option\": 0\n" +
                "}\n";

        Question q = new Question();
        q.setText("question text");
        q.setOption1("option1");
        q.setOption2("option2");
        q.setOption3("option3");
        q.setOption4("option4");

        assertEquals(q.toJsonObject(), JsonReader.parse(jsonString));
    }

    @Test
    void isAnsweredCorrectly() {
        Question q = new Question();

        // when correct-answer and chosen-answer are the same
        q.setChosenOption(0);
        q.setCorrectOption(0);
        assertTrue(q.isAnsweredCorrectly());

        // when correct answer and chosen-answer are different
        q.setCorrectOption(1);
        q.setChosenOption(2);
        assertFalse(q.isAnsweredCorrectly());
    }
}