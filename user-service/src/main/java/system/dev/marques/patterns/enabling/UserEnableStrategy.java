package system.dev.marques.patterns.enabling;


import system.dev.marques.domain.User;

public interface UserEnableStrategy {

    boolean supports(Object request);
    void updateUser(Object request, User user);
}
