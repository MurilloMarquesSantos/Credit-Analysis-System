package system.dev.marques.util;

import system.dev.marques.domain.dto.requests.DeleteForm;

public class FormCreator {

    public static DeleteForm createDeleteForm() {
        return DeleteForm.builder()
                .reason("no need of the service anymore").build();
    }


    public static DeleteForm createInvalidForm() {
        return DeleteForm.builder().build();
    }
}
