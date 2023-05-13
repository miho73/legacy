import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import '../css/univ.scss';
import LoginPage from './pages/login';

function App() {
    return (
        <>
            <Router>
                <Routes>
                    <Route path='/' element={<Link to='/login'>Login</Link>}/>
                    <Route path='/login' element={<LoginPage/>}></Route>
                </Routes>
            </Router>
        </>
    );
}

export default App;
