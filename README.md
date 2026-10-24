# MotorcyclePartsStore - One Prompt Away

Java Swing desktop app (NetBeans / Ant project) for the "One Prompt Away - Motorcycle Parts Store System".
Main class: `motorcyclepartsstore.Main`.

## Run
NetBeans: open the project, Run (F6). Command line: `ant run`.

## Demo accounts (in-memory, reset on every start)
| Level | Username | Password |
|---|---|---|
| Admin | admin | admin123 |
| Inventory clerk | clerk | clerk123 |
| Sales personnel | sales | sales123 |
| Instructor | instructor | instructor123 |

Sign-ups create a *Pending* account without a user level; an Admin assigns the level in User management
(Edit), after which the user can sign in.

## Notes
- All data (users, inventory, sales) is in memory demo data in `motorcyclepartsstore.service`.
- Predictions use the Weka library already in `lib/`. They are trained at start-up in the background on a
  **synthetic, deterministic weekly dataset** (not real shop data). The combined model is the average of
  Simple Linear Regression and Random Forest. Metrics (test split + 10-fold CV) are computed for real on that data.
- Save/Load model (Instructor > Evaluation) writes/reads `.model` files, default folder `models/`.
- Weka may print an `--add-opens java.base/java.lang` notice on newer JDKs; it is harmless.
- Fonts: uses Barlow / Barlow Condensed if installed, otherwise falls back to system fonts.

## GUI forms (NetBeans GUI Builder)
Screens and dialog bodies are NetBeans form pairs (`X.java` + `X.form`) in `motorcyclepartsstore.screens` and
`motorcyclepartsstore.ui`: AuthPanel, Sidebar, ConfirmForm, AdminDashboardPanel, AdminPredictionPanel,
UserManagementPanel, UserForm, InstructorDashboardPanel, InstructorPredictionPanel, EvaluationPanel,
MetricsPanel, ConfusionMatrix, InventoryDashboardPanel, InventoryPanel, ProductForm, StockInForm,
SalesDashboardPanel, SalesTransactionPanel, ReceiptForm.
Edit layout in the NetBeans designer; fonts/colors come from `Styler`/`Theme` by component name.
Custom beans used in the palette-less designer: RoundedPanel, RoundButton/SecondaryButton/DangerButton, Chip,
SummaryCard, DataTable, LineAreaChart, HBarChart, DonutChart, LinkLabel, LogoMark, Avatar.
Kept as plain code on purpose: AppFrame/ShellPanel (window + page switching), ModalDialog (dialog chrome),
Dialogs, PredictionController and the custom-painted components.


## CSV datasets

The `datasets/` directory contains four synthetic CSV datasets for classroom use: `weekly_sales.csv` (1,248 records), `sales_transactions.csv` (1,500 records), `sales_items.csv` (4,492 records), and `inventory_snapshots.csv` (1,500 records). `PredictionService` loads `datasets/weekly_sales.csv` for model training when the full 104-week product matrix is present; otherwise, it falls back to generated demo data. The transaction and inventory CSVs are included for analysis/import exercises and do not populate the app's in-memory transaction or inventory screens automatically. See `datasets/README.md` for field descriptions. All records are synthetic, not real shop data.
