package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - a Product Breakdown
 */
public class Breakdown extends Sale implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252103L;

  private Recipe _recipe;

  public Breakdown(int transactionKey, Partner partner, Product product, int baseDate, int amount, int basePrice, int dueDate, Recipe recipe) {
    super(transactionKey, partner, product, baseDate, amount, basePrice, dueDate);
    _recipe = recipe;
  }

  /** accepts a visitor - specifically, a TransactionVisitor */
  public void accept(TransactionVisitor visitor) {
    visitor.visitBreakdown(this);
  }

  public Recipe getRecipe() {
    return _recipe;
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      super.toString(),
      getRecipe().toString()
    );
  }

}
