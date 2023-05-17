import axios from 'axios';
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Logout() {
    const navigation = useNavigate();

    useEffect(() => {
        axios.get('/login/logout').then(res => {
            navigation('/');
        }).catch(err => {
            console.error(err);
        });
    });

    return (
        <>
            <p>로그아웃중...</p>
        </>
    );
}

export default Logout;