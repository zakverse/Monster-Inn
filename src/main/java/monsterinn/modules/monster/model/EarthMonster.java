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
@DiscriminatorValue("EARTH")
public class EarthMonster extends Monster {
    private double soilNutrientCost;

    public EarthMonster(String idMonster, String name, double baseCost, double soilNutrientCost) {
        super(idMonster, name, baseCost);
        this.soilNutrientCost = soilNutrientCost;
    }

    @Override
    public double calculateTotalCost() {
        // Total = (Base + Nutrisi) * Hari + Extra (Layanan)
        return ((baseCost + soilNutrientCost) * stayDays) + extraCost;
    }

    @Override
    public String getElement() {
        return "EARTH";
    }

    @Override
    public String getDetail() {
        return super.getDetail() + String.format(" | Elemen: EARTH | Total: Rp%.2f", calculateTotalCost());
    }
}