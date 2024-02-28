package gr.aueb.radio.content.domain.genre;

import gr.aueb.radio.content.domain.song.Song;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

//    @OneToMany(mappedBy = "genre",fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private List<Song> songs = new ArrayList<>();

    public Genre() {
    }

    public Genre(String title) {
        this.title = title;
    }
    public Integer getId(){
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
//    public List<Song> getSongs() {
//        return this.songs;
//    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
