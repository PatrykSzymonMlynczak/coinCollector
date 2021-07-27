INSERT INTO person(name,debt)
VALUES('Zamor',0),('Oltman',-20);

INSERT INTO product(my_price,name)
VALUES(10,'Lemon Haze'),(10,'AK 47');

INSERT INTO quantity_price_map(product_id,price,quantity)
VALUES((SELECT id from product WHERE name='Lemon Haze'),20,1),
((SELECT id from product WHERE name='Lemon Haze'),16,5),
((SELECT id from product WHERE name='AK 47'),20,1),
((SELECT id from product WHERE name='AK 47'),16,5);


INSERT INTO sale(discount,earned,income,my_sort_price,quantity,day,moment,person_id,product_id)
VALUES
(20,0,50,10,5,'2021-07-26','15:29:02',(SELECT id from person WHERE name='Zamor'),(SELECT id from product WHERE name='Lemon Haze')),
(-10,40,90,10,5,'2021-07-26','16:04:35',(SELECT id from person WHERE name='Oltman'),(SELECT id from product WHERE name='AK 47'));