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
