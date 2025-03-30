# PDF Generator Tool

It is an PDF generator tool created in springboot with itextPDF library.

## Postman Testing Instructions:
1. Pdf generate request: If new data found then api creates a new pdf else return the older response pdf file base on hash calculation.
### POST request
`http://localhost:8080/api/invoices/generate`

Requires data in body:
```
{
    "seller": "XYZ Pvt. Ltd.",
    "sellerGstin": "29AABBCCDD121ZD",
    "sellerAddress": "New Delhi, India",
    "buyer": "Vedant Computers",
    "buyerGstin": "29AABBCCDD131ZD",
    "buyerAddress": "New Delhi, India",
    "items": [
        {
            "name": "Product 1",
            "quantity": "12 Nos",
            "rate": 125.00,
            "amount": 1476.00
        },
        {
            "name": "Product 2",
            "quantity": "24 Nos",
            "rate": 125.00,
            "amount": 1500.00
        },
        {
            "name": "Product 3",
            "quantity": "26 Nos",
            "rate": 125.00,
            "amount": 1800.00
        }
    ]
}
```
Response:

<img width="1059" alt="Screenshot 2025-03-30 at 6 22 04 PM" src="https://github.com/user-attachments/assets/f2a9b629-ceb1-4edb-b1a5-699e1192c38a" />

### 2. Get Request
`http://localhost:8080/api/invoices/download/{hashx}`

Response:

<img width="1058" alt="Screenshot 2025-03-30 at 6 25 28 PM" src="https://github.com/user-attachments/assets/2fb733b3-f52a-4e49-b0c3-7a80142bbe94" />

Thanks for checking out the project!!!
