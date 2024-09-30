
USE test;

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
	`review`        TEXT NULL
);

CREATE TABLE `role` (
	roleNo		INT				NOT NULL AUTO_INCREMENT PRIMARY KEY,
	roleNm		VARCHAR(50) 	NOT NULL
);
  
  
  DROP TABLE `Users`;
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

INSERT INTO `Users` (
  `userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`,  `review`
) VALUES (
   'lonelone', '1111', '김인철', '유민이', '01046527985', 'alscjf@example.com', NOW(), NOW(),  'This is a sample review.'
);	
--회원가입 정보 입력하기

SELECT COUNT(*) FROM Users WHERE userId = 'nono'
--회원가입시 아이디 중복 확인하기 위한것

 SELECT `userId`, `password`, `user_Nnm`, `phone`, `email` FROM Users WHERE userId = 'momo'
			



