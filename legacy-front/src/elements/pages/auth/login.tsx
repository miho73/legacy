import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import '../../../css/univ.scss';
import '../../../css/login.scss';

function LoginPage() {
    return (
        <>
            <div className='page-header'>
                <h1>Legacy ID</h1>
                <div className='ctrls'>
                    <Link to='/signup'>Legacy ID 만들기</Link>
                </div>
            </div>
            <hr/>
            <form className='login-section' method='post' action='/login/auth'>
                <div className='header'>
                    <h2>Legacy ID</h2>
                    <p>Legacy에 로그인</p>
                </div>
                <div className='field'>
                    <input type='text' name='id' placeholder='Legacy ID' autoComplete='username' className='large'/>
                    <input type='password' name='pwd' placeholder='암호' autoComplete='current-password' className='large'/>
                    <button>로그인</button>
                </div>
                <Link to='/login/findmy'>Legacy ID 또는 암호 찾기</Link>
            </form>
        </>
    );
}

export default LoginPage;
