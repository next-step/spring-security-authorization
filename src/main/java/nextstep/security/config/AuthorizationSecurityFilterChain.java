package nextstep.security.config;

import java.util.List;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters;

    public AuthorizationSecurityFilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    public AuthorizationSecurityFilterChain(Filter... filters) {
        this(List.of(filters));
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
