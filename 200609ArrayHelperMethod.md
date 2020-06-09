#### JS Array Helper Method 정리

1. forEach()

   * Array의 요소를 순회한 뒤 출력함
   * for문처럼 반복적인 작업에 활용
   * forEach는 return값이 없음(map()은 return값이 있음)

   1. for문 예제

      ```js
      const colors = ["red", "blue", "green"];
      // 배열의 요소를 순회 & 출력
      for (let i = 0; i < colors.length; i++) {
        console.log(colors[i]);
      }
      // 순회문으로서의 for문
      for (let color of colors) {
        console.log(color);
      }
      ```

      

   2. forEach문 예제

      ```js
      const colors = ["red", "blue", "green"];
      // forEach 헬퍼 메서드
      const result = colors.forEach(function (color) {
        console.log(color);
      });
      
      console.log(result);
      ```

   * 같은 값을 출력함

2. filter()

   * 특정한 조건에 의해 필터된 Array문 제작에 사용
   * 특정 조건을 만족하는 Data만 Array에 남게됨

   1. filter() 없이 forEach()만으로 사용했을 때 예제

   ```js
   const numbers = [-20, -15, 5, 10];
   
   const positiveNumbers1 = [];
   numbers.forEach((number) => {
     if (number > 0) {
       positiveNumbers1.push(number);
     }
   });
   ```

   2. filter()를 활용한 예제
      * 0보다 작은 데이터를 제외하고 Array을 만드는 구문

   ```js
   const numbers = [-20, -15, 5, 10];
   //filter를 통해 코드를 줄일 수 있음
   const positiveNumbers2 = numbers.filter((number) => {
     return number > 0;
   });
   console.log(positiveNumbers2);
   ```

   

3. map()

   * map을 사용하는 경우
     * 순회를하며, 내부의 모든 요소에 동일한 작업을 해야할 때
     * 숫자배열 <-> 문자배열, 동일한 숫자, 또는 데이터를 적용해야하는 경우

   1. map()을 활용한 예제
      * String 타입 배열을 Number타입으로 변경

   ```js
   inputs = ["1", "5", "3", "6"];
   const numberInputs = inputs.map((input) => {
     return parseInt(input);
   });
   console.log(numberInputs);
   //forEach를 사용해서 NumberType 배열의 합을 구하는 예제
   let sum = 0;
   numberInputs.forEach((num) => {
     sum += num;
   });
   
   console.log(sum);
   ```

   

4. reduce()

   * 순회를 하며, 내부의 모든 요소를 **하나의 값**으로 환원해야 하는 경우

   1. reduce()를 활용한 예제

   ```js
   inputs = [1, 5, 3, 6];
   let sum = 0;
   sum = numberInputs.reduce(function (acc, cur) {
     console.log("누적값:" + acc);
     console.log("현재값:" + cur);
     return acc + cur;
   });
   
   console.log(sum);
   ```

5. find()

   * Array를 처음부터 순환하여 **조건을 먼저 만족하는 요소 하나만 리턴**
   * 배열의 요소를 리턴함

   1. find() 예제

   ```js
   const array1 = [5, 12, 8, 130, 44];
   
   const found = array1.find(element => element > 10);
   
   console.log(found);
   //조건을 먼저 만족하는 12만 출력됨
   ```

   

   * arr.find(콜백함수) 형식으로도 사용가능
     * arr이 find(콜백함수)를 순환하여 가장먼저 만족하는 data만 출력됨

6. every(), some()

   * 두 구문 모두 Array를 순환하며 boolean 값을 리턴한다.
   * every(): 조건을 모두 만족하면 True값을 리턴
     * 만약 순회중 조건을 만족하지않는 값이 발견되면 **그 즉시 순회 중단 후 False 리턴**
   * some(): 조건을 모두 만족하지 못하면 False값을 리턴
     * 만약 순회중 조건을 만족하는 값이 발견되면 **그 즉시 순회 중단 후 True 리턴**
     * 함수 표현식에서 return을 생략하려면 {}도 생략해야함

