## React 프로젝트

* Node js, React, SQL를 활용한 SNS 프로젝트 제작

1. 기본 세팅(클라이언트)

   1. cmd 창에서 입력: create-react-app 프로젝트폴더명

   2. public폴더 favicon.ico 빼고 다 제거

   3. src폴더 내 파일 모두 제거

   4. public/index.html 생성

      **index.html**

      ```html
      <!DOCTYPE html>
      <html lang="en">
        <head>
          <meta charset="UTF-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
          <title>SNS 시스템</title>
        </head>
        <body>
          <div id="container"></div>
        </body>
      </html>
      
      ```

      

   5. src폴더 내 index.jsx 생성

      **index.jsx**

      ```jsx
      import React from "react";
      import ReactDOM from "react-dom";
      import "./index.css";
      import MenuContainer from "./MenuContainer";
      
      ReactDOM.render(<MenuContainer />, document.querySelector("#container"));
      
      ```

      

      

   6. index.jsx 꾸미기 (임시)

      **index.css**

      ```css
      body {
        background-color: #7af67a;
        font-family: sans-serif;
        font-size: 20px;
        padding: 20px;
        margin: 0;
        overflow: auto;
      }
      
      #container li {
        margin-bottom: 10px;
      }
      
      h1,
      h2,
      p,
      ul,
      li {
        font-family: sans-serif;
      }
      
      ul.header li {
        display: inline;
        list-style-type: none;
        margin: 0;
      }
      
      ul.header {
        background-color: #111;
        padding: 0;
      }
      
      ul.header li a {
        color: #fff;
        font-weight: bold;
        text-decoration: none;
        padding: 20px;
        display: inline-block;
      }
      .content {
        background-color: #fff;
        padding: 20px;
      }
      .content h2 {
        padding: 0;
        margin: 0;
      }
      .content li {
        margin-bottom: 10px;
      }
      
      .active {
        background-color: #0099ff;
      }
      
      ```

      

   7. MenuContainer.jsx작성(네비게이션 역할)

      * component 임포트
      * npm i react-router-dom
      * react-router-dom 임포트
      * npm i axios

      **MenuContainer.jsx**

      ```jsx
      import React, { Component } from "react";
      import Home from "./Components/Home";
      import Contact from "./Components/Contact";
      import Login from "./Components/Login";
      import { Route, NavLink, HashRouter } from "react-router-dom";
      
      class MenuContainer extends Component {
        render() {
          return (
            <div>
              <HashRouter>
                <div>
                  <h1>목표달성 SNS</h1>
                  <ul className="header">
                    <li>
                      <NavLink exact to="/">
                        Home
                      </NavLink>
                    </li>
                    <li>
                      <NavLink to="/contact">회원가입</NavLink>
                    </li>
                    <li>
                      <NavLink to="/login">로그인</NavLink>
                    </li>
                  </ul>
                  <div className="content">
                    <Route exact path="/" component={Home}></Route>
                    <Route path="/contact" component={Contact}></Route>
                    <Route path="/login" component={Login}></Route>
                  </div>
                </div>
              </HashRouter>
            </div>
          );
        }
      }
      
      export default MenuContainer;
      
      ```

      

   8. Components 폴더 생성 후 각 Component 파일 생성

      **home.jsx** (게시판 쓰기 및 보기)

      ```jsx
      import React, { Component } from "react";
      import axios from "axios";
      
      axios.defaults.withCredentials = true;
      const headers = { withCredentials: true };
      
      class Home extends Component {
        state = [];
      
        addBoard = () => {
          const send_param = {
            headers,
            nick: this._BoardNick.value,
            comments: this._Comments.value
          };
      
          axios
            .post("http://localhost:8080/board/insert", send_param)
            .then(returnData => {
              alert(returnData.data.message);
            })
            .catch(err => {
              console.log(err);
            });
      
          // alert(this._BoardNick.value);
        };
      
        render() {
          return (
            <div>
              <div>
                닉네임<input ref={ref => (this._BoardNick = ref)}></input>
                내용
                <input type="text" ref={ref => (this._Comments = ref)}></input>
                <button onClick={this.addBoard}>글 게시</button>
              </div>
              <div>
                <h2>글제목</h2>
                <p>글내용~~</p>
              </div>
            </div>
          );
        }
      }
      
      export default Home;
      
      ```

      **Contact.jsx** (회원가입)

      ```jsx
      import React, { Component } from "react";
      import axios from 'axios';
      
      axios.defaults.withCredentials = true;
      const headers = { withCredentials: true };
      
      
      
      class Contact extends Component {
        state = {
          name: ""
        };
        memberInsert = () => {
          const send_param={
            headers,
            name:this.nameE.value,
            email:this.emailE_Contact.value,
            pw:this.pwE_Contact.value,
            comments:this.commentsE.value
          }
      
          axios.post("http://localhost:8080/member/insert",send_param)
          .then((returnData)=>{alert(returnData.data.message);})
          .catch((err)=>{console.log(err);})
      
      
      
        };
      
        render() {
          return (
            <div>
              <h2>Contact</h2>
              <p>회원가입</p>
              이름<input ref={ref => (this.nameE = ref)}></input>
              <br />
              이메일<input ref={ref => (this.emailE_Contact = ref)}></input>
              <br />
              비밀번호<input ref={ref => (this.pwE_Contact = ref)}></input>
              <br />
              comments<input ref={ref => (this.commentsE = ref)}></input>
              <br />
              <button onClick={this.memberInsert}>회원가입</button>
            </div>
          );
        }
      }
      
      export default Contact;
      
      ```

      **login.jsx** (로그인 창)

      ```jsx
      import React, { Component } from "react";
      import axios from "axios";
      
      axios.defaults.withCredentials = true;
      const headers = { withCredentials: true };
      class Login extends Component {
        state = {
          name: ""
        };
        memberLogin = () => {
         const send_param={
           headers,
          email:this.emailE_Login.value,
          pw:this.pwE_Login.value
        }
          axios.post("http://localhost:8080/member/login",send_param)
          .then((returnData)=>{alert(returnData.data.message)})
          .catch((err)=>{console.log(err);})
      
        };
      
        render() {
          return (
            <div>
              <h2>로그인</h2>
              이메일<input ref={ref => (this.emailE_Login = ref)}></input>
              <br />
              비밀번호<input ref={ref => (this.pwE_Login = ref)}></input>
              <br />
              <button onClick={this.memberLogin}>로그인</button>
            </div>
          );
        }
      }
      
      export default Login;
      
      ```

      

