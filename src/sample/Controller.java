package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    public ListView<String>quizList;
    public ListView<String> questionList;
    public Label questionNumberLabel;
    public TextArea questionText;
    public RadioButton option1;
    public RadioButton option3;
    public RadioButton option2;
    public RadioButton option4;
    public Button resetButton;
    public Button saveAnswersButton;
    public Button calculateScoreButton;
    public TextField scoreText;
    public ListView<String> quizList1;
    public Button addQuizButton;
    public ListView<String> questionListTab2;
    public Button addQuestion;
    public Label questionNumberLabel1;
    public TextArea questionTextTab2;
    public RadioButton option1Tab2;
    public TextField option1Text;
    public RadioButton option3Tab2;
    public TextField option3Text;
    public RadioButton option2Tab2;
    public TextField option2Text;
    public RadioButton option4Tab2;
    public TextField option4Text;
    public Button saveQuestionButton;
    public Button resetButton1;
    public Button loadQuizzesButtonTab2;
    public Button loadQuizzesButton;
    public TextField quizNameText;
    public Button deleteQuizButton;
    public Button deleteQuestionButton;
    public ListView questionsAnsweredIncorrectly;

    File file = new File("quizzes.txt");

    private Map<String, Object> jsonObject = new HashMap<String, Object>();;

    /*
    updates the file based on the jsonObject
     */
    private void updateFile() throws IOException {
        JsonWriter jw = new JsonWriter(file, jsonObject);
    }

    /*
    updates the jsonObject based on the content of the file.
    If the file is empty, the file will be '{ "quizzes": [] }' which is
    the initial value of jsonObject
     */
    private void updateJsonObject() throws IOException {
        if (!jsonObject.containsKey("quizzes") && file.length() == 0) {
            jsonObject = new HashMap<String, Object>();
            jsonObject.put("quizzes", new ArrayList<Map<String, Object>>());
            updateFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        jsonObject = JsonReader.parse(br);
        br.close();

        // TODO: remove all the questions that don't have aren't assigned a text or correctOption
    }

    /**
     * searches the quizName in the json-object, returns null if it doesn't exist.
     *
     * @param quizName
     * @return a quiz whose name is same as quizName, returns null if no quiz has that name
     * @throws IOException
     */
    private Quiz getQuiz(String quizName) throws IOException {
        updateJsonObject();
        Object objectQuizzes = jsonObject.get("quizzes");
        ArrayList<Map<String, Object>> quizzes = (ArrayList<Map<String, Object>>) objectQuizzes;
        for (Map<String, Object> quiz: quizzes) {
            String currentName = (String) quiz.get("name");
            if (currentName.equals(quizName)) {
                return Quiz.jsonToQuiz(quiz);
            }
        }
        return null;
    }

    /**
     * changes the quiz whose name is quizName the given quiz
     *
     * @param quizName the name of the quiz to be changed.
     * @param quiz the new value to be changed to.
     * @throws IOException
     */
    private void setQuiz(String quizName, Quiz quiz) throws IOException {
        updateJsonObject();
        Object objectQuizzes = jsonObject.get("quizzes");
        ArrayList<Map<String, Object>> quizzes = (ArrayList<Map<String, Object>>) objectQuizzes;
        for (int i = 0; i < quizzes.size(); i++) {
            Map<String, Object> currentQuiz = quizzes.get(i);
            String currentQuizName = (String) currentQuiz.get("name");
            if (currentQuizName.equals(quiz.name)) {
                quizzes.set(i, quiz.toJsonObject());
                break;
            }
        }
        updateFile();
    }

    private void setQuestion(String quizName, int questionIndex, Question question) throws IOException {
        Quiz currentQuiz = getQuiz(quizName);
        currentQuiz.questions.set(questionIndex, question);
        setQuiz(quizName, currentQuiz);
        updateFile();
    }

    public void addQuiz(ActionEvent actionEvent) throws IOException {
        updateJsonObject();
        Object objectQuizzes = jsonObject.get("quizzes");
        ArrayList<Object> quizzes = (ArrayList<Object>) objectQuizzes;
        quizzes.add((new Quiz(quizNameText.getText())).toJsonObject());
        jsonObject.put("quizzes", quizzes);
        updateFile();
        showQuizList();

        questionListTab2.getItems().clear();

        addQuizButton.setDisable(true);
        quizNameText.setText("");
        deleteQuizButton.setDisable(true);
    }

    public void deleteQuiz(ActionEvent actionEvent) throws IOException {
        updateJsonObject();
        Object objectQuizzes = jsonObject.get("quizzes");
        ArrayList<Object> quizzes = (ArrayList<Object>) objectQuizzes;
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        Quiz quiz = getQuiz(quizName);
        quizzes.remove(quiz.toJsonObject());

        jsonObject.put("quizzes", quizzes);
        updateFile();
        showQuizList();

        deleteQuizButton.setDisable(true);
        addQuestion.setDisable(true);
    }

    public void loadQuizzesTab2(ActionEvent actionEvent) throws IOException {
        showQuizList();
    }

    private void showQuizList() throws IOException {
        updateJsonObject();
        quizList1.getItems().clear();
        ArrayList<Map<String, Object>> quizzes = (ArrayList<Map<String, Object>>) jsonObject.get("quizzes");
        for (Map<String, Object> quiz: quizzes) {
            quizList1.getItems().add((String) quiz.get("name"));
        }
    }

    public void loadQuizes(ActionEvent actionEvent) throws IOException {
        updateJsonObject();
        int size = ((ArrayList<Map<String, Object>>) jsonObject.get("quizzes")).size();
        quizList.getItems().clear();
        for (int i = 0; i < size; i++) {
            quizList.getItems().add((String) ((ArrayList<Map<String, Object>>) jsonObject.get("quizzes")).get(i).get("name"));
        }
    }



    public void showQuestionListTab2() throws IOException {
        updateJsonObject();
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        questionListTab2.getItems().clear();
        Quiz currentQuiz = getQuiz(quizName);
        for (int i = 0; i < currentQuiz.questions.size(); i++) {
            questionListTab2.getItems().add("Question " + Integer.toString(i + 1));
        }
    }

    public void addQuestion(ActionEvent actionEvent) throws IOException {
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        Quiz currentQuiz = getQuiz(quizName);
        currentQuiz.questions.add(new Question());
        setQuiz(quizName, currentQuiz);
        updateFile();
        showQuestionListTab2();
    }

    public void deleteQuestion(ActionEvent actionEvent) throws IOException {
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        Quiz currentQuiz = getQuiz(quizName);
        int questionIndex = questionListTab2.getSelectionModel().getSelectedIndex();
        Question currentQuestion = getQuestion(questionIndex, quizName);
        System.out.println(currentQuiz.toJsonObject());
        currentQuiz.questions.remove(questionIndex);
        setQuiz(quizName, currentQuiz);
        updateFile();

        clearQuestion();

        showQuestionListTab2();
        deleteQuestionButton.setDisable(true);
    }

    private Question getQuestion(int questionIndex, String quizName) throws IOException {
        Quiz currentQuiz = getQuiz(quizName);
        return currentQuiz.questions.get(questionIndex);
    }

    public void showQuiz(MouseEvent mouseEvent) throws IOException {
        updateJsonObject();
        String quizName = quizList.getSelectionModel().getSelectedItem();

        Quiz quiz = getQuiz(quizName);

        questionList.getItems().clear();
        for (int i = 0; i < quiz.questions.size(); i++) {
            questionList.getItems().add("Question " + Integer.toString(i + 1));
        }
        resetButton.setDisable(false);
        for (Question q: quiz.questions) {
            if (q.getChosenOption() != 0) {
                calculateScoreButton.setDisable(false);
                break;
            }
        }
    }

    public void showQuestion(MouseEvent mouseEvent) throws IOException {
        updateJsonObject();
        String quizName = quizList.getSelectionModel().getSelectedItem();
        int questionIndex = questionList.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);

        questionText.setText(question.getText());
        option1.setText(question.getOptions().get(0));
        option2.setText(question.getOptions().get(1));
        option3.setText(question.getOptions().get(2));
        option4.setText(question.getOptions().get(3));

        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);
        option4.setSelected(false);

        switch (question.getChosenOption()) {
            case 1:
                option1.setSelected(true);
                break;
            case 2:
                option2.setSelected(true);
                break;
            case 3:
                option3.setSelected(true);
                break;
            case 4:
                option4.setSelected(true);
                break;
        }
    }

    public void chooseOption1(ActionEvent actionEvent) throws IOException {
        option1.setSelected(true);
        option2.setSelected(false);
        option3.setSelected(false);
        option4.setSelected(false);

        String quizName = quizList.getSelectionModel().getSelectedItem();
        int questionIndex = questionList.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);
        question.setChosenOption(1);
        setQuestion(quizName, questionIndex, question);

        calculateScoreButton.setDisable(false);
        scoreText.clear();
        questionsAnsweredIncorrectly.getItems().clear();
    }

    public void chooseOption2(ActionEvent actionEvent) throws IOException {
        option1.setSelected(false);
        option2.setSelected(true);
        option3.setSelected(false);
        option4.setSelected(false);

        String quizName = quizList.getSelectionModel().getSelectedItem();
        int questionIndex = questionList.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);
        question.setChosenOption(2);
        setQuestion(quizName, questionIndex, question);

        calculateScoreButton.setDisable(false);
        scoreText.clear();
        questionsAnsweredIncorrectly.getItems().clear();
    }

    public void chooseOption3(ActionEvent actionEvent) throws IOException {
        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(true);
        option4.setSelected(false);

        String quizName = quizList.getSelectionModel().getSelectedItem();
        int questionIndex = questionList.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);
        question.setChosenOption(3);
        setQuestion(quizName, questionIndex, question);

        calculateScoreButton.setDisable(false);
        scoreText.clear();
        questionsAnsweredIncorrectly.getItems().clear();
    }

    public void chooseOption4(ActionEvent actionEvent) throws IOException {
        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);
        option4.setSelected(true);

        String quizName = quizList.getSelectionModel().getSelectedItem();
        int questionIndex = questionList.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);
        question.setChosenOption(4);
        setQuestion(quizName, questionIndex, question);

        calculateScoreButton.setDisable(false);
        scoreText.clear();
        questionsAnsweredIncorrectly.getItems().clear();
    }

    public void showQuizTab2(MouseEvent mouseEvent) throws IOException {
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        Quiz quiz = getQuiz(quizName);

        questionListTab2.getItems().clear();
        for (int i = 0; i < quiz.questions.size(); i++) {
            questionListTab2.getItems().add("Question " + Integer.toString(i + 1));
        }

        clearQuestion();
        addQuestion.setDisable(false);
        deleteQuizButton.setDisable(false);
    }

    public void clearQuestion() {
        questionTextTab2.clear();
        option1Text.clear();
        option2Text.clear();
        option3Text.clear();
        option4Text.clear();
        option1Tab2.setSelected(false);
        option2Tab2.setSelected(false);
        option3Tab2.setSelected(false);
        option4Tab2.setSelected(false);

    }

    public void showQuestionTab2(MouseEvent mouseEvent) throws IOException {
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        int questionIndex = questionListTab2.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);

        questionTextTab2.setText(question.getText());
        option1Text.setText(question.getOptions().get(0));
        option2Text.setText(question.getOptions().get(1));
        option3Text.setText(question.getOptions().get(2));
        option4Text.setText(question.getOptions().get(3));

        option1Tab2.setSelected(false);
        option2Tab2.setSelected(false);
        option3Tab2.setSelected(false);
        option4Tab2.setSelected(false);

        switch (question.getCorrectOption()) {
            case 1:
                option1Tab2.setSelected(true);
                break;
            case 2:
                option2Tab2.setSelected(true);
                break;
            case 3:
                option3Tab2.setSelected(true);
                break;
            case 4:
                option4Tab2.setSelected(true);
                break;
        }
        deleteQuestionButton.setDisable(false);
    }

    public void enablrSaveQuestion() {
        if (!questionTextTab2.getText().isEmpty()
                && (option4Tab2.isSelected() || option3Tab2.isSelected() || option2Tab2.isSelected() || option1Tab2.isSelected())) {
            saveQuestionButton.setDisable(false);
        }
    }

    public void chooseOption1Tab2(ActionEvent actionEvent) {
        option1Tab2.setSelected(true);
        option2Tab2.setSelected(false);
        option3Tab2.setSelected(false);
        option4Tab2.setSelected(false);
        enablrSaveQuestion();
    }

    public void setOption1(ActionEvent actionEvent) {
    }

    public void chooseOption3Tab2(ActionEvent actionEvent) {
        option1Tab2.setSelected(false);
        option2Tab2.setSelected(false);
        option3Tab2.setSelected(true);
        option4Tab2.setSelected(false);
        enablrSaveQuestion();
    }

    public void setOption3(ActionEvent actionEvent) {
    }

    public void chooseOption2Tab2(ActionEvent actionEvent) {
        option1Tab2.setSelected(false);
        option2Tab2.setSelected(true);
        option3Tab2.setSelected(false);
        option4Tab2.setSelected(false);
        enablrSaveQuestion();
    }

    public void setOption2(ActionEvent actionEvent) {
    }

    public void chooseOption4Tab2(ActionEvent actionEvent) {
        option1Tab2.setSelected(false);
        option2Tab2.setSelected(false);
        option3Tab2.setSelected(false);
        option4Tab2.setSelected(true);
        enablrSaveQuestion();
    }

    public void setOption4(ActionEvent actionEvent) throws IOException {
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        int questionIndex = quizList1.getSelectionModel().getSelectedIndex();
        Question question = getQuestion(questionIndex, quizName);

        question.setOption4(option4.getText());
        setQuestion(quizName, questionIndex, question);
    }

    public void saveQuestion(ActionEvent actionEvent) throws IOException {
        // get the current question
        String quizName = quizList1.getSelectionModel().getSelectedItem();
        int questionIndex = questionListTab2.getSelectionModel().getSelectedIndex();
        System.out.println(questionIndex);
        Question question = getQuestion(questionIndex, quizName);



        // set options
        question.setOption1(option1Text.getText());
        question.setOption2(option2Text.getText());
        question.setOption3(option3Text.getText());
        question.setOption4(option4Text.getText());

        // set text
        question.setText(questionTextTab2.getText());

        // set the correct option
        if (option1Tab2.isSelected()) {
            question.setCorrectOption(1);
        } else if (option2Tab2.isSelected()) {
            question.setCorrectOption(2);
        } else if (option3Tab2.isSelected()) {
            question.setCorrectOption(3);
        } else {
            question.setCorrectOption(4);
        }

        // save the question
        setQuestion(quizName, questionIndex, question);

        questionTextTab2.clear();
        option1Tab2.setSelected(false);
        option2Tab2.setSelected(false);
        option3Tab2.setSelected(false);
        option4Tab2.setSelected(false);
        option1Text.clear();
        option2Text.clear();
        option3Text.clear();
        option4Text.clear();
        saveQuestionButton.setDisable(true);
    }

    public void enableAddQuizButton(KeyEvent keyEvent) throws IOException {
        if (quizNameText.getText().length() > 0) {
            if (getQuiz(quizNameText.getText()) == null)
                addQuizButton.setDisable(false);
        } else {
            addQuizButton.setDisable(true);
        }
    }

    public void setQuestionText(KeyEvent keyEvent) {
        enablrSaveQuestion();
    }

    public void resetAnswers(ActionEvent actionEvent) throws IOException {
        String quizName = quizList.getSelectionModel().getSelectedItem();
        Quiz quiz = getQuiz(quizName);
        for (Question question: quiz.questions) {
            question.setChosenOption(0);
        }
        setQuiz(quizName, quiz);

        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);
        option4.setSelected(false);

        scoreText.clear();
        questionsAnsweredIncorrectly.getItems().clear();
    }

    public void calculateScore(ActionEvent actionEvent) throws IOException {
        String quizName = quizList.getSelectionModel().getSelectedItem();
        Quiz quiz = getQuiz(quizName);
        scoreText.setText(Integer.toString(quiz.getScore()));

        questionsAnsweredIncorrectly.getItems().clear();
        for (int i = 0; i < quiz.questions.size(); i++) {
            if (quiz.questions.get(i).getChosenOption() !=
                quiz.questions.get(i).getCorrectOption())
            questionsAnsweredIncorrectly.getItems().add("Question " + Integer.toString(i + 1) + ", " +
                                                        "Correct answer: " + Integer.toString(quiz.questions.get(i).getCorrectOption()) + ", " +
                                                        "Chosen answer: " + Integer.toString(quiz.questions.get(i).getChosenOption()));
        }
    }

    public void showQuestion_(MouseEvent mouseEvent) throws IOException {
        showQuestion(mouseEvent);
    }
}
