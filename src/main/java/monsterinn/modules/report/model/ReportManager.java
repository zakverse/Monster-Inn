package monsterinn.modules.report.model;

import monsterinn.modules.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ReportManager {
    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction tidak boleh kosong");
        }
        if (!transaction.isPaid()) {
            throw new IllegalStateException("Transaksi gagal tidak boleh masuk laporan");
        }
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public List<Transaction> getRecentTransactions() {
        return transactions.stream()
            .sorted(Comparator.comparing(Transaction::getCheckoutTime).reversed())
            .limit(10)
            .toList();
    }

    public double getTotalRevenue() {
        return transactions.stream()
            .mapToDouble(Transaction::getTotalCost)
            .sum();
    }

    public long getTotalCheckedOutMonsters() {
        return transactions.size();
    }

    public Map<String, Long> getElementPopularity() {
        Map<String, Long> popularity = new LinkedHashMap<>();
        popularity.put("FIRE", countByElement("FIRE"));
        popularity.put("WATER", countByElement("WATER"));
        popularity.put("EARTH", countByElement("EARTH"));
        return popularity;
    }

    public void clear() {
        transactions.clear();
    }

    private long countByElement(String element) {
        return transactions.stream()
            .filter(t -> element.equalsIgnoreCase(t.getElement()))
            .count();
    }
}
