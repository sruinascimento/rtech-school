package br.com.alura.school.lesson;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
public class Lesson {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String code;
    private String title;
    @ManyToOne
    private User author;
    @ManyToOne
    private Course course;

    public Lesson() {
    }

    public Lesson(String code, String title, User author) {
        this.code = code;
        this.title = title;
        this.author = author;
    }

    public Lesson(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }

    public Course getCourse() {
        return course;
    }
}
