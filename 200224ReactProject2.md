## React 프로젝트 2

* 목표: 게시판작업
* 로그인 유저만 글 작성 가능
* 비로그인 유저도 글 목록 보기 가능

1. 로그인 유저만 글 작성 가능하도록 변경

   * cookie가 있을때만 글 작성창이 보이도록 수정
   * jquery import / state 값 설정 / render 조건문 설정 및 스타일 명 생성

   **home.jsx** import 추가

   ```jsx
   import $ from "jquery";
   import {} from "jquery.cookie";
   ```

   **home.jsx** component에 state값 추가

   ```jsx
     state = {
       writeStyle: "none"
     };
   
   ```

   **home.jsx** render값 수정

   ```jsx
     render() {
       const writeStyle = {
         display: this.state.writeStyle
       };
       if ($.cookie("login_email")) {
         writeStyle.display = "inline-block";
       }
   
       return (
         <div>
           <div style={writeStyle}>
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
   ```

   * home.jsx의 닉네임 부분을 제목으로 변경
   * _BoardNick 을 _BoardTitle로 변경
   * 서버쪽의 boardRouter도 title로 변경

   **Home.js** addBoard 함수 변경

   ```js
     addBoard = () => {
       const send_param = {
         headers,
         title: this._BoardTitle.value,
         comments: this._Comments.value,
         no: $.cookie("login_no")
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
   ```

   **boardRouter.js** insert 추가

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
     const no = req.body.no;
     const title = req.body.title;
     const comments = req.body.comments;
     var sql = `INSERT INTO board (m_no,title,content) VALUES (${no}, '${title}','${comments}')`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         console.log("1 record inserted");
         res.json({ message: "작성완료" });
       }
     });
   });
   ```

   

2. 게시판 보기 기능추가

   * get 방식으로 렌더링 보다 먼저 나오도록 변경

   **Home.jsx** ( state 부분 추가 / 함수 추가)

   ```jsx
     state = {
       writeStyle: "none",
       posts: []
     };
   
     componentWillMount = () => {
       axios
         .get("http://localhost:8080/board/view", { headers })
         .then(returnData => {
           
           this.setState({
             posts:returnData.data.posts
           })
         });
     };
   ```

   **Home.jsx** (render(){ 이 부분 추가})

   ```jsx
       const showPosts=this.state.posts.map((posts)=>{
       return (<div key={posts.createdAt}><h2>{posts.title}</h2><p>{posts.content}</p><p>작성자:{posts.name}</p></div>)
       })
       
       //리턴 부분 변경
               <div>
             {showPosts}
           </div>
   ```

   **boardRouter.js** (다음 부분 추가)

   ```js
   router.get("/view", (req, res) => {
     var sql = `SELECT * FROM members INNER JOIN board ON members.no = board.m_no ORDER BY createdAt DESC`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         res.json({ posts: result });
       }
     });
   });
   ```

3. 정리

   **boardRouter.js**

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
   
   router.get("/view", (req, res) => {
     var sql = `SELECT * FROM members INNER JOIN board ON members.no = board.m_no ORDER BY createdAt DESC`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         res.json({ posts: result });
       }
     });
   });
   
   router.post("/insert", (req, res) => {
     const no = req.body.no;
     const title = req.body.title;
     const comments = req.body.comments;
     var sql = `INSERT INTO board (m_no,title,content) VALUES (${no}, '${title}','${comments}')`;
     con.query(sql, function(err, result) {
       if (err) {
         console.log(err);
         res.json({ message: false });
       } else {
         console.log("1 record inserted");
         res.json({ message: "작성완료" });
         
       }
     });
   });
   
   module.exports = router;
   
   ```

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

   **Home.jsx**

   ```jsx
   import $ from "jquery";
   import {} from "jquery.cookie";
   import React, { Component } from "react";
   import axios from "axios";
   
   axios.defaults.withCredentials = true;
   const headers = { withCredentials: true };
   
   class Home extends Component {
     state = {
       writeStyle: "none",
       posts: []
     };
   
     componentWillMount = () => {
       axios
         .get("http://localhost:8080/board/view", { headers })
         .then(returnData => {
           
           this.setState({
             posts:returnData.data.posts
           })
         });
     };
   
     addBoard = () => {
       const send_param = {
         headers,
         title: this._BoardTitle.value,
         comments: this._Comments.value,
         no: $.cookie("login_no")
       };
   
       axios
         .post("http://localhost:8080/board/insert", send_param)
         .then(returnData => {
           alert(returnData.data.message);
           window.location.reload();
         })
         .catch(err => {
           console.log(err);
         });
   
       // alert(this._BoardNick.value);
     };
   
     render() {
       const writeStyle = {
         display: this.state.writeStyle
       };
       if ($.cookie("login_email")) {
         writeStyle.display = "inline-block";
       }
       const showPosts=this.state.posts.map((posts)=>{
       return (<div key={posts.createdAt}><h2>{posts.title}</h2><p>{posts.content}</p><p>작성자:{posts.name}</p></div>)
       })
   
       return (
         <div>
           <div style={writeStyle}>
             제목<input ref={ref => (this._BoardTitle = ref)}></input>
             내용
             <input type="text" ref={ref => (this._Comments = ref)}></input>
             <button onClick={this.addBoard}>글 게시</button>
           </div>
           <div>
             {showPosts}
           </div>
         </div>
       );
     }
   }
   
   export default Home;
   
   ```

   **Contact.jsx**

   ```js
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

   