#### Spring MVC setting

1. 기본 세팅 : Java / eclipse EE / tomcat 9.0 설치

2. 프로젝트 생성 후, Maven 프로젝트로  변경한 뒤 pom.xml을 통해 라이브러리 세팅

   1. 다이나믹 웹 프로젝트 생성

   2. 프로젝트우클릭 config -> convert to Maven Project -> finish

   3. pom.xml에 다음의 라이브러리를 추가한다

      1. servlet-api / jsp-api / jstl / springWebMVC

      2. maven 사이트로 들어가 dependency를 받음

         **pom.xml**
         
         ```java
         </build>
         <!-- 라이브러리 버전관리 -->
         	<properties>
         		<javax.servlet-version>4.0.1</javax.servlet-version>
         		<javax.servlet.jsp-version>2.3.3</javax.servlet.jsp-version>
         		<javax.servlet.jsp.jstl-version>1.2</javax.servlet.jsp.jstl-version>
         		<org.springframework-version>5.2.2.RELEASE</org.springframework-version>
         		<!-- <org.springframework-version>4.3.25.RELEASE</org.springframework-version> -->
         	</properties>
         	<!-- 라이브러리 셋팅 -->
         	<dependencies>
         		<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
         		<dependency>
         			<groupId>javax.servlet</groupId>
         			<artifactId>javax.servlet-api</artifactId>
         			<version>${javax.servlet-version}</version>
         			<scope>provided</scope>
         		</dependency>
         		<!-- https://mvnrepository.com/artifact/javax.servlet.jsp/javax.servlet.jsp-api -->
         		<dependency>
         			<groupId>javax.servlet.jsp</groupId>
         			<artifactId>javax.servlet.jsp-api</artifactId>
         			<version>2.3.3</version>
         			<scope>${javax.servlet.jsp-version}</scope>
         		</dependency>
         		<!-- https://mvnrepository.com/artifact/javax.servlet.jsp.jstl/jstl -->
         		<dependency>
         			<groupId>javax.servlet</groupId>
         			<artifactId>jstl</artifactId>
         			<version>${javax.servlet.jsp.jstl-version}</version>
         		</dependency>
         		<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
         		<dependency>
         			<groupId>org.springframework</groupId>
         			<artifactId>spring-webmvc</artifactId>
         			<version>${org.springframework-version}</version>
         		</dependency>
         
         	</dependencies>
      </project>
         ```

