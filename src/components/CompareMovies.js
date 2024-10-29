// src/components/CompareMovies.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Card, Button, Spinner, Alert } from 'react-bootstrap';

const CompareMovies = () => {
  const [pair, setPair] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  const fetchComparePair = () => {
    setLoading(true);
    setMessage('');
    axios.get('http://localhost:8080/api/movies/compare-pair')
      .then(response => {
        setPair(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Ошибка при получении пары фильмов:', error);
        setMessage('Не удалось получить пару фильмов для сравнения.');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchComparePair();
  }, []);

  const handleCompare = (winnerId, loserId) => {
    axios.post('http://localhost:8080/api/movies/compare', null, {
      params: {
        winnerId,
        loserId
      }
    })
      .then(response => {
        setMessage(response.data);
        fetchComparePair(); // Получить новую пару после обновления рейтингов
      })
      .catch(error => {
        console.error('Ошибка при обновлении рейтинга:', error);
        if (error.response && error.response.data) {
          setMessage(`Ошибка: ${error.response.data}`);
        } else {
          setMessage('Произошла ошибка при обновлении рейтинга.');
        }
      });
  };

  if (loading) {
    return (
      <div className="text-center mt-4">
        <Spinner animation="border" role="status">
          <span className="sr-only">Загрузка...</span>
        </Spinner>
      </div>
    );
  }

  if (message) {
    return (
      <div className="mt-4">
        <Alert variant="info" onClose={() => setMessage('')} dismissible>
          {message}
        </Alert>
        <Button variant="primary" onClick={fetchComparePair}>Попробовать снова</Button>
      </div>
    );
  }

  return (
    <div>
      <h2 className="mt-4">Сравните Фильмы</h2>
      <div className="d-flex justify-content-around mt-3">
        {pair.map(movie => (
          <Card key={movie.id} style={{ width: '18rem' }}>
            <Card.Img variant="top" src={movie.url} alt={movie.name} />
            <Card.Body>
              <Card.Title>{movie.name}</Card.Title>
              <Card.Text>Рейтинг: {movie.ranking.toFixed(2)}</Card.Text>
              <Button variant="success" onClick={() => handleCompare(movie.id, pair.find(m => m.id !== movie.id).id)}>
                Выбрать как лучший
              </Button>
            </Card.Body>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default CompareMovies;
