### 3. Hyper Ledger 3

#### First Network 시작

* VM에서 작동하고있는 도커를 정지
* docker rm -f $(docker ps -aq)
* apt install tilix (화면 분할도구, 실행: tilix)

1. 기본 세팅

   1. 인증서 세팅

      1. 경로 이동: cd ~/HLF/fabric-samples/first-network 이 경로에서 다음 명령어 입력
      2. ../bin/cryptogen generate --config=./crypto-config.yaml

   2. genesis 블록 생성

      1. FABRIC_CFG_PATH=$PWD
      2. ../bin/configtxgen -profile TwoOrgsOrdererGenesis -channelID byfn-sys-channel -outputBlock ./channel-artifacts/genesis.block

   3. 채널 트랜잭션 아티팩트 생성

      1. export CHANNEL_NAME=mychannel
      2. ../bin/configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channel-artifacts/channel.tx -channelID $CHANNEL_NAME

   4. Anchor peer 아티팩트 생성(Anchor는 다른 org peer와 소통하는 역할)

      1. ../bin/configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP
      2. ../bin/configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org2MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org2MSP

   5. 컨테이너 실행

      1. docker-compose -f docker-compose-cli.yaml up -d

   6. cli 컨테이너로 접속 후 channel을 만들기

      1. docker exec -it cli bash
      2. cli> export CHANNEL_NAME=mychannel
      3. cli> peer channel create -o orderer.example.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/channel.tx --tls $CORE_PEER_TLS_ENABLED --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

   7. 채널에 피어들을 참여시키기

      1. Peer0.org1
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer0.org1.example.com:7051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         * peer channel join -b $CHANNEL_NAME.block
      2. Peer1.org1
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer1.org1.example.com:8051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer1.org1.example.com/tls/ca.crt
         * peer channel join -b $CHANNEL_NAME.block
      3. Peer0.org2
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer0.org2.example.com:9051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
         * peer channel join -b $CHANNEL_NAME.block
      4. Peer1.org2
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer1.org2.example.com:10051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer1.org2.example.com/tls/ca.crt
         * peer channel join -b $CHANNEL_NAME.block

   8. Anchor 피어 설정 (각 조직별 Peer0을 Anchor 피어로 설정)

      1. Peer0.org1
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer0.org1.example.com:7051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         * peer channel create -o orderer.example.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/Org1MSPanchors.tx --tls $CORE_PEER_TLS_ENABLED --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
      2. Peer0.org2
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer0.org2.example.com:9051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
         * peer channel create -o orderer.example.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/Org2MSPanchors.tx --tls $CORE_PEER_TLS_ENABLED --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

   9. 체인코드 디플로이 (인스톨) : 각 조직의 Peer0만 체인코드 설치

      1. Peer0.org1
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer0.org1.example.com:7051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         * peer chaincode install -n mycc -v 1.0 -p github.com/chaincode/chaincode_example02/go
      2. Peer0.org2
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer0.org2.example.com:9051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
         * peer chaincode install -n mycc -v 1.0 -p github.com/chaincode/chaincode_example02/go

   10. 체인코드 초기화 (instantiate) : 여기서 Endorsement 정책을 결정함

      * 여기서 정책은 각 단체별 MSP를 가진 member의 승인을 받으면 통과되도록 설정

      1. 환경변수 설정 후, instatiate
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer0.org1.example.com:7051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         * peer chaincode instantiate -o orderer.example.com:7050 --tls $CORE_PEER_TLS_ENABLED --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc -v 1.0 -c '{"Args":["init","a", "100", "b","200"]}' -P "AND ('Org1MSP.member','Org2MSP.member')"
         * 기존 basic_network와 차이: 정책 추가, orderer 추가

   11. Query

       * peer chaincode query -C $CHANNEL_NAME -n mycc -c '{"Args":["query","a"]}'

   12. invoke

       * peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["invoke","a","b","10"]}'

   13. Peer1.org2 환경변수 설정 

       * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer1.org2.example.com:10051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer1.org2.example.com/tls/ca.crt
       * peer chaincode install -n mycc -v 1.0 -p github.com/chaincode/chaincode_example02/go
       * peer chaincode query -C mychannel -n mycc -c '{"Args":["query","a"]}'

   * CLI 를 통해 Peer 접속시 사용할 환경변수 명령어 모음
     * peer0.org1
       * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer0.org1.example.com:7051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
     * peer1.org1
       * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer1.org1.example.com:8051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer1.org1.example.com/tls/ca.crt
     * peer0.org2
       * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer0.org2.example.com:9051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
     * peer1.org2
       * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer1.org2.example.com:10051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer1.org2.example.com/tls/ca.crt




