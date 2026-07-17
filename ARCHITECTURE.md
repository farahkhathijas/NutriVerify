# NutriVerify Architecture

## Package Structure
- com.nutriverify.Main
- com.nutriverify.app: application bootstrap and main menu loop
- com.nutriverify.ui: ANSI console rendering, banners, tables, progress bars
- com.nutriverify.model: domain objects such as FoodLabel, Ingredient, NutrientFact, Claim, Report, AnalysisResult
- com.nutriverify.engine: claim validation, nutrition checks, ingredient analysis, authenticity scoring, comparison logic
- com.nutriverify.service: orchestration services for analysis, report generation, history, export
- com.nutriverify.repository: dataset loading and persistence to local files
- com.nutriverify.config: constants, thresholds, and configuration values
- com.nutriverify.exception: custom exception hierarchy
- com.nutriverify.util: reusable formatting and validation helpers

## Runtime Flow
1. Startup banner and loading animation are rendered.
2. The main menu loop accepts user input and dispatches to analysis, comparison, search, history, export, or help actions.
3. A food label is either entered manually or loaded from a bundled sample dataset.
4. The analysis pipeline validates claims, nutrition facts, and ingredients, then produces a score and recommendation set.
5. Results are rendered and optionally persisted to history and exported to disk.
