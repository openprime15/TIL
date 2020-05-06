### 4. Hyper Ledger 4

#### First Network + Fabcar + example02_node + MyWeb

* 기존 basic_network에서 하던 MyWeb을 이용한 쿼리 추가
* MyWeb 폴더에 있는 Wallet 폴더 삭제

1. 기본 세팅(Fabcar Docker가 실행되어있는 상태여야함)
   1. docker exec -it cli bash
      
      1. cli로 접속
      
   2. 모든 피어에 체인코드 인스톨
      * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer1.org1.example.com:8051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer1.org1.example.com/tls/ca.crt
      * peer chaincode install -n send_money -v 1.0 -l node -p /opt/gopath/src/github.com/chaincode/chaincode_example02/node
      * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer0.org2.example.com:9051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
      * peer chaincode install -n send_money -v 1.0 -l node -p /opt/gopath/src/github.com/chaincode/chaincode_example02/node
      * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer1.org2.example.com:10051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer1.org2.example.com/tls/ca.crt
      * peer chaincode install -n send_money -v 1.0 -l node -p /opt/gopath/src/github.com/chaincode/chaincode_example02/node
      
   3. first-network에 있는 connection-org1.json을 복사 / 붙여넣기 => first_articles/connection-org1.json

   4. fabcar/javascript 안에 있는 wallet 폴더를 복사 / 붙여넣기 => 폴더명을 wallet2로 변경

   5. routes/first_network_router.js를 작성

      **first_network_router.js**

      ```js
      const express = require('express');
      const router = express.Router();
      const fs = require('fs');
      const path = require('path');
      
      const FabricCAServices = require('fabric-ca-client');
      const {
        FileSystemWallet,
        X509WalletMixin,
        Gateway,
      } = require('fabric-network');
      
      const ccpPath = path.resolve(
        __dirname,
        '..',
        'first_articles',
        'connection-org1.json'
      );
      console.log(ccpPath);
      const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
      const ccp = JSON.parse(ccpJSON);
      
      // Create a new CA client for interacting with the CA.
      const caInfo = ccp.certificateAuthorities['ca.org1.example.com'];
      const caTLSCACerts = caInfo.tlsCACerts.pem;
      const ca = new FabricCAServices(
        caInfo.url,
        { trustedRoots: caTLSCACerts, verify: false },
        caInfo.caName
      );
      
      // Create a new file system based wallet for managing identities.
      const walletPath = path.join(process.cwd(), 'wallet2');
      const wallet = new FileSystemWallet(walletPath);
      console.log(`Wallet path: ${walletPath}`);
      
      /* GET */
      router.get('/connect', async (req, res, next) => {
        try {
          // Check to see if we've already enrolled the admin user.
          console.log(walletPath);
          const adminExists = await wallet.exists('admin');
          console.log(adminExists);
          if (!adminExists) {
            // Enroll the admin user, and import the new identity into the wallet.
            const enrollment = await ca.enroll({
              enrollmentID: 'admin',
              enrollmentSecret: 'adminpw',
            });
            const identity = X509WalletMixin.createIdentity(
              'Org1MSP',
              enrollment.certificate,
              enrollment.key.toBytes()
            );
            wallet.import('admin', identity);
            console.log(
              'Successfully enrolled admin user "admin" and imported it into the wallet'
            );
          }
      
          // Check to see if we've already enrolled the user.
          const userExists = await wallet.exists('user1');
          console.log(userExists);
          if (!userExists) {
            // Create a new gateway for connecting to our peer node.
            const gateway = new Gateway();
            await gateway.connect(ccp, {
              wallet,
              identity: 'admin',
              discovery: { enabled: false },
            });
      
            // Get the CA client object from the gateway for interacting with the CA.
            const ca = gateway.getClient().getCertificateAuthority();
            const adminIdentity = gateway.getCurrentIdentity();
      
            // Register the user, enroll the user, and import the new identity into the wallet.
            const secret = await ca.register(
              {
                affiliation: 'org1.department1',
                enrollmentID: 'user1',
                role: 'client',
              },
              adminIdentity
            );
            const enrollment = await ca.enroll({
              enrollmentID: 'user1',
              enrollmentSecret: secret,
            });
            const userIdentity = X509WalletMixin.createIdentity(
              'Org1MSP',
              enrollment.certificate,
              enrollment.key.toBytes()
            );
            wallet.import('user1', userIdentity);
            console.log(
              'Successfully registered and enrolled admin user "user1" and imported it into the wallet'
            );
          }
      
          res.json({ msg: 'ok' });
        } catch (e) {
          console.log(e);
          res.json({ msg: 'connect error' });
        }
      });
      
      router.get('/query', async (req, res, next) => {
        try {
          // Create a new file system based wallet for managing identities.
          const walletPath = path.join(process.cwd(), 'wallet2');
          const wallet = new FileSystemWallet(walletPath);
          console.log(`Wallet path: ${walletPath}`);
      
          // Check to see if we've already enrolled the user.
          const userExists = await wallet.exists('user1');
          if (!userExists) {
            console.log(
              'An identity for the user "user1" does not exist in the wallet'
            );
            console.log('Run the registerUser.js application before retrying');
            return;
          }
      
          // Create a new gateway for connecting to our peer node.
          const gateway = new Gateway();
          await gateway.connect(ccpPath, {
            wallet,
            identity: 'user1',
            discovery: { enabled: true, asLocalhost: true },
          });
      
          // Get the network (channel) our contract is deployed to.
          const network = await gateway.getNetwork('mychannel');
      
          // Get the contract from the network.
          const contract = network.getContract('send_money');
      
          // Evaluate the specified transaction.
          const a_result = await contract.evaluateTransaction('getBalance', 'a');
          const b_result = await contract.evaluateTransaction('getBalance', 'b');
      
          console.log(
            `Transaction has been evaluated, result is: ${a_result.toString()} , ${b_result.toString()}`
          );
          res.json({ a_amount: a_result.toString(), b_amount: b_result.toString() });
        } catch (e) {
          console.log(e);
          res.json({ msg: 'query error' });
        }
      });
      
      /* POST */
      router.post('/send', async (req, res, next) => {
        try {
          // Create a new file system based wallet for managing identities.
          const walletPath = path.join(process.cwd(), 'wallet2');
          const wallet = new FileSystemWallet(walletPath);
          console.log(`Wallet path: ${walletPath}`);
      
          // Check to see if we've already enrolled the user.
          const userExists = await wallet.exists('user1');
          if (!userExists) {
            console.log(
              'An identity for the user "user1" does not exist in the wallet'
            );
            console.log('Run the registerUser.js application before retrying');
            return;
          }
      
          // Create a new gateway for connecting to our peer node.
          const gateway = new Gateway();
          await gateway.connect(ccpPath, {
            wallet,
            identity: 'user1',
            discovery: { enabled: true, asLocalhost: true },
          });
      
          // Get the network (channel) our contract is deployed to.
          const network = await gateway.getNetwork('mychannel');
      
          // Get the contract from the network.
          const contract = network.getContract('send_money');
      
          // Submit the specified transaction.
          await contract.submitTransaction('invoke', 'a', 'b', '1');
          console.log('Transaction has been submitted');
      
          // Disconnect from the gateway.
          await gateway.disconnect();
      
          console.log(`Transaction has been submitted`);
          res.json({ msg: 'ok' });
        } catch (e) {
          console.log(e);
          res.json({ msg: 'send error' });
        }
      });
      
      module.exports = router;
      
      ```

   6. server.js / index.jsx 변경

      **server.js**

      ```js
      const express=require("express");
      const path=require("path");
      const app=express(); 
       
      app.use(express.static(path.join(__dirname,"/public")));
       
      app.use(express.json());
      const basic_network_router=require('./routes/basic_network_router');
      app.use('/basic_network', basic_network_router);
      const first_network_router=require('./routes/first_network_router');
      app.use('/first_network', first_network_router);
       
      app.listen(3000,function(){
          console.log("3000 server ready...");
      });
      
      ```

      **index.jsx**

      ```jsx
      const {Component}=React;
      const {Router,Route,IndexRoute,Link}=ReactRouter;
       
      class Main extends Component{
          render(){
              return(
                  <div>
                      <h1>Hyperledger Fabric Study</h1>
                      <ul className="header">
                          <li><Link exact to="/">Home</Link></li>
                          <li><Link to="/basic">BasicNetwork</Link></li>
                          <li><Link to="/first">FirstNetwork</Link></li>
                      </ul>
                      
                      
                      <div>
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
                      <h2>Home</h2>
                  </div>
              );
          }
      }
      class BasicNetwork extends Component{
          state={
              a_amount:0,
              b_amount:0
          }
       
          basic_network_connect=()=>{
              axios.get('basic_network/connect')
              .then((res)=>{
                  console.log(res);
              })
              .catch((error)=>{
                  console.log(error);
              });
          }
       
          query=()=>{        
              axios.get('/basic_network/query')
              .then((response)=>{            
                  this.setState({a_amount:response.data.a_amount, b_amount:response.data.b_amount});
              })
              .catch((error)=>{
                  console.log(error);
              });
          }
       
          send=()=>{
              alert(this.amount.value);
              axios.post('/basic_network/send',{"amount":this.amount.value})
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
                      <h2>BasicNetwork
                      에 <button onClick={this.basic_network_connect}>연결</button></h2>
                      <br/>
                      <button onClick={this.query}  > 잔액 확인</button> {' '} 
                      a : <Amount bgColor='green' amount={this.state.a_amount}></Amount>
                      b : <Amount bgColor='red' amount={this.state.b_amount}></Amount>
                      <br/>               
                      <br/> 
                      <div>a가 b에게 {' '}
                      <input placeholder='송금량' ref={ref=>this.amount=ref} />원을 {' '} 
                      <button onClick={this.send}  > 보내기</button><br/>               
                      </div>
       
                  </div>
              );
          }
      }
      class Amount extends Component{
          render(){
              var amountStyle={
                  padding:10,
                  margin:20,
                  display:"inline-block",
                  backgroundColor: this.props.bgColor,
                  borderRadius: "50%" ,
                  width : this.props.amount,
                  height: this.props.amount,            
                  textAlign: "center"            
              }
              return (
                  <span style={amountStyle}>{this.props.amount}원</span>
              );
          }
      }
       
      class FirstNetwork extends Component{
          state={
              a_amount:0,
              b_amount:0
          }
       
          first_network_connect=()=>{
              axios.get('first_network/connect')
              .then((res)=>{
                  console.log(res);
              })
              .catch((error)=>{
                  console.log(error);
              });
          }
       
          query=()=>{        
              axios.get('/first_network/query')
              .then((response)=>{            
                  this.setState({a_amount:response.data.a_amount, b_amount:response.data.b_amount});
              })
              .catch((error)=>{
                  console.log(error);
              });
          }
       
          send=()=>{
              alert(this.amount.value);
              axios.post('/first_network/send',{"amount":this.amount.value})
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
                      <h2>FirstNetwork
                      에 <button onClick={this.first_network_connect}>연결</button></h2>
                      <br/>
                      <button onClick={this.query}  > 잔액 확인</button> {' '} 
                      a : <Amount bgColor='magenta' amount={this.state.a_amount}></Amount>
                      b : <Amount bgColor='cyan' amount={this.state.b_amount}></Amount>
                      <br/>               
                      <br/> 
                      <div>a가 b에게 {' '}
                      <input placeholder='송금량' ref={ref=>this.amount=ref} />원을 {' '} 
                      <button onClick={this.send}  > 보내기</button><br/>               
                      </div>
       
                  </div>
              );
          }
      }
       
      ReactDOM.render(
          (<Router>
              <Route path="/" component={Main} >
                  <IndexRoute component={Home} />
                  <Route path="basic" component={BasicNetwork} />
                  <Route path="first" component={FirstNetwork} />
              </Route>
          </Router>)
           , document.getElementById("root")
      );
      
      ```

      

