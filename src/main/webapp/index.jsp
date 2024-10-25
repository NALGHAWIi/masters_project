<!DOCTYPE html>
<html>
<head>
    <title>ABC Store</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            padding: 20px;
        }
        h1 {
            color: #333;
            font-size: 2.5em;
        }
        button {
            padding: 10px 20px;
            margin: 20px 0;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1em;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
    <script type="text/javascript">
        function navigateTo(url) {
            window.location.href = url;
        }
    </script>
</head>
<body>
    <h1>Welcome to the ABC Store</h1>
    <button type="button" onclick="navigateTo('products')">View Products</button>
</body>
</html>
