package nextstep.app.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nextstep.app.domain.Member;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MemberDto {

    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;
    private final Set<String> roles;

    @JsonCreator
    public MemberDto(@JsonProperty("email") final String email,
                     @JsonProperty("password") final String password,
                     @JsonProperty("name") final String name,
                     @JsonProperty("imageUrl") final String imageUrl,
                     @JsonProperty("roles") final Set<String> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.roles = roles;
    }

    public static MemberDto toDto(final Member member) {
        return new MemberDto(
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getImageUrl(),
                member.getRoles()
        );
    }

    public static List<MemberDto> toList(final List<Member> members) {
        return members.stream()
                .map(MemberDto::toDto)
                .collect(Collectors.toList());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
