import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BudgetTests {

    private final IBudgetRepo budgetRepo = Mockito.mock(IBudgetRepo.class);
    private final Accounting accounting = new Accounting(budgetRepo);

    @Test
    public void no_budgets() {
        totalAmountShouldBe(0,
                LocalDate.of(2000, 4, 1),
                LocalDate.of(2000, 4, 1));
    }

    @Test
    public void period_inside_budget_month() {
        givenBudgets(new Budget("200004", 30));

        totalAmountShouldBe(1,
                LocalDate.of(2000, 4, 1),
                LocalDate.of(2000, 4, 1));
    }

    @Test
    public void period_no_overlap_before_budget_first_day() {
        givenBudgets(new Budget("200004", 30));

        totalAmountShouldBe(0,
                LocalDate.of(2000, 3, 31),
                LocalDate.of(2000, 3, 31));
    }

    @Test
    public void period_no_overlap_after_budget_last_day() {
        givenBudgets(new Budget("200004", 30));

        totalAmountShouldBe(0,
                LocalDate.of(2000, 5, 1),
                LocalDate.of(2000, 5, 1));
    }

    @Test
    public void period_overlap_budget_first_day() {
        givenBudgets(new Budget("200004", 30));

        totalAmountShouldBe(2,
                LocalDate.of(2000, 3, 31),
                LocalDate.of(2000, 4, 2));
    }

    @Test
    public void period_overlap_budget_last_day() {
        givenBudgets(new Budget("200004", 30));

        totalAmountShouldBe(1,
                LocalDate.of(2000, 4, 30),
                LocalDate.of(2000, 5, 1));
    }

    @Test
    public void invalid_period() {
        givenBudgets(new Budget("200004", 30));

        totalAmountShouldBe(0,
                LocalDate.of(2000, 4, 30),
                LocalDate.of(2000, 4, 1));
    }

    @Test
    public void daily_amount_is_10() {
        givenBudgets(new Budget("200004", 300));

        totalAmountShouldBe(20,
                LocalDate.of(2000, 3, 30),
                LocalDate.of(2000, 4, 2));
    }

    @Test
    public void multiple_budgets() {
        givenBudgets(
                new Budget("200003", 31),
                new Budget("200004", 300),
                new Budget("200005", 3100));

        totalAmountShouldBe(2 + 300 + 100 * 10,
                LocalDate.of(2000, 3, 30),
                LocalDate.of(2000, 5, 10));
    }

    private void givenBudgets(Budget... budgets) {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(budgets));
    }

    private void totalAmountShouldBe(int expected, LocalDate start, LocalDate end) {
        assertEquals(expected,
                accounting.totalAmount(start, end),
                0.00);
    }
}
