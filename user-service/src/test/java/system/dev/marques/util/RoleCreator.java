package system.dev.marques.util;

import system.dev.marques.domain.Roles;

public class RoleCreator {

    public static Roles createRoleAdmin() {
        return Roles.builder()
                .name("ADMIN")
                .build();
    }

}
