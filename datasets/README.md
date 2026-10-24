# Motorcycle Parts Store CSV datasets

All files in this folder are **synthetic demonstration data** created for this project. They do not describe real customers, transactions, or store operations. Data records below exclude the CSV header row.

| File | Data records | Purpose |
|---|---:|---|
| `weekly_sales.csv` | 1,248 | Weekly product demand used by the prediction models. It contains 104 weeks for each of the 12 demo products. |
| `sales_transactions.csv` | 1,500 | Transaction-level summaries. `transaction_id` joins to `sales_items.csv`. |
| `sales_items.csv` | 4,492 | Item-level lines for each transaction, with quantities and line totals. The exact count is the number of rows in this file. |
| `inventory_snapshots.csv` | 1,500 | Weekly stock and price observations for 12 demo products over 125 weeks. |

## Important notes

- The demo application stores live inventory and sales in memory; these companion CSV files do not automatically populate the inventory or sales screens.
- `PredictionService` attempts to load `weekly_sales.csv` from this folder when starting model training. If that file is missing or invalid, it falls back to its built-in deterministic synthetic dataset.
- These datasets are useful for classroom demonstration, testing, data analysis, and experimenting with import workflows. Replace synthetic data with authorized real records before using a model for operational decisions.

## Key fields

- `weekly_sales.csv`: `date`, `product_id`, `sku`, `product`, `category`, `qty_sold`, `unit_price`, `stock`, `week_index`, `week_of_year`.
- `sales_transactions.csv`: `transaction_id`, timestamp, synthetic customer label, payment method, line count, unit count, subtotal, and status.
- `sales_items.csv`: transaction ID, timestamp, SKU, product/category, quantity, unit price, and line total.
- `inventory_snapshots.csv`: weekly snapshot date, SKU, product/category, stock-on-hand, unit price, reorder level, and stock status.
