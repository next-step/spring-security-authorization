package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collection;

public class RequestAuthorizationAuthoritiesContext implements RequestAuthorizationContext<Collection<GrantedAuthority>> {

    private final HttpServletRequest request;
    private final Collection<GrantedAuthority> others;

    public RequestAuthorizationAuthoritiesContext(final HttpServletRequest request, final Collection<GrantedAuthority> others) {
        this.request = request;
        this.others = others;
    }

    @Override
    public HttpServletRequest getRequest() {
        return this.request;
    }

    @Override
    public Collection<GrantedAuthority> getOthers() {
        return this.others;
    }
}
