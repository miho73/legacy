import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../../../css/univ.scss';
import '../../../css/login.scss';
import axios from 'axios';

function ActivationPage() {
    const [id, setId] = useState('');
    const [pwd, setPwd] = useState('');

    const [status, setStatus] = useState({disp: false, st: -1});

    const checkActiveState = function() {
        axios.post('/signup/activation/check', {
            id: id,
            pwd: pwd
        }).then((res) => {
            var re = res.data['result'];
            setStatus({
                disp: true,
                st: re
            })
        }).catch((err) => {
            console.error(err);
            setStatus({
                disp: true,
                st: 9
            })
        });
        return false;
    }

    const checkAgain = function() {
        setStatus({
            disp: false,
            st: -1
        });
    }

    return (
        <>
            <div className='page-header'>
                <h1>
                    <Link to='/'>Legacy ID Activation</Link>
                </h1>
                <div className='ctrls'>
                    <Link to='/login'>로그인</Link>
                </div>
            </div>
            <hr/>
            {!status.disp &&
                <form className='login-section' method='post' action='/login/auth'>
                    <div className='header'>
                        <h2>활성화 확인</h2>
                        <p>Legacy ID 활성화 상태 확인</p>
                    </div>
                    <div className='field'>
                        <input type='text' name='id' placeholder='Legacy ID' autoComplete='username' value={id} onChange={e => setId(e.target.value)}/>
                        <input type='password' name='pwd' placeholder='암호' autoComplete='current-password' value={pwd} onChange={e => setPwd(e.target.value)}/>
                        <button className='norm' type='button' onClick={checkActiveState}>확인</button>
                    </div>
                    <Link to='/login/findmy'>Legacy ID 또는 암호 찾기</Link>
                </form>
            }
            {status.disp &&
                <div className='login-section active-check-section'>
                    <h2>{id}에 대한 활성화 확인</h2>
                    {status.st === 0 &&
                        <p className='green'>활성화됨</p>
                    }
                    {status.st === 1 &&
                        <p className='red'>활성화 되지 않음</p>
                    }
                    {status.st === 2 &&
                        <p className='red'>거절됨</p>
                    }
                    {status.st === 3 &&
                        <p className='red'>인증에 실패했습니다.</p>
                    }
                    {status.st === 9 &&
                        <p className='red'>확인 중 문제가 발생했습니다.</p>
                    }
                    <button className='norm' onClick={checkAgain}>다시 확인</button>
                </div>
            }
        </>
    );
}

export default ActivationPage;
