package nextstep.security.authorization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoleHierarchyImpl implements RoleHierarchy {
    private final Map<String, Set<String>> hierarchyMap = new HashMap<>();

    @Override
    public Set<String> getReachableRoleAuthorities(String authority) {
        Set<String> reachableRole = hierarchyMap.get(authority);

        if (reachableRole == null) {
            NullRoleHierarchy nullRoleHierarchy = new NullRoleHierarchy();
            return nullRoleHierarchy.getReachableRoleAuthorities(authority);
        }

        return reachableRole;
    }

    public void setHierarchy(String hierarchy) {
        // ADMIN > USER > GUEST
        //또는
        // ADMIN > USER\nUSER > GUEST
        String[] lines = hierarchy.split("\\n");
        for (String line : lines) {
            String trimmedLine = line.replace(" ", "").trim();
            int delimiterIndex = trimmedLine.indexOf(">");
            if (delimiterIndex == -1) {
                throw new IllegalArgumentException("Roles must be expressed as a hierarchy delimited by the '>' symbol." + line);
            }

            String[] splitRoleArray = trimmedLine.split(">");
            //정의된 키가 없다면 등록하고 role 추가
            for (String role : splitRoleArray) {
                hierarchyMap.computeIfAbsent(role, r -> new HashSet<>()).add(role);
            }

            for (int i = 0; i < splitRoleArray.length; i++) {
                //하위 역할 등록
                for (int j = i + 1; j < splitRoleArray.length; j++) {
                    hierarchyMap.get(splitRoleArray[i]).add(splitRoleArray[j]);
                }
            }
        }
    }
}
