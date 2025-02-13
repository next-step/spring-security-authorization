package nextstep.security.role;

import org.springframework.util.StringUtils;

import java.util.*;

public class RoleHierarchyImpl implements RoleHierarchy {

    Map<String, Set<GrantedAuthority>> roleHierarchy;

    private static final List<GrantedAuthority> EMPTY_AUTHORITIES = Collections.emptyList();

    public RoleHierarchyImpl(Map<String, Set<GrantedAuthority>> roleHierarchy) {
        this.roleHierarchy = new HashMap<>(roleHierarchy);
    }

    public static Builder builder() {
        return new RoleHierarchyImpl.Builder();
    }

    @Override
    public Collection<GrantedAuthority> getReachableRoles(Collection<GrantedAuthority> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return EMPTY_AUTHORITIES;
        }

        Set<GrantedAuthority> reachableRoles = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            reachableRoles.add(authority);

            Collection<GrantedAuthority> impliedAuthorities = roleHierarchy.get(authority.getAuthority());
            if (impliedAuthorities != null) {
                reachableRoles.addAll(impliedAuthorities);
            }
        }

        return reachableRoles;
    }

    public static final class Builder {

        private final Map<String, Set<GrantedAuthority>> hierarchy;

        private Builder() {
            this.hierarchy = new HashMap<>();
        }

        public Builder.ImpliedRoles role(String role) {
            if (!StringUtils.hasText(role)) {
                throw new IllegalArgumentException("role must not be null or empty");
            }

            return new Builder.ImpliedRoles(role);
        }

        public RoleHierarchyImpl build() {
            return new RoleHierarchyImpl(this.hierarchy);
        }

        private RoleHierarchyImpl.Builder addHierarchy(String role, String... impliedRoles) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            for (String impliedRole : impliedRoles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(impliedRole));
            }

            this.hierarchy.put(role, grantedAuthorities);
            return this;
        }

        public final class ImpliedRoles {

            private final String role;

            private ImpliedRoles(String role) {
                this.role = role;
            }

            public Builder addImplies(String... impliedRoles) {
                if (impliedRoles == null) {
                    throw new IllegalArgumentException("at least one implied role must be provided");
                }

                for (String impliedRole : impliedRoles) {
                    if (!StringUtils.hasText(impliedRole)) {
                        throw new IllegalArgumentException("at least one implied role must be provided");
                    }
                }

                return Builder.this.addHierarchy(this.role, impliedRoles);
            }
        }

    }
}
