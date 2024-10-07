import 'bootstrap/dist/css/bootstrap.min.css';
import React, {useEffect} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './MainPage';
import DetailPage from './DetailPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<MainPage/>} />
                <Route path="/detail" element={<DetailPage/>} />
            </Routes>
        </Router>
    );
};

export default App;


