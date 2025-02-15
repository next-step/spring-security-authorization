# spring-security-authorization

## 실습

1. [x] GET /members/me 엔드포인트 구현 및 테스트 작성
2. [x] 권한 검증 로직을 AuthorizationFilter로 리팩터링

## 🚀 1단계 - AuthorizationManager를 활용

요구사항

- [x] AuthorizationManager 를 활용하여 인가 과정 추상화
- [x] 인가를 처리해줄 AuthorizationManager 생성
- [x] RequestMatcherDelegatingAuthorizationManager 를 통한 AuthorizationManager 한번에 관리?
- [x] 인가 과정을 추상화한 AuthorizationManager 를 작성한다. 이 때 필요한 AuthorizationDecision도 함께 작성한다. (실제 AuthorizationManager에는
  verify도 있는데 이 부분에 대한 구현은 선택)
- [x] SecuredMethodInterceptor와 Authorization Filter에서 작성된 인가 로직을 AuthorizationManager로 리팩터링 한다.

## 🚀 2단계 - 요청별 권한 검증 정보 분리

요구사항

- [x] 요청별 권한 검증 정보를 별도의 객체로 분리하여 관리
- [x] RequestMatcherRegistry와 RequestMatcher를 작성하고, RequestMatcher의 구현체를 작성한다.
- [x] AnyRequestMatcher: 모든 경우 true를 리턴한다.
- [x] MvcRequestMatcher: method와 pattern(uri)가 같은지 비교하여 리턴한다.
- [x] RequestMatcherEntry의 T entry는 아래에 해당되는 각 요청별 인가 로직을 담당하는 AuthorizationManager가 된다.
- [x] /login은 모든 요청을 받을 수 있도록 PermitAllAuthorizationManager로 처리
- [x] /members/me는 인증된 사용자만에게만 권한을 부여하기 위해 AuthenticatedAuthorizationManager로 처리
- [x] /members는 "ADMIN" 사용자만에게만 권한을 부여하기 위해 HasAuthorityAuthorizationManager로 처리
- [x] 그 외 모든 요청은 권한을 제한하기 위해 DenyAllAuthorizationManager로 처리

아래 객체와 시큐리티 코드 확인
// SpEL
// Role Authority
// Role Hierarchy
// AuthoritiesAuthorizationManager
// SecureMethodSecurityConfiguration
// SecuredAuthorizationManager


## 🚀 선택 - RoleHierarchy

요구 사항

    RoleHierarchy는 기본적으로 NullRoleHierarchy가 설정된 다음 계층 구조의 권한 설정이 생길 경우 RoleHierarchyImpl가 동작하도록 되어있다. 실제 시큐리티의 구조를 참고해서 아래와 같이 설정하도록 수정한다.
- [x] RoleHierarchy를 설정하지 않았을 때 동작하도록 NullRoleHierarchy을 구현한다.\
따라서, 설정하지 않았을 땐 기본적으로 NullRoleHierarchy가 동작하고, 설정할 경우에는 RoleHierarchyImpl가 동작하도록 한다.
- [x] GrantedAuthority의 구현은 선택, 필수는 아니다.
