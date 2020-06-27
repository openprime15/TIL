<<<<<<< HEAD
### GIT 기초

* 파일 편집, 보기
  * vim 파일명(.확장자) 를 통해 편집화면으로 진입
  * i(insert)를 통해 수정, 수정 후 ESC키, :, W(저장), Q(나가기)
  * 수정된 파일은 cat "파일명"을 통해 내용 확인 가능
* 버전관리 하기
  * GIT은 버전관리, 비관리의 구분을 하기 때문에 버전관리 등록이 필요
  * 버전관리 여부 확인 명령어는 git status
  * git add 파일명 을 통해 버전관리 등록
* 버전 명 설정
  * 버전관리 등록을 한 후 버전명 생성이 필요
  * git status를 통해 버전명 등록(commit)이 되었는지 확인 (안 되어있다면 붉은색 표시)
  * git commit을 통해 버전명 설정 페이지로 진입해서 버전명 등록
  * 버전명 등록 후 git status를 통해 확인
  * 버전 목록은 git log를 통해 확인
* 새 버전 생성
  * vim 파일명 을 통해 파일 수정
  * 수정 후 git add를 통한 버전관리 등록 필요! (commit 전에 add를 꼭 해야함)
  * git add를 먼저 하는 이유는 commit 대기상태로 만들기 위함
  * git commit을 통해 버전명 설정
  * git status를 통해 버전명 설정여부 확인 가능
  * git log(파일 수정 역사)를 통해 버전 목록 확인 가능
### GIT 관련 명령어

* code .
  * VS Code 실행

1. 코드 관리 명령어

   1. git init

   2. git status

      * 현재 repo 상태 확인

   3. git add [파일명/폴더명]

      * 새로운 버전에 추가할 파일 설정
      * git add . <= 변경된 파일 전체 add

   4. git commit (-m "버전명")

      * 새로운 버전 생성

   5. git log (--online)

      * 저장된 버전(commit)을 조회

   6. git checkout [버전 해시값]

      * 입력한 버전명으로 롤백
      * 다시 원상복귀가 필요하면 git checkout master

   7. git stash

      * 최근 commit한 파일을 임시저장소(stash)로 보냄

      1. git stash list
         * 지금 stash된 버전명 확인
      2. git stash pop
         * 저장소 파일을 원상복귀시키는 명령어

2. 원격 저장소 관련 명령어

   1. git remote
      * 현재 설정된 모든 원격저장소에 관한 정보를 조회
   2. git remote add [저장소의 별명] [저장소의 주소]
      * 저장소의 주소로 현재 디렉토리를 맵핑
      * ex) git remote add origin https://github.com/openprime15/test_repo.git
   3. git push [저장소의 별명] [브랜치의 이름]
      * ex) git push origin master
   4. git clone [저장소의 주소] ([디렉토리 이름])

3. branch 관련 명령어

   1. git branch
      * branch 확인
   2. git branch [새로 만들 브랜치명]
      * 새로운 branch 생성
   3. git checkout [이동할 branch명]
      * 입력한 branch로 이동
   4. git merge [(합칠) 브랜치명]
      * ex) git checkout master => git merge test: 이러면 test에 있는 파일이 merge로 들어옴
   5. git branch -d [(삭제할)브랜치명]
      * 브랜치 삭제 명령어
      * git branch -D [브랜치명]
      * -D 는 병합되지않은 브랜치를 삭제할 경우
   6. 명령어를 활용한 협업 실습
      1. 만들어진 레포를 받음
         1. git clone [레포 주소]
         2. git check -b [브랜치명]
         3. 내용 수정 후 add, commit
         4. git push origin [브랜치명] <== **master**가 아님
      2. pull request 가 끝난뒤
         1. master branch 변경 후(git checkout -b master)
         2. git pull origin master <== **항상 pull은 master branch에서 해야한다**
      3. merge된 branch 받아온 뒤 기존 branch 제거 과정
         1. git checkout [merge된 브랜치명]
         2. git branch -D [제거할 브랜치명]
         3. git pull origin [merge된 브랜치명]
         4. git branch [새로 만들 브랜치명]
         5. git checkout [새로만들 브랜치명]

