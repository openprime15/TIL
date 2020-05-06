#### HyperLedger private data 활용

1. https://velog.io/@wnjoon/Using-Private-Data-in-Fabric 사이트 실습

2. 기본세팅

   1. ~/HLF/fabric-samples/first-network 로 이동

   2. byfn.sh 파일에 있는 명령어를 확인

      1.   echo "	byfn.sh generate -c mychannel"
           echo "	byfn.sh up -c mychannel -s couchdb"
           echo "    byfn.sh up -c mychannel -s couchdb -i 1.4.0"
           echo "	byfn.sh up -l node"
           echo "	byfn.sh down -c mychannel"
           echo "        byfn.sh upgrade -c mychannel"

   3. ./byfn.sh -m down (실행)

   4. 실행 후 각 명령어 입력

      1. ./byfn.sh generate -c mychannel
      2. byfn.sh up -c mychannel -s couchdb -i 1.4.3

   5. 명령어 입력 후 cli로 들어감

      1. docker exec -it cli bash
      2. cli> echo $CORE_PEER_ADDRESS 
         * 이명령어를 통해 환경변수 위치를 확인 할 수 있음

   6. 모든 피어에 체인코드 설치(이하 내용은 모두 cli에서 실행)

      1. peer0.org1.example.com 에 설치

         1. ```bash
            peer chaincode install -n marblesp -v 1.0 -p github.com/chaincode/marbles02_private/go/
            ```

      2. peer1.org1.example.com에 설치

         1. ```bash
            export CORE_PEER_ADDRESS=peer1.org1.example.com:8051
            peer chaincode install -n marblesp -v 1.0 -p github.com/chaincode/marbles02_private/go/
            ```

      3. peer1.org1.example.com에 설치

         1. ```bash
            export CORE_PEER_LOCALMSPID=Org2MSP
            export PEER0_ORG2_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
            export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
            export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
            export CORE_PEER_ADDRESS=peer0.org2.example.com:9051
            peer chaincode install -n marblesp -v 1.0 -p github.com/chaincode/marbles02_private/go/
            ```

      4. peer1.org2.example.com에 설치

         1. ```bash
            export CORE_PEER_ADDRESS=peer1.org2.example.com:10051
            peer chaincode install -n marblesp -v 1.0 -p github.com/chaincode/marbles02_private/go/
            ```

   7. 체인코드를 인스턴트화 실시

      1. ```bash
         export ORDERER_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
         peer chaincode instantiate -o orderer.example.com:7050 --tls --cafile $ORDERER_CA -C mychannel -n marblesp -v 1.0 -c '{"Args":["init"]}' -P "OR('Org1MSP.member','Org2MSP.member')" --collections-config $GOPATH/src/github.com/chaincode/marbles02_private/collections_config.json
         ```

   8. org1에서 private data 저장

      1. ```bash
         export CORE_PEER_ADDRESS=peer0.org1.example.com:7051
         export CORE_PEER_LOCALMSPID=Org1MSP
         export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
         export PEER0_ORG1_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
         ```

      2. ```bash
         export MARBLE=$(echo -n "{\"name\":\"marble1\",\"color\":\"blue\",\"size\":35,\"owner\":\"tom\",\"price\":99}" | base64 | tr -d \\n)
         ```

      3. ```bash
         peer chaincode invoke -o orderer.example.com:7050 --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n marblesp -c '{"Args":["initMarble"]}'  --transient "{\"marble\":\"$MARBLE\"}"
         ```

   9. org1에서 데이터 쿼리 실행

      1. ```bash
         peer chaincode query -C mychannel -n marblesp -c '{"Args":["readMarble","marble1"]}'
         ```

      2. ```bash
         peer chaincode query -C mychannel -n marblesp -c '{"Args":["readMarblePrivateDetails","marble1"]}'
         ```

          

   10. org2에서 데이터 쿼리 실행

       1. ```bash
          export CORE_PEER_LOCALMSPID=Org2MSP
          export PEER0_ORG2_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
          export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
          export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
          ```

       2. ```bash
          peer chaincode query -C mychannel -n marblesp -c '{"Args":["readMarble","marble1"]}'
          ```

           

       3. ```bash
          peer chaincode query -C mychannel -n marblesp -c '{"Args":["readMarblePrivateDetails","marble1"]}'
          ```

           => 이 쿼리는 동작하지 않음



* Orderer를 다수 만드는 이유
  * Orderer 설정 방식은 solo / kafka 방식으로 나누어짐
  * 다수의 orderer가 있으면 한 orderer가 셧다운이 되어도 프로세스 유지가 가능
  * kafa: 다수의 orderer를 활용하는 기능
    * orderer마다 트렌잭션을 수집하고, 그 중 설정된 한개의 orderer가 각 orderer에게 모인 트랜잭션을 정리해서 블록으로 만듬
    *  예를 들어 orderer1~5까지 있는 경우 orderer4가 셧다운이 되면 가지고 있는 트랜잭션은 다른 orderer에게 전송됨
  * hyperledger fabric 2.0 에서 새로운 방식 (raft)가 생길 예정
    * orderer중에 계급을 만들어 leader orderer 한개가 설정, 나머지는 follow orderer
    * leader orderer만 트랜잭션을 모음
    * leader orderer에게 모인 트랜잭션이 follow orderer에게 동기화됨
    * 따라서 leader orderer가 셧다운이 되어도 follow orderer 중 하나가 leader로 재 선출되면서 프로세스 유지가 가능(물론 트랜잭션도 동기화가 계속 되어있기 때문에 트랜잭션 손실 문제도 발생하지 않음)