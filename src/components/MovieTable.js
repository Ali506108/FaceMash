// src/components/MovieTable.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, Image } from 'react-bootstrap';

const MovieTable = () => {
  const [movies, setMovies] = useState([]);

  const fetchMovies = () => {
    axios.get('http://localhost:8080/api/movies')
      .then(response => {
        setMovies(response.data);
      })
      .catch(error => {
        console.error('Ошибка при получении фильмов:', error);
      });
  };

  useEffect(() => {
    fetchMovies();
  }, []);


  // useEffect будет вызываться при монтировании компонента и при изменении refreshFlag
  return (
    <div>
      <h2 className="mt-4">Список Фильмов</h2>
      <Table striped bordered hover className="mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Изображение</th>
            <th>Рейтинг</th>
            <th>Количество Сравнений</th>
          </tr>
        </thead>
        <tbody>
          {movies.map(movie => (
            <tr key={movie.id}>
              <td>{movie.id}</td>
              <td>{movie.name}</td>
              <td>
                <Image src={movie.url} alt={movie.name} thumbnail width="100" />
              </td>
              <td>{movie.ranking.toFixed(2)}</td>
              <td>{movie.comparisonCount}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};



export default MovieTable;
