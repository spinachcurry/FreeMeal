// DetailPage.js
import React, { useState } from 'react';
import 'swiper/swiper-bundle.css'; // Swiper 스타일
import './DetailPage.css';
import KakaoMap from './components/KakaoMap'; // NaverMap 컴포넌트 import
import ReviewSection from './components/ReviewSection'; // ReviewSection 컴포넌트 import

const images = [
    '/Static/124038680xqle.jpg',
    '/Static/01.jpeg',
    '/Static/02.jpg',
    '/Static/03.jpg',
    '/Static/04.jpg',
];

const DetailPage = () => {
    const [dropdownOpen, setDropdownOpen] = useState(false);

    const handleMouseEnter = () => setDropdownOpen(true);
    const handleMouseLeave = () => setDropdownOpen(false);

    return (
        <div className="container-fluid p-0 bg-dark text-white" style={{ height: '2000px' }}>
            <header className="header text-center fixed-tob be-dark">
                <div>
                    <ul className='nav justify-content-end'>
                        <nav className='navbar navbar-expand-sm navbar-dark'>
                            <div className="container-fluid">
                                <a className='rounded-pill' href="#">
                                    <img src='/logo.png' alt="로고자리" /> 
                                </a>
                            </div>

                            <div className="container-fluid input-group mt-3 mb-3" style={{ margin: '0 30vw', width: '40vw' }}>
                                <button type="button" className="btn btn-outline-primary dropdown-toggle s9-3" data-bs-toggle="dropdown" style={{ width: '200px', '--bs-btn-color': '#ffffff', '--bs-btn-border-color': '#ffffff', '--bs-btn-hover-bg': '#ff0000', '--bs-btn-hover-border-color': '#ff0000', '--bs-btn-active-bg': 'none', '--bs-btn-active-border-color': '#ffffff' }}>
                                    지역 선택
                                </button>
                                <ul className="dropdown-menu" style={{ width: '200px' }}>
                                    <li><a className="dropdown-item" href="#">강남구</a></li>
                                    <li><a className="dropdown-item" href="#">강동구</a></li>
                                    <li><a className="dropdown-item" href="#">강서구</a></li>
                                    <li><a className="dropdown-item" href="#">양천구</a></li>
                                    <li><a className="dropdown-item" href="#">마포구 </a></li>
                                    <li><a className="dropdown-item" href="#">종로구</a></li>
                                </ul>
                                <input type="text" className="form-control" placeholder="오늘 뭐 먹지?" />
                                <button type='button' className='btn btn-primary' style={{ backgroundColor: 'red', border: 'red' }}>검색</button>
                            </div>
                        </nav>

                        <li className="nav">
                            <a className="nav-link" style={{ color: 'white', fontWeight: '1000' }} href="#">로그인</a>
                        </li>
                        <li className="nav">
                            <a className="nav-link" style={{ color: 'white', fontWeight: '1000' }} href="#">회원가입</a>
                        </li>
                    </ul>
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

            <div className="menu-section">
                <h2>메뉴</h2>
                <ul>
                    <li>곱창 - 15,000원</li>
                    <li>막창 - 18,000원</li>
                    <li>볶음밥 - 3,000원</li>
                </ul>
            </div>

            <div className="map-section" style={{ border: '1px solid black', padding: '10px', margin: '20px 0' }}>
                <KakaoMap />
            </div>

            <ReviewSection />

            <footer className="footer">
                <p>주소: 서울시 양천구 목동</p>
                <p>전화번호: 02-1234-5678</p>
            </footer>
        </div>
    );
};

export default DetailPage;