2. 기본세팅(서버)

   1. mysql port:3307 / database: nojs로 members 테이블 생성(name, email, pw, comments, )

   2. npm init // npm i express // package.jon에서 scripts에서 "start: nodemon server" 추가

   3. server.js 생성(포트 8080)

      * npm i cors
      * npm i express-session

      **server.js**

      ```js
      const memberRouter = require("./routes/memberRouter");
      const boardRouter = require("./routes/boardRouter");
      // 라우터 사용
      const express = require("express");
      const cors = require("cors");
      const session = require("express-session");
      
      const corsOptions = {
        origin: true,
        credentials: true
      };
      
      const app = express();
      app.use(cors(corsOptions));
      
      app.use(express.json());
      app.use(express.urlencoded({ extended: true }));
      
      app.use(
        session({
          resave: false,
          saveUninitialized: true,
          secret: "킹--민--철",
          cookie: {
            httpOnly: true,
            secure: false
          }
        })
      );
      
      app.use("/member", memberRouter);
      app.use("/board", boardRouter);
      //라우터명
      
      app.listen(8080, () => {
        console.log("8080 server ready..");
      });
      
      ```

      

   4. routes폴더 내 boardRouter.js와 memberRouter.js 생성

      **boardRouter.js**

      ```js
      const express = require("express");
      const router = express.Router();
      
      router.post("/insert", (req, res) => {
        const nick = req.body.nick;
        const comments = req.body.comments;
        res.json({ message: nick });
      });
      
      module.exports = router;
      
      ```

      **memberRouter.js**

      ```js
      const express = require("express");
      const router = express.Router();
      
      router.post("/login", (req, res) => {
          const email=req.body.email;
          const pw=req.body.pw
          res.json({message:email});
      
      });
      router.post("/insert", (req, res) => {
          const name=req.body.name;
          const email=req.body.email;
          const pw=req.body.pw;
          const comments=req.body.comments;
          res.json({message:name});
      });
      
      module.exports = router;
      
      ```

      

   5. 클라이언트와 서버 구동 후 회원가입, 로그인, 게시판 버튼 클릭시 alert 작동하는지 확인