2.  npm start로 확인

   1. (연결) / (잔액확인)
      * 잔액확인이 안되면 router 쿼리문 확인 (getBalance => query 변경 여부)



#### Fabcar도 MyWeb에 추가

1. Myweb/server.js

   1. server.js

      **server.js**

      ```js
      const express = require('express');
      const path = require('path');
      const app = express();
      
      app.use(express.static(path.join(__dirname, '/public')));
      
      app.use(express.json());
      const basic_network_router = require('./routes/basic_network_router');
      app.use('/basic_network', basic_network_router);
      const first_network_router = require('./routes/first_network_router');
      app.use('/first_network', first_network_router);
      const fabcar_router = require('./routes/fabcar_router');
      app.use('/fabcar_network', fabcar_router);
      
      app.listen(3000, function () {
        console.log('3000 server ready...');
      });
      
      ```

      

2. Myweb/routes/fabcar_router.js

   ```js
   const express = require('express');
   const router = express.Router();
   const fs = require('fs');
   const path = require('path');
   
   const FabricCAServices = require('fabric-ca-client');
   const {
     FileSystemWallet,
     X509WalletMixin,
     Gateway,
   } = require('fabric-network');
   
   const ccpPath = path.resolve(
     __dirname,
     '..',
     'first_articles',
     'connection-org1.json'
   );
   const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
   const ccp = JSON.parse(ccpJSON);
   
   // Create a new CA client for interacting with the CA.
   const caInfo = ccp.certificateAuthorities['ca.org1.example.com'];
   const caTLSCACerts = caInfo.tlsCACerts.pem;
   const ca = new FabricCAServices(
     caInfo.url,
     { trustedRoots: caTLSCACerts, verify: false },
     caInfo.caName
   );
   
   // Create a new file system based wallet for managing identities.
   const walletPath = path.join(process.cwd(), 'wallet2');
   const wallet = new FileSystemWallet(walletPath);
   console.log(`Wallet path: ${walletPath}`);
   
   /* GET */
   router.get('/get_all_car', async (req, res, next) => {
     try {
       // Check to see if we've already enrolled the user.
       const userExists = await wallet.exists('user1');
       if (!userExists) {
         console.log(
           'An identity for the user "user1" does not exist in the wallet'
         );
         console.log('Run the registerUser.js application before retrying');
         return;
       }
   
       // Create a new gateway for connecting to our peer node.
       const gateway = new Gateway();
       await gateway.connect(ccpPath, {
         wallet,
         identity: 'user1',
         discovery: { enabled: true, asLocalhost: true },
       });
   
       // Get the network (channel) our contract is deployed to.
       const network = await gateway.getNetwork('mychannel');
   
       // Get the contract from the network.
       const contract = network.getContract('fabcar');
   
       // Evaluate the specified transaction.
       const result = await contract.evaluateTransaction('queryAllCars');
       console.log(
         `Transaction has been evaluated, result is: ${result.toString()} `
       );
       res.json({ queryAllCars: result.toString() });
     } catch (e) {
       console.log(e);
       res.json({ msg: 'query error' });
     }
   });
   
   /* post */
   router.post('/get_car', async (req, res, next) => {
     try {
       // Check to see if we've already enrolled the user.
       const userExists = await wallet.exists('user1');
       if (!userExists) {
         console.log(
           'An identity for the user "user1" does not exist in the wallet'
         );
         console.log('Run the registerUser.js application before retrying');
         return;
       }
   
       // Create a new gateway for connecting to our peer node.
       const gateway = new Gateway();
       await gateway.connect(ccpPath, {
         wallet,
         identity: 'user1',
         discovery: { enabled: true, asLocalhost: true },
       });
   
       // Get the network (channel) our contract is deployed to.
       const network = await gateway.getNetwork('mychannel');
   
       // Get the contract from the network.
       const contract = network.getContract('fabcar');
   
       // Evaluate the specified transaction.
       const result = await contract.evaluateTransaction(
         'queryCar',
         `${req.body.queryName}`
       );
       console.log(
         `Transaction has been evaluated, result is: ${result.toString()} `
       );
       res.json({ queryCar: result.toString() });
     } catch (e) {
       console.log(e);
       res.json({ msg: 'query error' });
     }
   });
   
   /* post */
   router.post('/add_car', async (req, res, next) => {
     try {
       // Check to see if we've already enrolled the user.
       const userExists = await wallet.exists('user1');
       if (!userExists) {
         console.log(
           'An identity for the user "user1" does not exist in the wallet'
         );
         console.log('Run the registerUser.js application before retrying');
         return;
       }
   
       // Create a new gateway for connecting to our peer node.
       const gateway = new Gateway();
       await gateway.connect(ccpPath, {
         wallet,
         identity: 'user1',
         discovery: { enabled: true, asLocalhost: true },
       });
   
       // Get the network (channel) our contract is deployed to.
       const network = await gateway.getNetwork('mychannel');
   
       // Get the contract from the network.
       const contract = network.getContract('fabcar');
   
       // Evaluate the specified transaction.
       await contract.submitTransaction(
         'createCar',
         `${req.body.car_id}`,
         `${req.body.car_make}`,
         `${req.body.car_model}`,
         `${req.body.car_colour}`,
         `${req.body.car_owner}`
       );
       console.log(`Transaction has been evaluated, result is ok`);
       res.json({
         code: '1',
         msg: `${req.body.car_id}가 정상적으로 입력되었습니다`,
       });
     } catch (e) {
       console.log(e);
       res.json({ code: '0', msg: `${req.body.car_id} 입력 오류` });
     }
   });
   
   /* post */
   router.post('/change_owner', async (req, res, next) => {
     try {
       // Check to see if we've already enrolled the user.
       const userExists = await wallet.exists('user1');
       if (!userExists) {
         console.log(
           'An identity for the user "user1" does not exist in the wallet'
         );
         console.log('Run the registerUser.js application before retrying');
         return;
       }
   
       // Create a new gateway for connecting to our peer node.
       const gateway = new Gateway();
       await gateway.connect(ccpPath, {
         wallet,
         identity: 'user1',
         discovery: { enabled: true, asLocalhost: true },
       });
   
       // Get the network (channel) our contract is deployed to.
       const network = await gateway.getNetwork('mychannel');
   
       // Get the contract from the network.
       const contract = network.getContract('fabcar');
   
       // Evaluate the specified transaction
       await contract.submitTransaction(
         'changeCarOwner',
         `${req.body.car_id}`,
         `${req.body.car_owner}`
       );
       console.log(`Transaction has been evaluated, result is ok`);
       res.json({
         code: '1',
         msg: `${req.body.car_id}가 정상적으로 변경되었습니다`,
       });
     } catch (e) {
       console.log(e);
       res.json({ code: '0', msg: `${req.body.car_id} 변경 오류` });
     }
   });
   
   module.exports = router;
   
   ```

