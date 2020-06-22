### Programming 기본 세팅

1. Visual Studio Code 설치

   1. https://code.visualstudio.com/ 링크를 통해 다운로드
   2. 실행후 Extensions탭 -> Prettier 설치
   3. File -> Preferences -> Settings -> Text Editor -> Formatting
   4. Format On Save 체크
   5. VS Code User Snippet 설정
      * <https://code.visualstudio.com/docs/editor/userdefinedsnippets>
      * File => preferences => User Snippets => 적용할 언어 선택

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

   ---

   
   
   1. Java 세팅
   
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
   
   2. 리액트 시작
   
   1. npx -v 로 확인
      2. 다음 명령어 입력
      * npx create-react-app [app 이름]
      3. npm start 로 시작
   
   3. 리액트 타입스크립트 시작
   
      1. 타입 스크립트 글로벌 설치
      * npm install -g typescript (또는 yarn global add typescript)
      2. 다음 명령어 입력
      * npx create-react-app [app 이름] --template typescript
   3. npm start로 시작
   
5. Vue-Cli 설치

   1. 구글에서 Vue cli 검색후 사이트에서 get start를 통해 확인
      
   2. Vue-cli 글로벌 설치
      
      * ```bash
       npm install -g @vue/cli
        # OR
       yarn global add @vue/cli
        ```
      
   3. Vue 버전 확인

         * ```bash
          vue --version
           ```

   4. 프로젝트 생성

         * vue create [프로젝트명]
           
      * 실행중 npm인지 yarn인지 선택해야 함

   5. VS code extension Vetur 설치

   6. 실행:

      * npm run server

   7. vue 파일 생성 단축키

      * <vue> 탭

   8. VueX 설치

         * cli에서 다음 명령어 입력
           * vue add vuex
         * git으로 임시저장이 필요한 경우 N을 입력 후 commit을 하고 재시작

   9. Vue router 설치

         1. vue add router
         2. Y 입력

6. FireBase

      1. FireBase 사이트 접속후, 회원가입, 로그인, 프로젝트 추가

      2. 프로젝트 추가 후 Database => firestore 생성

      3. test로 생성 후 +앱추가(웹)

      4. 다음과 같은 생성키 확인

         ```javascript
         var config = {
           apiKey: process.env.VUE_APP_FIREBASE_KEY,
           authDomain: "t4ir-blockchain-testprime.firebaseapp.com",
           databaseURL: "https://t4ir-blockchain-testprime.firebaseio.com",
           projectId: "t4ir-blockchain-testprime",
           storageBucket: "t4ir-blockchain-testprime.appspot.com",
           messagingSenderId: "350660345449",
           appId: "1:350660345449:web:e3bd9365e440cdc1306cf8",
           measurementId: "G-2M1FGCNFFQ",
         };
         
         ```

         

      5. 프로젝트 폴더의 firebase/init.js에 붙여넣기 후 firebase 설치

      6. npm i firebase

      7. import 및 export 설정

         ```javascript
         import firebase from "firebase";
         
         var config = {
           apiKey: process.env.VUE_APP_FIREBASE_KEY,
           authDomain: "t4ir-blockchain-testprime.firebaseapp.com",
           databaseURL: "https://t4ir-blockchain-testprime.firebaseio.com",
           projectId: "t4ir-blockchain-testprime",
           storageBucket: "t4ir-blockchain-testprime.appspot.com",
           messagingSenderId: "350660345449",
           appId: "1:350660345449:web:e3bd9365e440cdc1306cf8",
           measurementId: "G-2M1FGCNFFQ",
         };
         // Initialize Firebase
         const firebaseApp = firebase.initializeApp(config);
         
         export default firebaseApp.firestore();
         ```

         * 키 못찾으면 settings에서 확인

      8. 받은 db 활용 쿼리

         * ```javascript
           import db from "@/firebase/init.js";
           ```

         * ```javascript
            created() {
               // firebase 데이터베이스(firestore)에 요청 보내어
               //   db.collection("컬렉션 이름");
               db.collection("todos")
                 .get()
                 .then((snapshot) => {
                   snapshot.forEach((doc) => {
                     //   console.log(doc.id);
                     //   console.log(doc.data());
                     this.todos.push({
                       id: doc.id,
                       content: doc.data().content,
                       isCompleted: doc.data().isCompleted,
                     });
                   });
                 });
               // 데이터를 받아와
               // todos를 채워넣기
             },
           ```

7. Python 설치

   1. https://www.python.org/ 로들어가 windows용 다운로드를 받는다
   2. **설치창에서 Add Python 3.8 to PATH** 를 체크할것!
   3. python --version으로 버전 확인
   4. 파이썬 노트북(jupyter 사용)
      * pip list : pip 확인
      * 버전업이 필요할 경우 다음 명령어 입력
        * python -m pip install --upgrade pip
      * pip install jupyter
      * 노트북 실행
        * jupyter notebook
   * 단축키
        * 코드 실행: Ctrl + Enter
        * 아래 코드 추가: ESC + b

   1. ㄴㅇㄹㅇㄴ

