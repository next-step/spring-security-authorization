# spring-security-authorization

## 🚀 1단계 - AuthorizationManager를 활용
- AuthorizationManager를 활용하여 인가 과정 추상화

## 🚀 2단계 - 요청별 권한 검증 정보 분리
- 요청별 권한 검증 정보를 별도의 객체로 분리하여 관리

```java
@Bean
public RequestAuthorizationManager requestAuthorizationManager() {
    List<RequestMatcherEntry<AuthorizationManager>> mappings = new ArrayList<>();
    mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members/me"), new AuthenticatedAuthorizationManager()));
    mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members"), new AuthorityAuthorizationManager("ADMIN")));
    mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/search"), new PermitAllAuthorizationManager()));
    mappings.add(new RequestMatcherEntry<>(new AnyRequestMatcher(), new PermitAllAuthorizationManager()));
    return new RequestAuthorizationManager(mappings);
}
```
