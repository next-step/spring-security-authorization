package nextstep.security.rolehierarchy;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public record RoleRelation(GrantedAuthority parentRole, GrantedAuthority childRole) {

    public Collection<GrantedAuthority> getAllRoles() {
        return Set.of(parentRole, childRole);
    }
}
