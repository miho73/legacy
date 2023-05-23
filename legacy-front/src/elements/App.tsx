import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import '../css/univ.scss';
import LoginPage from './pages/auth/login';
import SignupPage from './pages/auth/signup';
import ActivationPage from './pages/auth/activation';
import ActivationDocs from './pages/docs/activation';
import Index from './pages/legacy';
import UploadPage from './pages/legacy/upload';
import Logout from './pages/auth/logout';
import ErrorPage from './pages/docs/error';

function App() {
    return (
        <>
            <Router>
                <Routes>
                    <Route index path='/' element={<Index/>}/>
                    <Route path='/login' element={<LoginPage/>}/>
                    <Route path='/signup' element={<SignupPage/>}/>
                    <Route path='/signup/activation' element={<ActivationPage/>}/>
                    <Route path='/docs/activation' element={<ActivationDocs/>}/>
                    <Route path='/upload' element={<UploadPage/>}/>
                    <Route path='/logout' element={<Logout/>}/>
                    <Route path='*' element={<ErrorPage errorTitle='찾으시는 페이지가 없어요.' explain='입력하신 주소가 정확한지 다시 한 번 확인해주세요.'/>}/>
                </Routes>
            </Router>
        </>
    );
}

export default App;
