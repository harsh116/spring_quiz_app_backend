package com.example.demo.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.demo.Constants;
import com.example.demo.Helpers;
import com.example.demo.model.Questions.Questions;
import com.example.demo.model.Questions.QuestionsForm;
import com.example.demo.model.Questions.QuestionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class QuestionRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper objmapper;

    public Optional<Questions> findQuestionById(int id) throws SQLException {
        // String sql = String.format( "select * from %s where id=%d",Constants.QUESTIONS_TABLE_NAME,id);
        String sql = String.format("select * from %s where id=?", Constants.QUESTIONS_TABLE_NAME);
        List<Questions> ql = jdbcTemplate.query(sql, new QuestionRowMapper(), id);
        // Questions q = jdbcTemplate.queryForObject(sql, Questions.class);
        // return Optional.ofNullable(q);
        return ql.stream().findFirst();
    }

    public QuestionsResponse insertQuestion(QuestionsForm qf) throws Exception {

        System.out.println("Inserting questions");

        // System.out.println("questionForm: "+qf.toString());
        // int id = Integer.parseInt(qf.get)
        String question = qf.getQuestion();
        int correctOptionNumber = Integer.parseInt(qf.getCorrectOption());
        String technology = qf.getTechnology();
        List<String> options = new ArrayList<String>();

        if (qf.getOption1() != null && qf.getOption1().length() > 0) {
            options.add(qf.getOption1());
        }
        if (qf.getOption2() != null && qf.getOption2().length() > 0) {
            options.add(qf.getOption2());
        }

        if (qf.getOption3() != null && qf.getOption3().length() > 0) {
            options.add(qf.getOption3());
        }

        if (qf.getOption4() != null && qf.getOption4().length() > 0) {
            options.add(qf.getOption4());
        }

        String optionsJSON = objmapper.writeValueAsString(options);

        // String insertSQL= "insert into ? (?) values (?,?,?,?)";
        String insertSQL = String.format("insert into %s (question,options,correct_option_number,technology) values (?,?,?,?)", Constants.QUESTIONS_TABLE_NAME);
        // Object[] args= new Object[] {Constants.QUESTIONS_TABLE_NAME, "question,options,correct_option_number,technology", question,optionsJSON,correctOptionNumber,technology};
        Object[] args = new Object[]{question, optionsJSON, correctOptionNumber, technology};
        // int rowsAffected=jdbcTemplate.update(insertSQL, args);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSQL, new String[]{"id"});
            ps.setString(1, question);
            ps.setString(2, optionsJSON);
            ps.setInt(3, correctOptionNumber);
            ps.setString(4, technology);
            return ps;
        }, keyHolder);

        System.out.println("rowsAffected: " + rowsAffected);

        int key = keyHolder.getKey().intValue();

        QuestionsResponse qr = new QuestionsResponse(key, question, options, correctOptionNumber, technology);

        // System.out.println("json options: "+objmapper.writeValueAsString(options));
        // System.out.println(String.join(",", options));
        // QuestionsResponse qr= new QuestionsResponse();
        // qr
        // qr.setId();
        if (rowsAffected > 0) {
            return qr;
        } else {
            return new QuestionsResponse();
        }
    }

    public Optional<List<QuestionsResponse>> getAll() throws Exception {

        String sql = String.format("select * from %s where isDeleted=false", Constants.QUESTIONS_TABLE_NAME);
        List<Questions> questionList = jdbcTemplate.query(sql, new QuestionRowMapper());

        List<QuestionsResponse> qrList = new ArrayList<>();

        for (Questions q : questionList) {
            List<String> options = objmapper.readValue(q.getOptions(), objmapper.getTypeFactory().constructCollectionType(List.class, String.class));
            int id = q.getId();
            String questions = q.getQuestion();
            int correctOptionNumber = q.getCorrect_option_number();
            String technology = q.getTechnology();

            boolean isDeleted = q.isDeleted();

            if (!isDeleted) {

                QuestionsResponse qr = new QuestionsResponse(id, questions, options, correctOptionNumber, technology);
                // System.out.println();
                // System.out.println(qr.toString());
                qrList.add(qr);
            }

        }

        return Optional.ofNullable(qrList);

    }

    public Optional<QuestionsResponse> updateQuestionById(QuestionsForm qf, int id) throws Exception {
        System.out.println("inside updatebyid repo method");
        Optional<Questions> q = findQuestionById(id);

        System.out.println("update q: " + q.get().toString());

        String sql = String.format("update %s set question=?,options=?, correct_option_number=?, technology=? where id=?", Constants.QUESTIONS_TABLE_NAME);
        if (q.isPresent()) {
            Questions newQuestion = Helpers.QuestionFormsToQuestions(qf, id);
            // Object args= new Object[] {newQuestion.getQuestion(),newQuestion.getOptions(),newQuestion.getCorrect_option_number(),newQuestion.getTechnology(),id};
            // jdbcTemplate.update(sql, args);
            int rowsAffected = jdbcTemplate.update(sql, newQuestion.getQuestion(), newQuestion.getOptions(), newQuestion.getCorrect_option_number(), newQuestion.getTechnology(), id);

            QuestionsResponse qr = Helpers.QuestionFormToQuestionResponse(qf, id);
            if (rowsAffected == 0) {
                return null;
            }
            return Optional.ofNullable(qr);
        } else {

            return null;
        }
    }

    public Optional<QuestionsResponse> deleteQuestionById(int id) throws Exception {
        Optional<Questions> q = findQuestionById(id);
        if (q.isPresent()) {
//            String sql = String.format("delete from %s where id=?", Constants.QUESTIONS_TABLE_NAME);
//            String sql = String.format("update %s set isDeleted=true where id=?", Constants.QUESTIONS_TABLE_NAME);

            // creating a procedure that deletes the record is current record key is not foreign key of quiz_questions table
            // if it exist then updating isDeleted field to true to stop it from fetching 
            String sql = String.format("CREATE PROCEDURE if not EXISTS UpdateOrDeleteQuestion(IN primaryKeyValue INT)\n"
                    + "BEGIN\n"
                    + "    IF EXISTS (SELECT 1 FROM %s WHERE questionID = primaryKeyValue) THEN\n"
                    + "        -- Update the record in questions\n"
                    + "        UPDATE %s\n"
                    + "        SET isDeleted = TRUE\n"
                    + "        WHERE id = primaryKeyValue;\n"
                    + "    ELSE\n"
                    + "        -- Delete the record in questions\n"
                    + "        DELETE FROM %s\n"
                    + "        WHERE id = primaryKeyValue;\n"
                    + "    END IF;\n"
                    + "END;"
                    + "\n"
                    + "-- Now you can call the procedure with the primary key value\n"
                    + "CALL UpdateOrDeleteQuestion(%d);", Constants.QUIZ_QUESTIONS_TABLE_NAME, Constants.QUESTIONS_TABLE_NAME, Constants.QUESTIONS_TABLE_NAME, id);
//            int rowsAffected = jdbcTemplate.update(sql,id);
            jdbcTemplate.execute(sql);

//            if (rowsAffected == 0) {
//                return null;
//            }
            QuestionsResponse qr = Helpers.QuestionsToQuestionsResponse(q.get());
            return Optional.ofNullable(qr);

        } else {
            return null;
        }
    }
}
