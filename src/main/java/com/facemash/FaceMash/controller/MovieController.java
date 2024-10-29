package com.facemash.FaceMash.controller;


import com.facemash.FaceMash.model.Movie;
import com.facemash.FaceMash.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {


    private final MovieService service;

    @Autowired
    public MovieController(MovieService service) {
        this.service = service;
    }

    @PostMapping("/compare")
    public ResponseEntity<String> compareMovie(@RequestParam Long winnerId, @RequestParam Long loserId) {
        try {
            service.updateRanking(winnerId, loserId);
            return ResponseEntity.ok("Рейтинг обновлён успешно.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при обновлении рейтинга: " + e.getMessage());
        }
    }

    @GetMapping("/compare-pair")
    public ResponseEntity<List<Movie>> getComparePair() {
        try{
            List<Movie> pair = service.getRandomPair();
            if(pair.size() < 2 ){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(pair);
        }catch(Exception e) {
            return ResponseEntity.status(500).build();
        }
    }



    @PostMapping("/create")
    public ResponseEntity<?> createMovie(@RequestParam("name") String name,
                                         @RequestParam("image") MultipartFile image) {
        if (name == null || image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body("Name and image are required");
        }

        Movie movie = new Movie();
        movie.setName(name);

        try {
            Movie createdMovie = service.createMovie(movie, image);
            return ResponseEntity.ok(createdMovie);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Ошибка при создании фильма: " + e.getMessage());
        }
    }

    // Эндпоинт для обновления фильма с возможной заменой изображения
    @PutMapping("/update/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "image", required = false) MultipartFile image) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Movie updatedMovie = service.updateMovie(id, name, image);
            return ResponseEntity.ok(updatedMovie);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Эндпоинт для удаления фильма
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.badRequest().build();
        } else {
            try {
                Movie deletedMovie = service.deleteMovie(id);
                return ResponseEntity.ok(deletedMovie);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    // Эндпоинт для получения фильма по ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieId(@PathVariable Long id ) {
        try {
            Optional<Movie> movieOpt = service.descId(id);
            if (movieOpt.isPresent()) {
                return ResponseEntity.ok(movieOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("")
    public List<Movie> getAll() {
        return service.allMovies();
    }
}
