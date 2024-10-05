package com.example.demo.dao;

import com.example.demo.Constants;
import static com.example.demo.Helpers.objectMapper;
import com.example.demo.model.Questions.QuestionsResponse;
import com.example.demo.model.Quiz.Quiz;
import com.example.demo.model.Quiz.QuizRequestBody;
import com.example.demo.model.Quiz.QuizResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class QuizRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public Optional<Quiz> findQuizByID(int id) {
        String sql = String.format("select * from %s where id=?", Constants.QUIZZES_TABLE_NAME);
        List<Quiz> quizzes = jdbcTemplate.query(sql, new QuizRowMapper(), id);

        return quizzes.stream().findFirst();
    }

    public List<QuestionsResponse> getAllQuestionsFromQuiz(int quizID) throws Exception {
        String sql = String.format("select qq.quizID, qq.questionID,\n"
                + "q.question,q.`options`,q.correct_option_number,q.technology \n"
                + "from %s qq \n"
                + "join %s q on qq.questionID =q.id where qq.quizID=?", Constants.QUIZ_QUESTIONS_TABLE_NAME, Constants.QUESTIONS_TABLE_NAME);
        List< Map<String, Object>> records = jdbcTemplate.queryForList(sql, quizID);
        List<QuestionsResponse> questionsResponses = new ArrayList<>();

        for (Map<String, Object> questionResponseMap : records) {

            int questionID = ((BigInteger) questionResponseMap.get("questionID")).intValue();
            String question = (String) questionResponseMap.get("question");
            String optionsJSON = (String) questionResponseMap.get("options");
            List<String> options = objectMapper.readValue(optionsJSON, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            int correctNumber = (int) questionResponseMap.get("correct_option_number");
            String technology = (String) questionResponseMap.get("technology");

            QuestionsResponse qr = new QuestionsResponse(questionID, question, options, correctNumber, technology);

            questionsResponses.add(qr);

        }

        return questionsResponses;

    }

    @Transactional
    public QuizResponse addQuiz(QuizRequestBody qr) throws Exception {

        String technology = qr.getTechnology();
        String name = qr.getQuizName();
        List<QuestionsResponse> questionsResponseList = qr.getQuestions();

        if (technology.isEmpty() || name.isEmpty()) {
            throw new Exception("fields are empty");
        }

        String sql = String.format("insert into %s (technology,name) values (?,?)", Constants.QUIZZES_TABLE_NAME);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, technology);
            ps.setString(2, name);

            return ps;
        }, keyHolder);

        System.out.println("rowsAffected: " + rowsAffected);

        int key = keyHolder.getKey().intValue();

        QuizResponse quizResponse = new QuizResponse(key, name, technology, questionsResponseList);

        String addQuestionsSql = String.format("insert into %s (quizID,questionID) values (?,?)", Constants.QUIZ_QUESTIONS_TABLE_NAME);

        int[] rowsAffected2 = jdbcTemplate.batchUpdate(addQuestionsSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, key);
                ps.setInt(2, questionsResponseList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return questionsResponseList.size();
            }
        });

        return quizResponse;

//        jdbcTemplate.update(sql,technology,name);
    }

    public Map<String, Map<Integer, QuizResponse>> getQuizzesForUser() throws JsonProcessingException {
        Map<String, Map<Integer, QuizResponse>> userquizzes = new HashMap<>();
        String sql = String.format("SELECT  q.id as quiz_id,q.technology,q.name\n"
                + ",q2.id as question_id,q2.question,q2.`options`    \n"
                + "from %s q join %s qq \n"
                + "on q.id = qq.quizID \n"
                + "join %s q2 on qq.questionID =q2.id ;", Constants.QUIZZES_TABLE_NAME, Constants.QUIZ_QUESTIONS_TABLE_NAME, Constants.QUESTIONS_TABLE_NAME);

//        List<Object> quizzes = jdbcTemplate.query(sql, new RowMapper<Object>() {
//            @Override
//            public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Object obj = new Object();
//                obj.=rs.getInt("quiz_id");
//            }
//        });
        List<Map<String, Object>> quizzes = jdbcTemplate.queryForList(sql);

        System.out.println("quizzes: " + quizzes.toString());

        // iterating throw list of rows {column_name: column_value}
        for (Map<String, Object> quiz : quizzes) {
            int quiz_id = ((BigInteger) quiz.get("quiz_id")).intValue();
            String technology = (String) quiz.get("technology");
            String name = (String) quiz.get("name");
            String question = (String) quiz.get("question");
            String optionsJSON = (String) quiz.get("options");

            List<String> options = objectMapper.readValue(optionsJSON, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));

            int question_id = ((BigInteger) quiz.get("quiz_id")).intValue();

            QuestionsResponse questionsResponse = new QuestionsResponse(question_id, question, options, 0, technology);
//            List<QuestionsResponse>

            Map<Integer, QuizResponse> quiz_questions_map = new HashMap<>();
            // if map referencing technology to another map (referencing quizid to its own)  is  found then no need to
            // create a new map and using map of current technology
            if (userquizzes.containsKey(technology)) {
                quiz_questions_map = userquizzes.get(technology);
            }

            // if quiz is already there means new question is found for this so adding new question in this quiz
            if (quiz_questions_map.containsKey(quiz_id)) {

                QuizResponse quizResponse = quiz_questions_map.get(quiz_id);
                List<QuestionsResponse> questionsResponseList = quizResponse.getQuestions();
                questionsResponseList.add(questionsResponse);
                quizResponse.setQuestions(questionsResponseList);

                quiz_questions_map.put(quiz_id, quizResponse);

            } // if quiz is not there means new quiz is found for this so adding new quiz and
            // then creating questionlist for quiz and adding current question in this questionlist
            else {
                List<QuestionsResponse> questionsResponseList = new ArrayList<>();
                questionsResponseList.add(questionsResponse);
                QuizResponse quizResponse = new QuizResponse(quiz_id, name, technology, questionsResponseList);

                quiz_questions_map.put(quiz_id, quizResponse);
            }

            userquizzes.put(technology, quiz_questions_map);
        }

        return userquizzes;
    }
}
