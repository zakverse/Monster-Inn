package monsterinn.modules.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String serviceName;
    private Double price;
    private String targetElement; // Api, Air, Tanah
    private String icon; // fa-fire, fa-droplet, dll

    public ServiceEntity(String serviceName, Double price, String targetElement, String icon) {
        this.serviceName = serviceName;
        this.price = price;
        this.targetElement = targetElement;
        this.icon = icon;
    }
}