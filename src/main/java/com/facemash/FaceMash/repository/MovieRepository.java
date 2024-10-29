package com.facemash.FaceMash.repository;

import com.facemash.FaceMash.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie , Long> {

    Movie findByName(String query);

    List<Movie> findAllByOrderByRankingDesc();
}
