## React 프로젝트 3

* 목표: 디자인 수정 / DB 인젝션 위험 수정 / 추가 기능

1. 디자인 수정

   * npm i @material-ui/core

   * npm i --save @material-ui/icons

   * 웹폰트 수정

     * index.css 맨 위 추가 / body 클래스 부분 font family 추가

       ```.css
       @import url(http://fonts.googleapis.com/earlyaccess/notosanskr.css);
       
       
       font-family: "Noto Sans KR"
       ```

     * index.jsx 다음과 같이 수정

       ```jsx
       import React from "react";
       import ReactDOM from "react-dom";
       import "./index.css";
       import MenuContainer from "./MenuContainer";
       import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
       
       const theme = createMuiTheme({
           typography: {
               fontFamily: '"Noto Sans KR", serif'
           }
       })
       
       ReactDOM.render(<MuiThemeProvider theme={theme}><MenuContainer /></MuiThemeProvider>, document.querySelector("#container"));
       
       ```

       

   * 메뉴 바 모양 변경

   * Material v3 사이트로 가서 App Bar 활용(Component형을 쓰기 때문)

     * import 및 const styles 복붙
     * class 아래 render 부분 const{class} = this.props; 로 작성
     * return값 도 복붙 한 뒤 exports 부분을 아래와 같이 변경
     * export default withStyles(styles)(MenuContainer);

   * Router 부분 활용

     * 라우터 import 부분 NavLink 제거 후 Link as RouterLink 작성
     * NavLink는 디자인이 가능한 Link를 만들 때 쓰기 때문에 UI 활용시 활용도가 떨어짐
     * 링크를 걸고자 하는 부분에 component={RouterLink} to="/주소" 를 추가 (버튼형식도 가능)

   * 디자인 수정

     * import IconButton from '@material-ui/core/IconButton';를 작성하여 메뉴버튼 부분을 홈버튼으로 수정

     * 메뉴버튼 부분클릭시 홈으로 링크가 되도록 아래와 같이 수정

       ```jsx
       <IconButton className={classes.menuButton} color="inherit" aria-label="Menu" component={RouterLink} exact to="/">
                   <HomeIcon />
       ```

   * 기본 배경 변경

     * CssBaseline 을 import 하여 기본 배경 폼 작성

     * CssBaseline import 및 return의 AppBar 시작 부분에 해당 모듈 추가

       ```jsx
       import CssBaseline from '@material-ui/core/CssBaseline';
       <CssBaseline />
       
       <AppBar position="static">
               <CssBaseline />
               <Toolbar>
                   ...
       ```

       

   * 정리

     **MenuContainer.jsx**

     ```jsx
     import $ from "jquery";
     import {} from "jquery.cookie";
     import React, { Component } from "react";
     import Home from "./Components/Home";
     import Contact from "./Components/Contact";
     import Login from "./Components/Login";
     import { Route, HashRouter, Link as RouterLink } from "react-router-dom";
     import axios from "axios";
     
     import { withStyles } from '@material-ui/core/styles';
     import AppBar from '@material-ui/core/AppBar';
     import Toolbar from '@material-ui/core/Toolbar';
     import Typography from '@material-ui/core/Typography';
     import Button from '@material-ui/core/Button';
     import IconButton from '@material-ui/core/IconButton';
     import HomeIcon from '@material-ui/icons/Home';
     import CssBaseline from '@material-ui/core/CssBaseline';
     
     axios.defaults.withCredentials = true;
     const headers = { withCredentials: true };
     
     const styles = {
       root: {
         flexGrow: 1,
       },
       grow: {
         flexGrow: 1,
       },
       menuButton: {
         marginLeft: -12,
         marginRight: 20,
       },
     };
     
     
     
     
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
     
         const { classes } = this.props;
     
         return (
           <div>
                  
             <div className={classes.root}>
             <HashRouter>
           <AppBar position="static">
             <CssBaseline />
             <Toolbar>
               <IconButton className={classes.menuButton} color="inherit" aria-label="Menu" component={RouterLink} exact to="/">
                 <HomeIcon />
               </IconButton>
               
               <Typography variant="h6" color="inherit" className={classes.grow}>
                 목표달성 SNS
                 </Typography>
               <Button component={RouterLink} color="inherit" to="/contact">
                 회원가입
                 </Button>
               <Button component={RouterLink} color="inherit" to="/login" style={loginStyle}>
                 Login
                 </Button>
                 <Button component={RouterLink} color="inherit" to="/login" style={logoutStyle} onClick={this.memberLogout}>
                 Logout
                 </Button>
             </Toolbar>
           </AppBar>
           </HashRouter>
         </div>
             
             <HashRouter>
                 <div className="content">
                   <Route exact path="/" component={Home}></Route>
                   <Route path="/contact" component={Contact}></Route>
                   <Route path="/login" component={Login}></Route>
                 </div>
             </HashRouter>
           </div>
         );
       }
     }
     
     export default withStyles(styles)(MenuContainer);
     
     ```

   * 로그인 폼 수정

   * Material-ui의 예제를 수정하여 작성

     **login.jsx**

     ```jsx
     import $ from "jquery";
     import {} from "jquery.cookie";
     import React, { Component } from "react";
     import axios from "axios";
     
     import Avatar from '@material-ui/core/Avatar';
     import Button from '@material-ui/core/Button';
     import CssBaseline from '@material-ui/core/CssBaseline';
     import TextField from '@material-ui/core/TextField';
     import FormControlLabel from '@material-ui/core/FormControlLabel';
     import Checkbox from '@material-ui/core/Checkbox';
     import Link from '@material-ui/core/Link';
     import Grid from '@material-ui/core/Grid';
     import Box from '@material-ui/core/Box';
     import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
     import Typography from '@material-ui/core/Typography';
     import { withStyles } from '@material-ui/core/styles';
     import Container from '@material-ui/core/Container';
     
     
     axios.defaults.withCredentials = true;
     const headers = { withCredentials: true };
     
     
     const styles = theme => ({
       paper: {
         marginTop: theme.spacing(8),
         display: 'flex',
         flexDirection: 'column',
         alignItems: 'center',
       },
       avatar: {
         margin: theme.spacing(1),
         backgroundColor: theme.palette.secondary.main,
       },
       form: {
         width: '100%', // Fix IE 11 issue.
         marginTop: theme.spacing(1),
       },
       submit: {
         margin: theme.spacing(3, 0, 2),
       },
     });
     
     function Copyright() {
       return (
         <Typography variant="body2" color="textSecondary" align="center">
           {'Copyright © '}
           <Link color="inherit" href="https://material-ui.com/">
             Your Website
           </Link>{' '}
           {new Date().getFullYear()}
           {'.'}
         </Typography>
       );
     }
     
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
     
         const { classes} = this.props;
     
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
             <Container component="main" maxWidth="xs">
           <CssBaseline />
           <div className={classes.paper}>
             <Avatar className={classes.avatar}>
               <LockOutlinedIcon />
             </Avatar>
             <Typography component="h1" variant="h5">
               Sign in
             </Typography>
             <form className={classes.form} noValidate>
               <TextField
                 variant="outlined"
                 margin="normal"
                 required
                 fullWidth
                 id="email"
                 label="Email Address"
                 name="email"
                 autoComplete="email"
                 autoFocus
               />
               <TextField
                 variant="outlined"
                 margin="normal"
                 required
                 fullWidth
                 name="password"
                 label="Password"
                 type="password"
                 id="password"
                 autoComplete="current-password"
               />
               <FormControlLabel
                 control={<Checkbox value="remember" color="primary" />}
                 label="Remember me"
               />
               <Button
                 type="submit"
                 fullWidth
                 variant="contained"
                 color="primary"
                 className={classes.submit}
               >
                 Sign In
               </Button>
               <Grid container>
                 <Grid item xs>
                   <Link href="#" variant="body2">
                     Forgot password?
                   </Link>
                 </Grid>
                 <Grid item>
                   <Link href="#" variant="body2">
                     {"Don't have an account? Sign Up"}
                   </Link>
                 </Grid>
               </Grid>
             </form>
           </div>
           <Box mt={8}>
             <Copyright />
           </Box>
         </Container>
     
     
     
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
     
     export default withStyles(styles)(Login);
     
     ```

     

   * Login.jsx 수정

     **Login.jsx**

     ```jsx
     import $ from "jquery";
     import {} from "jquery.cookie";
     import React, { Component } from "react";
     import axios from "axios";
     
     import Avatar from '@material-ui/core/Avatar';
     import Button from '@material-ui/core/Button';
     import CssBaseline from '@material-ui/core/CssBaseline';
     import TextField from '@material-ui/core/TextField';
     import FormControlLabel from '@material-ui/core/FormControlLabel';
     import Checkbox from '@material-ui/core/Checkbox';
     import Link from '@material-ui/core/Link';
     import Grid from '@material-ui/core/Grid';
     import Box from '@material-ui/core/Box';
     import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
     import Typography from '@material-ui/core/Typography';
     import { withStyles } from '@material-ui/core/styles';
     import Container from '@material-ui/core/Container';
     
     axios.defaults.withCredentials = true;
     const headers = { withCredentials: true };
     
     function Copyright() {
       return (
         <Typography variant="body2" color="textSecondary" align="center">
           {'Copyright © '}
           <Link color="inherit" href="https://material-ui.com/">
             목표달성 SNS
           </Link>{' '}
           {new Date().getFullYear()}
           {'.'}
         </Typography>
       );
     }
     
     const styles = theme => ({
       paper: {
         marginTop: theme.spacing(8),
         display: 'flex',
         flexDirection: 'column',
         alignItems: 'center',
       },
       avatar: {
         margin: theme.spacing(1),
         backgroundColor: theme.palette.secondary.main,
       },
       form: {
         width: '100%', // Fix IE 11 issue.
         marginTop: theme.spacing(1),
       },
       submit: {
         margin: theme.spacing(3, 0, 2),
       },
     });
     
     
     class Login extends Component {
       state = {
         login_email: ""
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
               $.cookie("login_name", returnData.data.name);
               alert(returnData.data.message);
     
               this.setState({
                 login_email: returnData.data.email
               });
               window.location.href="/";
             } else {
               alert("로그인 실패");
             }
           })
           .catch(err => {
             console.log(err);
           });
       };
     
       render() {
     
         const {classes} = this.props;
     
     
         return (
           <div>
             <Container component="main" maxWidth="xs">
           <CssBaseline />
           <div className={classes.paper}>
             <Avatar className={classes.avatar}>
               <LockOutlinedIcon />
             </Avatar>
             <Typography component="h1" variant="h5">
               Sign in
             </Typography>
             <div className={classes.form} noValidate>
               <TextField
                 variant="outlined"
                 margin="normal"
                 required
                 fullWidth
                 id="email"
                 inputRef={ref => (this.emailE_Login = ref)}
                 label="Email Address"
                 name="email"
                 autoComplete="email"
                 autoFocus
               />
               <TextField
                 variant="outlined"
                 margin="normal"
                 required
                 fullWidth
                 name="password"
                 label="Password"
                 type="password"
                 id="password"
                 inputRef={ref => (this.pwE_Login = ref)}
                 autoComplete="current-password"
               />
              
               <FormControlLabel
                 control={<Checkbox value="remember" color="primary" />}
                 label="Remember me"
               />
               <Button
                 type="submit"
                 fullWidth
                 variant="contained"
                 color="primary"
                 className={classes.submit}
                 onClick={this.memberLogin}
               >
                 로그인
               </Button>
               <Grid container>
                 <Grid item xs>
                   <Link href="#" variant="body2">
                     Forgot password?
                   </Link>
                 </Grid>
                 <Grid item>
                   <Link href="#" variant="body2">
                     {"Don't have an account? Sign Up"}
                   </Link>
                 </Grid>
               </Grid>
             </div>
           </div>
           <Box mt={8}>
             <Copyright />
           </Box>
         </Container>
     
           </div>
         );
       }
     }
     
     export default withStyles(styles)(Login);
     
     ```
     
     
     
   * 회원가입 / 홈 부분 수정
   
     **Contact.jsx**
   
     ```jsx
     import React, { Component } from "react";
     import axios from "axios";
     
     import Avatar from '@material-ui/core/Avatar';
     import Button from '@material-ui/core/Button';
     import CssBaseline from '@material-ui/core/CssBaseline';
     import TextField from '@material-ui/core/TextField';
     import FormControlLabel from '@material-ui/core/FormControlLabel';
     import Checkbox from '@material-ui/core/Checkbox';
     import Link from '@material-ui/core/Link';
     import Grid from '@material-ui/core/Grid';
     import Box from '@material-ui/core/Box';
  import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
     import Typography from '@material-ui/core/Typography';
  import { withStyles } from '@material-ui/core/styles';
     import Container from '@material-ui/core/Container';
     
     axios.defaults.withCredentials = true;
     const headers = { withCredentials: true };
     
     function Copyright() {
       return (
         <Typography variant="body2" color="textSecondary" align="center">
           {'Copyright © '}
           <Link color="inherit" href="https://material-ui.com/">
             목표달성 SNS
           </Link>{' '}
           {new Date().getFullYear()}
           {'.'}
         </Typography>
       );
     }
     
     const styles = theme => ({
       paper: {
         marginTop: theme.spacing(8),
         display: 'flex',
         flexDirection: 'column',
         alignItems: 'center',
       },
       avatar: {
         margin: theme.spacing(1),
         backgroundColor: theme.palette.secondary.main,
       },
       form: {
         width: '100%', // Fix IE 11 issue.
         marginTop: theme.spacing(3),
       },
       submit: {
         margin: theme.spacing(3, 0, 2),
       },
     });
     
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
     
         const {classes} = this.props;
     
         return (
           <div>
             <Container component="main" maxWidth="xs">
           <CssBaseline />
           <div className={classes.paper}>
             <Avatar className={classes.avatar}>
               <LockOutlinedIcon />
             </Avatar>
             <Typography component="h1" variant="h5">
               회원가입
             </Typography>
             <div className={classes.form} noValidate>
               <Grid container spacing={2}>
                 <Grid item xs={12}>
                   <TextField
                     variant="outlined"
                     required
                     fullWidth
                     id="Name"
                     inputRef={ref => (this.nameE = ref)}
                     label="이름"
                     name="Name"
                     autoComplete="lname"
                   />
                 </Grid>
                 <Grid item xs={12}>
                   <TextField
                     variant="outlined"
                     required
                     fullWidth
                     id="email"
                     inputRef={ref => (this.emailE_Contact = ref)}
                     label="Email Address"
                     name="email"
                     autoComplete="email"
                   />
                 </Grid>
                 <Grid item xs={12}>
                   <TextField
                     variant="outlined"
                     required
                     fullWidth
                     name="password"
                     label="Password"
                     type="password"
                     id="password"
                     inputRef={ref => (this.pwE_Contact = ref)}
                     autoComplete="current-password"
                   />
                 </Grid>
                 <Grid item xs={12}>
                 <TextField
               id="outlined-multiline-static"
               inputRef={ref => (this.commentsE = ref)}
               label="하고싶은 말"
               fullWidth
               multiline
               rows="4"
               variant="outlined"
             />
             </Grid>
                 <Grid item xs={12}>
                   
                   <FormControlLabel
                     control={<Checkbox value="allowExtraEmails" color="primary" />}
                     label="정보 제공 이메일을 받겠습니다."
                   />
                 </Grid>
               </Grid>
               <Button
                 type="submit"
                 fullWidth
                 variant="contained"
                 color="primary"
                 className={classes.submit}
                 onClick={this.memberInsert}
               >
                 가입
               </Button>
               <Grid container justify="flex-end">
                 <Grid item>
                   <Link href="/#/login" variant="body2">
                     계정이 있으신가요? 지금 바로 로그인
                   </Link>
                 </Grid>
               </Grid>
             </div>
           </div>
           <Box mt={5}>
             <Copyright />
           </Box>
         </Container>
     
           </div>
         );
       }
     }
     
     export default withStyles(styles)(Contact);
     
     ```
   
     **Home.jsx**
   
     ```jsx
     import React, { Component } from "react";
     import axios from "axios";
     import $ from "jquery";
     import {} from "jquery.cookie";
     
     
     import Button from '@material-ui/core/Button';
     import Card from '@material-ui/core/Card';
     import CardActions from '@material-ui/core/CardActions';
     import CardContent from '@material-ui/core/CardContent';
     import CardMedia from '@material-ui/core/CardMedia';
     import CssBaseline from '@material-ui/core/CssBaseline';
     import Grid from '@material-ui/core/Grid';
     import Typography from '@material-ui/core/Typography';
     import { withStyles } from '@material-ui/core/styles';
     import Container from '@material-ui/core/Container';
     import TextField from '@material-ui/core/TextField';
     
     
     axios.defaults.withCredentials = true;
     const headers = { withCredentials: true };
     
     const styles = theme => ({
       icon: {
         marginRight: theme.spacing(2),
       },
       heroContent: {
         backgroundColor: theme.palette.background.paper,
         padding: theme.spacing(8, 0, 6),
       },
       heroButtons: {
         marginTop: theme.spacing(4),
       },
       cardGrid: {
         paddingTop: theme.spacing(8),
         paddingBottom: theme.spacing(8),
       },
       card: {
         height: '100%',
         display: 'flex',
         flexDirection: 'column',
       },
       cardMedia: {
         paddingTop: '56.25%', // 16:9
       },
       cardContent: {
         flexGrow: 1,
       }
     });
     
     
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
     
         const { classes} = this.props;
     
         const writeStyle = {
           display: this.state.writeStyle
         };
         if ($.cookie("login_email")) {
           writeStyle.display = "show";
         }
     
     
         return (
           <div>
              <React.Fragment>
           <CssBaseline />
             <div className={classes.heroContent}>
               <Container maxWidth="sm" >
               <Typography component="h1" variant="h2" align="center" color="textPrimary" gutterBottom style={writeStyle}>
               {$.cookie("login_name")}님 
               </Typography>
                 <Typography component="h1" variant="h2" align="center" color="textPrimary" gutterBottom>
                   당신의 목표는?
                 </Typography>
                 <div className={classes.heroButtons} style={writeStyle}>
                   <Grid container spacing={2} justify="center">
                     <Grid item>
                     <TextField
               id="standard-helperText"
               label="목표"
               inputRef={ref => (this._BoardTitle = ref)}
             />
                     </Grid>
                     <Grid item>
                     <TextField
               id="standard-helperText"
               label="내용"
               helperText="구체적으로 어떤 목표인가요"
               inputRef={ref => (this._Comments = ref)}
             />
                     </Grid>
                     <Grid item>
                       <Button variant="outlined" color="primary" onClick={this.addBoard}>
                         목표 게시
                       </Button>
                     </Grid>
                   </Grid>
                 </div>
               </Container>
             </div>
             <Container className={classes.cardGrid} maxWidth="md">
               {/* End hero unit */}
               <Grid container spacing={6}>
                 {this.state.posts.map(posts => (
                   <Grid item key={posts.b_no} xs={12} md={6}>
                     <Card className={classes.card}>
                       <CardMedia
                         className={classes.cardMedia}
                         image="https://source.unsplash.com/random"
                         title="Image title"
                       />
                       <CardContent className={classes.cardContent}>
                         <Typography gutterBottom variant="h5" component="h2">
                           {posts.title}
                         </Typography>
                         <Typography>
                          {posts.content}
                         </Typography>
                         <Typography>
                          작성자:{posts.name}
                         </Typography>
                       </CardContent>
                       <CardActions>
                         <Button size="small" color="primary">
                           View
                         </Button>
                         <Button size="small" color="primary">
                           Edit
                         </Button>
                       </CardActions>
                     </Card>
                   </Grid>
                 ))}
               </Grid>
             </Container>
         </React.Fragment>
           </div>
         );
       }
     }
     
     export default withStyles(styles)(Home);
     
     ```
   
     
   
   * 서버 부분 SQL문 수정
   
     **MemberRouter.js**
   
     ```js
     router.post("/insert", (req, res) => {
       const name = req.body.name;
       const email = req.body.email;
       const pw = req.body.pw;
       const comments = req.body.comments;
     
       var sql = `INSERT INTO members (name,email,pw,comments) VALUES (?, ?,?,?)`;
       con.query(sql, [name, email, pw, comments], function(err, result) {
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
   
     **boardRouter.js**
   
     ```js
     router.post("/insert", (req, res) => {
       const no = req.body.no;
       const title = req.body.title;
       const comments = req.body.comments;
       var sql = `INSERT INTO board (m_no,title,content) VALUES (?, ?,?)`;
       con.query(sql, [no, title, comments], function(err, result) {
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
   
     

