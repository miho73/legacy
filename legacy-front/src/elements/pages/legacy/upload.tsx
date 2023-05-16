import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import '../../../css/univ.scss';
import '../../../css/index.scss';

function TagElement(children: React.ReactElement) {
    return (
        <div>
            <p className='tag'>{children}</p>
        </div>
    )
}

function UploadPage() {
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
                <div className='header'>
                    <div className='search'>
                        <input type='text' placeholder='이름' className='small'/>
                    </div>
                    <button className='small norm'>업로드</button>
                </div>
                <hr style={{width: '100% !important'}}/>
                <div className='content'>
                <fieldset>
                        <label>설명</label>
                        <textarea className='explain' placeholder='설명'></textarea>
                    </fieldset>
                    <fieldset>
                        <label>파일</label>
                        <button className='norm small'>파일 선택</button>
                    </fieldset>
                </div>
            </div>
        </>
    );
}

export default UploadPage;