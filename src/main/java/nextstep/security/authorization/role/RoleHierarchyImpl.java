package nextstep.security.authorization.role;

import nextstep.security.SimpleGrantedAuthority;
import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class RoleHierarchyImpl implements RoleHierarchy {

    private final Map<String, Set<GrantedAuthority>> reachableAuthoritiesMap;

    private RoleHierarchyImpl(Map<String, Set<GrantedAuthority>> reachableAuthoritiesMap) {
        this.reachableAuthoritiesMap = reachableAuthoritiesMap;
    }

    public static RoleHierarchyImpl fromHierarchy(String hierarchy) {
        Map<String, Set<GrantedAuthority>> reachableAuthoritiesMap = convertHierarchicalRoles(hierarchy);
        return new RoleHierarchyImpl(reachableAuthoritiesMap);
    }

    public static Builder with() {
        return new Builder();
    }

    private static Map<String, Set<GrantedAuthority>> convertHierarchicalRoles(String hierarchy) {

        Map<String, Set<GrantedAuthority>> hierarchicalMap = new HashMap<>();
        for (String line : hierarchy.split("\n")) {
            String[] roles = line.trim().split("\\s*>\\s*");

            String parent = roles[0].trim();
            String child = roles[1].trim();

            hierarchicalMap.computeIfAbsent(parent, k -> new HashSet<>())
                    .add(new SimpleGrantedAuthority(child));
        }

        return hierarchicalMap;
    }

    @Override
    public Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<GrantedAuthority> authorities) {

        Set<GrantedAuthority> result = new HashSet<>();
        Set<GrantedAuthority> processed = new HashSet<>();
        Queue<GrantedAuthority> queue = new LinkedList<>(authorities);

        while (!queue.isEmpty()) {
            GrantedAuthority current = queue.poll();

            if (processed.contains(current)) {
                continue;
            }

            processed.add(current);
            result.add(current);

            reachableAuthoritiesMap.getOrDefault(current.getAuthority(), Collections.emptySet())
                    .stream().filter(a -> !processed.contains(a))
                    .forEach(queue::offer);
        }

        return result;
    }

    public static class Builder {
        private final Map<String, Set<GrantedAuthority>> hierarchy = new HashMap<>();

        public ImpliedRoles role(String parent) {
            return new ImpliedRoles(parent);
        }

        public RoleHierarchyImpl build() {
            return new RoleHierarchyImpl(hierarchy);
        }

        private Builder addHierarchy(String role, String... children) {
            Set<GrantedAuthority> authorities = hierarchy.computeIfAbsent(role, k -> new HashSet<>());
            for (String child : children) {
                authorities.add(new SimpleGrantedAuthority(child));
            }

            return this;
        }

        public final class ImpliedRoles {
            private final String parent;

            public ImpliedRoles(String parent) {
                this.parent = parent;
            }

            public Builder implies(String... children) {
                return Builder.this.addHierarchy(parent, children);
            }
        }
    }
}
