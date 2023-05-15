import React, { useState } from 'react';

function ActivationDocs() {
    return (
        <>
            <div className='page-header'>
                <h1>Legacy ID 활성화</h1>
                <div className='ctrls'></div>
            </div>
            <hr/>
            <div className='common-section'>
                <h1>Legacy ID 활성화</h1>
                <p>Legacy ID를 생성했습니다. 하지만 Legacy 서비스를 사용하려면 Legacy ID를 활성화해야 합니다.</p>
                <p>Legacy ID와 사용자를 확인한 후 Legacy ID를 활성화합니다. 이 과정은 최대 수일이 소요될 수 있습니다.</p>
                <p></p>
                <p>Legacy ID의 활성화 상태는 <a href='/signup/activation' target='_blank'>이곳</a>에서 확인할 수 있습니다.</p>
            </div>
        </>
    );
}

export default ActivationDocs;