3. 이후 XML 세팅하는 방법과 Java 코드로 세팅하는 방법 2가지가 있다.

   * 아래 설정을 하는 이유
     * MVC 기본 동작 방식 : 사용자의 요청이 들어올때 Controller를 거치기 전 Apache Tomcat이 관리하는 Servlet에서 먼저 반응을 한다. 이 Servlet이 DispatcherServlet이고 이 것이 어떤 Controller를 움직여야하는지를 결정한다. / view 포워딩은 viewResolver가 담당
     * Spring MVC는 기존 Spring Framework의 기능을 활용하기위해 DispatcherServlet을 재정의한다. 따라서 DispatcherServlet 클래스를 MVC에서 재정의한 클래스로 설정하는 과정이 필요

   1. XML 세팅방법

      1. web.xml 설정(Servers/Tomcat v9.0 아래)

         1. 17행쯤 다음 부분 복사

            ```java
            <web-app version="4.0" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee                       http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">
            
            ```

         2. 프로젝트 경로 / WebContent / WEB-INF / **web.xml 생성** 후 붙여넣기

            ```java
            <?xml version="1.0" encoding="UTF-8"?>
            <web-app version="4.0" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee                       http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">
            </web-app>
            ```

         3. Tomcat/web.xml의 430행쯤 복사 후 붙여넣기 / servlet-name 변경

         4. Tomcat/web.xml의 108행 부분을 init-param부분 제거 및 복사 후 붙여넣기 / 이름변경

            ```java
         <?xml version="1.0" encoding="UTF-8"?>
            <web-app version="4.0"
            	xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee                       http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">
            	<!-- 현재 웹 애플리케이션에서 받아들이는 모든 요청에 대해 appServlet이라는 이름으로 정의되어있는 서블릿을 사용하겠다. -->
            	<servlet-mapping>
            		<servlet-name>appServlet</servlet-name>
            		<url-pattern>/</url-pattern>
            	</servlet-mapping>
            	<!-- 요청 정보를 분석해서 컨트롤러를 선택하는 서블릿을 지정한다. -->
            	    <servlet>        
                    <servlet-name>appServlet</servlet-name>
                    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
                    <load-on-startup>1</load-on-startup>
                </servlet>
            
            </web-app>
            ```
         
         5. WebContent / index.jsp 생성후 되는지 확인(console err가 남)

      2. ApplicationContext / RootContext 파일 설정(Bean 정의)

         1. WEB-INF / config 폴더 생성

         2. config 폴더에 root-context.xml / servlet-context.xml 생성

            * servlet-context.xml은 DispatcherServlet에 맞춰 움직임 

         3. web.xml에 다음 명령어를 추가 / 변경

            **web.xml**

            ```xml
            	<!-- 요청 정보를 분석해서 컨트롤러를 선택하는 서블릿을 지정한다. -->
            	    <servlet>
                    <servlet-name>appServlet</servlet-name>
                    <!-- Spring MVC에서 제공하고 있는 기본 서블릿을 지정한다. -->
                    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
                    <!-- Spring MVC 설정을 위한 xml 파일을 지정한다. -->
                    <init-param>
                    	<param-name>contextConfigLocation</param-name>
                    	<param-value>/WEB-INF/config/servlet-context.xml</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
                </servlet>
                
                <!-- Bean을 정의할 xml 파일을 지정한다. -->
                <context-param>
                	<param-name>contextConfigLocation</param-name>
                	<param-value>/WEB-INF/config/root-context.xml</param-value>
                </context-param>
                
                <!-- 리스너 설정 Bean들을 구성해주는 부분 -->
                <listener>
                	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
                </listener>
            ```

         4. 실행시 404에러가 난다면 성공적으로 세팅이 끝난것

      3. 파라미터 필터 설정: 한글깨짐 방지위한 인코딩

         1. web.xml에 다음 내용 추가

            ```xml
            	<!-- 파라미터 인코딩 필터 설정 -->
            	<filter>
            		<filter-name>encodingFilter</filter-name>
            		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
            		<init-param>
            			<param-name>encoding</param-name>
            			<param-value>UTF-8</param-value>
            		</init-param>
            		<init-param>
            			<param-name>forceEncoding</param-name>
            			<param-value>true</param-value>
            		</init-param>
            	</filter>
            	<!-- /* 모든 요청에 대해 이 필터를 지나도록 함 -->
            	<filter-mapping>
            		<filter-name>encodingFilter</filter-name>
            		<url-pattern>/*</url-pattern>
            	</filter-mapping>
            ```

         1. index.jsp 삭제

      4. bean 설정

         1. root-context.xml에 다음 명령어로 bean 설정

            ```xml
            <beans xmlns="http://www.springframework.org/schema/beans"
            	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            	xsi:schemaLocation="http://www.springframework.org/schema/beans
            						http://www.springframework.org/schema/beans/spring-beans.xsd">
            </beans>
            ```

         2. servlet-context.xml에서 다음 명령어 입력

            ```xml
            <?xml version="1.0" encoding="UTF-8"?>
            <beans:beans xmlns="http://www.springframework.org/schema/mvc"
            			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            			xmlns:beans="http://www.springframework.org/schema/beans"
            			xmlns:context="http://www.springframework.org/schema/context"
            			xsi:schemaLocation="http://www.springframework.org/schema/mvc
            								http://www.springframework.org/schema/mvc/spring-mvc.xsd
            								http://www.springframework.org/schema/beans
            								http://www.springframework.org/schema/beans/spring-beans.xsd
            								http://www.springframework.org/schema/context
            								http://www.springframework.org/schema/context/spring-context.xsd">
            
            <!-- 스캔한 패키지 내부의 클래스 중 Controller 어노테이션을 가지고 있는 클래스들을 Controller로 로딩하도록 한다. -->
             <annotation-driven />
             
             <!-- 스캔할 bean들이 모여있는 패키지를 지정한다. --> 
            <context:component-scan base-package="kr.co.softcampus.controller" />
            			</beans:beans>
            ```

      5. controller 설정

         * Java Resources / src / 패키지 생성(kr.co.softcampus.controller)
         * 패키지에 class 생성(HomeController)
         * 클래스 위에 @Controller 입력 후 임포트
         * **HomeController.java**

         ```java
         package kr.co.softcampus.controller;
         
         import org.springframework.stereotype.Controller;
         import org.springframework.web.bind.annotation.RequestMapping;
         import org.springframework.web.bind.annotation.RequestMethod;
         
         @Controller
         public class HomeController {
         	// "/" 이 주소로 get방식으로 들어오면 아래 클래스를 호출하겠다.	
         	@RequestMapping(value = "/", method = RequestMethod.GET)
         	public String home() {
         		System.out.println("home");
         		return null;
         	}
         }
         ```

         1. 실행하면 500 Err : JSP가 결정되지 않았기 때문에 오류가 뜸

         2. WEB-INF/views 폴더 생성 후 index.jsp 생성

         3. HomeController 의 home 리턴값을 jsp로 변경

            ```java
            return "/WEB-INF/views/index.jsp";
            ```

      6. 리턴값에 계속 쓰일 공통부분을 prefix / suffix를 써서 세팅

         * HomeController 의 home 리턴값 변경

         ```java
         return "index";
         ```

         * servlet-context.xml에 다음 명령어 추가

         ```xml
         	<!-- Controller의 메서드에서 반환하는 문자열 뒤에 붙일 경로 정보를 세팅한다. -->
         	<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
         		<beans:property name="prefix" value="/WEB-INF/views/"></beans:property>
         		<beans:property name="suffix" value=".jsp"></beans:property>
         	</beans:bean>
         ```

      7. 정적파일 경로 설정

         1. servlet-context.xml에 다음 명령어 추가

            ```xml
            	<!-- 정적파일 경로 세팅(이미지, 사운드, 동영상, js, css 등등) -->
            	<resources location="/resources/" mapping="/*" />
            ```

         2. 이미지파일 WebContent / resources/ image 폴더 생성후 추가

         3. index.jsp에 다음 명령어로 되는지 확인

            ```html
            <img src="image/파일명" />
            ```

   2. Java class로 세팅방법

      * web.xml : AbstractAnnotationConfigDispatcherServletInitializer 상속 혹은 WebApplicationInitializer 인터페이스 구현
      * root-context.xml : 상속 없음
      * servlet-context.xml : WebMvcConfigurer 인터페이스 구현

      1. WebApplicationInitializer 인터페이스 구현

         1. Java Resources / src / 패키지 생성(kr.co.softcampus.config)

         2. SpringConfigClass.java 생성 / 클래스 뒤에  implements WebApplicationInitializer 입력 후 import 및 add

            ```java
            package kr.co.softcampus.config;
            
            import javax.servlet.ServletContext;
            import javax.servlet.ServletException;
            
            import org.springframework.web.WebApplicationInitializer;
            
            public class SpringConfigClass implements WebApplicationInitializer {
            
            	@Override
            	public void onStartup(ServletContext servletContext) throws ServletException {
            		// TODO Auto-generated method stub
            		System.out.println("onStartUp");
            	}
            
            }
            ```

            실행하면 404Err / 콘솔에 출력되는지 확인

      2. 프로젝트 설정파일(servlet / class) 지정

         1. SpringConfigClass.java 위치에 ServletAppContext.java 파일 생성

            1. WebMvcConfigurer 상속

            ```java
            package kr.co.softcampus.config;
            
            import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
            
            // Spring MVC 프로젝트에 관한 설정을 하는 클래스
            public class ServletAppContext implements WebMvcConfigurer {
            	
            }
            ```

         2. SpringConfigClass.java 의 onStartup 메소드에 다음 명령어 추가

            ```java
            		// Spring MVC 프로젝트 설정을 위해 작성하는 클래스의 객체를 설정한다.
            		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
            		servletAppContext.register(ServletAppContext.class);
            		
            		//요청 발생 시 요청을 처리하는 서블릿을 DispatcherServlet으로 설정해준다.
            		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
            		
            		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
            		
            		// 부가설정
            		servlet.setLoadOnStartup(1);
            		servlet.addMapping("/");
            ```

      3. Bean을 정의할 클래스 / 리스너 지정

         1. SpringConfigClass.java 위치에 RootAppContext.java 파일 생성

         2. SpringConfigClass.java 의 onStartup 메소드에 다음 명령어 추가

            ```java
            		// Bean을 정의하는 클래스를 지정한다.
            		AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
            		rootAppContext.register(RootAppContext.class);
            		
            		ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
            		servletContext.addListener(listener);
            		
            		// 파라미터 인코딩 설정
            		FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
            		filter.setInitParameter("encoding", "UTF-8");
            		filter.addMappingForServletNames(null, false, "dispatcher");
            ```

      4. View 의 경로 설정

         1.  ServletAppContext.java에서 configureViewResolvers 메소드를 생성하여 작업

            ```java
            // Spring MVC 프로젝트에 관한 설정을 하는 클래스
            @Configuration
            // Controller 어노테이션이 셋팅되어있는 클래스를 Controller로 등록한다.
            @EnableWebMvc
            // 스캔할 패키지를 지정한다.
            @ComponentScan("kr.co.softcampus.controller");
            public class ServletAppContext implements WebMvcConfigurer {
            	// Controller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙혀주도록 설정한다.
            	@Override
            	public void configureViewResolvers(ViewResolverRegistry registry) {
            		// TODO Auto-generated method stub
            		WebMvcConfigurer.super.configureViewResolvers(registry);
            		// 앞 뒤에 붙을 문자열
            		registry.jsp("/WEB-INF/views",".jsp");
            	}
            }
            ```

         2. 정적 파일의 경로 mapping을 위해 아래에 추가

            ```java
            // Spring MVC 프로젝트에 관한 설정을 하는 클래스
            @Configuration
            // Controller 어노테이션이 셋팅되어있는 클래스를 Controller로 등록한다.
            @EnableWebMvc
            // 스캔할 패키지를 지정한다.
            @ComponentScan("kr.co.softcampus.controller")
            public class ServletAppContext implements WebMvcConfigurer {
            	// Controller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙혀주도록 설정한다.
            	@Override
            	public void configureViewResolvers(ViewResolverRegistry registry) {
            		// TODO Auto-generated method stub
            		WebMvcConfigurer.super.configureViewResolvers(registry);
            		// 앞 뒤에 붙을 문자열
            		registry.jsp("/WEB-INF/views/",".jsp");
            	}
            	
            	// 정적 파일의 경로를 매핑한다.
            	@Override
            	public void addResourceHandlers(ResourceHandlerRegistry registry) {
            		// TODO Auto-generated method stub
            		WebMvcConfigurer.super.addResourceHandlers(registry);
            		registry.addResourceHandler("/**").addResourceLocations("/resources/");
            	}
            }
            ```

      5. 컨트롤러 추가

         1. src / 컨트롤러패키지 추가(kr.co.softcampus.controller)

         2. HomeController.java 생성

            * @Controller 설정
            * 클래스 내부에 @RequestMapping 등 설정

            ```java
            package kr.co.softcampus.controller;
            
            import org.springframework.stereotype.Controller;
            import org.springframework.web.bind.annotation.RequestMapping;
            import org.springframework.web.bind.annotation.RequestMethod;
            
            @Controller
            public class HomeController {
            
            	@RequestMapping(value = "/", method = RequestMethod.GET)
            	public String home() {
            		return "index";
            	}
            }
            ```

         3. WEB-INF / views 폴더 생성 후 index.jsp 생성 및 실행

         4. WebContent / resources / image폴더 생성 및 그림파일 저장

         5. index.jsp img 태그 추가 및 실행

      * Web.xml을 AbstractAnnotationConfigDispatcherServletInitializer로 구현하는 방법

      1. AbstractAnnotationConfigDispatcherServletInitializer 을 쓰면 더 간단해지는 대신 자율성은 떨어진다.

      2. SpringConfigClass.java의 클래스 부분을 주석처리하고 새로 작성

         ```java
         public class SpringConfigClass extends AbstractAnnotationConfigDispatcherServletInitializer{
         		// DispatcherServlet에 매핑할 요청 주소를 세팅한다.
         		@Override
         		protected String[] getServletMappings() {
         			// 모든 요청주소에 대해 dispatcherservlet이 반응하도록 하겠다.
         			return new String[] {"/"};
         		}
         		
         		// Spring MVC 프로젝트 설정을 위한 클래스를 지정한다.
         		@Override
         		protected Class<?>[] getServletConfigClasses() {
         		// TODO Auto-generated method stub
         		return new Class[] {ServletAppContext.class};
         		}
         		
         		// 프로젝트에서 사용할 Bean들을 정의하기 위한 클래스를 지정한다.
         		@Override
         		protected Class<?>[] getRootConfigClasses() {
         		// TODO Auto-generated method stub
         		return new Class[] {RootAppContext.class};
         		}
         		
         		// 파라미터 인코딩 필터 설정
         		@Override
         		protected Filter[] getServletFilters() {
         		// TODO Auto-generated method stub
         			CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
         			encodingFilter.setEncoding("UTF-8");
         			return new Filter[] {encodingFilter};
         		}
         		
         }
         ```

         


