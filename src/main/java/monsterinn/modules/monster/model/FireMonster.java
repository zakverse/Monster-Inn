package monsterinn.modules.monster.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@DiscriminatorValue("FIRE")
public class FireMonster extends Monster {
    private double thermalSurcharge;

    public FireMonster(String idMonster, String name, double baseCost, double thermalSurcharge) {
        super(idMonster, name, baseCost);
        this.thermalSurcharge = thermalSurcharge;
    }

    @Override
    public double calculateTotalCost() {
        // Total = (Base + Thermal) * Hari + Extra (Layanan)
        return ((baseCost + thermalSurcharge) * stayDays) + extraCost;
    }


    @Override
    public String getElement() {
        return "FIRE";
    }

    @Override
    public String getDetail() {
        return super.getDetail() + String.format(" | Elemen: FIRE | Total: Rp%.2f", calculateTotalCost());
    }
}