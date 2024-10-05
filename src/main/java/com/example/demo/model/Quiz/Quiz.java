package com.example.demo.model.Quiz;

// directly linked with fields of table name 'quizzes'
public class Quiz {

    int id;
    String technology;
    String name;

    public Quiz(int id, String technology, String name) {
        this.id = id;
        this.technology = technology;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Quiz{"
                + "id=" + id
                + ", technology='" + technology + '\''
                + ", name='" + name + '\''
                + '}';
    }
}
