package com.facemash.FaceMash.service;


import com.facemash.FaceMash.model.Movie;
import com.facemash.FaceMash.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    private static int K =32;
    @Autowired
    private CloudinaryService cloudinaryService;


    // Ranking

    public  List<Movie> getRandomPair() {
        List<Movie> allMovie = repository.findAll();
        if(allMovie.size() < 2) {
            throw new IllegalStateException("No movie found");
        }

        int index = new Random().nextInt(allMovie.size() -1);
        return allMovie.subList(index ,index+2);
    }

    public void updateRanking(Long winnerId , Long lodsserId) {

        Movie winner = findMovie(winnerId);
        Movie losser = findMovie(lodsserId);
        if(findMovie(winnerId) == null || findMovie(lodsserId) == null) {
            throw  new RuntimeException("Movie not found");
        }

        double expectedWinner = 1/(1 + Math.pow(10 , (losser.getRanking() - winner.getRanking()) /400));
        double expectedLosser = 1/(1 + Math.pow(10 , (winner.getRanking() - losser.getRanking()) /400));

        winner.setRanking(winner.getRanking() + K * (1 - expectedWinner));
        losser.setRanking(losser.getRanking() + K * (0 - expectedLosser));

        repository.save(winner);
        repository.save(losser);


    }



    //Search Engine

    public Movie SearchEngine(String query) {
        return repository.findByName(query);
    }

    // CRUD Create Update Delete

    public Optional<Movie> descId(Long id) {
        if(findMovie(id) != null) {
            return repository.findById(id);
        }else {
            return null;
        }
    }

    public List<Movie> allMovies() {
        return repository.findAllByOrderByRankingDesc();
    }

    public Movie createMovie(Movie movie, MultipartFile imageFile) {
        if (movie != null && imageFile != null && !imageFile.isEmpty()) {

            String ImageUrl = cloudinaryService.uploadToImage(imageFile);
            movie.setUrl(ImageUrl);
            movie.setRanking(1500.0);
            return repository.save(movie);
        }else{
            return null;
        }
    }

    public Movie updateMovie(Long id, String name, MultipartFile image) {
        Optional<Movie> existingMovieOpt = repository.findById(id);
        if(!existingMovieOpt.isPresent()) {
            throw new IllegalArgumentException("Фильм с ID " + id + " не найден");
        }

        Movie existingMovie = existingMovieOpt.get();

        if(name != null && !name.isEmpty()) {
            existingMovie.setName(name);
        }

        if(image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadToImage(image);
            existingMovie.setUrl(imageUrl);
        }

        return repository.save(existingMovie);
    }

    public Movie deleteMovie(Long id) {
        if(findMovie(id) != null) {
            repository.deleteById(id);
            return findMovie(id);
        }else{
            return null;
        }
    }

    private Movie findMovie(long id) {
        Optional<Movie> findMovie = repository.findById(id);

        if(findMovie.isPresent()) {
            return findMovie.get();
        }else{
            return null;
        }
    }



}
