drop
database if exists shoeShop;
create
database shoeShop;
use
shoeShop;

create table Customer
(
    Id       int         not null auto_increment PRIMARY KEY,
    Name     VARCHAR(50) not null,
    Mail     VARCHAR(50) not null,
    City     VARCHAR(50) not null,
    Password VARCHAR(50) not null
);

create table Product
(
    Id            int         not null auto_increment PRIMARY KEY,
    AmountInStock int         not null,
    SizeOfShoe    int         not null,
    Color         VARCHAR(20) not null,
    Price         int         not null,
    Brand         VARCHAR(50)
);

create table Category
(
    Id   int         not null auto_increment PRIMARY KEY,
    Name VARCHAR(20) not null
);

create table ShoeOrder
(
    Id          int not null auto_increment PRIMARY KEY,
    DateOfOrder DATE,
    CustomerId  int not null,
    FOREIGN KEY (CustomerId) REFERENCES Customer (Id)
);

create table ProductsInOrder
(
    ShoeOrderId int not null,
    ProductId   int,
    FOREIGN KEY (ShoeOrderId) REFERENCES ShoeOrder (Id) on delete cascade,
-- MOTIVERING --> Raderas en beställning finns det ingen anledning att spara information
-- om vilka produkter som fanns med i beställningen. Därav delete cascade.

    FOREIGN KEY (ProductId) REFERENCES Product (Id) on delete set null
-- MOTIVERING --> Raderas en produkt kan det vara bra att markera
-- detta i beställningar som innehåller produkten genom att deras
-- nya produktvärde blir NULL.
);

create table Belongs
(
    ProductId  int not null,
    CategoryId int,
    FOREIGN KEY (ProductId) REFERENCES Product (Id) on delete cascade,
-- MOTIVERING --> Om en produkt tas bort är det onödigt att spara information
-- i databasen kring vilken kategori denna produkt brukade höra till.

    FOREIGN KEY (CategoryId) REFERENCES Category (Id) on delete cascade
-- MOTIVERING --> Om en kategori tas bort bör även information om vilka produkter
-- som tillhör kategorin tas bort. On delete set null används inte eftersom
-- det hade varit onödigt att spara information om produkter som har null-värde
-- på kategori. Det säger oss ingenting och därför är det lika bra att använda
-- on delete cascade.
);


INSERT INTO Customer(Name, Mail, City, Password)
VALUES ('Sofia Petterson', 'sofia.petterson@gmail.com', 'Stockholm', 'xyw_hwo31'),
       ('Marcus Eriksson', 'marcus.eriksson@gmail.com', 'Stockholm', 'asdfg_werty'),
       ('Lollo von Essen', 'lollo.vonEssen@gmail.com', 'Göteborg', 'WdkwO_Hed'),
       ('Olivia Ellefors', 'oliviaellefors@hotmail.com', 'Stockholm', 'SVebSaxB'),
       ('Matthias Kraft', 'matthiasStockholm@outlook.se', 'Göteborg', 'kOlrYbw_!'),
       ('George Landin', 'george.landin@hotmail.com', 'Malmö', '!_wexBq_Erw'),
       ('Jonathan Lopez', 'jonteFotboll@gmail.com', 'Uppsala', 'OmbqLWsAq');

INSERT INTO Product(AmountInStock, SizeOfShoe, Color, Price, Brand)
VALUES (2, 36, 'Black', 899, 'Nike'),
       (11, 39, 'White', 1200, 'Tommy Hilfiger'),
       (32, 43, 'Black', 599, 'Adidas'),
       (4, 41, 'Black', 769, 'Puma'),
       (21, 35, 'Blue', 559, 'Vagabond'),
       (8, 44, 'Pink', 1199, 'Ecco'),
       (4, 40, 'White', 1399, 'Nike'),
       (1, 41, 'White', 2100, 'Tiger Of Sweden'),
       (17, 38, 'Black', 579, 'Ecco'),
       (9, 39, 'Blue', 1300, 'Salomon');

INSERT INTO Category(Name)
VALUES ('Women\'s shoes'),
        ('Men\'s shoes'),
       ('Running shoes'),
       ('Sneakers'),
       ('Heels'),
       ('Formal shoes'),
       ('Hiking boots'),
       ('Sandals');

