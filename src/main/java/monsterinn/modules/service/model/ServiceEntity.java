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
    
    @Column(name = "service_name") // SINKRONKAN DENGAN KOLOM MYSQL
    private String serviceName;
    
    private Double price;
    
    @Column(name = "target_element") // SINKRONKAN DENGAN KOLOM MYSQL
    private String targetElement; 
    
    private String icon; 

    public ServiceEntity(String serviceName, Double price, String targetElement, String icon) {
        this.serviceName = serviceName;
        this.price = price;
        this.targetElement = targetElement;
        this.icon = icon;
    }
}