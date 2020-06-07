### Programming 기본 세팅

1. Visual Studio Code 설치

   1. https://code.visualstudio.com/ 링크를 통해 다운로드
   2. 실행후 Extensions탭 -> Prettier 설치
   3. File -> Preferences -> Settings -> Text Editor -> Formatting
   4. Format On Save 체크

2. GIT 설치

   1. https://git-scm.com/ 링크를 통해 다운로드
   2. Default로 설치
   3. Git Bash 실행
      * git config --global user.name "이름"
      * git config --global user.email "메일주소"
      * git config --list : 리스트 확인

   * code . : VSC 실행

3. Nodejs 설치

   1. https://nodejs.org/ko/download/ 링크를 통해 다운로드
   2. node -v 로 버전 확인 가능

4. npm 기본세팅

   1. 테스트 폴더 생성

   2. npm init -y

   3. npm i express

   4. app.js 생성

      ```js
      const express = require("express");
      
      const app = express();
      
      const server = app.listen(3000, () => {
        console.log("3000 port ready");
      });
      
      server;
      ```

      * node app 으로 실행

   5. package.json 수정

      1. scripts 부분 다음 명령어 추가
         * "start": "node app.js",
      2. npm start 명령어로 실행가능

   6. nodemon 설치

      1. npm i -g nodemon
      2.  nodemon -v로 버전 확인 가능

   7. package.json 재수정

      1. scripts 부분 다음 명령어로 변경
         * "start": "nodemon app.js",
      2. npm start 명령어로 실행 가능

   8. app.js 서버 및 정적 기본 설정

      1. 다음 명령어 추가

         ```js
         const path = require("path");
         
         app.use(express.static(path.join(__dirname, "public")));
         app.use(express.json());
         app.use(express.urlencoded({ extended: true }));
         ```

         

      2. app.js

         ```js
         const express = require("express");
         const path = require("path");
         
         const app = express();
         
         app.use(express.static(path.join(__dirname, "public")));
         app.use(express.json());
         app.use(express.urlencoded({ extended: true }));
         
         const server = app.listen(3000, () => {
           console.log("3000 port ready");
         });
         
         server;
         ```

   9. Java 세팅

      1. JDK 다운로드(Java SE Development Kit) 및 설치(Default 값)
      2. 환경변수 설정
         1. Jdk 설치폴더의 bin 폴더까지 경로 복사 (C:\Program Files\Java\jdk1.8.0_171\bin)
         2. 시스템->고급시스템설정->환경변수 클릭
         3. 시스템변수의 Path 부분을 클릭
         4. 새로만들기를 눌러 다음 경로를 붙여넣기
         5. 시스템 변수의 새로만들기 클릭
         6. 변수이름은 JAVA_HOME
         7. 변수 값은 복사한 경로의 **bin**폴더만 제거(C:\Program Files\Java\jdk1.8.0_171)
      3. 버전 확인
         1. cmd 창에서 다음 명령어 입력
            * java -version
      4. 이클립스 설치
         1. 일단 자바 사용이면 Java Developers 설치
         2. JPA와 같은 서버 이용 목적이면 Java EE(Enterprise) 설치

   10. 리액트 시작

       1. npx -v 로 확인
       2. 다음 명령어 입력
          * npx create-react-app [app 이름]
       3. npm start 로 시작

   11. 리액트 타입스크립트 시작

       1. 타입 스크립트 글로벌 설치
          * npm install -g typescript (또는 yarn global add typescript)
       2. 다음 명령어 입력
          * npx create-react-app [app 이름] --template typescript
       3. npm start로 시작

   12. aaa