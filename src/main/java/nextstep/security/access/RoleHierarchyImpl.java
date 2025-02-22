package nextstep.security.access;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleHierarchyImpl implements RoleHierarchy {

    private final Map<String, Set<String>> rolesReachableInOneOrMoreStepsMap = new HashMap<>();

    @Override
    public Collection<String> getReachableGrantedAuthorities(final Collection<String> authorities) {
        return rolesReachableInOneOrMoreStepsMap.entrySet()
                .stream()
                .filter(entry -> authorities.contains(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void setHierarchy(final String roleHierarchyStringRepresentation) {
        Arrays.stream(roleHierarchyStringRepresentation.split("\n")).toList()
                .stream().filter(hierarchyLine -> !hierarchyLine.isBlank())
                .map(hierarchyLine -> hierarchyLine.split(">"))
                .forEach(hierarchy -> {
                    final String role = hierarchy[0].trim();
                    final String step = hierarchy[1].trim();

                    this.rolesReachableInOneOrMoreStepsMap.putIfAbsent(role, new HashSet<>());

                    this.rolesReachableInOneOrMoreStepsMap.get(role).add(role);
                    this.rolesReachableInOneOrMoreStepsMap.get(role).add(step);
                });

        this.rolesReachableInOneOrMoreStepsMap.forEach(this::buildRolesReachableInOneOrMoreStepsMap);
    }

    private void buildRolesReachableInOneOrMoreStepsMap(final String role, final Set<String> steps) {
        rolesReachableInOneOrMoreStepsMap.forEach((key, value) -> {
            final boolean hasNotRole = !value.contains(role);

            if (hasNotRole) {
                return;
            }

            value.addAll(steps);
        });
    }

    public Map<String, Set<String>> buildFullHierarchyMap(final String hierarchy) {
        // 최종 결과를 저장할 Map
        Map<String, Set<String>> fullHierarchyMap = new HashMap<>();

        // 빈 문자열이나 null 체크
        if (hierarchy == null || hierarchy.trim().isEmpty()) {
            return fullHierarchyMap;
        }

        // 직접적인 계층 관계 Map 먼저 생성
        Map<String, Set<String>> directHierarchyMap = new HashMap<>();

        // 줄바꿈으로 구분된 각 계층 규칙을 처리
        String[] hierarchyArr = hierarchy.split("\n");
        for (String hierarchyLine : hierarchyArr) {
            hierarchyLine = hierarchyLine.trim();
            if (hierarchyLine.isEmpty()) continue;

            // ">" 기준으로 상위/하위 역할 분리
            String[] roles = hierarchyLine.split(">");
            if (roles.length != 2) {
                throw new IllegalArgumentException("Invalid hierarchy format: " + hierarchyLine);
            }

            String higherRole = roles[0].trim();
            String lowerRole = roles[1].trim();

            // 직접적인 계층 관계 Map에 추가
            directHierarchyMap.computeIfAbsent(higherRole, k -> new HashSet<>())
                    .add(lowerRole);
        }

        // 각 역할에 대해 도달 가능한 모든 하위 역할 찾기
        for (String role : directHierarchyMap.keySet()) {
            Set<String> reachableRoles = new HashSet<>();
            findAllReachableRoles(role, directHierarchyMap, reachableRoles);
            fullHierarchyMap.put(role, reachableRoles);
        }

        return fullHierarchyMap;
    }

    private void findAllReachableRoles(String role,
                                       Map<String, Set<String>> directHierarchyMap,
                                       Set<String> reachableRoles) {
        Set<String> directRoles = directHierarchyMap.get(role);
        if (directRoles == null) {
            return;
        }

        for (String directRole : directRoles) {
            reachableRoles.add(directRole);
            findAllReachableRoles(directRole, directHierarchyMap, reachableRoles);
        }
    }
}
