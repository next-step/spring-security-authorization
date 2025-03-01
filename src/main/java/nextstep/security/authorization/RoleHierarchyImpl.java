package nextstep.security.authorization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoleHierarchyImpl implements RoleHierarchy {
    private final Map<String, Set<String>> hierarchyMap = new HashMap<>();

    public RoleHierarchyImpl(String hierarchy) {
        setHierarchy(hierarchy);
    }

    @Override
    public Set<String> getReachableRoleAuthorities(String authority) {
        return hierarchyMap.get(authority);
    }

    private void setHierarchy(String hierarchy) {
        // 순환참조 dfs확인용 그래프
        Map<String, Set<String>> graph = new HashMap<>();
        String[] lines = hierarchy.split("\\n");
        for (String line : lines) {
            String trimmedLine = line.replace(" ", "").trim();
            int delimiterIndex = trimmedLine.indexOf(">");
            if (delimiterIndex == -1) {
                throw new IllegalArgumentException("Roles must be expressed as a hierarchy delimited by the '>' symbol: " + line);
            }
            String[] roles = trimmedLine.split(">");
            // 각 역할을 노드로 등록
            for (String role : roles) {
                graph.putIfAbsent(role, new HashSet<>());
            }
            // 상위 역할에서 하위 역할로의 간선 추가
            for (int i = 0; i < roles.length - 1; i++) {
                String parent = roles[i];
                for (int j = i + 1; j < roles.length; j++) {
                    String child = roles[j];
                    graph.get(parent).add(child);
                }
            }
        }

        // dfs를 이용한 순환참조 검출. 검출만 함
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        for (String role : graph.keySet()) {
            if (detectCycle(role, graph, visited, stack)) {
                throw new IllegalArgumentException("Cycle detected in role hierarchy.");
            }
        }

        //이전에 순환참조가 없는걸 확인했으니 dfs로 모든 하위역할들을 추가
        for (String role : graph.keySet()) {
            Set<String> reachable = new HashSet<>();
            dfs(role, graph, reachable);
            reachable.add(role); // 자기 자신 포함
            hierarchyMap.put(role, reachable);
        }
    }

    // dfs를 이용해 순환참조 검출
    private boolean detectCycle(String role, Map<String, Set<String>> graph, Set<String> visited, Set<String> stack) {
        if (stack.contains(role)) {
            return true;
        }
        if (visited.contains(role)) {
            return false;
        }
        visited.add(role);
        stack.add(role);
        for (String child : graph.get(role)) {
            if (detectCycle(child, graph, visited, stack)) {
                return true;
            }
        }
        stack.remove(role);
        return false;
    }

    // dfs를 이용해 현재 역할의 모든 하위 역할들을 추가
    private void dfs(String role, Map<String, Set<String>> graph, Set<String> reachable) {
        for (String child : graph.get(role)) {
            if (!reachable.contains(child)) {
                reachable.add(child);
                dfs(child, graph, reachable);
            }
        }
    }
}
