import 'bootstrap/dist/css/bootstrap.min.css';
import './MainPage.css';
import React, { useState, useEffect } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react'; 
import { Navigation, Pagination } from 'swiper/modules'; 
import 'swiper/swiper-bundle.css'; 
// import { Link } from 'react-router-dom';

const MainPage = () => {

    // 근처 가게 목록
    // const [latitude, setLatitude] = useState("");
    // const [longitude, setLongitude] = useState("");
    const [location, setLocation] = useState({
        latitude : null,
        longitude : null
    })
    useEffect = (() => {
        if(window.navigator.geolocation) {  // geolocation 지원할 경우 현재 위치 get
            window.navigator.geolocation.getCurrentPosition(success,error);
        }
    },[location]);

    function success(event) {
    // 성공했을 때 처리할 콜백 함수
        setLatitude(event.coords.latitude);   // 위도
        setLongitude(event.coords.longitude);  // 경도
        console.log(latitude);
        console.log(longitude);
    }
    function error(event) {
    // 실패 했을 때 처리할 콜백 함수
    }

    const [dropdownOpen, setDropdownOpen] = useState(false);

    const handleMouseEnter = () => setDropdownOpen(true);
    const handleMouseLeave = () => setDropdownOpen(false);

    // 맨 위로 가기
    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };


    const restaurants1 = [
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        // ... (다른 맛집 데이터)
    ];

    const restaurants2 = [
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        // ... (다른 맛집 데이터)
    ];

    const restaurants3 = [
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        {
                id: 1,
            name: "파스타 왕국",
            description: "분위기도 맛도 좋아요.",
            rating: "⭐️⭐️⭐️⭐️",
            address: "위치: 서울시 서대문구 창천동 11",
            imgSrc: "./static/img/pasta.jpg",
        },
        // ... (다른 맛집 데이터)
    ];


    return (
        <div className="container-fluid p-0 bg-dark text-white text-center" style={{ height: '2500px', background: '#f0f0f0' }}>
            <img src="/static/img/back.jpg" className="img-fluid p-0" style={{ width: '100%', maxHeight: '60vh', opacity: 0.4, objectFit: 'cover' }} alt="배경 이미지" />

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



                <div className="container-fluid input-group mt-3" style={{ margin: '0 30vw', width: '40vw' }}>
                        {/* 지역 선택 select 박스 */}
                        <select className="form-select" aria-label="지역 선택" style={{ textAlign:'center',backgroundColor: 'red', color: 'white', border:'none'}}>
                            <option value="" style={{backgroundColor:'white', color:'black'}}> 지역 선택</option>
                            <option value="강남구" style={{backgroundColor:'white', color:'black'}}>강남구</option>
                            <option value="강동구" style={{backgroundColor:'white', color:'black'}}>강동구</option>
                            <option value="강서구" style={{backgroundColor:'white', color:'black'}}>강서구</option>
                            <option value="양천구" style={{backgroundColor:'white', color:'black'}}>양천구</option>
                            <option value="마포구" style={{backgroundColor:'white', color:'black'}}>마포구</option>
                            <option value="종로구" style={{backgroundColor:'white', color:'black'}}>종로구</option>
                        </select>

                        {/* 텍스트 입력 및 버튼 */}
                        <input
                            type="text"
                            className="form-control s9-3"
                            placeholder="오늘 뭐 먹지?"
                            style={{ width:'15vw'}}
                        />
                        <button
                            type="button"
                            className="btn btn-primary"
                            style={{ flex: 0.5, backgroundColor: 'red', border: 'red' }}
                        >
                            검색
                        </button>
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
            <p></p>
            <div>
                <h2 style={{ textAlign: 'left', fontSize: '25px' }}>가격대별 맛집 추천</h2>
                <Swiper modules={[Navigation, Pagination]} spaceBetween={30} slidesPerView={4} navigation className="swiper-container">
                    {restaurants2.map((restaurant) => (
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
            <p></p>
            <div>
                <h2 style={{ textAlign: 'left', fontSize: '25px' }}>방문자별 맛집 추천</h2>
                <Swiper modules={[Navigation, Pagination]} spaceBetween={30} slidesPerView={4} navigation className="swiper-container">
                    {restaurants3.map((restaurant) => (
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
