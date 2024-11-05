# spring-security-authorization

## 1. 사용자 권한 추가 및 검증

- [x] ✅ `BasicAuthTest`와 `FormLoginTest`의 모든 테스트가 통과해야 한다.
- [x] ✅ `SecuredTest`의 모든 테스트가 통과해야 한다.

## 🚀 2단계 - 리팩터링

- [x] GET /members/me 엔드포인트 구현 및 테스트 작성
- [x] 권한 검증 로직을 AuthorizationFilter 로 리팩터링

## 🚀 3단계 - 스프링 시큐리티 구조 적용

- [x] AuthorizationManager를 활용하여 인가 과정 추상화
- [x] AuthorizationManager 구현
- [x] AuthorizationDecision 구현
- [x] RequestMatcherDelegatingAuthorizationManager 구현
- [x] AuthorityAuthorizationManager 구현
- [x] SecuredAuthorizationManager 구현