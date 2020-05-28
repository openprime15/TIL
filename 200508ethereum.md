### 이더리움 부동산 앱 실행

1. 기본세팅
   1. truffle을 버전에 맞게 설치
      1. npm uninstall -g truffle
      2. npm install -g truffle@4.1.15
   2. truffle migrate --compile-all --reset --network ganache
   3. 실행 잘되면 다음 명령어 실행
      1. npm run dev
2. localhost:3000/ 으로 확인



Spring Framework

- DI, Spring-Test, jdbc, MyBatis, AOP, Servlet/JSP Spring MVC (FSP, JSTL)
- REST Service (JSON, XML)

Spring Boot

- JPA, Spring MVC(Thymeleaf), REST



void: 리턴값 필요없음

abstract:  이하에 나오는 함수들 무조껀 써야함

protected: 상속간 사용가능 + 다른패키지에도 들어갈수 있음

private: 다른 패키지는 들어갈 수없음

프레임워크와 라이브러리의 차이: 통제권(유저코드를 유저가제어(라이브러리), 유저코드를 프레임워크가 제어(프레임워크))

