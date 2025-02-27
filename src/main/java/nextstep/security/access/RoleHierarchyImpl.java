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

                    this.rolesReachableInOneOrMoreStepsMap.putIfAbsent(step, new HashSet<>());
                    this.rolesReachableInOneOrMoreStepsMap.get(step).add(step);

                });

        this.rolesReachableInOneOrMoreStepsMap.forEach(this::buildRolesReachableInOneOrMoreStepsMap);

        checkCircularHierarchy();
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

    private void checkCircularHierarchy() {
        this.rolesReachableInOneOrMoreStepsMap.forEach((role, steps) -> {
            final HashSet<String> visited = new HashSet<>();
            visited.add(role);

            steps.forEach(step -> checkCircularHierarchyInternal(role, step, visited));
        });
    }

    private void checkCircularHierarchyInternal(final String role, final String step, final Set<String> visited) {
        if (visited.contains(step)) {
            return;
        }

        visited.add(step);

        final Set<String> steps = this.rolesReachableInOneOrMoreStepsMap.get(step);

        if (steps.contains(role)) {
            throw new IllegalStateException("Cyclic role inheritance definition");
        }

        steps.forEach(nextStep -> checkCircularHierarchyInternal(role, nextStep, visited));
    }
}