INSERT INTO ShoeOrder(DateOfOrder, CustomerId)
VALUES ('2023-01-02', 1),
       ('2023-01-02', 2),
       ('2023-01-12', 3),
       ('2023-01-24', 4),
       ('2023-01-29', 5),
       ('2023-02-02', 6),
       ('2023-02-04', 7),
       ('2023-02-25', 6);

INSERT INTO ProductsInOrder(ShoeOrderId, ProductId)
VALUES (1, 7),
       (1, 3),
       (2, 2),
       (3, 1),
       (4, 10),
       (4, 6),
       (4, 2),
       (5, 8),
       (6, 5),
       (6, 1),
       (7, 4),
       (8, 9);

INSERT INTO Belongs(ProductId, CategoryId)
VALUES (1, 1),
       (1, 3),
       (2, 6),
       (3, 2),
       (3, 4),
       (4, 1),
       (4, 4),
       (5, 1),
       (5, 2),
       (5, 6),
       (6, 2),
       (7, 3),
       (8, 2),
       (8, 6),
       (9, 8),
       (10, 7),
       (10, 1),
       (10, 2);



-- -------------------------------- INDEX -----------------------------------------

-- En order är ofta starkt förknippad med personens namn och mailadress.
-- När en order ska hanteras är det därför vanligt att man söker på just detta.
-- Allt från att skapa fraktsedel till kundservice som retur eller reklamation.
CREATE INDEX IX_CustomerName ON Customer (Name);
CREATE INDEX IX_CustomerMail ON Customer (Mail);

-- Kunder och anställda filtrerar ofta sina sökningar utifrån storlek, färg, pris och märke.
CREATE INDEX IX_sizeOfShoe ON Product (SizeOfShoe);
CREATE INDEX IX_color ON Product (Color);
CREATE INDEX IX_price ON Product (Price);
CREATE INDEX IX_brand ON Product (Brand);


-- --------------------------------- QUERIES ----------------------------------

-- PERSONER SOM KÖPT SVARTA ECCO SANDALER I STR 38
SELECT Customer.Name
FROM Customer
         JOIN ShoeOrder ON Customer.Id = ShoeOrder.CustomerId
         JOIN ProductsInOrder ON ShoeOrder.Id = ProductsInOrder.ShoeOrderId
         JOIN Product ON ProductsInOrder.ProductId = Product.Id
         JOIN Belongs ON Product.Id = Belongs.ProductId
         JOIN Category ON Belongs.CategoryId = Category.Id
WHERE Product.Brand = 'Ecco'
  AND Product.Color = 'Black'
  AND Product.SizeOfShoe = 38
  AND Category.Name = 'Sandals';

-- ANTAL PRODUKTER/KATEGORI
SELECT Category.Name, COUNT(Belongs.CategoryId) AntalProdukter
FROM Category
         LEFT JOIN Belongs ON Category.Id = Belongs.CategoryId
GROUP BY Category.Id;

-- KUNDLISTA, NAMN, SUMMA
SELECT Customer.Name, SUM(Product.Price) as HandlatFör
FROM Customer
         JOIN ShoeOrder ON Customer.Id = ShoeOrder.CustomerId
         JOIN ProductsInOrder ON ShoeOrder.Id = ProductsInOrder.ShoeOrderId
         JOIN Product ON ProductsInOrder.ProductId = Product.Id
GROUP BY Customer.Name;

-- TOTALA BESTÄLLNINGSVÄRDE/STAD DÄR SUMMAN ÄR > 1000KR
SELECT Customer.City, SUM(Product.Price) as Summa
FROM Customer
         JOIN ShoeOrder ON Customer.Id = ShoeOrder.CustomerId
         JOIN ProductsInOrder ON ShoeOrder.Id = ProductsInOrder.ShoeOrderId
         JOIN Product ON ProductsInOrder.ProductId = Product.Id
GROUP BY Customer.City
HAVING SUM(Product.Price) > 1000;

-- TOP 5 MEST SÅLDA PRODUKTER
SELECT Product.Brand,
       Product.Color,
       Product.SizeOfShoe,
       Category.Name                    as Kategori,
       COUNT(ProductsInOrder.ProductId) as AntalSålda
FROM Product
         JOIN ProductsInOrder ON Product.Id = ProductsInOrder.ProductId
         JOIN Belongs ON Product.Id = Belongs.ProductId
         JOIN Category On Belongs.CategoryId = Category.Id
