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

   9. sfsdf

   10. sdffsd