3. 회원가입 프로세스 생성

   **Contact.jsx** axios.then() 부분을 다음과같이 수정

   ```jsx
   .then(returnData => {
           alert(returnData.data.message);
           window.location.href = "/login#/login";
         })
   ```

   **memberRouter.js** const추가 및 router.post를 다음과 같이 수정

   ```js
   const mysql= require("mysql");
   
   const con = mysql.createConnection({
     host: "localhost",
     user: "root",
     password: "mysql",
     database: "nodejs",
     port: "3307"
   });
   
   router.post("/insert", (req, res) => {
     const name = req.body.name;
     const email = req.body.email;
     const pw = req.body.pw;
     const comments = req.body.comments;
   
     var sql = `INSERT INTO members (name,email,pw,comments) VALUES ('${name}', '${email}','${pw}','${comments}')`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         console.log("1 record inserted");
         res.json({ message: name + "님 회원가입 환영합니다." });
       }
     });
   });
   ```

   

4. 로그인 프로세스 생성

   * 로그인 과정을 먼저 작성한 뒤, 로그아웃 프로세스를 변경하며 세션과 쿠키를 추가할 예정

   **memberRouter.js** select 쿼리 추가

   ```jsx
   router.post("/login", (req, res) => {
     const email = req.body.email;
     const pw = req.body.pw;
     var sql = `SELECT * FROM members WHERE email = '${email}' && pw = '${pw}'`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
           res.json({ message: result[0].name + "님 로그인 환영합니다."})
           
       }
     });
   });
   ```

   

   

5. 로그아웃 프로세스 생성

   * 작성 전 server.js에 session 필요
   * npm i express-session
   * server.js에 const session = require("express-session"); 추가

   **memberRouter.js** login 부분에 세션을 추가

   ```js
   router.post("/login", (req, res) => {
     const email = req.body.email;
     const pw = req.body.pw;
     var sql = `SELECT * FROM members WHERE email = '${email}' && pw = '${pw}'`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         if (result[0]) {
           req.session.no = result[0].no;
           req.session.email = result[0].email;
           req.session.name = result[0].name;
   
           console.log(email + "로그인");
   
           res.json({
             message: result[0].name + "님 로그인 환영합니다.",
             email: req.session.email,
             no: req.session.no
           });
         } else {
           console.log(err);
           res.json({ message: false });
         }
       }
     });
   });
   ```

   * **클라이언트 부분** jquery 설치 필요
   * npm i jquery // npm i jquery.cookie
   * Login.jsx 에서 다음 구문을 추가

   **Login.jsx**

   ```jsx
   import $ from "jquery";
   import {} from "jquery.cookie";
   ```

   

   **Login.jsx** memberLogin 부분 수정

   ```jsx
     memberLogin = () => {
       const send_param = {
         headers,
         email: this.emailE_Login.value,
         pw: this.pwE_Login.value
       };
       axios
         .post("http://localhost:8080/member/login", send_param)
         .then(returnData => {
           if (returnData.data.email) {
             $.cookie("login_email", returnData.data.email);
             $.cookie("login_no", returnData.data.no);
             alert(returnData.data.message);
             this.setState({
               login_email: returnData.data.email
             });
           } else {
             alert("로그인 실패");
           }
         })
         .catch(err => {
           console.log(err);
         });
     };
   ```

   **Login.jsx** memberLogout 부분 생성

   ```jsx
     memberLogout = () => {
       axios
         .get("http://localhost:8080/member/logout", { headers })
         .then(returnData => {
           if (returnData.data.message) {
             $.removeCookie("login_email");
             $.removeCookie("login_no");
             this.setState({
               login_email: "",
               loginStyle: "inline-block",
               logoutStyle: "none"
             });
           }
         });
     };
   ```

   **Loginjsx** render 이하 부분 로그인/로그아웃 추가

   ```jsx
       state = {
       name: "",
       loginStyle: "inline-block",
       logoutStyle: "none"
     };
   
   
   	render() {
       const loginStyle = {
         display: this.state.loginStyle
       };
       const logoutStyle = {
         display: this.state.logoutStyle
       };
       let login_email;
       if ($.cookie("login_email")) {
         login_email = $.cookie("login_email");
         loginStyle.display = "none";
         logoutStyle.display = "inline-block";
       }
   
       return (
         <div>
           <div style={loginStyle}>
             <h2>로그인</h2>
             이메일<input ref={ref => (this.emailE_Login = ref)}></input>
             <br />
             비밀번호<input ref={ref => (this.pwE_Login = ref)}></input>
             <br />
             <button onClick={this.memberLogin}>로그인</button>
           </div>
   
           <div style={logoutStyle}>
             {login_email}님 환영합니다.
             <button onClick={this.memberLogout}>로그아웃</button>
           </div>
         </div>
       );
     }
   ```

   * 서버쪽 세션 만료 부분 추가

   **memberRouter.js**

   ```js
   router.get("/logout", (req, res) => {
     req.session.destroy(() => {
       res.json({ message: "logout 되었습니다." });
     });
   });
   ```

   

