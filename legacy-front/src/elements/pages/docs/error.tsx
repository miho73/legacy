import React from 'react';

function ErrorPage(props) {
    return (
        <>
            <div className='page-header'>
                <h1>
                    <a href='/'>Legacy</a>
                </h1>
                <div className='ctrls'></div>
            </div>
            <hr/>
            <div className='common-section'>
                <br/>
                <h2>{props.errorTitle}</h2>
                <p>{props.explain}</p>
            </div>
        </>
    )
}

export default ErrorPage;