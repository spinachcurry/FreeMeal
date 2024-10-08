// DetailPage.js
import React, { useState } from 'react';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'swiper/swiper-bundle.css'; // Swiper 스타일
import './DetailPage.css';
import KakaoMap from './components/KakaoMap'; // NaverMap 컴포넌트 import
import ReviewSection from './components/ReviewSection'; // ReviewSection 컴포넌트 import

const images = [
    './static/img/pasta.jpg',
    './static/img/pasta.jpg',
    './static/img/pasta.jpg',
    './static/img/pasta.jpg',
    './static/img/pasta.jpg',
   
];

const DetailPage = () => {

    // 맨 위로 가기
    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    return (
        <div className="container-fluid p-0 bg-dark text-white" style={{ height: '2000px' }}>
            <header className="header text-center fixed-tob be-dark">
                <div>
                    <ul className='nav justify-content-end'>
                        <nav className='navbar navbar-expand-sm navbar-dark'>
                            <div className="container-fluid fixed-top">
                                <a className='rounded-pill' href="#">
                                    <img src='/logo.png' alt="로고자리" /> 
                                </a>
                            </div>
                        </nav>

                        <li className="nav">
                            <a className="nav-link" style={{ color: 'white', fontWeight: '1000' }} href="#">로그인</a>
                        </li>
                        <li className="nav">
                            <a className="nav-link" style={{ color: 'white', fontWeight: '1000' }} href="#">회원가입</a>
                        </li>
                    </ul>

                    <div className="container-fluid input-group mt-3 mb-3"  style={{ margin: '0 30vw', width: '40vw' }}>
                        <button type="button" className="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" style={{backgroundColor:'red'}}
                        >지역 선택  </button>

                        {/* 드롭다운 목록 */}
                        <ul className="dropdown-menu" style={{ width: '200px' }}>
                            <li><a className="dropdown-item" href="#">강남구</a></li>
                            <li><a className="dropdown-item" href="#">강동구</a></li>
                            <li><a className="dropdown-item" href="#">강서구</a></li>
                            <li><a className="dropdown-item" href="#">양천구</a></li>
                            <li><a className="dropdown-item" href="#">마포구</a></li>
                            <li><a className="dropdown-item" href="#">종로구</a></li>
                        </ul>
                        {/* 텍스트 입력 및 버튼 */}
                        <input
                            type="text" className="form-control s9-3" placeholder="오늘 뭐 먹지?"/>
                        <button
                            type="button" className="btn btn-primary" style={{ flex: 0.4, backgroundColor: 'red', border: 'red' }}
                        >검색 </button>
                    </div>
                    <br />
                    <h1>유미 곱창</h1>
                    <p>잡내 없이 신선한 곱창</p>
                </div>
            </header>

            <div className="image-section" style={{ border: '1px solid white', padding: '0px', display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                {images.map((image, index) => (
                    <img 
                        key={index}
                        src={image} 
                        alt={`식당 이미지 ${index + 1}`} 
                        style={{ width: 'calc(20% - 10px)', margin: '5px', objectFit: 'cover' }} 
                    />
                ))}
            </div>
            <br></br>
            <div className='store-info' style={{border:'solid 1px white'}}> <h4>둘이 먹다가 하나가 죽어도 모를 맛</h4>
                <div className="menu-section">
                    <h2>메뉴 목록</h2>
                    <ul>
                        <li>곱창 - 15,000원</li>
                        <li>막창 - 18,000원</li>
                        <li>볶음밥 - 3,000원</li>
                    </ul>
                </div>
            </div>
            <div className="map-section" style={{ border: '1px solid black', padding: '10px', margin: '20px 0' }}>
                <KakaoMap />
                <div className=''></div>
            </div>

                
            <ReviewSection />

            <footer className="footer">
                <div className="footer-info">
                    <h2>꽁밥</h2>
                    <p>주소: 서울특별시 종로구 평창로 123</p>
                    <p>전화: 02-1234-5678</p>
                    <p>이메일: info@ggongbob.com</p>
                    <p>개인정보처리방침 | 이용약관</p>
                    <p>&copy; 2024 꽁밥. All rights reserved.</p>
                    <button className="scroll-to-top" onClick={scrollToTop}>
                        맨 위로 가기
                    </button>
                </div>
            </footer>
        </div>
    );
};

export default DetailPage;
