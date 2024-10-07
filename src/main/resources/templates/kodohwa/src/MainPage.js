import 'bootstrap/dist/css/bootstrap.min.css';
import './MainPage.css';
import React, { useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react'; 
import { Navigation, Pagination } from 'swiper/modules'; 
import 'swiper/swiper-bundle.css'; 
// import { Link } from 'react-router-dom';

const MainPage = () => {

    const [dropdownOpen, setDropdownOpen] = useState(false);
    
    const handleMouseEnter = () => setDropdownOpen(true);
    const handleMouseLeave = () => setDropdownOpen(false);

    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const restaurants1 = [
        {
            id: 1,
            name: "맛집 1 이름",
            description: "간단한 설명",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 주소",
            imgSrc: "/Static/img/124038680xqle.jpg",
        },
        // ... (다른 맛집 데이터)
    ];

    return (
        <div className="container-fluid p-0 bg-dark text-white text-center" style={{ height: '2000px', background: '#f0f0f0' }}>
            <img src="/Static/img/240_F_475026985_ZC85NJHIIqZMz6zvHfaM7Du0bHtPm044.jpg" className="img-fluid p-0" style={{ width: '100%', maxHeight: '60vh', opacity: 0.4, objectFit: 'cover' }} alt="배경 이미지" />

            <div style={{ position: 'absolute', top: '0vh', width: '100%', left: 0 }}>
                <ul className='nav justify-content-end'>
                    <nav className='navbar navbar-expand-sm navbar-dark fixed-top'>
                        <div className="nav-item dropdown" onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
                            <button className='btn btn-primary' style={{ backgroundColor: 'red' }}>
                                메뉴
                            </button>
                            {dropdownOpen && (
                                <ul className="dropdown-menu show">
                                    <li><a className="dropdown-item" href="#">지역별</a></li>
                                    <li><a className="dropdown-item" href="#">가격별</a></li>
                                    <li><a className="dropdown-item" href="#">방문별</a></li>
                                </ul>
                            )}
                        </div>
                    </nav>
                    <li className="nav">
                        <a className="nav-link" style={{ color: 'white', fontWeight: '1000' }} href="#">로그인</a>
                    </li>
                    <li className="nav">
                        <a className="nav-link" style={{ color: 'white', fontWeight: '1000' }} href="#">회원가입</a>
                    </li>
                </ul>
                <h1 className="gff">꽁밥</h1>
                <p className="secTitle">우리동네 믿고 먹는 맛집 대장!</p>
                <div className="container-fluid input-group mt-3 mb-3" style={{ margin: '0 30vw', width: '40vw' }}>
                    <button type="button" className="btn btn-outline-primary dropdown-toggle s9-3" data-bs-toggle="dropdown" style={{ width: '200px' }}>
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
                    <input type="text" className="form-control s9-3" placeholder="오늘 뭐 먹지?" />
                    <button type='button' className='btn btn-primary' style={{ flex: 0.2, backgroundColor: 'red', border: 'red' }}>검색</button>
                </div>
            </div>

            <div>
                <h2 style={{ textAlign: 'left', fontSize: '25px' }}>당신을 위한 이 지역 맛집 추천</h2>
                <Swiper modules={[Navigation, Pagination]} spaceBetween={30} slidesPerView={4} navigation className="swiper-container">
                    {restaurants1.map((restaurant) => (
                        <SwiperSlide key={restaurant.id} className="swiper-slide">
                            <div className="restaurant-item">
                                <img src={restaurant.imgSrc} alt={restaurant.name} />
                                <h3>{restaurant.name}</h3>
                                <p>{restaurant.description}</p>
                                <span>{restaurant.rating}</span>
                                <p>{restaurant.address}</p>
                            </div>
                        </SwiperSlide>
                    ))}
                </Swiper>
            </div>

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

export default MainPage;
