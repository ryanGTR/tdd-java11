import java.time.LocalDate;
import java.util.List;

public class Accounting {
    private final IBudgetRepo budgetRepo;

    public Accounting(IBudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    public double totalAmount(LocalDate start, LocalDate end) {
        List<Budget> budgets = budgetRepo.getAll();
        if (budgets.size() > 0) {
            Budget budget = budgets.get(0);
            Period period = new Period(start, end);
            double dailyAmount = dailyAmount(budget);
            return dailyAmount * period.overlappingDays(budget.createPeriod());
        }
        return 0;
    }

    private double dailyAmount(Budget budget) {
        return budget.amount / budget.days();
    }
}