#### Fabcar(javascript 체인코드) 설치

1. 기본세팅

   1. HLF/fabric-sample/fabcar/startFabric.sh를 수정

      **StartFabric.sh**

   ```sh
   #!/bin/bash
   #
   # Copyright IBM Corp All Rights Reserved
   #
   # SPDX-License-Identifier: Apache-2.0
   #
   # Exit on first error
   set -e
   
   # don't rewrite paths for Windows Git Bash users
   export MSYS_NO_PATHCONV=1
   starttime=$(date +%s)
   CC_SRC_LANGUAGE=${1:-"go"}
   CC_SRC_LANGUAGE=`echo "$CC_SRC_LANGUAGE" | tr [:upper:] [:lower:]`
   if [ "$CC_SRC_LANGUAGE" = "go" -o "$CC_SRC_LANGUAGE" = "golang"  ]; then
   	CC_RUNTIME_LANGUAGE=golang
   	CC_SRC_PATH=github.com/chaincode/fabcar/go
   elif [ "$CC_SRC_LANGUAGE" = "java" ]; then
   	CC_RUNTIME_LANGUAGE=java
   	CC_SRC_PATH=/opt/gopath/src/github.com/chaincode/fabcar/java
   elif [ "$CC_SRC_LANGUAGE" = "javascript" ]; then
   	CC_RUNTIME_LANGUAGE=node # chaincode runtime language is node.js
   	CC_SRC_PATH=/opt/gopath/src/github.com/chaincode/fabcar/javascript
   elif [ "$CC_SRC_LANGUAGE" = "typescript" ]; then
   	CC_RUNTIME_LANGUAGE=node # chaincode runtime language is node.js
   	CC_SRC_PATH=/opt/gopath/src/github.com/chaincode/fabcar/typescript
   	echo Compiling TypeScript code into JavaScript ...
   	pushd ../chaincode/fabcar/typescript
   	npm install
   	npm run build
   	popd
   	echo Finished compiling TypeScript code into JavaScript
   else
   	echo The chaincode language ${CC_SRC_LANGUAGE} is not supported by this script
   	echo Supported chaincode languages are: go, javascript, and typescript
   	exit 1
   fi
   
   
   # clean the keystore
   # rm -rf ./hfc-key-store
   rm -rf ./javascript/wallet
   
   # launch network; create channel and join peer to channel
   cd ../first-network
   echo y | ./byfn.sh -m down
   echo y | ./byfn.sh up -a -n -s couchdb
   
   CONFIG_ROOT=/opt/gopath/src/github.com/hyperledger/fabric/peer
   ORG1_MSPCONFIGPATH=${CONFIG_ROOT}/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
   ORG1_TLS_ROOTCERT_FILE=${CONFIG_ROOT}/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
   ORG2_MSPCONFIGPATH=${CONFIG_ROOT}/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
   ORG2_TLS_ROOTCERT_FILE=${CONFIG_ROOT}/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
   ORDERER_TLS_ROOTCERT_FILE=${CONFIG_ROOT}/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
   set -x
   
   echo "Installing smart contract on peer0.org1.example.com"
   docker exec \
     -e CORE_PEER_LOCALMSPID=Org1MSP \
     -e CORE_PEER_ADDRESS=peer0.org1.example.com:7051 \
     -e CORE_PEER_MSPCONFIGPATH=${ORG1_MSPCONFIGPATH} \
     -e CORE_PEER_TLS_ROOTCERT_FILE=${ORG1_TLS_ROOTCERT_FILE} \
     cli \
     peer chaincode install \
       -n fabcar \
       -v 1.0 \
       -p "$CC_SRC_PATH" \
       -l "$CC_RUNTIME_LANGUAGE"
   
   echo "Installing smart contract on peer1.org1.example.com"
   docker exec \
     -e CORE_PEER_LOCALMSPID=Org1MSP \
     -e CORE_PEER_ADDRESS=peer1.org1.example.com:8051 \
     -e CORE_PEER_MSPCONFIGPATH=${ORG1_MSPCONFIGPATH} \
     -e CORE_PEER_TLS_ROOTCERT_FILE=${ORG1_TLS_ROOTCERT_FILE} \
     cli \
     peer chaincode install \
       -n fabcar \
       -v 1.0 \
       -p "$CC_SRC_PATH" \
       -l "$CC_RUNTIME_LANGUAGE"
   
   echo "Installing smart contract on peer0.org2.example.com"
   docker exec \
     -e CORE_PEER_LOCALMSPID=Org2MSP \
     -e CORE_PEER_ADDRESS=peer0.org2.example.com:9051 \
     -e CORE_PEER_MSPCONFIGPATH=${ORG2_MSPCONFIGPATH} \
     -e CORE_PEER_TLS_ROOTCERT_FILE=${ORG2_TLS_ROOTCERT_FILE} \
     cli \
     peer chaincode install \
       -n fabcar \
       -v 1.0 \
       -p "$CC_SRC_PATH" \
       -l "$CC_RUNTIME_LANGUAGE"
   
   echo "Installing smart contract on peer1.org2.example.com"
   docker exec \
     -e CORE_PEER_LOCALMSPID=Org2MSP \
     -e CORE_PEER_ADDRESS=peer1.org2.example.com:10051 \
     -e CORE_PEER_MSPCONFIGPATH=${ORG2_MSPCONFIGPATH} \
     -e CORE_PEER_TLS_ROOTCERT_FILE=${ORG2_TLS_ROOTCERT_FILE} \
     cli \
     peer chaincode install \
       -n fabcar \
       -v 1.0 \
       -p "$CC_SRC_PATH" \
       -l "$CC_RUNTIME_LANGUAGE"
   
   echo "Instantiating smart contract on mychannel"
   docker exec \
     -e CORE_PEER_LOCALMSPID=Org1MSP \
     -e CORE_PEER_MSPCONFIGPATH=${ORG1_MSPCONFIGPATH} \
     cli \
     peer chaincode instantiate \
       -o orderer.example.com:7050 \
       -C mychannel \
       -n fabcar \
       -l "$CC_RUNTIME_LANGUAGE" \
       -v 1.0 \
       -c '{"Args":[]}' \
       -P "AND('Org1MSP.member','Org2MSP.member')" \
       --tls \
       --cafile ${ORDERER_TLS_ROOTCERT_FILE} \
       --peerAddresses peer0.org1.example.com:7051 \
       --tlsRootCertFiles ${ORG1_TLS_ROOTCERT_FILE}
   
   echo "Waiting for instantiation request to be committed ..."
   sleep 10
   
   echo "Submitting initLedger transaction to smart contract on mychannel"
   echo "The transaction is sent to all of the peers so that chaincode is built before receiving the following requests"
   docker exec \
     -e CORE_PEER_LOCALMSPID=Org1MSP \
     -e CORE_PEER_MSPCONFIGPATH=${ORG1_MSPCONFIGPATH} \
     cli \
     peer chaincode invoke \
       -o orderer.example.com:7050 \
       -C mychannel \
       -n fabcar \
       -c '{"function":"initLedger","Args":[]}' \
       --waitForEvent \
       --tls \
       --cafile ${ORDERER_TLS_ROOTCERT_FILE} \
       --peerAddresses peer0.org1.example.com:7051 \
       --peerAddresses peer1.org1.example.com:8051 \
       --peerAddresses peer0.org2.example.com:9051 \
       --peerAddresses peer1.org2.example.com:10051 \
       --tlsRootCertFiles ${ORG1_TLS_ROOTCERT_FILE} \
       --tlsRootCertFiles ${ORG1_TLS_ROOTCERT_FILE} \
       --tlsRootCertFiles ${ORG2_TLS_ROOTCERT_FILE} \
       --tlsRootCertFiles ${ORG2_TLS_ROOTCERT_FILE}
   set +x
   
   cat <<EOF
   
   Total setup execution time : $(($(date +%s) - starttime)) secs ...
   
   Next, use the FabCar applications to interact with the deployed FabCar contract.
   The FabCar applications are available in multiple programming languages.
   Follow the instructions for the programming language of your choice:
   
   JavaScript:
   
     Start by changing into the "javascript" directory:
       cd javascript
   
     Next, install all required packages:
       npm install
   
     Then run the following applications to enroll the admin user, and register a new user
     called user1 which will be used by the other applications to interact with the deployed
     FabCar contract:
       node enrollAdmin
       node registerUser
   
     You can run the invoke application as follows. By default, the invoke application will
     create a new car, but you can update the application to submit other transactions:
       node invoke
   
     You can run the query application as follows. By default, the query application will
     return all cars, but you can update the application to evaluate other transactions:
       node query
   
   TypeScript:
   
     Start by changing into the "typescript" directory:
       cd typescript
   
     Next, install all required packages:
       npm install
   
     Next, compile the TypeScript code into JavaScript:
       npm run build
   
     Then run the following applications to enroll the admin user, and register a new user
     called user1 which will be used by the other applications to interact with the deployed
     FabCar contract:
       node dist/enrollAdmin
       node dist/registerUser
   
     You can run the invoke application as follows. By default, the invoke application will
     create a new car, but you can update the application to submit other transactions:
       node dist/invoke
   
     You can run the query application as follows. By default, the query application will
     return all cars, but you can update the application to evaluate other transactions:
       node dist/query
   
   Java:
   
     Start by changing into the "java" directory:
       cd java
   
     Then, install dependencies and run the test using:
       mvn test
   
     The test will invoke the sample client app which perform the following:
       - Enroll admin and user1 and import them into the wallet (if they don't already exist there)
       - Submit a transaction to create a new car
       - Evaluate a transaction (query) to return details of this car
       - Submit a transaction to change the owner of this car
       - Evaluate a transaction (query) to return the updated details of this car
   
   EOF
   ```

   2. 실행
      1. ./startFabric.sh javascript 
      2. cd javascript (이하는 javascript 폴더에서 실행)
      3. npm i
      4. node enrollAdmin.js
         * Admin Wallet 생성 
      5. node registerUser.js
         * User Wallet 생성
      6. node query.js
         * 차량 정보 조회
      7. node invoke.js
         * CAR12 추가
      8. 정책변경이 필요하면 startFabric.sh의 121행
         -P "OR('Org1MSP.member','Org2MSP.member')" \
         이 부분을 수정하면 됨



