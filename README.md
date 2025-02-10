# spring-security-authorization

## 1단계 - AuthorizationManager를 활용

AuthorizationManager를 활용하여 인가 과정 추상화
스프링 시큐리티는 다양한 인가 방식을 처리할 수 있도록 AuthorizationManager 형태로 추상화해 놓았다. 기존에 구현한 다양한 인가 처리 로직을 추상화 하고 인가 방식에 따른 구현체를 구현한다.

### 목표 
SecuredMethodInterceptor와 AuthorizationFilter에서 작성된 인가 로직을 AuthorizationManager로 리팩터링 한다.
주요 클래스: AuthorizationManager, RequestMatcherDelegatingAuthorizationManager, AuthorityAuthorizationManager, SecuredAuthorizationManager

 
- [X] 인가에 대한 추상화 객체인 AuthorizationManager 작성
- [X] 인가에 대한 결정을 담은 VO AuthorizationDecision 작성
- [X] CheckAuthenticationFilter에 사용될 RequestMatcherDelegatingAuthorizationManager 구현 
 