6. 로그아웃 버튼 수정

   * MenuContainer.jsx 수정(쿠키 정보를 통해 로그인시 로그아웃 버튼으로 변경)
   * 로그아웃 버튼 클릭시 Home화면으로 넘어가면서 쿠키 제거하도록 변경

   **MenuContainer.jsx** 추가(로그아웃 함수를 복붙)

   ```jsx
     memberLogout = () => {
       axios
         .get("http://localhost:8080/member/logout", { headers })
         .then(returnData => {
           if (returnData.data.message) {
             $.removeCookie("login_email");
             $.removeCookie("login_no");
   
             this.setState({
               login_email: "",
               loginStyle: "inline-block",
               logoutStyle: "none"
             });
             window.location.reload();
           }
         });
     };
   ```

   **MenuContainer.jsx** render 이하에 변경

   ```jsx
     state = {
       loginStyle: "",
       logoutStyle: "none"
     };  
   
   render() {
       const loginStyle = {
         display: this.state.loginStyle
       };
       const logoutStyle = {
         display: this.state.logoutStyle
       };
       if ($.cookie("login_email")) {
         loginStyle.display = "none";
         logoutStyle.display = "show";
       }
   
       return (
         <div>
           <HashRouter>
             <div>
               <h1>목표달성 SNS</h1>
               <ul className="header">
                 <li>
                   <NavLink exact to="/">
                     Home
                   </NavLink>
                 </li>
                 <li>
                   <NavLink to="/contact">회원가입</NavLink>
                 </li>
                 <li style={loginStyle}>
                   <NavLink to="/login">로그인</NavLink>
                 </li>
                 <li style={logoutStyle}>
                   <NavLink exact to="/" onClick={this.memberLogout}>
                     로그아웃
                   </NavLink>
                 </li>
               </ul>
               <div className="content">
                 <Route exact path="/" component={Home}></Route>
                 <Route path="/contact" component={Contact}></Route>
                 <Route path="/login" component={Login}></Route>
               </div>
             </div>
           </HashRouter>
         </div>
       );
     }
   ```

   

