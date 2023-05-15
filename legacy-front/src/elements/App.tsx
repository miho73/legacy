import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import '../css/univ.scss';
import LoginPage from './pages/auth/login';
import SignupPage from './pages/auth/signup';
import ActivationPage from './pages/auth/activation';
import ActivationDocs from './pages/docs/activation';

function App() {
    return (
        <>
            <Router>
                <Routes>
                    <Route path='/' element={<Link to='/login'>Login</Link>}/>
                    <Route path='/login' element={<LoginPage/>}/>
                    <Route path='/signup' element={<SignupPage/>}/>
                    <Route path='/signup/activation' element={<ActivationPage/>}/>
                    <Route path='/docs/activation' element={<ActivationDocs/>}/>
                </Routes>
            </Router>
        </>
    );
}

export default App;
