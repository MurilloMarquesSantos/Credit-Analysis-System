package system.dev.marques.strategy.enable;


import system.dev.marques.domain.User;

public interface UserEnableStrategy {

    boolean supports(Object request);
    void updateUser(Object request, User user);
}
