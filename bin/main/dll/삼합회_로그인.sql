
USE test;
USE edu;

SELECT REPLACE(USER, '/r/n', '') ,COUNT(*) AS cnt FROM root_data GROUP BY REPLACE(USER,'/r/n','') ORDER BY 1;
 
CREATE TABLE `Users` (
	`userNo`        INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`userId`        VARCHAR(100) NOT NULL,
	`password`      VARCHAR(255) NOT NULL,
	`name`          VARCHAR(100) NOT NULL,
	`user_Nnm`      VARCHAR(50) NOT NULL,
	`phone`         VARCHAR(15) NOT NULL,
	`email`         VARCHAR(100) NOT NULL,
	`createDate`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	`modifiedDate`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
	`status`        VARCHAR(10)		NOT NULL DEFAULT '1',
	`review`        TEXT NULL ,
	`profileImageUrl` VARCHAR(255) NULL
);

CREATE TABLE `role` (
	roleNo		INT				NOT NULL AUTO_INCREMENT PRIMARY KEY,
	roleNm		VARCHAR(50) 	NOT NULL
);
  
  CREATE TABLE `Reviews` (
	`reviewNo`       INT AUTO_INCREMENT PRIMARY KEY, -- 리뷰 ID를 고유 기본 키로 설정
	`address`        VARCHAR(50) NOT NULL,                   -- 스토어 ID
	`userId`         VARCHAR(50) NOT NULL,                   -- 사용자 ID
	`content`        TEXT DEFAULT NULL,              -- 리뷰 내용 (선택 사항)
	`createDate`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, 
	`modifiedDate`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
	`status`         VARCHAR(20) DEFAULT '일반' NOT NULL -- 리뷰 상태 (기본값: 일반)
);
 
  
  CREATE TABLE `Dibs` (
	`userId`	VARCHAR(50) NOT NULL,  
	`address`	VARCHAR(250) NOT NULL,  
	`status`	TINYINT(1) DEFAULT 0
	)
-- 가게 찜하기 기능용 테이블
  
ALTER TABLE `Users`
ADD COLUMN `profileImageUrl` VARCHAR(255) NULL AFTER `review`;
--이미지 추가 유저테이블 수정  
   
  ALTER TABLE Dibs ADD UNIQUE KEY unique_user_address (userId, address);

  DROP TABLE `Dibs`;
  COMMIT; 
  
--------------------------------- 로그인 관련 테이블 생성 --------------------------------- 

SELECT userId, password  FROM Users  WHERE userId = "nono" AND PASSWORD = 1111  
SELECT userId, password  FROM Users  WHERE userId = {#userID}  AND password = {#userPW}
--로그인  매칭정보 찾기

SELECT r.roleNm 
	 FROM Users AS ur 
		INNER JOIN role AS r 
			   ON ur.status = r.roleNo 
--USER/ADIM/DEVELOPERE등의 유저와 관리자와 개발자 등급을 주었음.

 
	INSERT INTO `Users` (`userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`,`status`, `review`, `profileImageUrl`
	 ) VALUES (
	  'momo1', '1234', '우량미', '유민이', '01046527985', 'alscjf@example.com', NOW(), NOW(), 1, 'This is a sample review.', '/uploads/user1.png'
		);
	
		INSERT INTO `Users` (`userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`,`status`, `review`, `profileImageUrl`
	 ) VALUES (
	  'dddd', '1234', 'DEVELOPER', '유민이', '01046527985', 'alscjf@example.com', NOW(), NOW(), 3, 'This is a sample review.', 'user1.png'
		);
--회원가입 정보 입력하기

INSERT INTO `Reviews` (`address`, `userId`,  `content`, `status`)
VALUES ('서울특별시 강동구 성내동 556', 'momo1',  '12ㄴㄴ3123라.', '일반');
--리뷰테스트 인서트데이터
UPDATE Reviews SET STATUS = '신고' WHERE reviewNo =3 

SELECT COUNT(*) FROM Users WHERE userId = 'nono'
--회원가입시 아이디 중복 확인하기 위한것

			
			
 SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, 
			re.userId, re.content, re.modifiedDate, re.status 
			FROM test_freemeal AS tf 
			INNER JOIN Reviews AS re 
			ON tf.address = re.address 
			WHERE re.userId = 'momo'
			GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status 
			ORDER BY re.modifiedDate DESC;
			
			SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty,
			re.userId, re.content, re.modifiedDate,re.createDate, re.status 
			FROM test_freemeal AS tf 
			INNER JOIN Reviews AS re 
			ON tf.address = re.address 
			WHERE tf.address = '서울특별시 강동구 성내동 556' AND STATUS ='일반'
			GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status;
			
			SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, 
		 	re.reviewNo, re.userId, re.content, re.modifiedDate,re.createDate, re.status 
			 FROM test_freemeal AS tf 
		 	 INNER JOIN Reviews AS re 
			 ON tf.address = re.address 
			 	WHERE re.STATUS ='일반'
		 	GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status
			 ORDER BY re.modifiedDate DESC;
			
			
 SELECT `userId`, `password`, `user_Nnm`, `phone`, `email` FROM Users WHERE userId = 'momo'
 
 SELECT r.reviewNo, r.storeId, ur.userId, r.menuId, r.rating, r.content,r.createDate
	 FROM Users AS ur 
		INNER JOIN Reviews AS r 
			   ON ur.userId = 'momo'
--유저아이디를 기준하여 리뷰를 불러온다.
UPDATE Reviews 
	SET content = "웅냥냥", rating = 2, modifiedDate = NOW() 
			WHERE reviewNo = #{reviewNo}
--리뷰를 업데이트한다.
insert into Reviews 
		SET address = '서울특별시 강동구 성내동 556', userId='momo1', content = '123', createDate= NOW(), modifiedDate = NOW()			


 SELECT  tf.title,tf.link, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty 
			FROM test_freemeal AS tf
				GROUP BY tf.title, tf.address, tf.category
				
  INSERT INTO Dibs SET userId='dddd' , address ='서울특별시 강남구 청담동 79 Trinity 빌딩 지하1층' , STATUS = '1'
--  찜하기
SELECT * FROM Dibs 
WHERE userId='dddd' and address ='서울특별시 강남구 청담동 79 Trinity 빌딩 지하1층' and  STATUS = '1'

			SELECT * FROM users
			SELECT * FROM Reviews
			SELECT * FROM Dibs
			SELECT * FROM test_freemeal
			
SELECT COUNT(userId) AS COUNT 
FROM Dibs
WHERE STATUS = '1' AND address ='서울특별시 강남구 청담동 28-13' ;

SELECT tf.*, did.userId ,did.STATUS
FROM test_freemeal AS tf 
		 	 INNER JOIN Dibs AS did
		 	 	ON tf.address = did.address 
WHERE did.STATUS = '1' AND did.userId ='momo' ;

	SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, did.*
			 FROM test_freemeal AS tf 
		 	 INNER JOIN Dibs AS did 
			 ON tf.address = did.address  
			 WHERE did.STATUS = '1' AND did.userId ='momo'
		 	GROUP BY tf.title, tf.address, tf.category
			 


INSERT INTO Dibs (userId, address, status) VALUES ('dddd', '1', '0')  
	         ON DUPLICATE KEY UPDATE STATUS = '1'
 INSERT INTO Dibs SET userId='momo' , address ='서울특별시 강남구 청담동 28-13' , STATUS = '1'