3. Myweb/public/index.jsx

   **index.jsx**

   ```jsx
   const { Component } = React;
   const { Router, Route, IndexRoute, Link } = ReactRouter;
   
   class Main extends Component {
     render() {
       return (
         <div>
           <h1>Hyperledger Fabric Study</h1>
           <ul className="header">
             <li>
               <Link exact to="/">
                 Home
               </Link>
             </li>
             <li>
               <Link to="/basic">BasicNetwork</Link>
             </li>
             <li>
               <Link to="/first">FirstNetwork</Link>
             </li>
             <li>
               <Link to="/fabcar">Fabcar</Link>
             </li>
           </ul>
   
           <div>{this.props.children}</div>
         </div>
       );
     }
   }
   
   class Fabcar extends Component {
     render() {
       return (
         <div>
           <a href="fabcar.html"> 중고차 시장 가기 </a>
         </div>
       );
     }
   }
   
   class Home extends Component {
     render() {
       return (
         <div>
           <h2>Home</h2>
         </div>
       );
     }
   }
   class BasicNetwork extends Component {
     state = {
       a_amount: 0,
       b_amount: 0,
     };
   
     basic_network_connect = () => {
       axios
         .get('basic_network/connect')
         .then((res) => {
           console.log(res);
         })
         .catch((error) => {
           console.log(error);
         });
     };
   
     query = () => {
       axios
         .get('/basic_network/query')
         .then((response) => {
           this.setState({
             a_amount: response.data.a_amount,
             b_amount: response.data.b_amount,
           });
         })
         .catch((error) => {
           console.log(error);
         });
     };
   
     send = () => {
       alert(this.amount.value);
       axios
         .post('/basic_network/send', { amount: this.amount.value })
         .then((response) => {
           console.log(response);
         })
         .catch((error) => {
           console.log(error);
         });
     };
   
     render() {
       return (
         <div>
           <h2>
             BasicNetwork 에{' '}
             <button onClick={this.basic_network_connect}>연결</button>
           </h2>
           <br />
           <button onClick={this.query}> 잔액 확인</button> a :{' '}
           <Amount bgColor="green" amount={this.state.a_amount}></Amount>b :{' '}
           <Amount bgColor="red" amount={this.state.b_amount}></Amount>
           <br />
           <br />
           <div>
             a가 b에게{' '}
             <input placeholder="송금량" ref={(ref) => (this.amount = ref)} />
             원을 <button onClick={this.send}> 보내기</button>
             <br />
           </div>
         </div>
       );
     }
   }
   class Amount extends Component {
     render() {
       var amountStyle = {
         padding: 10,
         margin: 20,
         display: 'inline-block',
         backgroundColor: this.props.bgColor,
         borderRadius: '50%',
         width: this.props.amount,
         height: this.props.amount,
         textAlign: 'center',
       };
       return <span style={amountStyle}>{this.props.amount}원</span>;
     }
   }
   
   class FirstNetwork extends Component {
     state = {
       a_amount: 0,
       b_amount: 0,
     };
   
     first_network_connect = () => {
       axios
         .get('first_network/connect')
         .then((res) => {
           console.log(res);
         })
         .catch((error) => {
           console.log(error);
         });
     };
   
     query = () => {
       axios
         .get('/first_network/query')
         .then((response) => {
           this.setState({
             a_amount: response.data.a_amount,
             b_amount: response.data.b_amount,
           });
         })
         .catch((error) => {
           console.log(error);
         });
     };
   
     send = () => {
       alert(this.amount.value);
       axios
         .post('/first_network/send', { amount: this.amount.value })
         .then((response) => {
           console.log(response);
         })
         .catch((error) => {
           console.log(error);
         });
     };
   
     render() {
       return (
         <div>
           <h2>
             FirstNetwork 에{' '}
             <button onClick={this.first_network_connect}>연결</button>
           </h2>
           <br />
           <button onClick={this.query}> 잔액 확인</button> a :{' '}
           <Amount bgColor="magenta" amount={this.state.a_amount}></Amount>b :{' '}
           <Amount bgColor="cyan" amount={this.state.b_amount}></Amount>
           <br />
           <br />
           <div>
             a가 b에게{' '}
             <input placeholder="송금량" ref={(ref) => (this.amount = ref)} />
             원을 <button onClick={this.send}> 보내기</button>
             <br />
           </div>
         </div>
       );
     }
   }
   
   ReactDOM.render(
     <Router>
       <Route path="/" component={Main}>
         <IndexRoute component={Home} />
         <Route path="basic" component={BasicNetwork} />
         <Route path="first" component={FirstNetwork} />
         <Route path="fabcar" component={Fabcar} />
       </Route>
     </Router>,
     document.getElementById('root')
   );
   
   ```

