# Convertly

Convertly is an AI-powered application that generates captivating Word documents from your handwriting. By converting handwritten documents into editable Word files that can be easily modified and stored in the cloud, Convertly ensures your documents are preserved and accessible.

## Features

- Converts handwritten documents into Word files
- Corrects OCR errors using AI
- Generates well-formatted documents
- Easy cloud storage and modification

## Technologies Used

- **OpenAI GPT-3.5**: For formatting the Word document and correcting any errors from the OCR.
- **Azure Computer Vision API**: For recognizing words and digits.
- **JavaFX**: For building the application interface.

## Techniques

OCR tools, even modern ones, often struggle with accurately converting handwriting to text. Typically, OCR errors are limited to specific letters within words. To mitigate this, Convertly feeds the OCR output into a GPT model, which corrects these errors and converts the text into the appropriate and correct form. The model also understands the syntax and semantics of the document, ensuring the final output is well-formatted.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java Development Kit (JDK)
- Azure Computer Vision API Key
- OpenAI GPT-3.5 API Key

## Conclusion

Convertly is a cutting-edge application designed to bridge the gap between handwritten and digital documents. By leveraging advanced AI technologies, Convertly ensures accurate, efficient, and well-formatted conversions, making it an invaluable tool for preserving and managing handwritten content in the digital age. We hope this project will benefit many users and demonstrate the power of combining multiple machine learning models to solve real-world problems.
