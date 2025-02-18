package nextstep.security.authorization.role;

import java.util.Collection;

public class NullRoleHierarchy implements RoleHierarchy {
    public static NullRoleHierarchy getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Collection<String> getReachableGrantedAuthorities(Collection<String> authorities) {
        return authorities;
    }

    private static class SingletonHolder {
        private static final NullRoleHierarchy INSTANCE = new NullRoleHierarchy();
    }
}
