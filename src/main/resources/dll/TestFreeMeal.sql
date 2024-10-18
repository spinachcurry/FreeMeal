USE test;

##테스트용 꽁밥 테이블 생성
CREATE OR REPLACE TABLE `test_freeMeal`(
	`no` INT(20) PRIMARY KEY AUTO_INCREMENT,
	`title` VARCHAR(100),
	`link` TEXT,
	`category` VARCHAR(255),
	`description` VARCHAR(255),
	`telephone` VARCHAR(100),
	`address` VARCHAR(255),
	`roadAddress` VARCHAR(255),
	`mapx` INT(100),
	`mapy` INT(100),
	`price` INT(50),
	`party` INT(20),
	`visitDate` DATE
); 

SELECT `date`, `storeNm`, `party`, `price`, `areaNm` FROM `root_data`; ##전체 데이터 가져오기

test
INSERT INTO test_freemeal (`title`,`link`, `category`, `description`, `telephone`, `address`, `roadAddress`, `mapx`, `mapy`, `price`,`party`,`visitDate`) VALUE ();

COMMIT;

##일일 호출 완료된 API 횟수 점검(확인) 테이블
CREATE OR REPLACE TABLE `call_count` (
	`no` INT PRIMARY KEY AUTO_INCREMENT,
	`date` DATE UNIQUE, ##얘는 중복되면 안된다~
	`count` INT (255)
);

SELECT count FROM call_count WHERE `date` = '2024-09-25';

ALTER TABLE `test_freemeal` DROP `areaNm`;
ALTER TABLE `test_freemeal` ADD `areaNm` VARCHAR(50) DEFAULT '강동구';

UPDATE root_data SET `check` = 0 WHERE `no` = 1;

#전체 가게 목록 SQL
SELECT `no`, `title`,`link`, `telephone`, `areaNm`, `mapx`, `mapx`, `address`, `roadAddress`, `category`, `description`, 
	SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty 
FROM test_freemeal GROUP BY `title`, `areaNm`;

#가게 상세 페이지  SQL
SELECT `title`,`link`, `telephone`, `areaNm`, `mapx`, `mapx`, `address`, `roadAddress`, `category`, `description`, 
	test_freeMealSUM(`price`) AS totalPrice, SUM(`party`) AS totalParty 
FROM test_freemeal GROUP BY `title`, `areaNm`;
