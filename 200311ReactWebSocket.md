## 웹 소켓을 활용하여 실시간 채팅 구현하기

* WebSocket은 양방향 연결을 지속시켜주는 프로토콜
* 기존 요청을 받는 경우에만 응답을 해 페이지 또는 DOM을 바꾸는 HTTP, AJAX와 다르다.
* WebSocket은 요청이 없는 경우에도 응답을 가능하게 한다.
* WebSocket 은 브라우저마다 차이는 있지만 표준 프로토콜 개념
* 하지만 WebSocket은 Tomcat을 포함하여 지원되지 않는 서비스가 존재함
* Socket.io는 Node에서 제작된 모듈
* Socket.io는 브라우저에 상관없이 실시간 웹 구현이 가능
* WS만 사용하여 실시간 채팅 구현해보자

1. 순서

   1. server 부분 WS 설정
   2. client 부분 WS 설정

2. Server 부분 WS 설정

   1. **npm i ws** 를 통해  ws 설치 

   2. 서버가 열릴때 웹소켓부분이 움직이도록 설정을 해야한다.

   3. 그래서 server.js의 listen 부분을 웹소켓과 함께 실행되도록 변경

   4. server.js listen부분 수정과 socket.js라는 라우터 생성

      **server.js**

      ```js
      const server=app.listen(8080, () => {
        console.log("8080 server ready..");
      });
      
      const a = require("./socket");
      //라우터 설정
      a(server);
      //웹소켓이 실행될 라우터와 함께 실행
      ```

      **socket.js**

      ```js
      const WebSocket = require("ws");
      //ws를 받아옴
      
      socket=(server)=>{
          const wss = new WebSocket.Server({server:server});
          //server 부분에 새로운 웹소켓 서버 생성
      wss.on("connection", (ws, req)=>{
          const ip = req.headers["x-forwarded-for"] || req.connection.remoteAddress;
          console.log("새로운 클라이언트 접속" + ip);
      // 클라이언트 접속 시 발동   
          ws.on("message", message => {
              // 메시지를 받았을때 발동(send로 날라옴)
              wss.clients.forEach((client)=>{
                  if(client.readyState === WebSocket.OPEN){
                      client.send(message); //열려있는 클라이언트들에게 전송
      
                  }
              })
          })
          ws.on("close", ()=>{
              console.log("클라이언트 접속 종료:"+ip);
          })
          ws.on("error",(error)=>{
              console.log(error);
          })
      
      })
      
      }
      
      module.exports = socket;
      ```

      

3. Client 부분 WS 설정

   1. Chatting.jsx라는 새로운 파일 생성
   2. ID 값을 입력한 뒤 채팅시작 버튼을 누르면 웹소켓에 연결되도록 설정
   3. 메시지를 입력하고 엔터를 누르면 데이터를 서버로 보냄
   4. 웹소켓 연결 후 입력된 채팅내용이 보여지도록 설정(onmessage)

   **Chatting.jsx**

   ```jsx
   import React, { Component } from "react";
   
   class Chatting extends Component {
     render() {
       return (
         <div>
           <h2>실시간 채팅</h2>
           <input id="chatId" placeholder="아이디 입력" />
           <button onClick={this.startChat}>채팅 시작</button>
           <br /><br />
           <textarea row="10" cols="50" id="chatArea" ></textarea>
           <br />
           <input onKeyPress={this.sendMsg} id="sendMessage"></input>
         </div>
       );
     }
   }
   
   export default Chatting;
   
   ```

   * 모양 설정 후 테스트
   * 채팅시작 버튼 클릭시, 메시지 전송시 작동할 함수 제작

   **Chatting.jsx**

   ```jsx
     startChat = ()=>{
       webSocket = new WebSocket("ws://localhost:8080");
       // onopen: 소켓에 연결
       webSocket.onopen = function() {
         chatId = "[" + document.getElementById("chatId").value + "]";
         if (chatId) {
           alert("채팅 시작");
         } else {
           alert("채팅 아이디를 입력하세요");
         }
       };
       // onmessage: 메시지를 받았을때
       webSocket.onmessage = function(event) {
         console.log(event.data);
         document.getElementById("chatArea").value += event.data;
       };
     }
   
     sendMsg=(event)=>{
       console.log(event);
       if (event.key === "Enter") {
         console.log("서버로 전송시작");
         // send: 서버에 메시지를 보낼때
         webSocket.send(chatId + document.getElementById("sendMessage").value + "\n");
         document.getElementById("sendMessage").value = "";
       }
     }
   ```

   * 정리

     **Chatting.jsx**

     ```jsx
     import React, { Component } from "react";
     
     
     
     let webSocket, chatId;
     
     
     class Chatting extends Component {
       state = {
         name: ""
       };
     
       
       startChat = ()=>{
         webSocket = new WebSocket("ws://localhost:8080");
         // onopen: 소켓에 연결
         webSocket.onopen = function() {
           chatId = "[" + document.getElementById("chatId").value + "]";
           if (chatId) {
             alert("채팅 시작");
           } else {
             alert("채팅 아이디를 입력하세요");
           }
         };
         // onmessage: 메시지를 받았을때
         webSocket.onmessage = function(event) {
           console.log(event.data);
           document.getElementById("chatArea").value += event.data;
         };
       }
     
       sendMsg=(event)=>{
         console.log(event);
         if (event.key === "Enter") {
           console.log("서버로 전송시작");
           // send: 서버에 메시지를 보낼때
           webSocket.send(chatId + document.getElementById("sendMessage").value + "\n");
           document.getElementById("sendMessage").value = "";
         }
       }
     
     
       render() {
     
     
         return (
           <div>
             <h2>실시간 채팅</h2>
             <input id="chatId" placeholder="아이디 입력" />
             <button onClick={this.startChat}>채팅 시작</button>
             <br /><br />
             <textarea row="10" cols="50" id="chatArea" ></textarea>
             <br />
             <input onKeyPress={this.sendMsg} id="sendMessage"></input>
           </div>
         );
       }
     }
     
     export default Chatting;
     
     ```

     

