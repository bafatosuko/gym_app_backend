package gr.myproject.wod_core_backend.dto;

public class CustomerReadOnlyDTO {

    private Long id;
    private Boolean isActive;
    private Boolean isSubscribed;
    private UserReadOnlyDTO user;
}
