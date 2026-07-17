# 🛡️ NutriVerify
### AI-Powered Food Label Authenticity Analyzer

> **Empowering consumers with transparent food choices through intelligent nutritional verification and rule-based authenticity analysis.**

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-Build-blue?style=for-the-badge&logo=apachemaven)
![Platform](https://img.shields.io/badge/Platform-Console-success?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## 📌 Overview

**NutriVerify** is a Java-based intelligent console application that analyzes packaged food labels and evaluates their authenticity using a comprehensive rule-based verification engine.

Instead of relying solely on printed nutritional information, the system validates ingredients, nutritional consistency, and health claims to provide consumers with a transparent authenticity assessment.

Designed with clean architecture and modern Java principles, NutriVerify demonstrates how intelligent decision systems can be implemented without machine learning while maintaining explainability and reliability.

---

## 🎯 Problem Statement

Modern food packaging often contains:

- Misleading health claims
- Hidden high-risk ingredients
- Inconsistent nutritional values
- Difficult-to-understand labels

Consumers frequently lack a simple way to determine whether the information presented is trustworthy.

NutriVerify addresses this challenge by performing automated authenticity analysis and generating a detailed explanation for every decision.

---

# ✨ Key Features

### 🧾 Food Label Analysis
- Analyze complete food product information
- Validate nutritional values
- Detect suspicious entries
- Evaluate ingredient quality

### 🥗 Ingredient Intelligence
- Artificial colors detection
- Preservative identification
- Palm oil detection
- MSG detection
- High-risk ingredient analysis
- Ingredient risk categorization

### 📊 Nutrition Validation
- Calories verification
- Sugar consistency analysis
- Protein validation
- Fat analysis
- Sodium evaluation
- Nutrition completeness checks

### ✅ Health Claim Verification
Automatically validates claims such as:

- Zero Sugar
- High Protein
- Organic
- Natural
- Low Fat
- No Added Sugar

and identifies contradictions between marketing claims and nutritional facts.

### 🧠 Authenticity Engine

Generates an intelligent authenticity score based on:

- Ingredient quality
- Nutrition consistency
- Claim validation
- Risk factors
- Data completeness

### ⚠️ Risk Classification

Products are categorized as:

- 🟢 Safe
- 🟡 Low Risk
- 🟠 Medium Risk
- 🔴 High Risk
- ⚫ Misleading Label

### 📄 Professional Report Generation

Generate comprehensive reports containing:

- Product Summary
- Nutrition Analysis
- Ingredient Assessment
- Claim Validation
- Authenticity Score
- Recommendations

---

# 🏗️ Architecture

```
NutriVerify
│
├── Model Layer
├── Analysis Engine
├── Validation Layer
├── Repository Layer
├── Service Layer
├── Console UI
├── Report Generator
└── Utility Components
```

Built using a modular architecture that emphasizes:

- Separation of Concerns
- SOLID Principles
- Object-Oriented Design
- Reusability
- Maintainability

---

# 💻 Technologies

| Technology | Purpose |
|------------|----------|
| Java 17+ | Core Application |
| Maven | Dependency Management |
| CSV | Local Dataset Storage |
| ANSI Console | Rich CLI Experience |
| OOP | Modular Design |

---

# 🚀 Console Experience

NutriVerify delivers a premium command-line experience featuring:

- ANSI colored output
- Unicode tables
- ASCII banners
- Interactive menus
- Progress indicators
- Structured reports
- Clean terminal interface

---

# 📂 Project Structure

```
NutriVerify/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nutriverify/
│   │   └── resources/
│
├── reports/
├── target/
├── pom.xml
└── README.md
```

---

# ▶️ Getting Started

## Clone Repository

```bash
git clone https://github.com/kayalvizhij0820-hue/NutriVerify.git
```

## Navigate

```bash
cd NutriVerify
```

## Compile

```bash
mvn clean compile
```

## Run

```bash
mvn exec:java
```

---

# 🎯 Project Highlights

- Rule-Based AI Decision Engine
- Food Label Authenticity Verification
- Explainable Analysis
- Console-Based Interactive Interface
- Modular Java Architecture
- Production-Oriented Design
- Recruiter-Friendly Clean Code
- Extensible for Future ML Integration

---

# 🌟 Future Enhancements

- OCR-based label scanning
- Barcode lookup integration
- FSSAI database support
- OpenFoodFacts integration
- USDA nutritional database support
- Machine Learning anomaly detection
- PDF report export
- Multi-language support

---

# 🤝 Contributions

Contributions, feature requests, and suggestions are welcome.

If you'd like to improve NutriVerify, feel free to fork the repository and submit a pull request.

---

# 👩‍💻 Author

**Kayalvizhi J**

Information Technology Student

Passionate about Java, AI-inspired systems, Software Engineering, and building impactful solutions through clean architecture.

---

## ⭐ If you found this project interesting, consider giving it a star!
