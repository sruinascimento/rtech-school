package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;
import br.com.alura.school.video.Video;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
public class Section {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String code;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Video> videos = new ArrayList<>();

    public Section() {
    }

    public Section(String code, String title, User author) {
        this.code = code;
        this.title = title;
        this.author = author;
    }

    public Section(String code, String title) {
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

    public boolean videoExists(String url) {
        return videos.stream()
                .map(Video::getUrl)
                .anyMatch(videoUrl -> videoUrl.equals(url));
    }

    public List<Video> getVideos() {
        return Collections.unmodifiableList(videos);
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", course=" + course +
                ", videos=" + videos +
                '}';
    }
}