7. 정리 및 확인(회원가입 / 로그인 /로그아웃 되는지)

   ### Client

   **MenuContainer.jsx**

   ```jsx
   import $ from "jquery";
   import {} from "jquery.cookie";
   import React, { Component } from "react";
   import Home from "./Components/Home";
   import Contact from "./Components/Contact";
   import Login from "./Components/Login";
   import { Route, NavLink, HashRouter } from "react-router-dom";
   import axios from "axios";
   
   axios.defaults.withCredentials = true;
   const headers = { withCredentials: true };
   
   class MenuContainer extends Component {
     state = {
       loginStyle: "",
       logoutStyle: "none"
     };
   
     memberLogout = () => {
       axios
         .get("http://localhost:8080/member/logout", { headers })
         .then(returnData => {
           if (returnData.data.message) {
             $.removeCookie("login_email");
             $.removeCookie("login_no");
   
             this.setState({
               login_email: "",
               loginStyle: "inline-block",
               logoutStyle: "none"
             });
             window.location.reload();
           }
         });
     };
   
     render() {
       const loginStyle = {
         display: this.state.loginStyle
       };
       const logoutStyle = {
         display: this.state.logoutStyle
       };
       if ($.cookie("login_email")) {
         loginStyle.display = "none";
         logoutStyle.display = "show";
       }
   
       return (
         <div>
           <HashRouter>
             <div>
               <h1>목표달성 SNS</h1>
               <ul className="header">
                 <li>
                   <NavLink exact to="/">
                     Home
                   </NavLink>
                 </li>
                 <li>
                   <NavLink to="/contact">회원가입</NavLink>
                 </li>
                 <li style={loginStyle}>
                   <NavLink to="/login">로그인</NavLink>
                 </li>
                 <li style={logoutStyle}>
                   <NavLink exact to="/" onClick={this.memberLogout}>
                     로그아웃
                   </NavLink>
                 </li>
               </ul>
               <div className="content">
                 <Route exact path="/" component={Home}></Route>
                 <Route path="/contact" component={Contact}></Route>
                 <Route path="/login" component={Login}></Route>
               </div>
             </div>
           </HashRouter>
         </div>
       );
     }
   }
   
   export default MenuContainer;
   
   ```

   **Login.jsx**

   ```jsx
   import $ from "jquery";
   import {} from "jquery.cookie";
   import React, { Component } from "react";
   import axios from "axios";
   
   axios.defaults.withCredentials = true;
   const headers = { withCredentials: true };
   class Login extends Component {
     state = {
       login_email: "",
       loginStyle: "inline-block",
       logoutStyle: "none"
     };
   
     memberLogout = () => {
       axios
         .get("http://localhost:8080/member/logout", { headers })
         .then(returnData => {
           if (returnData.data.message) {
             $.removeCookie("login_email");
             $.removeCookie("login_no");
   
             this.setState({
               login_email: "",
               loginStyle: "inline-block",
               logoutStyle: "none"
             });
             window.location.reload();
           }
         });
     };
   
     memberLogin = () => {
       const send_param = {
         headers,
         email: this.emailE_Login.value,
         pw: this.pwE_Login.value
       };
       axios
         .post("http://localhost:8080/member/login", send_param)
         .then(returnData => {
           if (returnData.data.email) {
             $.cookie("login_email", returnData.data.email);
             $.cookie("login_no", returnData.data.no);
             alert(returnData.data.message);
   
             this.setState({
               login_email: returnData.data.email
             });
             window.location.reload();
           } else {
             alert("로그인 실패");
           }
         })
         .catch(err => {
           console.log(err);
         });
     };
   
     render() {
       const loginStyle = {
         display: this.state.loginStyle
       };
       const logoutStyle = {
         display: this.state.logoutStyle
       };
       let login_email;
       if ($.cookie("login_email")) {
         login_email = $.cookie("login_email");
         loginStyle.display = "none";
         logoutStyle.display = "inline-block";
       }
   
       return (
         <div>
           <div style={loginStyle}>
             <h2>로그인</h2>
             이메일<input ref={ref => (this.emailE_Login = ref)}></input>
             <br />
             비밀번호<input ref={ref => (this.pwE_Login = ref)}></input>
             <br />
             <button onClick={this.memberLogin}>로그인</button>
           </div>
   
           <div style={logoutStyle}>
             {login_email}님 환영합니다.
             <button onClick={this.memberLogout}>로그아웃</button>
           </div>
         </div>
       );
     }
   }
   
   export default Login;
   
   ```

   **Contact.jsx**

   ```jsx
   import React, { Component } from "react";
   import axios from "axios";
   
   axios.defaults.withCredentials = true;
   const headers = { withCredentials: true };
   
   class Contact extends Component {
     state = {
       name: ""
     };
     memberInsert = () => {
       const send_param = {
         headers,
         name: this.nameE.value,
         email: this.emailE_Contact.value,
         pw: this.pwE_Contact.value,
         comments: this.commentsE.value
       };
   
       axios
         .post("http://localhost:8080/member/insert", send_param)
         .then(returnData => {
           alert(returnData.data.message);
           window.location.href = "/login#/login";
         })
         .catch(err => {
           console.log(err);
         });
     };
   
     render() {
       return (
         <div>
           <h2>Contact</h2>
           <p>회원가입</p>
           이름<input ref={ref => (this.nameE = ref)}></input>
           <br />
           이메일<input ref={ref => (this.emailE_Contact = ref)}></input>
           <br />
           비밀번호<input ref={ref => (this.pwE_Contact = ref)}></input>
           <br />
           comments<input ref={ref => (this.commentsE = ref)}></input>
           <br />
           <button onClick={this.memberInsert}>회원가입</button>
         </div>
       );
     }
   }
   
   export default Contact;
   
   ```

   ## Server

   **server.js**

   ```js
   const memberRouter = require("./routes/memberRouter");
   const boardRouter = require("./routes/boardRouter");
   const express = require("express");
   const cors = require("cors");
   const session = require("express-session");
   
   const corsOptions = {
     origin: true,
     credentials: true
   };
   
   const app = express();
   app.use(cors(corsOptions));
   
   app.use(express.json());
   app.use(express.urlencoded({ extended: true }));
   
   app.use(
     session({
       resave: false,
       saveUninitialized: true,
       secret: "킹--민--철",
       cookie: {
         httpOnly: true,
         secure: false
       }
     })
   );
   
   app.use("/member", memberRouter);
   app.use("/board", boardRouter);
   
   app.listen(8080, () => {
     console.log("8080 server ready..");
   });
   
   ```

   **boardRouter.js**

   ```js
   const express = require("express");
   const router = express.Router();
   
   router.post("/insert", (req, res) => {
     const nick = req.body.nick;
     const comments = req.body.comments;
     res.json({ message: nick });
   });
   
   module.exports = router;
   
   ```

   **memberRouter.js**

   ```js
   const mysql = require("mysql");
   const express = require("express");
   const router = express.Router();
   
   const con = mysql.createConnection({
     host: "localhost",
     user: "root",
     password: "mysql",
     database: "nodejs",
     port: "3307"
   });
   
   router.get("/logout", (req, res) => {
     req.session.destroy(() => {
       res.json({ message: "logout 되었습니다." });
     });
   });
   
   router.post("/login", (req, res) => {
     const email = req.body.email;
     const pw = req.body.pw;
     var sql = `SELECT * FROM members WHERE email = '${email}' && pw = '${pw}'`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         if (result[0]) {
           req.session.no = result[0].no;
           req.session.email = result[0].email;
           req.session.name = result[0].name;
   
           console.log(email + "로그인");
   
           res.json({
             message: result[0].name + "님 로그인 환영합니다.",
             email: req.session.email,
             no: req.session.no
           });
         } else {
           console.log(err);
           res.json({ message: false });
         }
       }
     });
   });
   router.post("/insert", (req, res) => {
     const name = req.body.name;
     const email = req.body.email;
     const pw = req.body.pw;
     const comments = req.body.comments;
   
     var sql = `INSERT INTO members (name,email,pw,comments) VALUES ('${name}', '${email}','${pw}','${comments}')`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         console.log("1 record inserted");
         res.json({ message: name + "님 회원가입 환영합니다." });
       }
     });
   });
   
   module.exports = router;
   
   ```

   