GROUP BY Product.Id, Product.Brand, Product.SizeOfShoe, Product.Color, Category.Name
ORDER BY AntalSålda desc LIMIT 5;

-- MÅNAD MED STÖRST FÖRSÄLJNING
SELECT MONTH (ShoeOrder.DateOfOrder) as Månad, SUM (Product.Price) Summa
FROM ShoeOrder
    JOIN ProductsInOrder
ON ShoeOrder.Id = ProductsInOrder.ShoeOrderId
    JOIN Product ON ProductsInOrder.ProductId = Product.Id
GROUP BY MONTH (ShoeOrder.DateOfOrder)
ORDER BY Summa DESC
    LIMIT 1;


-- ------------------------ STORED PROCEDURE -----------------------------

DELIMITER
//

CREATE PROCEDURE AddToCart(IN cust_Id INT, IN ord_Id INT, IN prod_Id INT)
BEGIN
		DECLARE
amountOfOrders INT DEFAULT -1;
		DECLARE
dateOfToday DATE DEFAULT CURDATE();

        -- Då alla fel genererar 1644 har jag valt att hantera felen
        -- separat i if-satserna. Exit handlern ser till att en rollback
        -- görs varje gång ett fel orsakas.
        DECLARE
EXIT HANDLER FOR sqlexception
BEGIN
ROLLBACK;
RESIGNAL;
END;

START TRANSACTION;
IF
cust_Id IS NULL OR prod_Id IS NULL THEN
			SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'CustomerId/ProduktId can not be NULL';

        ELSEIF
NOT EXISTS (SELECT Id FROM CUSTOMER WHERE Id = cust_Id) THEN
			SIGNAL SQLSTATE '45002'
            SET MESSAGE_TEXT = 'Invalid CustomerId. Customer not found';

		ELSEIF
NOT EXISTS (SELECT Id FROM PRODUCT WHERE Id = prod_Id) THEN
			SIGNAL SQLSTATE '45003'
            SET MESSAGE_TEXT = 'Invalid ProductId. Product does not exist.';
END IF;

SELECT COUNT(*)
INTO amountOfOrders
FROM ShoeOrder
WHERE Id = ord_Id;

IF
(
SELECT Product.amountInStock
FROM Product
WHERE Id = prod_Id) > 0 THEN

		IF ord_Id IS NULL OR amountOfOrders = 0 THEN
INSERT
INTO ShoeOrder(DateOfOrder, CustomerId)
VALUES
    (dateOfToday, cust_Id);

INSERT INTO ProductsInOrder(ShoeOrderId, ProductId)
VALUES (LAST_INSERT_ID(), prod_Id);

ELSEIF
amountOfOrders = 1 THEN
				 IF ((SELECT ShoeOrder.CustomerId FROM ShoeOrder WHERE ShoeOrder.Id = ord_Id) = cust_Id) THEN
					INSERT INTO ProductsInOrder(ShoeOrderId, ProductId) VALUES
					(ord_Id, prod_Id);
ELSE
					SIGNAL SQLSTATE '45004'
					SET MESSAGE_TEXT = 'CustomerId is not the same as previous order';
END IF;
		ELSEIF
amountOfOrders = -1 THEN
				SIGNAL SQLSTATE '45005'
                SET MESSAGE_TEXT = 'Something went wrong. Number of orders with specified number not found';
ELSE
				SIGNAL SQLSTATE '45006'
				SET MESSAGE_TEXT = 'Something went wrong. Several orders with the same Id have been detected.';

END IF;

		IF
amountOfOrders IS NULL OR amountOfOrders IN (0, 1) THEN

UPDATE Product
SET AmountInStock = AmountInStock - 1
WHERE Id = prod_Id;
END IF;

ELSE
			SIGNAL SQLSTATE '45006'
			SET MESSAGE_TEXT = 'The product is out of stock';

END IF;

COMMIT;
END
//

DELIMITER ;

select *
from Product;
select *
from ShoeOrder;
select *
from ProductsInOrder;
select *
from Customer;
select *
from Belongs;

SELECT Product.Id,
       Product.SizeOfShoe,
       Product.Color,
       Product.Price,
       Product.Brand,
       GROUP_CONCAT(Category.Name SEPARATOR ', ') AS Categories
FROM Product
         JOIN Belongs ON Product.Id = Belongs.ProductId
         JOIN Category ON Belongs.CategoryId = Category.Id
GROUP BY Product.Id;
