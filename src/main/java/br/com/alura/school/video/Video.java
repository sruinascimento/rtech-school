package br.com.alura.school.video;

import br.com.alura.school.section.Section;

import javax.persistence.*;

@Entity
@Table
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;
    public Video() {
    }

    public Video(String url, Section section) {
        this.url = url;
        this.section = section;
    }

    public Video(String url) {
        this.url = url;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "url: " + url;
    }
}
