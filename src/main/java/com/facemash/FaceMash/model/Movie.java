package com.facemash.FaceMash.model;


import jakarta.persistence.*;

@Entity
@Table(name = "face_rating")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private double ranking;


    public Movie(Long id , String name , String url ) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Movie() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRanking() {
        return ranking;
    }

    public void setRanking(double ranking) {
        this.ranking = ranking;
    }
}