4. Myweb/public/fabcar.html

   ```html
   <!-- SPDX-License-Identifier: Apache-2.0 -->
   
   <!DOCTYPE html>
   <html>
     <head>
       <title>Hyperledger Fabric Tuna Application</title>
       <link rel="icon" href="favicon.png" type="image/gif" />
   
       <!-- require jquery and bootstrap scripts -->
       <link
         rel="stylesheet"
         href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
       />
       <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
       <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   
       <!-- adding style here -->
       <style type="text/css">
         header {
           background-color: lightgray;
           font-size: 20px;
           padding: 15px;
         }
         header,
         .form-group {
           margin-bottom: 3%;
         }
         .form-group {
           width: 50%;
         }
         #body {
           margin-left: 3%;
           margin-right: 3%;
         }
         .form-control {
           margin: 8px;
         }
         #right_header {
           width: 20%;
           font-size: 15px;
           margin-right: 0px;
         }
         #left_header {
           margin-left: 0;
           width: 40%;
           display: inline-block;
         }
         #id {
           width: 49%;
           display: inline-block;
         }
         table {
           font-family: arial, sans-serif;
           border-collapse: collapse;
           width: 100%;
         }
         td,
         th {
           border: 1px solid #dddddd;
           text-align: left;
           padding: 8px;
         }
         tr:nth-child(even) {
           background-color: #dddddd;
         }
       </style>
     </head>
     <body ng-app="application" ng-controller="appController">
       <header>
         <div id="left_header">Hyperledger Fabric FabCar Application</div>
         <i id="right_header"
           >Example Blockchain Application for Introduction to Hyperledger
           Fabric</i
         >
       </header>
   
       <div id="body">
         <div class="form-group">
           <label>Query All Cars</label>
           <!--
             <p><input id="queryAllCar" type="submit" value="Query" class="btn btn-primary" ng-click="queryAllCar()"></p>
           -->
           <p>
             <input
               id="queryAllCar"
               type="submit"
               value="QueryAllCars"
               class="btn btn-primary"
               ng-click="queryAllCar()"
             />
           </p>
         </div>
   
         <div id="all_car"></div>
         <!--   <table id="all_car" class="table" align="center">
   
           <tr>
             <th>ID</th>
             <th>Color</th>
             <th>Make</th>
             <th>Model</th>
             <th>Owner</th>
           </tr>
   
           <tr ng-repeat="car in all_car">
             <td>{{car.Key}}</td>
             <td>{{car.colour}}</td>
             <td>{{car.make}}</td>
             <td>{{car.model}}</td>
             <td>{{car.owner}}</td>
           </tr>
         </table> -->
   
         <div class="form-group">
           <label>Query a Specific Car</label><br />
           <h5 style="color: red; margin-bottom: 2%;" id="error_query">
             Error: Please enter a valid Car Id
           </h5>
   
           Enter a Car number:
           <!--<input id="createName" class="form-control" type="text" placeholder="CAR0" ng-model="car_id">
           <input id="querySubmit" type="submit" value="Query" class="btn btn-primary" ng-click="queryCar()">-->
           <input
             id="queryName"
             class="form-control"
             type="text"
             value="CAR0"
             ng-model="car_id"
           />
           <input
             id="querySubmit"
             type="submit"
             value="Query"
             class="btn btn-primary"
           />
         </div>
   
         <div id="query_car_content">
           <table class="table" align="center">
             <tr>
               <th>Color</th>
               <th>Make</th>
               <th>Model</th>
               <th>Owner</th>
             </tr>
   
             <tr id="query_car">
               <td>{{car.colour}}</td>
               <td>{{car.make}}</td>
               <td>{{car.model}}</td>
               <td>{{car.owner}}</td>
             </tr>
           </table>
         </div>
   
         <div class="form-group">
           <label>Create New Car</label>
           <h5 style="color: green; margin-bottom: 2%;" id="success_create"></h5>
           <br />
           Enter Car Number:
           <input class="form-control" type="text" value="CAR10" id="car_id" />
           Enter name of Owner:
           <input
             class="form-control"
             type="text"
             value="EunSuJEON"
             id="car_owner"
           />
           Enter Make:
           <input class="form-control" type="text" value="KIA" id="car_make" />
           Enter Model:
           <input
             class="form-control"
             type="text"
             value="Sorrento"
             id="car_model"
           />
           Enter Color:
           <input class="form-control" type="text" value="red" id="car_colour" />
           <!--  <input id="createSubmit" type="submit" value="Create" class="btn btn-primary" ng-click="recordCar()">  -->
           <input
             id="createSubmit"
             type="submit"
             value="Create"
             class="btn btn-primary"
           />
         </div>
   
         <div class="form-group">
           <label>Change Car Owner</label><br />
           <h5 style="color: green; margin-bottom: 2%;" id="success_holder"></h5>
           <!-- <h5 style="color:red;margin-bottom:2%" id="error_holder">Error: Please enter a valid Car Id</h5> -->
           Enter a Car Number :
           <input class="form-control" id="car_id2" value="CAR10" /> Enter name of
           new Owner:
           <input class="form-control" id="car_owner2" value="EunSu JEON" />
           <input
             id="transferSubmit"
             type="submit"
             value="Change"
             class="btn btn-primary"
           />
         </div>
       </div>
     </body>
     <!-- requiring the angular page 
       <script type="text/javascript" src="app.js"> </script>-->
     <script type="text/javascript" src="my.js"></script>
   </html>
   
   ```

   

