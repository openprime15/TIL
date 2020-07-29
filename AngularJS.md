## AngularJS

1. 개발환경 세팅

   1. JDK1_8 / Apache Tomcat 9 /Eclipse(Java EE) 설치

   2. Eclipse Workspace 경로 수정 / Server 추가(Tomcat 9)

   3. CDN 추가

      ```javascript
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular.min.js"></script>
      ```

      

2. 기본 개념

   1. 지시자
      1. ng-app : AngularJS 에 대한 초기설정
      2. ng-init : 사용할 데이터 설정
      3. ng-model : HTML에서 입력 요소들을 컨트롤 하기 위한 지시자
      4. ng-repeat: 지정된 태그를 배열에 들어 있는 요소 만큼 반복하여 출력
      5. 지시자 생성: directive
         * restrict 속성 (E: 태그,A: 속성,C: class 속성,M: 주석, 생략시 EA)
      6. 기타
         1. ng-repeat : for문
         2. ng-show: 조건에 맞으면 생성, 유효성 검사($error)
         3. $valid 유효성 확인 / $dirty 초기값 변경 / $touched 포커스 확인
         4. ng-click: 클릭 이벤트 리스너
      
   2. Controller 
      
      1.  데이터를 세팅해서 가져가도록 하는 개념
      2.  $scope 객체를 활용해 값 설정
      3.  Controller 대신 .run(function($rootScope){}) 로 데이터 설정 가능(하지만 컨트롤러가 우선)
      
   3. Filter : 구문 | 필터

      1. 조건에 따라 설정 후 출력
      2. 

   4. Service : AngularJS에서 제공되는 다양한 객체

      1. $location, $http, $timeout 등
      2. .service 를 이용해 커스텀 Service를 만들 수 있음
      3. $http를 통해 json 방식도 가져올 수 있음

   5. Select : 태그 구성을 위해 ng-repeat 나 ng-options 를 사용

   6. Route

      1. CDN 추가

         ```javascript
         <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-route.js"></script>
         ```

      2. 모듈에 라우터 설정

         ```javascript
         	const app = angular.module("test_app",["ngRoute"])
         ```

      3. DOM 부분

         ```javascript
         <div ng-view></div>
         ```

      4. 라우팅 부분

         ```javascript
         	app.config(function($routeProvider){
         		$routeProvider.when("/", {
         			template : "<h1>Main Page</h1>"
         		})
         	})
         ```

   7. UI-Router : 데이터 세팅 view를 여러개 두어 각 부분별로 제어 가능

      1. CDN 추가

         ```javascript
         <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/1.0.27/angular-ui-router.js"></script>
         ```

         

      2. DOM 부분

         ```javascript
         <ui-view></ui-view>
         ```

      3. 모듈에 라우터 설정

         ```javascript
         	const app = angular.module("test_app",["ui.router"]);
         ```

      4.  라우팅 부분

         ```javascript
         	app.config(function($stateProvider){
         		const mainState = {
         			name : "main",
         			url : "/main",
         			template : "<h1>Main</h1>"
         		}
         		$stateProvider.state(mainState)
         	})
         ```

      5. 하이퍼링크 : name값 main을 찾음

         ```javascript
         	<a ui-sref="main">main</a>
         ```

      6. 컴포넌트를 통해 탬플릿 분리 가능

         ```javascript
         	app.component("mainComponent", {
         		template : "<h1>Main</h1>"
         	})
         
         		const mainState= {
         			name: "main",
         			url: "/main",
         			// template : "<h1>Main</h1>"
         			component : "mainComponent"
         		}
         ```

         

   8. ㄴㅇㄹ

   9. sdfsdf


