### 이더리움 실습

1. 기본세팅

   1. geth download로 설치(1.8 또는 1.9 버전 기준)

      1. geth가 이미 설치되어있는경우 제거 후, 재 설치방법
         1. C://설치한 계정/AppData/ethash 폴더 제거 
         2. / Roaming/ethereum 폴더 제거 
         3. Local 폴더의 ethash,ethereum폴더 제거
      2. **geth 설치 시 개발자도구 체크할것**
      3. 환경변수 -> 시스템변수 -> PATH에 다음 경로 추가
         * C:\Program Files\Geth
      4. cmd 창에서 geth version 으로 확인

   2. 블록생성

      1. 작업을 시작할 폴더 생성(여기서는 ethereum폴더)

      2. 생성 파일에서 다음 명령어 입력

         1. c:\ethereum>puppeth

         2. ```shell
            * Please specify a network name to administer (no spaces, hyphens or capital letters please)
            > mynetwork
            What would you like to do? (default = stats)
            1. Show network stats
            2. Configure new genesis
            3. Track new remote server
            4. Deploy network components
            > 2
            * What would you like to? (default = create)
            1. Create new genesis from scratch
            2. Import already existing genesis
            > 1
            * Which consensus engine to use? (default = clique)
            1. Ethash - proof-of-work
            2. Clique - proof-of-authority
            > 1
            * Which accounts should be pre-funded? (advisable at least one)
            > 0x
            * Should the precompile-addresses (0x1 .. 0xff) be pre-funded with 1 wei? (advisable yes)
            > yes
            * Specify your chain/network ID if you want an explicit one (default = random)
            > 4386
            * What would you like to do? (default = stats)
            1. Show network stats
            2. Manage existing genesis
            3. Track new remote server
            4. Deploy network components
            > 2
            1. Modify existing fork rules
            2. Export genesis configurations
            3. Remove genesis configuration
            > 2
            * Which folder to save the genesis specs into? (default = current)
            Will create mynetwork.json, mynetwork-aleth.json, mynetwork-harmony.json, mynetworkparity.json
            * enter를 치고 ctrl+c로 exit핚다.
            
            ```

      3. Genesis 블록을 이용해 Node 초기화

         ```shell
         c:\blockchain> geth --datadir . init mynetwork.json
         ```

         * geth 커맨드를 사용해서 현재 디렉토리에 private node 데이터를 저장한다는 뜻. init은 private node를 초기화 하기 위한 genesis 블록의 파일 이름을 지정할 때 사용하는 옵션이다. 
         * geth와 keystore 디렉토리가 생성되었는지 확인

      4. Account 생성(2개의 계정을 만들기)

         ```sh
         c:\blockchain> geth --datadir . account new
         ```

         * 비번은 1234로 해서 2번 입력

      5. Account 목록 보기

         ```sh
         c:\blockchain> geth --datadir . account list
         ```

      6. 해당 디렉터리에 password.sec 파일 작성

         1. 내용에 비밀번호(1234) 입력해서 저장

      7. 해당 디렉터리에 nodestart.cmd 파일 작성

         1. 다음 내용을 작성(geth 버전 1.8 기준)
            * geth --networkid 4386 --mine --minerthreads 2 --datadir "./" --nodiscover --rpc --rpcport "8545" --rpccorsdomain "*" --nat "any" --rpcapi eth,web3,personal,net --unlock 0 -- password ./password.sec
         2.  geth 버전이 1.9이상인 경우
            * geth --networkid 4386 --mine --minerthreads 2 --datadir "./" --nodiscover --rpc --rpcport "8545" --rpccorsdomain "*" --nat "any" --rpcapi eth,web3,personal,net --allow-insecure-unlock --unlock  0 -- password ./password.sec

      8. nodestart.cmd 파일 실행

         * nodestart.cmd 입력
         * c:\blockchain> nodestart.cmd

      9. C:\Users\student\AppData\Ethash 폴더에 파일이 생겼는지 확인

      10. 마이닝 멈추는 방법

          1. 새 cmd 창을 열어 다음 명령어 입력
             *  c:\blockchain> geth attach ipc:\\.\pipe\geth.ipc
          2. 채굴 확인
             * eth.mining
             * true이면 채굴중, false면 채굴 종료
          3. 채굴 멈춤
             * miner.stop()

2. 채굴량 확인

   1. 노드로 연결

      * c:\blockchain> geth attach ipc:\\.\pipe\geth.ipc

   2. 코인베이스 계정(첫번째 계정) 확인

      * \> eth.coinbase

   3. 전체 주소 확인

      * \> eth.accounts

   4. 잔액 확인(코인베이스)

      * \> eth.getBalance(eth.coinbase)
      * \> web3.fromWei(eth.getBalance(eth.accounts[0]),"ether")

   5. 잔액 확인(두번째 계정)

      * \> eth.getBalance(eth.accounts[1])

   6. 그 밖의 명령어들

      * Ether API 사용

      1. 현재 채굴된 블록의 번호 확인
         * \> eth.blockNumber
      2. 현재 해쉬레이트
         * \> eth.hashrate
      3. Gas Price 정보 확인
         * \> eth.gasPrice
      4. 현재 진행을 기다리고 있는 트랜잭션
         * \> eth.pendingTransactions
      5. 채굴 시작
         * \> miner.start()
      6. 채굴 확인
         * \> eth.mining

      * Ether API 사용

      1. 실행중인 Geth 노드가 현재 모든 데이터베이스를 저장하는 데 사용하는 절대 경로
         * \> admin.datadir
      2. 실행중인 Geth 노드에 대한 정보
         * \> admin.nodeInfo

3. 계좌간 송금

   * miner.start() 를 하고 실행

   1. 송금을 하기 위해 200초 동안 두번째 계정을 unlock 시킨다.
      * \> personal.unlockAccount(eth.accounts[1],“1234",200)
   2. 코인베이스에서 2번째 계정으로 20 ether를 송금하는 명령어
      * \> var tx = {from:eth.coinbase,to:eth.accounts[1],value:web3.toWei(20,"ether")}
      * tx를 통해 확인
      * personal.sendTransaction(tx,“1234") 
        * => 입력 후  나오는 txid를 기억
   3. 트랜잭션 정보 확인
      * eth.getTransaction(txid)
   4. 두번째 계정의 잔액을 확인
      * \> eth.getBalance(eth.accounts[1])
      * \> web3.fromWei(eth.getBalance(eth.accounts[1]), "ether")

   * 채굴 멈춤 상태에서 송금해서 pendingTransactions 확인해보기( minor.stop() )

   1. 송금
      * \> var tx = {from:eth.coinbase,to:eth.accounts[1],value:web3.toWei(20,"ether")}
      * \> personal.sendTransaction(tx,“1234")
      * \> eth.getTransaction(txid) -> 정보 확인
   2. 진행을 기다리고있는 트랜잭션을 확인
      * \> eth.pendingTransactions
   3. 이후 miner.start를 통해 처리되었는지 확인하면 됨
      * \> miner.start()
      * \> eth.getTransaction(txid)
      * \> eth.getBlock(blockid)
      * \> eth.getBalance(eth.accounts[1])