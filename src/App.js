// src/App.js
import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import MovieTable from './components/MovieTable';
import CompareMovies from './components/CompareMovies';
import './App.css'; // Импортируем CSS файл



function App() {
  return (
    <Container>
      <Row>
        <Col>
          <h1 className="text-center my-4">FaceMash - Рейтинг Фильмов</h1>
        </Col>
      </Row>
      <Row className="compare-table-row">
        <Col md={6} className="compare-table-col">
          <CompareMovies />
        </Col>
        <Col md={6} className="compare-table-col">
          <MovieTable />
        </Col>
      </Row>
    </Container>
  );
}

export default App;
