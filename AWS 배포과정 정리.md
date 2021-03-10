### AWS 배포과정 정리

1. python 자동실행파일 batch 설정

   1. python 파일을 ec2로 옮긴다 (WinSCP 활용)

   2. venv 사용

      1. sudo yum install python3 -y
      2. python3 -m venv my_app/env
      3. source ~/my_app/env/bin/activate
      4. 가상환경으로 들어가짐 이후 pip install을 통한 의존성 설치
      5. deactivate로 나옴
      6. 가상환경 자동 활성화 사용
      7. echo "source ${HOME}/my_app/env/bin/activate" >> ${HOME}/.bashrc
      8. source ~/.bashrc 만 쓰면 가상환경으로 이동이된다

   3. crontab 사용

      1. crontab -e 를 이용해서 vi에서 설정함

      2. ```bash
         * * * * * source ~/.bashrc && python batch/yf_btch.py > batch.test.log
         ```

         

      3. 이렇게 쓰면 가상환경으로 들어간 뒤 / python파일을 실행시켜 준다.
   
2. 서버배포

   1. 인텔리J 경우 우측 Maven 클릭 후 package -> maven build
   2. 완료된 파일은 target 안에 있음
   3. winScp로 로그인 및 putty 접속
   4. putty 접속 후 ps -ef 로 확인
   5. ID 확인 후 kill -9 [pid값]
   6. 폴더에 jar파일 복사 후 다음 명령어로 실행
   7. nohup java -jar [...jar] &

3. 프론트 배포

   1.  yarn build --prod
   2. build 폴더 안에 생김
   3. nginx 중지
   4. sudo systemctl stop nginx
   5. ps -ef 로 확인
   6. 해당 파일 복사 후 nginx 실행
   7. sudo systemctl start nginx

