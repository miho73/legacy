import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import '../../../css/univ.scss';
import '../../../css/login.scss';
import { inRange } from '../../service/checker';
import { changeBit, getBit } from '../../service/bitmask';
import axios from 'axios';

function SignupPage() {
    const [sectionState, setSection] = useState(0);
    const [errorState, setError] = useState(0);

    const [name, setName] = useState('');
    const [code, setCode] = useState(0);
    const [id, setId] = useState('');
    const [pwd, setPwd] = useState('');

    const changeSection = function(now: number, to: number) {
        var errors = 0, ok = true;
        switch(now) {
            case 0:
                if(!inRange(2, 10, name.length)) {
                    errors = changeBit(errors, 0);
                    ok = false;
                }
                if(!inRange(1000, 9999, code)) {
                    errors = changeBit(errors, 1);
                    ok = false;
                }
                break;
            case 1:
                if(!inRange(4, 20, id.length)) {
                    errors = changeBit(errors, 2);
                    ok = false;
                }
                if(!inRange(6, 120, pwd.length)) {
                    errors = changeBit(errors, 4);
                    ok = false;
                }
                break;
        }
        setError(errors);
        if(ok) setSection(to);
        return false;
    }

    const create = function() {
        var pg1e = false, pg2e = false, errors = 0;
        // PG 1 test
        if(!inRange(2, 10, name.length)) {
            errors = changeBit(errors, 0);
            pg1e = true;
        }
        if(!inRange(1000, 9999, code)) {
            errors = changeBit(errors, 1);
            pg1e = true;
        }

        // PG 2 test
        if(!inRange(4, 20, id.length)) {
            errors = changeBit(errors, 2);
            pg2e = true;
        }
        if(!inRange(6, 120, pwd.length)) {
            errors = changeBit(errors, 5);
            pg2e = true;
        }
        duplicationTest(id, pg1e, pg2e, errors);
        return false;
    }

    const duplicationTest = function(id: string, pg1e: boolean, pg2e: boolean, errors: number) {
        axios.post('/signup/test/id-duplication', {
            id: id
        }).then((res) => {
            const obj = res.data;
            // 0: OK, 1: duplicated
            switch(obj['result']) {
                case 0:
                    if(!pg1e && !pg2e) {
                        makeId();
                    }
                    break;
                case 1:
                    pg2e = true;
                    errors = changeBit(errors, 3);
                    break;
            }
        }).catch((err) => {
            pg2e = true;
            console.error(err);
            errors = changeBit(errors, 4);
        }).finally(() => {
            if(pg1e) {
                setSection(0);
            }
            else if(pg2e) {
                setSection(1);
            }
            setError(errors);
         });
    }

    const navigate = useNavigate();
    const makeId = function() {
        axios.post('/signup/post', {
            name: name,
            code: code,
            id: id,
            pwd: pwd
        }).then(res => {
            navigate("/docs/activation");
        }).catch(err => {
            console.error(err);
            setError(64);
        });
    }

    return (
        <>
            <div className='page-header'>
                <h1>Legacy ID</h1>
                <div className='ctrls'>
                    <Link to='/login'>로그인</Link>
                </div>
            </div>
            <hr/>
            <form className='login-section' method='post' action='/login/auth'>
                <div className='header'>
                    <h2>Legacy ID</h2>
                    <p>Legacy ID 만들기</p>
                </div>
                {sectionState === 0 &&
                    <div className='field'>
                        <fieldset>
                            <label>이름</label>
                            <input type='text' name='name' placeholder='이름' autoComplete='name' value={name} onChange={e => setName(e.target.value)}/>
                            { getBit(errorState, 0) === 1 &&
                                <p>이름은 2자~10자 사이여야 해요.</p>
                            }
                        </fieldset>
                        <fieldset>
                            <label>학번</label>
                            <input type='number' name='code' placeholder='학번' value={code} onChange={e => {
                                var x = parseInt(e.target.value);
                                setCode(x);
                            }}/>
                            { getBit(errorState, 1) === 1 &&
                                <p>학번을 '1220'과 같은 형태로 적어주세요.</p>
                            }
                        </fieldset>
                        <button type='button' onClick={() => changeSection(0, 1)}>다음</button>
                    </div>
                }
                {sectionState === 1 &&
                    <div className='field'>
                        <fieldset>
                            <label>Legacy ID</label>
                            <input type='text' name='id' placeholder='Legacy ID' autoComplete='username' value={id} onChange={e => setId(e.target.value)}/>
                            { getBit(errorState, 2) === 1 &&
                                <p>Legacy ID는 4자에서 20자 사이여야 해요.</p>
                            }
                            { getBit(errorState, 3) === 1 &&
                                <p>이미 사용중인 Legacy ID이에요.</p>
                            }
                            { getBit(errorState, 4) === 1 &&
                                <p>Legacy ID 중복검사를 하지 못했어요.</p>
                            }
                        </fieldset>
                        <fieldset>
                            <label>암호</label>
                            <input type='password' name='pwd' placeholder='암호' autoComplete='current-password' value={pwd} onChange={e => setPwd(e.target.value)}/>
                            { getBit(errorState, 5) === 1 &&
                                <p>암호는 최소 6자 이상이어야 해요.</p>
                            }
                        </fieldset>
                        <div className='button-set'>
                            <button type='button' onClick={() => changeSection(1, 0)}>이전</button>
                            <button type='button' onClick={() => create()}>다음</button>
                        </div>
                        { getBit(errorState, 6) === 1 &&
                            <p>Legacy ID를 만들지 못했어요.</p>
                        }
                    </div>
                }
            </form>
        </>
    );
}

export default SignupPage;
