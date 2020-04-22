## HyperLedger

### 1. basic network + sacc 설치

1. 기본세팅

   * node -v (shemsms qksemtl 8.x.x 버전 이어야함, 9버전 이상은 안됨)

   * mkdir myweb

   * cd myweb

   * npm init

   * npm i express

   * npm i -g nodemon

   * mkdir public

   * public/ 에 index.html 파일 생성

     **index.html**

     ```html
     <!DOCTYPE html>
     <html lang="en">
       <head>
         <meta charset="utf-8" />
         <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" >
         <script src="https://cdnjs.cloudflare.com/ajax/libs/react/0.14.3/react.min.js"></script>
         <script src="https://cdnjs.cloudflare.com/ajax/libs/react/0.14.3/react-dom.min.js"></script>
         <script src="https://cdnjs.cloudflare.com/ajax/libs/react-router/1.0.2/ReactRouter.js"></script>
         <script src="https://unpkg.com/babel-standalone@6.26.0/babel.js"></script>
         <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
         <link rel="stylesheet" type="text/css" href="index.css">
         <title>React App</title>
       </head>
       <body>
        <div id="root" ></div>
        <script type="text/babel" src="index.jsx" />
       </body>
     </html>
     
     ```

     **index.jsx**

     ```jsx
     var {Component} = React;
     var {Router, Route, IndexRoute, Link} = ReactRouter;
      
     class Main extends Component{
         render(){
             return(            
                 <div>
                     <h1>Hyperledger Fabric Study</h1>
                     <ul className="header" >
                         <li><Link exact to="/">Home</Link></li>
                         <li><Link to="/basic">BasicNetwork</Link></li>
                         <li><Link to="/first">FirstNetwork</Link></li>
                     </ul>
                     <div className="content">
                     {this.props.children}
                     </div>
                 </div>            
             );
         }
     }
      
     class Home extends Component{
         render(){
             return(
                 <div>
                     <h2>HELLO</h2>
                     <p>안녕하세요? 하이퍼레저에 접속하는 노드 웹 예제입니다. 잘해보죠~!!!</p>
                 </div>
             );
         }
     }
     class BasicNetwork extends Component{
         basic_network=()=>{
             axios.get('/basic_network')
             .then((response)=>{
                 console.log(response);
                 
             })
             .catch((error)=>{
                 console.log(error);
             });
         }
         send=()=>{
             alert(this.amount.value);
             axios.post('/basic_network',{"amount":this.amount.value})
             .then((response)=>{
                 console.log(response);
                 
             })
             .catch((error)=>{
                 console.log(error);
             });
         }
      
         render(){
             return(
                 <div>
                     <h2>BasicNetwork</h2>
                     <p><button onClick={this.basic_network}>연결</button></p>
                     <br/>
                     <div>a가 b에게 {' '}
                     <input placeholder='송금량' ref={ref=>this.amount=ref} />원을 {' '} 
                     <button onClick={this.send}  > 보내기</button><br/>               
                     </div>
                 </div>
             );
         }
     }
     class FirstNetwork extends Component{  
      
         render(){
             return(
                 <div>
                     <h2>FirstNetwork에 연결 해보세요</h2>                
                 </div>
             );
         }
     }
      
     ReactDOM.render(
         (<Router>
             <Route path="/" component={Main} >   
                 <Route path="basic" component={BasicNetwork}/>
                 <Route path="first" component={FirstNetwork} />
                 <IndexRoute component={Home} />
             </Route>
         </Router>)
         ,document.getElementById("root")
     );
     
     ```

     **index.css**

     ```css
     body{
         background-color: #FFCC00;
         padding:20px;
         margin:0;
     }
     h1,h2,p,ul,li{
         font-family:sans-serif;
     }
     ul.header li{
         display: inline;
         list-style-type:none;
         margin:0;
     }
     ul.header{
         background-color: #111;
         padding: 0;
     }
     ul.header li a{
         color:#FFF;
         font-weight: bold;
         text-decoration: none;
         padding: 20px;
         display: inline-block;
     }
     .content{
         background-color: #FFF;
         padding: 20px;
     }
     .content h2{
         padding: 0;margin:0;
     }
     .content li{
         margin-bottom: 10px;
     }
     a:hover{
         background-color: #0099ff;
     }
     
     ```

     **myweb/server.js**

     ```js
     const express=require("express");
     const path=require("path");
     const app=express(); 
      
     app.use(express.static(path.join(__dirname,"/public")));
      
     app.use(express.json());
      
     app.use('/basic_network', require('./routes/basic_network_router'));
      
     app.listen(3000,function(){
         console.log("3000 server ready...");
     
     ```

     **myweb/routes/basic_network_router.js**

     ```js
     const express = require('express');
     const router = express.Router();
     
     /* GET */
     router.get('/', function(req, res, next) {
         res.json({"msg":"ok"});
       });
       
     module.exports = router;
     
     ```

     **package.json**에 스크립트 항목 추가

     ```js
     "scripts": {
         "start": "nodemon server.js",
     
     ```

   * npm start로 확인

   * package.json의 dependencies에 다음 항목 추가 후 npm i
     (만약 dependencies가 없으면 npm i fabric-...@버전명.. 으로 각각 설치하기)

     ```json
         "fabric-ca-client": "~1.4.0",
         "fabric-client": "~1.4.0",
         "fabric-network": "^1.4.4",
     
     ```

     

   

