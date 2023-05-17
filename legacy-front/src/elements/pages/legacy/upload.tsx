import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import '../../../css/univ.scss';
import '../../../css/index.scss';
import axios from 'axios';
import {fileFromPath} from 'formdata-node/file-from-path'
import { getBit } from '../../service/bitmask';

function TagElement(children: React.ReactElement) {
    return (
        <div>
            <p className='tag'>{children}</p>
        </div>
    )
}

function UploadPage() {
    const [name, setName] = useState('');
    const [explain, setExplain] = useState('');
    const [selectedFile, setSelectedFile] = useState(null);
    const [fileName, setFileName] = useState('');
    const [error, setError] = useState(0);

    const navigation = useNavigate();

    const uploadArticle = function() {
        const form = new FormData();
        form.append("name", name);
        form.append("explain", explain);
        form.append("tags", '0');
        form.append('file', selectedFile);

        axios.post('/upload/cdn/post', form, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then(res => {
            navigation('/');
        }).catch(err => {
            console.error(err);
            var code = err.response.data['result'];
            switch(code) {
                case 0:
                    setError(1);
                    break;
                case 1:
                    setError(2);
                    break;
                case 2:
                    setError(4);
                    break;
                default:
                    setError(8);
                    break;
            }
        });

        return false;
    }

    return (
        <>
            <div className='page-header'>
                <h1>Legacy</h1>
                <div className='ctrls'>
                    <Link to='/logout'>로그아웃</Link>
                </div>
            </div>
            <hr/>
            <div className='common-section index upload'>
                <form>
                    <div className='header'>
                        <div className='search'>
                            <input type='text' name='name' placeholder='이름' className='small' value={name} onChange={e => setName(e.target.value)}/>
                        </div>
                        <button type='button' className='small norm' onClick={uploadArticle}>업로드</button>
                    </div>
                    <hr style={{width: '100% !important'}}/>
                    <div className='content'>
                        <fieldset>
                                <label>설명</label>
                                <textarea className='explain' name='explain' placeholder='설명' value={explain} onChange={e => setExplain(e.target.value)}></textarea>
                            </fieldset>
                            <fieldset>
                            <label>파일</label>
                            <label for='fs' className='btn small'>
                                <span>파일 선택</span>
                            </label>
                            <span className='filename'>{fileName}</span>
                            { getBit(error, 0) === 1 &&
                                <p>권한 거부</p>
                            }
                            { getBit(error, 1) === 1 &&
                                <p>이미 존재하는 파일입니다.</p>
                            }
                            { getBit(error, 2) === 1 &&
                                <p>파일은 최대 500 MB까지 올릴 수 있습니다.</p>
                            }
                            { getBit(error, 3) === 1 &&
                                <p>업로드하지 못했습니다.</p>
                            }
                            <input type='file' name='file' id='fs' className='norm small' onChange={e => {
                                setSelectedFile(e.target.files[0]);
                                setFileName(e.target.files[0].name);
                            }}/>
                        </fieldset>
                        <input name='tags' value={0} hidden/>
                    </div>
                </form>
            </div>
        </>
    );
}

export default UploadPage;