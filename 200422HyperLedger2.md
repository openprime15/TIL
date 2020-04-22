## HyperLedger

### 2. Node로 체인코드 설치 및 업데이트 하기

1. Node 위치 확인
   1. 홈에서 cd /HLF/fabric-samples/chaincode/chaincode_example02로 node폴더 확인 
   2. Cli로 동기화되는지 확인
   3. docker exec -it cli bash
   4. cli>/opt/gopath/src/github.com 확인
2. 노드 체인코드 설치
   1. cli>peer chaincode install -n jes_cc_node -v 1.0 -l node -p /opt/gopath/src/github.com/chaincode_example02/node/
      (go 와는 다르게 -l 언어 입력 및 절대경로로 입력해야함)
   2. peer0 접속
   3. docker exec -it peer0.org1.example.com bash
   4. peer0>cd /var/hyperledger/production/chaincodes 로 설치된 것을 확인
   5. cli로 들어가 mychannel 연결
   6. cli>peer chaincode instantiate -C mychannel -n jes_cc_node -l node -v 1.0 -c '{"Args":["init","a","100","b","200"]}' 
   7. cli> peer chaincode query -C mychannel -n jes_cc_node -c '{"Args":["query","a"]}'
      (chaincode로 쿼리 날려보기)
   8. cli> peer chaincode invoke -C mychannel -n jes_cc_node -c '{"Args":["invoke","a","b","10"]}'
      cli> peer chaincode query -C mychannel -n jes_cc_node -c '{"Args":["query","a"]}'
3. 체인코드 업그레이드
   1. 홈파일~/HLF/fabric-samples/chaincode/chaincode_example02/node#의 chaincode_example02.js 수정
      1. 소문자로 시작하는 부분을 바꿈
      2. 62행 invoke -> send로 수정
      3. 115행 query -> getBalance로 수정
   2. cli> peer chaincode install -n jes_cc_node -v 1.1 -l node -p /opt/gopath/src/github.com/chaincode_example02/node/
   3. **버전 업그레이드 후에는 instantiate가 아닌 upgrade를 써줘야함**
      cli> peer chaincode upgrade -n jes_cc_node -v 1.1 -c '{"Args":["init", "a", "100","b","0"]}' -C mychannel
   4. cli> peer chaincode list --instantiated -C mychannel
      (설치 확인)
   5. peer chaincode invoke -C mychannel -n jes_cc_node -c '{"Args":["send","a","b","10"]}'
      (장부 수정, **invoke명령어를 send로 변경**)
   6. peer chaincode query -C mychannel -n jes_cc_node -c '{"Args":["getBalance","a"]}'
      (잔액 확인, **query명령어를 getBalance로 변경**)
4. Chaincode Go 설치
   1. peer chaincode install -n jes_cc_go -v 1.0 -p github.com/chaincode_example02/go/
   2. peer chaincode instantiate -C mychannel -n jes_cc_go -v 1.0 -c '{"Args":["init","a","200","b","100"]}' 
