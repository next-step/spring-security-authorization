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
 
## 2단계 - 요청별 권한 검증 정보 분리
요청별 권한 검증에 필요한 규칙은 security 패키지에 위치하는 것 보다 app 패키지 내에 위치하는게 자연스럽다. RequestMatcherRegistry와 RequestMatcher를 이용하여 요청별 권한을 설정하는 부분을 객체로 분리하고, 
이 객체를 SecurityConfig에서 전달하기 수월한 구조로 만든다.

실제 RequestMatcherDelegatingAuthorizationManager에서 RequestMatcherRegistry 정보들을 가지는 변수의 타입이 List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> 인데, 편의상 RequestAuthorizationContext부분은 제거하고 List<RequestMatcherEntry<AuthorizationManager>>로 하는 것을 추천한다.
RequestMatcherDelegatingAuthorizationManager객체의 mappings 정보는 AuthorizeHttpRequestsConfigurer를 통해서 설정되는데 HttpSecurity를 비롯한 XXXConfigurer의 동작은 4일차에서 진행할 예정이므로 지금 단계에서는 무시하고 AuthorizationManager객체와 mappings을 생성하는 것에 집중한다.

## 목표 
- [X] RequestMatcherRegistry와 RequestMatcher를 작성하고, RequestMatcher의 구현체를 작성한다.
    - [X] AnyRequestMatcher: 모든 경우 true를 리턴한다.
    - [X] MvcRequestMatcher: method와 pattern(uri)가 같은지 비교하여 리턴한다.
- [X] RequestMatcherEntry 작성 

### 예시 
- /login은 모든 요청을 받을 수 있도록 PermitAllAuthorizationManager로 처리  
- /members/me는 인증된 사용자만에게만 권한을 부여하기 위해 AuthenticatedAuthorizationManager로 처리  
- /members는 "ADMIN" 사용자만에게만 권한을 부여하기 위해 HasAuthorityAuthorizationManager로 처리  
  