#### example02_node 체인코드 추가하기

* First Network + fabcar + example02_node

1. 기본 세팅

   1. fabcar 실행된 상태에서 cli 들어가서 peer 수정

   2. docker exec -it cli bash

      1. 환경변수 설정 (Org1 Org2)
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/mspCORE_PEER_ADDRESS=peer0.org1.example.com:7051CORE_PEER_LOCALMSPID="Org1MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         * CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/mspCORE_PEER_ADDRESS=peer0.org2.example.com:9051CORE_PEER_LOCALMSPID="Org2MSP"CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
      2. peer chaincode install -n send_money -v 1.0 -l node -p /opt/gopath/src/github.com/chaincode/chaincode_example02/node
         * 각 환경변수 별로 설치
      3. peer chaincode instantiate -o orderer.example.com:7050 --tls $CORE_PEER_TLS_ENABLED --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n send_money -l node -v 1.0 -c '{"Args":["init","a", "100", "b","200"]}' -P "OR ('Org1MSP.member','Org2MSP.member')"
         * 체인코드 디플로이(초기화) : 환경변수 상관없이 한번만 해주면 됨

   3. 홈에서 실행

      1. /fabcar/javascript# 에서 query_money.js 생성

         **query_money.js**

         

         /* * SPDX-License-Identifier: Apache-2.0 */
         'use strict';
         const { FileSystemWallet, Gateway } = require('fabric-network');const path = require('path');
         const ccpPath = path.resolve(__dirname, '..', '..', 'first-network', 'connection-org1.json');
         async function main() {  try {
             // Create a new file system based wallet for managing identities.    const walletPath = path.join(process.cwd(), 'wallet');    const wallet = new FileSystemWallet(walletPath);    console.log(`Wallet path: ${walletPath}`);
             // Check to see if we've already enrolled the user.    const userExists = await wallet.exists('user1');    if (!userExists) {      console.log('An identity for the user "user1" does not exist in the wallet');      console.log('Run the registerUser.js application before retrying');      return;    }
             // Create a new gateway for connecting to our peer node.    const gateway = new Gateway();    await gateway.connect(ccpPath, { wallet, identity: 'user1', discovery: { enabled: true, asLocalhost: true } });
             // Get the network (channel) our contract is deployed to.    const network = await gateway.getNetwork('mychannel');
             // Get the contract from the network.    const contract = network.getContract('send_money');
             // Evaluate the specified transaction.    // queryCar transaction - requires 1 argument, ex: ('queryCar', 'CAR4')    // queryAllCars transaction - requires no arguments, ex: ('queryAllCars')    const result = await contract.evaluateTransaction('query','a');    console.log(`Transaction has been evaluated, result is: ${result.toString()}`);
           } catch (error) {    console.error(`Failed to evaluate transaction: ${error}`);    process.exit(1);  }}
         main(); 

      2. /fabcar/javascript# 에서 invoke_money.js 생성

         **invoke_money.js**

         ```js
         /*
          * SPDX-License-Identifier: Apache-2.0
          */
         
         'use strict';
         
         const { FileSystemWallet, Gateway } = require('fabric-network');
         const path = require('path');
         
         const ccpPath = path.resolve(__dirname, '..', '..', 'first-network', 'connection-org1.json');
         
         async function main() {
             try {
         
                 // Create a new file system based wallet for managing identities.
                 const walletPath = path.join(process.cwd(), 'wallet');
                 const wallet = new FileSystemWallet(walletPath);
                 console.log(`Wallet path: ${walletPath}`);
         
                 // Check to see if we've already enrolled the user.
                 const userExists = await wallet.exists('user1');
                 if (!userExists) {
                     console.log('An identity for the user "user1" does not exist in the wallet');
                     console.log('Run the registerUser.js application before retrying');
                     return;
                 }
         
                 // Create a new gateway for connecting to our peer node.
                 const gateway = new Gateway();
                 await gateway.connect(ccpPath, { wallet, identity: 'user1', discovery: { enabled: true, asLocalhost: true } });
         
                 // Get the network (channel) our contract is deployed to.
                 const network = await gateway.getNetwork('mychannel');
         
                 // Get the contract from the network.
                 const contract = network.getContract('send_money');
         
                 // Submit the specified transaction.
                 // createCar transaction - requires 5 argument, ex: ('createCar', 'CAR12', 'Honda', 'Accord', 'Black', 'Tom')
                 // changeCarOwner transaction - requires 2 args , ex: ('changeCarOwner', 'CAR10', 'Dave')
                 await contract.submitTransaction('invoke', 'a', 'b', '1');
                 console.log('Transaction has been submitted');
         
                 // Disconnect from the gateway.
                 await gateway.disconnect();
         
             } catch (error) {
                 console.error(`Failed to submit transaction: ${error}`);
                 process.exit(1);
             }
         }
         
         main();
         ```

      3.  node query_money.js

      4.  node invoke_money.js

      * 만약 query_money 를 했을 때 result 값이 안나온다면
        chaincode의 chaincode_example02.js를 확인!
      * 쿼리문이 getBalance 와 send로 되어있는지 확인

   

