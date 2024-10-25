<!DOCTYPE html>
<html>
<head>
    <title>Add Product</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            padding: 20px;
        }
        form {
            margin: 20px 0;
        }
        label {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input, textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            width: auto;
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h1>Add a New Product</h1>
    <form action="addProduct" method="post">
        <label for="product_name">Product Name:</label>
        <input type="text" id="product_name" name="product_name" />
        
        <label for="price">Price:</label>
        <input type="text" id="price" name="price" inputmode="decimal" pattern="[0-9]*[.,]?[0-9]*" />
        
        <label for="product_description">Description:</label>
        <textarea id="product_description" name="product_description"></textarea>
        
        <input type="submit" value="Add Product" />
    </form>
</body>
</html>
