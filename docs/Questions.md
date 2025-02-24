### (예외 처리) MemberController::getMember에서 IllegalStateException을 호출 중.

- IllegalArgumentException이 더 어울리지 않았을까?
- 이미 인증 필터에서 인증된 상태의 유저를 찾았는데, 
  - 비즈니스 로직에서 username과 일치하는 member가 없다는 것은클라이언트의 잘못(IllegalArgumentException)이라기 
  보다는 서버 상태의 이상을 의미하기 때문에 IllegalStateException으로 선언했음. 적절한 예외였을지 궁금. 

### 컨트롤러에서 SecurityContext 직접 접근하는게 괜찮을지?

- 실무라면 `@AuthenticatedMember`라는 애노테이션을 만들고 MethodArgumentResolver로 Member객체를 주입해줄 것 같음.
- 비즈니스 로직은 인증된 상태에서 실행하는 건데, 
  - 비즈니스 로직에서 인증된 컨텍스트를 또 접근하는 것이 좋은 방향일지 고민. 
