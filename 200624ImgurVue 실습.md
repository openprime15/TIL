### Imgur API를 활용한 Vue 실습

1. imgur 사이트 접속 후 회원가입
2. imgur Register an Application 으로 가서 키를 받으면 됨(imger-uploader)
3. Vue 설치
   1. vue create imgur-uploader
   2. npm i loadash qs axios
   3. vue add router
   4. vue add vuex
4. semantic ui 적용
   1. index.html에 다음 명령어 붙여넣기
      * <link
              rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"
            />
5. 정리
   1. 로그인 버튼을 누르면, imgur OAUTH 페이지로 이동
   2. 사용자가 imgur에서 권한을 준다.
   3. Imgur가 AccessToken 과 함께 우리 APP으로 redirect
   4. /oauth2/callback 으로 오면, AuthHandler 컴포넌트 렌더
   5. AuthHandler 컴포넌트에서 action(finalizeLogin) 실행
   6. finalizeLogin 에서 토큰 추출 + state 갱신
   7. / 으로 리다이렉트

6. 2번째 세팅

   1. npm i vue-cookies

      **main.js**

      ```javascript
      import VueCookies from 'vue-cookies'
      
      Vue.use(VueCookies)
      ```

      쿠키를 사용하는 파일에도 import시켜줘야함

      
   

