import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../../css/univ.scss';
import '../../../css/login.scss';
import { inRange } from '../../service/checker';
import { changeBit, getBit } from '../../service/bitmask';
import axios from 'axios';

function LoginPage() {
    const [id, setId] = useState('');
    const [pwd, setPwd] = useState('');
    const [error, setError] = useState(0);

    const navigation = useNavigate();

    const goAuth = function() {
        var errors = 0;
        if(!inRange(4, 20, id.length)) {
            errors = changeBit(errors, 0);
        }
        if(!inRange(6, 120, pwd.length)) {
            errors = changeBit(errors, 1);
        }
        
        if(errors === 0) {
            axios.post('/login/auth', {
                id: id,
                pwd: pwd
            }).then(res => {
                if(res.data['result'] === 1) {
                    navigation('/');
                }
                else if(res.data['result'] === 2) {
                    setError(4);
                }
                else {
                    setError(8);
                }
            }).catch(err => {
                console.error(err);
                setError(16);
            });
        }
        else {
            setError(errors);
        }
    }

    return (
        <>
            <div className='page-header'>
                <h1><Link to='/'>Legacy ID</Link></h1>
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
                    <input type='text' name='id' placeholder='Legacy ID' autoComplete='username' className='large' value={id} onChange={e => setId(e.target.value)}/>
                    { getBit(error, 0) === 1 &&
                        <p>Legacy ID는 4자에서 20자 사이여야 해요.</p>
                    }
                    <input type='password' name='pwd' placeholder='암호' autoComplete='current-password' className='large' value={pwd} onChange={e => setPwd(e.target.value)}/>
                    { getBit(error, 1) === 1 &&
                        <p>암호는 최소 6자 이상이어야 해요.</p>
                    }
                    <button className='norm' type='button' onClick={goAuth}>로그인</button>
                    { getBit(error, 2) === 1 &&
                        <>
                            <p>Legacy ID가 활성화되어 있지 않아요</p>
                            <p>활성화 확인은 <Link to='/signup/activation'>여기</Link>에서 확인할 수 있어요.</p>
                        </>
                    }
                    { getBit(error, 3) === 1 &&
                        <p>Legacy ID 또는 암호가 잘못되었어요.</p>
                    }
                    { getBit(error, 4) === 1 &&
                        <p>문제가 발생했어요.</p>
                    }
                </div>
                <div className='field'>
                    <Link to='/login/findmy'>Legacy ID 또는 암호 찾기</Link>
                    <Link to='/signup/activation'>Legacy ID 활성화 확인</Link>
                </div>
            </form>
        </>
    );
}

export default LoginPage;
