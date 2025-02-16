package nextstep.security.rolehierarchy;

import nextstep.security.authorization.GrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class RoleHierarchyImpl implements RoleHierarchy {
    private final Map<GrantedAuthority, Set<GrantedAuthority>> grantMap;

    public RoleHierarchyImpl(final Collection<RoleRelation> roleRelations) {
        this.grantMap = new HashMap<>();
        buildRoleHierarchy(roleRelations);
        addSelf(roleRelations);
    }

    private void buildRoleHierarchy(Collection<RoleRelation> roleRelations) {
        for (RoleRelation relation : roleRelations) {
            grantMap.computeIfAbsent(relation.parentRole(), k -> new HashSet<>())
                    .add(relation.childRole());
        }

        grantMap.forEach((key, values) -> {
            values.forEach((value) -> {
                var childChildren = grantMap.get(value);
                if (childChildren != null) {
                    grantMap.get(key).addAll(childChildren);
                }
            });
        });
    }

    private void addSelf(Collection<RoleRelation> roleRelations) {
        roleRelations.stream()
                .flatMap(it -> it.getAllRoles().stream())
                .forEach(it -> {
                    var values = grantMap.get(it);
                    if (values != null) {
                        values.add(it);
                    }
                    if (values == null) {
                        grantMap.put(it, Set.of(it));
                    }
                });
    }

    @Override
    public Collection<GrantedAuthority> getReachableGrantedAuthorities(Collection<GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
                .map(grantMap::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                ;

    }

}
