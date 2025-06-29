# Sustainability Scanner App

The Sustainability Scanner is an Android application that helps users make informed and eco-conscious decisions by scanning product barcodes and displaying their sustainability ratings. Inspired by the Google Pay scanning interface, this app offers a seamless way to assess a product’s environmental impact in real-time.

---

## Features

- Barcode scanner UI (Google Pay-style)
- Fetches data from [Open Food Facts](https://world.openfoodfacts.org/)
- Displays sustainability ratings (A–E scale)
- Color-coded progress bar (Green = A, Red = E)
- Visual feedback for eco-score
- Instant product name and information display
- Secure handling of camera permissions

---

## Tech Stack

| Tool | Purpose |
|------|---------|
| Java | Core app development |
| Kotlin DSL | Build configuration |
| ZXing Library | Barcode scanning |
| Retrofit | API integration |
| OpenFoodFacts API | Sustainability data source |

---

## Getting Started

### Requirements
- Android Studio Flamingo or later
- Minimum SDK: 24
- Internet connection

### Installation
```bash
git clone https://github.com/Debatreya-sengupta/SustainabilityScanner.git
cd SustainabilityScanner