5. Myweb/public/my.js

   **my.js**

   ```js
   $(document).ready(function () {
     $('#error_query').hide();
     $('#success_create').hide();
     $('#success_holder').hide();
   
     $('#transferSubmit').click(function () {
       var owner = {
         car_id: $('#car_id2').val(),
         car_owner: $('#car_owner2').val(),
       };
       if (confirm(JSON.stringify(owner) + '\n 소유주 변경 정보가 맞습니까?')) {
         $.post({
           traditional: true,
           url: '/fabcar_network/change_owner/',
           contentType: 'application/json',
           data: JSON.stringify(owner),
           dataType: 'json',
           success: function (data) {
             alert(data.msg);
             if (data.code == '1') {
               $('#success_holder').show().html(`Success! Tx ID: ${data.msg}`);
             } else {
               $('#success_holder')
                 .show()
                 .html(`Fail! please retry: ${data.msg}`)
                 .css('color', 'red');
             }
           },
         });
       }
     });
   
     $('#createSubmit').click(function () {
       var car = {
         car_id: $('#car_id').val(),
         car_colour: $('#car_colour').val(),
         car_make: $('#car_make').val(),
         car_model: $('#car_model').val(),
         car_owner: $('#car_owner').val(),
       };
       if (
         confirm('[입력 차량 정보]\n' + JSON.stringify(car) + '\n가 맞습니까?')
       ) {
         $.post({
           traditional: true,
           url: '/fabcar_network/add_car/',
           contentType: 'application/json',
           data: JSON.stringify(car),
           dataType: 'json',
           success: function (data) {
             alert(data.msg);
             if (data.code == '1') {
               $('#success_create').show().html(`Success! Tx ID: ${data.msg}`);
             } else {
               $('#success_create')
                 .show()
                 .html(`Fail! please retry: ${data.msg}`)
                 .css('color', 'red');
             }
           },
         });
       }
     });
   
     $('#querySubmit').click(function () {
       var id = $('#queryName').val();
       alert(id + ' 정보를 조회합니다');
   
       $.post({
         traditional: true,
         url: '/fabcar_network/get_car/',
         contentType: 'application/json',
         data: JSON.stringify({ queryName: id }),
         dataType: 'json',
         success: function (data) {
           console.log(data);
           let content;
           try {
             let _data = JSON.parse(data.queryCar);
             content =
               `<td>${_data.colour}</td>` +
               `<td>${_data.make}</td>` +
               `<td>${_data.model}</td>` +
               `<td>${_data.owner}</td>`;
   
             $('#error_query').hide();
           } catch (e) {
             alert(id + ' 정보는 존재하지 않습니다');
             content =
               '<td>undefined</td><td>undefined</td><td>undefined</td><td>undefined</td>';
             $('#error_query').show();
           }
   
           $('#query_car').html(content);
         },
       });
     });
   
     $('#queryAllCar').click(function () {
       alert('모든 차 정보를 조회합니다');
   
       $.get('/fabcar_network/get_all_car/', function (data, status) {
         var result = JSON.parse(data.queryAllCars);
         console.log(result.length);
         var array = [];
         for (var i = 0; i < result.length; i++) {
           result[i].Record.Key = result[i].Key;
           array.push(result[i].Record);
         }
         array.sort(function (a, b) {
           return parseFloat(a.Key) - parseFloat(b.Key);
         });
   
         var content =
           '<table id="all_car" class="table" align="center">' +
           '<tr><th>ID</th><th>Color</th><th>Make</th><th>Model</th><th>Owner</th></tr>';
         array.forEach((car) => {
           content += `<tr><td>${car.Key}</td><td>${car.colour}</td><td>${car.make}</td> <td>${car.model}</td> <td>${car.owner}</td></tr>`;
         });
   
         content += '</table>';
         //alert(content);
         $('#all_car').html(content);
       });
     });
   });
   
   ```

6. Myweb/public/first_articles/connection-org1.json (first netword에서 가져옴)

7. Myweb/wallet2 (firstnetwork/javascript 에서 wallet폴더 이름 바꿔서 복사/붙여넣기)

* 만약 startFabric.sh를 다시 실행하게 되면 기존 네트워크가 새로 깔리는 것이기 때문에 wallet2 폴더 내용 변경 및 first_articles/connection-org1.json을 바꿔야함

확인