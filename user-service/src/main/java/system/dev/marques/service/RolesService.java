package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.Roles;
import system.dev.marques.repository.RolesRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;

    public Roles getRoleByName(String name) throws BadRequestException {
        return rolesRepository.findByName(name).orElseThrow(() -> new BadRequestException("This role does not exist"));
    }

}
