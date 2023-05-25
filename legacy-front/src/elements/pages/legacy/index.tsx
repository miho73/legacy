import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ReactComponent as More } from '../../../resources/downarrow.svg';
import axios from 'axios';
import ErrorPage from '../docs/error'
import '../../../css/univ.scss';
import '../../../css/index.scss';

function ListElement(props) {
    return (
        <div className='element'>
            <div className='head'>
                <div className='left'>
                    <p>{props.name}</p>
                </div>
                <div className='right'>
                    <p>{props.uploadDate}</p>
                    <button className='square' title='자세히' onClick={() => props.detailsCtrl(props.code)}>
                        <More className='more'/>
                    </button>
                </div>
            </div>
            {props.openCtrl &&
                <div className='details'>
                    <div className='left'>{props.explain}</div>
                    <div className='right'>
                        <a href={'/cdn/get/'+props.hash} target='_blank'>다운로드</a>
                    </div>
                </div>
            }
        </div>
    );
}

function Index() {
    const [openState, setOpenState] = useState<Array<boolean>>([]);
    const [fileList, setFileList] = useState<Array<Object>>([]);
    const [query, setQuery] = useState('');
    const [searchErrorFlag, setSearchErrorFlag] = useState(42);
    const [authState, setAuthState] = useState(0);
    const [auth, setAuth] = useState(false);

    const navigation = useNavigate();

    // Auth
    useEffect(() => {
        axios.get('/login/authorize', {withCredentials: true})
        .then(res => {
            if(res.data['result'] === false) {
                navigation('/login');
                setAuth(false);
            }
            else {
                setAuth(true);
            }
        })
        .catch(err => {
            console.error(err);
            setAuthState(1);
            setAuth(false);
        });
    }, []);

    const controlDetail = function(code) {
        const nextState = openState.map((c, i) => {
            if(i === code) {
                return !c;
            }
            else return false;
        });
        setOpenState(nextState);
    }

    const fillInArticleList = function(arr) {
        let nLst: Object[]  = [];
        let fLst: boolean[] = [];
        arr.forEach(e => {
            let ep = {
                name: e.name,
                explain: e.explain,
                hash: e.hash,
                tags: e.tags,
                date: e.date
            }
            nLst.push(ep);
            fLst.push(false);
        });
        setOpenState(fLst);
        setFileList(nLst);
        setSearchErrorFlag(0);
    }

    useEffect(() => {
        axios.get('/article/get/latest',{params: {
            length: 30
        }})
        .then(res => {
            let data = res.data.result;
            fillInArticleList(data);
        })
        .catch(err => {
            let data = err.response.data.result;
            console.error(data);
            switch(data) {
                case 1:
                    setSearchErrorFlag(1);
                    break;
                case 2:
                    setSearchErrorFlag(2);
                    break;
                default:
                    setSearchErrorFlag(3);
                    break;
            }
        })
    }, []);

    var list: any[] = [];

    let searchHandler = function() {
        axios.get('/article/get/search', {params:{
            query: query
        }})
        .then(res => {
            let data = res.data.result;
            fillInArticleList(data);
        })
        .catch(err => {
            let data = err.response.data.result;
            console.error(data);
            switch(data) {
                case 1:
                    setSearchErrorFlag(1);
                    break;
                case 2:
                    setSearchErrorFlag(2);
                    break;
                default:
                    setSearchErrorFlag(3);
                    break;
            }
        });
    }

    /* Fill List */
    var cnt = 0;
    fileList.forEach((e: any) => {
        list.push(
        <ListElement
            code={cnt}
            openCtrl={openState[cnt]}
            detailsCtrl={controlDetail}
            name={e.name}
            explain={e.explain}
            hash={e.hash}
            uploadDate={e.date}
        />);
        cnt++;
    });

    if(authState === 1) {
        return (
            <ErrorPage errorTitle='인증하지 못했어요.' explain='인증서버에 접속하지 못했어요. 잠시 후에 다시 시도해주세요.'/>
        )
    }

    if(!auth) {
        return (
            <>
                <div className='page-header'>
                    <h1><Link to='/'>Legacy</Link></h1>
                </div>
                <hr/>
                <div className='common-section index upload'>
                    <p>Authorizing</p>
                </div>
            </>
        )
    }

    return (
        <>
            <div className='page-header'>
                <h1><Link to='/'>Legacy</Link></h1>
                <div className='ctrls'>
                    <Link to='/logout'>로그아웃</Link>
                </div>
            </div>
            <hr/>
            <div className='common-section index'>
                <div className='header'>
                    <div className='search'>
                        <input type='text' placeholder='검색' className='small' value={query} onChange={e => setQuery(e.target.value)} onKeyDown={e => {
                            if(e.key === 'Enter') {
                                searchHandler();
                            }
                        }}/>
                        <button className='small norm' onClick={searchHandler}>검색</button>
                    </div>                    
                    <Link to='/upload'>업로드</Link>
                </div>
                <hr/>
                {searchErrorFlag === 0 &&
                    <div className='list'>{list}</div>
                }
                {searchErrorFlag !== 0 &&
                    <div className='list'>
                        {searchErrorFlag === 1 &&
                            <>
                                <br/>
                                <p>요청에 필요한 권한이 부족합니다.</p>
                            </>
                        }
                        {searchErrorFlag === 2 &&
                            <>
                                <br/>
                                <p>검색어는 50자 이하여야 합니다.</p>
                            </>
                        }
                        {searchErrorFlag === 3 &&
                            <>
                                <br/>
                                <p>문제가 발생해서 검색하지 못했어요.</p>
                            </>
                        }
                        {searchErrorFlag === 42 &&
                            <>
                                <br/>
                                <p>잠시만요</p>
                            </>
                        }
                    </div>
                }
            </div>
        </>
    )
}

export default Index;