2. Ubuntu 세팅

   * apt -y install docker.io ( 만약 apt 잘 안되면 apt update 및 apt upgrade 할것)

   * docker -v (19.xx.xx)

   * apt -y install docker-compose (도커 컴포즈 설치)

   * docker-compose -v (1.17.1)

   * apt -y install goalng-go (go 설치)

   * go version (1.10.4)

   * nvm 설치 방법(4가지)

     * curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.34.0/install.sh | sudo bash
     * source .bashrc
     * chmod 777 .nvm (혹시 일반 계정으로 작업하다 권한 오류나면)
     *  nvm install 8.9.0
       * 만약 nvm의 설치가 안되면 아래의 명령어를 차례대로 입력
       * apt install npm
       * npm install n -g
       * n 8.9.0

   * node -v (8.9.0)

   * npm -v (5.5.1)

   * go env (go 위치 확인)

     …

     GOPATH="/root/go"

     GOROOT="/usr/lib/go-1.10"

   *  sudo apt install -y vim

   * vi ~/.vimrc

     * 아래 내용을 입력

     ```
     set number    " line 표시
     set ai    " auto indent
     set si " smart indent
     set cindent    " c style indent
     set shiftwidth=4    " 자동 공백 채움 시 4칸
     set tabstop=4    " tab을 4칸 공백으로
     set ignorecase    " 검색 시 대소문자 무시
     set hlsearch    " 검색 시 하이라이트
     set nocompatible    " 방향키로 이동 가능
     set fileencodings=utf-8,euc-kr    " 파일 저장 인코딩 : utf-8, euc-kr
     set fencs=ucs-bom,utf-8,euc-kr    " 한글 파일은 euc-kr, 유니코드는 유니코드
     set bs=indent,eol,start    " backspace 사용가능
     set ruler    " 상태 표시줄에 커서 위치 표시
     set title    " 제목 표시
     set showmatch    " 다른 코딩 프로그램처럼 매칭되는 괄호 보여줌
     set wmnu    " tab 을 눌렀을 때 자동완성 가능한 목록
     syntax on    " 문법 하이라이트 on
     filetype indent on    " 파일 종류에 따른 구문 강조
     set mouse=a    " 커서 이동을 마우스로 가능하도록
     
     ```

   * vi bashrc (go 환경변수 설정, 다음 내용을 위에 추가)

   ```
   export GOPATH="/root/go"
   export GOROOT="/usr/lib/go-1.10"
   
   ```

   

   * source .bashrc (go 환경변수 적용)

   * echo $GOROOT && echo $GOPATH (🡺적용되었는지 확인)

   * mkdir HLF

   * cd HLF

   * **curl -sSL http://bit.ly/2ysbOFE | bash -s 1.4.3 (**🡺****하이퍼레저 패브릭을 설치, 2019/8/21 1.4.2 버전 설치됨,****🡺****2019/8/31에 1.4.3버전 설치됨)**

   * docker images

   *  cd fabric-samples/basic-network 로 이동 후

   * vi start.sh (docker-compose 실행부분 수정)

     ```sh
     #!/bin/bash
     #
     # Copyright IBM Corp All Rights Reserved
     #
     # SPDX-License-Identifier: Apache-2.0
     #
     # Exit on first error, print all commands.
     set -ev
     
     # don't rewrite paths for Windows Git Bash users
     export MSYS_NO_PATHCONV=1
     
     docker-compose -f docker-compose.yml down
     
     docker-compose -f docker-compose.yml up -d ca.example.com orderer.example.com peer0.org1.example.com couchdb cli (🡸 요기에 이렇게 cli 추가)
     docker ps -a
     
     # wait for Hyperledger Fabric to start
     # incase of errors when running later commands, issue export FABRIC_START_TIMEOUT=<larger number>
     export FABRIC_START_TIMEOUT=10
     #echo ${FABRIC_START_TIMEOUT}
     sleep ${FABRIC_START_TIMEOUT}
     
     # Create the channel
     docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.example.com/msp" peer0.org1.example.com peer channel create -o orderer.example.com:7050 -c mychannel -f /etc/hyperledger/configtx/channel.tx
     # Join peer0.org1.example.com to the channel.
     docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.example.com/msp" peer0.org1.example.com peer channel join -b mychannel.block
     
     ```

     

   * ./start.sh로 실행

   * docker ps로 5개 실행되는지 확인

   

3. cli를 통해 peer의 체인코드를 설치, 연동하기

   1. docker exec -it cli bash 로 cli로 들어가기
   2. cli>peer chaincode install -n jes -v 1.0 -p github.com/sacc (sacc를 설치)
   3. cli>peer chaincode instantiate -n jes -v 1.0 -c '{"Args":["a","10"]}' -C mychannel 
      (jes 체인코드가 mychannel에 연결됨, 여기서 a에 10을 세팅함)
   4. **새 터미널로 peer에 들어가기**
   5.  docker exec -it peer0.org1.example.com bash
   6. peer0>cd /var/hyperledger/production/chaincodes/ 에서 jes 1.0있는지 확인
   7. peer0> cd /var/hyperledger/production/ledgerData/chains/chains/mychannel/ 여기에 블록파일 생성 확인

4. cli를 통해 chaincode로  a값을 보고, 변경하기

   1. cli에서 변동을 하기 (docker exec -it cli bash)
   2. cli>peer chaincode query -n jes -c '{"Args":["get","a"]}' -C mychannel (10을 확인)
   3. cli>peer chaincode invoke -n jes -c '{"Args":["set","a","20"]}' -C mychannel (20변경)
   4. cli>peer chaincode query -n jes -c '{"Args":["get","a"]}' -C mychannel (20 확인)

   * invoke 명령어를 쓰면 내용 업데이트가 가능함
     (업데이트 기록이 남으므로 블록파일 용량 변동이 생김
     peer0> cd /var/hyperledger/production/ledgerData/chains/chains/mychannel/)
   * query는 내용 보기만 함(업데이트X, 따라서 블록파일 용량 변동 X)