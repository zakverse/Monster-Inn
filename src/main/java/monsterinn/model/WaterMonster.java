package monsterinn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@DiscriminatorValue("WATER")
public class WaterMonster extends Monster {
    private double filtrationCost;

    public WaterMonster(String idMonster, String name, double baseCost, double filtrationCost) {
        super(idMonster, name, baseCost);
        this.filtrationCost = filtrationCost;
    }

    @Override
    public double calculateTotalCost() {
        // Total = Base * Hari + Extra (Layanan)
        return ((baseCost * stayDays) + extraCost);
    }

    @Override
    public String getElement() {
        return "WATER";
    }

    @Override
    public String getDetail() {
        return super.getDetail() + String.format(" | Elemen: WATER | Total: Rp%.2f", calculateTotalCost());
    }
}