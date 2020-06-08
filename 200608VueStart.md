### Vue 시작

* 세팅

1. CDN

   ```js
   <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
   ```

   

2. Vue 공식문서

   * https://kr.vuejs.org/v2/guide/index.html

##### 

#### I. 핵심 개념

**1. SPA**

- Single Page Application : 페이지 전환 없이 UI를 변화
- 왜? 사용자 편의성, 페이지 리소스 절약 목적
  - 아래로 스크롤해서 새로운 정보를 전달하는 웹페이지(ex. Facebook)의 경우 새롭게 페이지가 바뀌게 되면 처음 부터 다시 내려서 데이터를 봐야하는 문제가 생김
  - 따라서 사용자의 편의에 맞는 페이지 개발이 됨
- 역사
  - 기존 SPA는 Adobe FlashPlayer를 통해 구현(기존 HTTP의 요청, 응답구조와 다르게 작동)
  - 하지만 Adobe사의 라이센스 정책 등의 문제로 순수 Javascript만으로 SPA를 구현하려는 움직임이 일어남
  - Google 지도는 Javascript로 동적 구현을 한 예
  - 하지만 코드 복잡함의 문제가 제기되어 SPA용 Javascript Framework가 제작되기 시작

**2. MVVM**

* MVC(Model-View-Control): Model과 View를 Controller가 제어(ex. Spring)
* MVVM(Model-View_ViewModel): Model이 바뀌면 View가 바뀌도록 ViewModel이 자동으로 컨트롤

**3. Declarative Programming**

* 선언적 프로그래밍 <-> 명령형 프로그래밍(Imperative Programming)
* 명령형(절차적): 변화를 단계적으로 명령하는 방식
  * Vanilla Javascript DOM 조작
* 선언형: 변화를 선언(그림 그리기)
  * 프레임워크를 통한 조작



#### II.Vue.js의 요소

**1. 지시자(Directive)**

* 'v-for' : 배열과 같은 데이터를 순회

* 'v-if' : 조건부 렌더링

* 'v-else : 조건부 렌더링

* 'v-on' : 이벤트 바인딩('addEventListener()')

  * v-on:click="함수명"
  * v-on:keypress.enter="함수명"

* 'v-model' : 양방향 데이터 바인딩

  * ex

    ```html
    <input type="text" v-model="newInput" />
    <!-- newInput에서 값을 받음 --!>
    ```

* 'v-bind': 클래스 바인딩에 쓰임

  * v-bind:class="{조건부로 적용할 클래스 이름: boolean값}"

  

