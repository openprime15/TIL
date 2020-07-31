### Spring Framework 정리

1. Framework 란?

   * 비기능적인 요소(로깅, 인증, Tx처리, 성능...)를 구현해놓은 라이브러리(구현체)
     (Semi Completed)
   * 개발의 생산성을 높이기 위함

2. Library 와 Framework 의 차이점

   * 제어 흐름의 주도권을 누가 가지고 있는지 여부
     * Library : 개발자가 제어 흐름의 주도권을 가지고 있다.
     * Framework: 프레임워크(Container)가 제어 흐름의 주도권을 가진다.

3. 프레임워크의 구성요소

   * Library + Design Pattern + Container

4. IoC (Inversion of Control)

   * 제어의 역전 -> 개발자가 가지고 있던 주도권을 가져와서 개발자가 작성한 클래스의 객체를 생성하고,
     메소드를 호출해준다.
   * Spring Bean Container를 제공한다.
   * IoC 구현하는 방법
     * DL (Dependency Lookup) - 의존하는 객체를 Look up, 
       																				JNDI(Java Naming Directory Interface) 를 사용
     * DI  (Dependency Injection) - 의존하는 객체를 주입(injection)을 받겠다.

5. DI(Dependency Injection)

   * Setter Injection

     * setter method의 Argument로 의존하는 객체의 레퍼런스(주소)를 한개씩 주입받겠다.

   * Constructor Injection

     * 객체가 생성될 때 생성자의 Argument로 의존하는 객체의 레퍼런스를 한꺼번에 여러개를 주입받는 방식

   * 전략1 : Bean에 설정을 모두 XML에 하는 방식

     * <bean> <property> <constructor-arg>

   * 전략2 : Bean 설정을 어노테이션과 XML을 혼용하겠다.

     * @Component @Repository @Service @Controller
       @Autowired @Qualifier @Value

     * 컴포넌트 오토 스캐닝

       <context:component-scan base-packages="" />

   * 전략3 : Bean 설정을 어노테이션과 Java 설정 클래스(Configuration)를 사용하겠다. (No XML)

     * @Configuration @Bean @ComponentScan

6. Spring MVC 관련 어노테이션

   * @Controller 
     @RestController : @Controller + @ResponseBody

     @RequestMapping

     @RequestParam

     ?key=value

     @PathVariable
     users/gildong

     @ModelAttribute

     

     public String method(){

     ​	return "userList";
     }

